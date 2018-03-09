package com.bluebrim.postscript.impl.server.drawingops;
import java.io.*;

public class Ascii85OutputStream extends java.io.FilterOutputStream {

	private int line_break;
	private int count;

	private byte indata[];
	private byte outdata[];

	/** Function produces five ASCII printing characters from
	    four bytes of binary data 
	 */
	private int maxline;
	private boolean flushed;
	private char terminator;

	public Ascii85OutputStream(java.io.OutputStream o) {
		super(o);
		line_break = maxline = 36 * 2;
		count = 0;
		indata = new byte[4];
		outdata = new byte[5];
		flushed = true;
		terminator = '~';
	}

	private final void Ascii85Tuple() {
		long word;
		word = ((((indata[0] << 8) | (indata[1] & 0xFF)) << 16) | ((indata[2] & 0xFF) << 8) | (indata[3] & 0xFF)) & 0xFFFFFFFFL;

		if (word == 0) {
			outdata[0] = 'z';
			outdata[1] = 0;
			return;
		}
		long x;
		x = word / 52200625L;
		outdata[0] = (byte) (x + '!');
		word -= x * 52200625L;

		x = word / 614125L;
		outdata[1] = (byte) (x + '!');
		word -= x * 614125L;

		x = word / 7225L;
		outdata[2] = (byte) (x + '!');
		word -= x * 7225L;

		x = word / 85L;
		outdata[3] = (byte) (x + '!');

		// word-=x*85L;

		outdata[4] = (byte) ((word % 85L) + '!');
	}

	public final void write(int b) throws IOException {
		flushed = false;
		indata[count++] = (byte) b;
		if (count < 4)
			return;
		Ascii85Tuple();
		for (int i = 0; i < 5; i++) {
			if (outdata[i] == 0)
				break;
			out.write(outdata[i]);
			if (--line_break == 0) {
				out.write('\n');
				line_break = maxline;
			}
		}
		count = 0;
	}

	public final void write(byte b[], int off, int sz) throws IOException {
		for (int i = 0; i < sz; i++) {
			if (count < 3)
				indata[count++] = b[off + i];
			else
				write(b[off + i]);
		}
	}

	public final void flush() throws IOException {
		if (flushed)
			return;
		if (count > 0) {
			for (int i = count; i < 4; i++)
				indata[i] = 0;
			Ascii85Tuple();
			if (outdata[0] == 'z')
				for (int i = 0; i < 5; i++) // expand 'z'
					outdata[i] = '!';
			for (int i = 0; i < count + 1; i++) {
				out.write(outdata[i]);
				if (--line_break == 0) {
					out.write('\n');
					line_break = maxline;
				}
			}
		}
		if (--line_break == 0)
			out.write('\n');
		out.write(terminator);
//		out.write('\n');
		count = 0;
		line_break = maxline;
		flushed = true;
		super.flush();
	}

	public void close() throws IOException {
		try {
			super.close();
		} finally {
			indata = outdata = null;
		}
	}

	protected void finalize() throws Throwable {
		try {
			flush();
		} finally {
			super.finalize();
		}

	}
}
