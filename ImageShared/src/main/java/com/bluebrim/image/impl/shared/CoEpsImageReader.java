package com.bluebrim.image.impl.shared;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.nio.ByteOrder;
import java.util.Iterator;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/**
 * @author Göran Stäck 2003-04-28
 *
 */

public class CoEpsImageReader extends ImageReader {
		
	public static final int EPS_WITH_BINARY_HEADER_INT_MAGIC = 0xc5d0d3c6;
	public static final short PURE_ASCII_EPS_SHORT_MAGIC = (('%' << 8) | '!');

	private int m_psOffset;
	private int m_psLen;
	private int m_tiffOffset;
	private int m_tiffLen;
	private ImageInputStream m_stream = null;
	private int m_height = 0;
	private int m_width = 0;
	private BufferedImage m_previewImage;
	private ImageReader m_previewReader;
	private boolean m_gotBoundingBox = false;

	public CoEpsImageReader(ImageReaderSpi originatingProvider) {
		super(originatingProvider);
	}
	
	public void setInput(Object input, boolean seekForwardOnly, boolean ignoreMetadata) {
		super.setInput(input, seekForwardOnly, ignoreMetadata);
		if (input == null) {
			m_stream = null;
			return;
		}
		if (input instanceof ImageInputStream) {
			m_stream = (ImageInputStream) input;
			try {
				readHeader();
			} catch (IOException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		} else {
			throw new IllegalArgumentException("bad input");
		}
	}

	public int getNumImages(boolean allowSearch) throws IIOException {
		return 1; // format can only encode a single image
	}

	private void checkIndex(int imageIndex) {
		if (imageIndex != 0) {
			throw new IndexOutOfBoundsException("bad index");
		}
	}

	public int getWidth(int imageIndex) throws IIOException {
		checkIndex(imageIndex); // must throw an exception if != 0
		try {
			decodeBoundingBox();
		} catch (IOException e) {
			throw new IIOException(e.getMessage());
		}
		return m_width;
	}

	public int getHeight(int imageIndex) throws IIOException {
		checkIndex(imageIndex);
		try {
			decodeBoundingBox();
		} catch (IOException e) {
			throw new IIOException(e.getMessage());
		}
		return m_height;
	}

	/**
	 * PENDING: Check for HighResBoundingBox and use it if it exists.
	 *
	 * @author Markus Persson 1999-09-30
	 * @author Markus Persson 2000-08-16
	 */
	private void decodeBoundingBox() throws IOException {
		if (m_gotBoundingBox)
			return;
		m_gotBoundingBox = true;
		StreamTokenizer toker = new StreamTokenizer(new InputStreamReader(getPostScript()));

		// Set up parsing table.
		toker.wordChars('!',255);
		toker.parseNumbers();
		toker.eolIsSignificant(true);

		// Left, Lower, Right, Upper
		double[] llru = new double[4];

		boundingBoxScanner:
		for(int prevType = 0; toker.ttype != StreamTokenizer.TT_EOF; prevType = toker.ttype, toker.nextToken()) {
			if ((prevType == StreamTokenizer.TT_EOL) &&
				(toker.ttype == StreamTokenizer.TT_WORD) &&
				toker.sval.equals("%%BoundingBox:")) {

				for(int i = 0; i < 4; i++) {
					if (toker.nextToken() != StreamTokenizer.TT_NUMBER)
						continue boundingBoxScanner;
					llru[i] = toker.nval;
				}
				double left = llru[0];
				double lower = llru[1];
				double right = llru[2];
				double upper = llru[3];

				double tmp;

				// Shouldn't be like this, but we might as well support it.
				if(left > right) {
					tmp = left;
					left = right;
					right = tmp;
				}

				if(lower > upper) {
					tmp = lower;
					lower = upper;
					upper = tmp;
				}
				m_height = (int) (upper - lower);
				m_width = (int) (right - left);

			}
		}

	}


	private void readHeader() throws IOException {
		if (m_stream.getStreamPosition() != 0) 
			m_stream.reset();

		long fileLen = m_stream.length();
		int magic = m_stream.readInt();

		if (magic == EPS_WITH_BINARY_HEADER_INT_MAGIC) {
			/* Both offsets and lengths are assumed to be unsigned 32-bit numbers.
			 * However, Java only uses int:s for crucial parameters related to
			 * the lengths why we read those as signed numbers and abort if they're
			 * negative.
			 * NOTE: By reading in chunks, this could be handled too. But now we're
			 * talking files > 2 Gbytes, which doesn't seem very likely this millenium ...
			 * /Markus 1999-10-28
			 */
			ByteOrder byteOrder = m_stream.getByteOrder();
			m_stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
			m_psOffset = m_stream.readInt();
			m_psLen = m_stream.readInt();
			// Skip WindowsMetaFile preview info for now.
			m_stream.skipBytes(8);
			m_tiffOffset = m_stream.readInt();
			m_tiffLen = m_stream.readInt();
			m_stream.setByteOrder(byteOrder);			
			if ((m_psLen < 0) || (m_tiffLen < 0))
				throw new IOException("PostScript or TIFF segment longer than Integer.MAX_VALUE.");
		} else if ((magic >>> 16) == PURE_ASCII_EPS_SHORT_MAGIC) {
			m_psOffset = 0;
			m_psLen = (int)fileLen;
			m_tiffOffset = 0;
			m_tiffLen = 0;
		} else
			throw new IOException("Stream is not EPS.");
	}
	
	private void extractPreviewImage() throws IOException {
		if (m_previewReader != null)
			return;

		m_stream.seek(m_tiffOffset);
		byte[] imageBytes = new byte[m_tiffLen];
		m_stream.readFully(imageBytes);

		Iterator iter = ImageIO.getImageReaders(new ByteArrayInputStream(imageBytes));
		if (iter.hasNext())
			m_previewReader = (ImageReader) iter.next();
		else
			throw new IOException("Failed to read preview image in EPS");
	}

	/**
	 * Deletegate to preview image reader
	 */
	public Iterator getImageTypes(int imageIndex) throws IOException {
		extractPreviewImage();
		return m_previewReader.getImageTypes(imageIndex);
	}

	/**
	 * Deletegate to preview image reader
	 */
	public IIOMetadata getStreamMetadata() throws IOException {
		extractPreviewImage();
		return m_previewReader.getStreamMetadata();
	}

	/**
	 * Deletegate to preview image reader
	 */
	public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
		extractPreviewImage();
		return m_previewReader.getImageMetadata(imageIndex);
	}

	public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException {
	//	extractPreviewImage();
	//	return m_previewReader.read(imageIndex, param);
		m_stream.seek(m_tiffOffset);
		byte[] imageBytes = new byte[m_tiffLen];
		m_stream.readFully(imageBytes);

		return ImageIO.read(new ByteArrayInputStream(imageBytes));

	}

	public InputStream getPostScript() throws IOException {
		m_stream.seek(m_psOffset);
		
		byte[] postScript = new byte[m_psLen];
		m_stream.readFully(postScript);
		return new ByteArrayInputStream(postScript);
	}

}
