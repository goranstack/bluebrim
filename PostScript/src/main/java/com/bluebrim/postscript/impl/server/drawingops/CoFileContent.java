package com.bluebrim.postscript.impl.server.drawingops;

import java.io.*;

import javax.imageio.*;
import javax.imageio.stream.*;

/**
 * Object representation of file contents.
 *
 * PENDING: Write content? (MIME) ContentType? /Markus 1999-10-27
 */
public class CoFileContent implements Serializable {
	protected byte[] m_content;

	public CoFileContent(ImageInputStream stream) {
		m_content = readStream(stream);
	}

	public CoFileContent(ImageInputStream stream, long offset, int length) {
		m_content = readStream(stream, offset, length);
	}

	public CoFileContent(File file) {
		try {
			ImageInputStream stream = ImageIO.createImageInputStream(file);
			m_content = readStream(stream);
			stream.close();
		} catch (IOException e) {
		}
	}

	public InputStream getAsStream() {
		return new ByteArrayInputStream(m_content);
	}

	public byte[] getContent() {
		return m_content;
	}
	/**
	 * Read a stream into a byte array.
	 *
	 * PENDING: available() always returns 0 for most (seekable) streams.
	 * Must find a better way to do this. /Markus 1999-11-15.
	 * Check CoSerializableSeekableStream!
	 */
	private byte[] readStream(ImageInputStream stream) {
		try {
			byte[] content = new byte[(int) stream.length()];
			stream.readFully(content);
			return content;
		} catch (IOException e) {
			System.out.println("IOException in CoFileContent.readStream().");
			return null;
		}
	}
	/**
	 * Read portions of a stream into a byte array.
	 *
	 * @author Markus Persson 2000-08-16
	 */
	private byte[] readStream(ImageInputStream stream, long offset, int length) {
		try {
			stream.reset();
			stream.seek(offset);
			byte[] content = new byte[length];
			stream.readFully(content);
			return content;
		} catch (IOException e) {
			System.out.println("IOException in CoFileContent.readStream() with length.");
			return null;
		}
	}
}
