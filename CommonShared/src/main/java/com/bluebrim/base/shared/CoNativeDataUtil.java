package com.bluebrim.base.shared;
import java.io.*;

/**
 * Toolkit class for reading and writing bytes to and from files, and similar.
 * If nothing else is said, big endian (Motorola style, network byte order) is assumed.
 *
 * <p><b>Creation date:</b> 2001-04-12
 * <br><b>Documentation last updated:</b> 2001-09-19
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se) (2001-09-19)
 */

public final class CoNativeDataUtil {
/**
 * Append a byte in hexadecimal form, as two ASCII bytes, e.g. "5F" for 0x5F, to a StringBuffer.
 *
 * @param b the byte to write.
 * @param out the string buffer to write to.
 */
public static void appendHexByte(byte b, StringBuffer buf) {
	// Write first char
	char c = (char) ((b >> 4) & 0xF);
	if (c > 9)
		c = (char) ((c - 10) + 'A');
	else
		c = (char) (c + '0');
	buf.append(c);

	// Write second char
	c = (char) (b & 0xF);
	if (c > 9)
		c = (char) ((c - 10) + 'A');
	else
		c = (char) (c + '0');
	buf.append(c);
}


/**
 * Read two bytes from a byte[] array, and interpret this as a 16-bit, big endian, char.
 *
 * @param b the byte[] array to read from.
 * @param startPos position in b of the first byte to read.
 *
 * @return the interpreted value, as an char.
 */
public static char read2BytesAsChar(byte[] b, int startPos) {
	int result = 0;
	
	for (int i = 0; i < 2; i++) {
		result <<= 8; // shift up old value one byte
		result += (int)(b[startPos + i] & 0xFF);
	}

	return (char) result;
}


/**
 * Read two bytes from a byte[] array, and interpret this as a unsigned 16-bit, big endian, value.
 *
 * @param b the byte[] array to read from.
 * @param startPos position in b of the first byte to read.
 *
 * @return the interpreted value, as an int.
 */
public static int read2BytesAsInt(byte[] b, int startPos) {
	int result = 0;
	
	for (int i = 0; i < 2; i++) {
		result <<= 8; // shift up old value one byte
		result += (int)(b[startPos + i] & 0xFF);
	}

	return result;
}


/**
 * Read two bytes from a byte[] array, and interpret this as a signed 16-bit, big endian, value.
 *
 * @param b the byte[] array to read from.
 * @param startPos position in b of the first byte to read.
 *
 * @return the interpreted value, as an (signed) short.
 */
public static short read2BytesAsShort(byte[] b, int startPos) {
	int result = 0;
	
	for (int i = 0; i < 2; i++) {
		result <<= 8; // shift up old value one byte
		result += (int)(b[startPos + i] & 0xFF);
	}

	return (short)result;
}


/**
 * Read four bytes from a byte[] array, and interpret this as a unsigned 32-bit, big endian, value.
 *
 * @param b the byte[] array to read from.
 * @param startPos position in b of the first byte to read.
 *
 * @return the interpreted value, as a long.
 */
public static long read4BytesAsLong(byte[] b, int startPos) {
	long result = 0;
	
	for (int i = 0; i < 4; i++) {
		result <<= 8; // shift up old value one byte
		result += (int) (b[startPos + i] & 0xFF);
	}

	return result;
}


/**
 * Read four bytes from a byte[] array, and interpret this as a unsigned 32-bit, little endian, value.
 *
 * @param b the byte[] array to read from.
 * @param startPos position in b of the first byte to read.
 *
 * @return the interpreted value, as a long.
 */
public static long read4BytesAsLongIntelEndian(byte[] b, int startPos) {
	long result = 0;

	for (int i = 3; i >= 0; i--) { // intel does things backwards... :-)
		result <<= 8; // shift up old value one byte
		result += (int) (b[startPos + i] & 0xFF);
	}

	return result;
}


/**
 * Read from a byte array, where pairs of bytes are considered to be a char (big endian), and return
 * this as a char array.
 *
 * @param b the byte[] array to read from.
 * @param startPos position in b of the first byte to read.
 * @param length the number of bytes (not resulting chars!) to read.
 *
 * @return the corresponding char[] array.
 */
public static char[] readDoubleBytesAsChars(byte[] b, int startPos, int length) {
	int charLength = length/2;
	char[] chars = new char[charLength];
	for (int i = 0; i < charLength; i++) {
		chars[i] = read2BytesAsChar(b, i*2);
	}
	
	return chars;
}


/**
 * Read from a byte array, where pairs of bytes are considered to be a char (big endian), and return
 * this as a String.
 *
 * @param b the byte[] array to read from.
 * @param startPos position in b of the first byte to read.
 * @param length the number of bytes (not resulting chars!) to read.
 *
 * @return the corresponding String.
 */
public static String readDoubleBytesAsString(byte[] b, int startPos, int length) {
	int charLength = length/2;
	char[] chars = new char[charLength];
	for (int i = 0; i < charLength; i++) {
		chars[i] = read2BytesAsChar(b, i*2 + startPos);
	}
	
	return new String(chars);
}


/**
 * Read four bytes from an InputStream, and interpret this as a signed 16.16 fixed point, big endian, value.
 *
 * @param in the input stream to read from.
 *
 * @return the interpreted fixed, as a double.
 *
 * @see #read4BytesAsLong(byte[], int)
 */
public static double readFixed32(InputStream in) throws IOException {
	byte[] buffer = new byte[4];

	in.read(buffer, 0, 4);
	short mantissa = read2BytesAsShort(buffer, 0);
	int fraction = read2BytesAsInt(buffer, 2);
	double result = mantissa + ((double) fraction / 65536.0);
	// The Fixed point format consists of a signed, 2's complement mantissa and an unsigned fraction. 
	// To compute the actual value, take the mantissa and add the fraction.
	// Source: OpenType spec, http://www.microsoft.com/typography/OTSPEC/otff.htm

	return result;
}


/**
 * Read two bytes from an InputStream, and interpret this as a signed 16-bit, big endian, value.
 *
 * @param in the input stream to read from.
 *
 * @return the interpreted value, as an (signed) short.
 *
 * @see #read2BytesAsShort(byte[], int)
 */
public static short readShort16(InputStream in) throws IOException {
	byte[] buffer = new byte[2];

	in.read(buffer, 0, 2);
	return read2BytesAsShort(buffer, 0);
}


/**
 * Read two bytes from an InputStream, and interpret this as a unsigned 16-bit, big endian, value.
 *
 * @param in the input stream to read from.
 *
 * @return the interpreted value, as an int.
 *
 * @see #read2BytesAsInt(byte[], int)
 */
public static int readUint16(InputStream in) throws IOException {
	byte[] buffer = new byte[2];

	in.read(buffer, 0, 2);
	return read2BytesAsInt(buffer, 0);
}


/**
 * Read four bytes from an InputStream, and interpret this as a unsigned 32-bit, big endian, value.
 *
 * @param in the input stream to read from.
 *
 * @return the interpreted value, as a long.
 *
 * @see #read4BytesAsLong(byte[], int)
 */
public static long readUint32(InputStream in) throws IOException {
	byte[] buffer = new byte[4];

	in.read(buffer, 0, 4);
	return read4BytesAsLong(buffer, 0);
}


/**
 * Read four bytes from an InputStream, and interpret this as a unsigned 32-bit, little endian, value.
 *
 * @param in the input stream to read from.
 *
 * @return the interpreted value, as a long.
 *
 * @see #read4BytesAsLongIntelEndian(byte[], int)
 */
public static long readUint32IntelEndian(InputStream in) throws IOException {
	byte[] buffer = new byte[4];

	in.read(buffer, 0, 4);
	return read4BytesAsLongIntelEndian(buffer, 0);
}


/**
 * Write a signed int as 4 bytes in a byte[] array, big endian style.
 *
 * @param b byte array to write to.
 * @param startPos position of first byte to write to.
 * @param data the value to write.
 */
public static void storeInByteArray(byte[] b, int startPos, int data) {
	for (int i = 0; i < 4; i++) {
		b[startPos + i] = (byte) ((data >>> (3 - i) * 8) & 0xFF);
	}
}


/**
 * Write a signed long as 8 bytes in a byte[] array, big endian style.
 *
 * @param b byte array to write to.
 * @param startPos position of first byte to write to.
 * @param data the value to write.
 */
public static void storeInByteArray(byte[] b, int startPos, long data) {
	for (int i = 0; i < 8; i++) {
		b[startPos + i] = (byte) ((data >>> (7 - i) * 8) & 0xFF);
	}
}


/**
 * Write a signed short as 2 bytes in a byte[] array, big endian style.
 *
 * @param b byte array to write to.
 * @param startPos position of first byte to write to.
 * @param data the value to write.
 */
public static void storeInByteArray(byte[] b, int startPos, short data) {
	for (int i = 0; i < 2; i++) {
		b[startPos + i] = (byte) ((data >>> (1 - i) * 8) & 0xFF);
	}
}


/**
 * Write a value between 0 and 65535 as 2 bytes in a byte[] array, big endian style.
 *
 * @param b byte array to write to.
 * @param startPos position of first byte to write to.
 * @param data the value to write, in an int.
 */
public static void storeInByteArrayAs2Bytes(byte[] b, int startPos, int data) {
	for (int i = 0; i < 2; i++) {
		b[startPos + i] = (byte) ((data >>> (1 - i) * 8) & 0xFF);
	}
}


/**
 * Write a value between -32k and 32k as 2 bytes in a byte[] array, big endian style.
 *
 * @param b byte array to write to.
 * @param startPos position of first byte to write to.
 * @param data the value to write, as a short.
 */
public static void storeInByteArrayAs2Bytes(byte[] b, int startPos, short data) {
	for (int i = 0; i < 2; i++) {
		b[startPos + i] = (byte) ((data >>> (1 - i) * 8) & 0xFF);
	}
}


/**
 * Write a signed value of 32 bit as 4 bytes in a byte[] array, big endian style.
 *
 * @param b byte array to write to.
 * @param startPos position of first byte to write to.
 * @param data the value to write, as an int.
 */
public static void storeInByteArrayAs4Bytes(byte[] b, int startPos, int data) {
	for (int i = 0; i < 4; i++) {
		b[startPos + i] = (byte) ((data >>> (3 - i) * 8) & 0xFF);
	}
}


/**
 * Write a (unsigned) value of 32 bit as 4 bytes in a byte[] array, big endian style.
 *
 * @param b byte array to write to.
 * @param startPos position of first byte to write to.
 * @param data the value to write, in a long.
 */
public static void storeInByteArrayAs4Bytes(byte[] b, int startPos, long data) {
	for (int i = 0; i < 4; i++) {
		b[startPos + i] = (byte) ((data >>> (3 - i) * 8) & 0xFF);
	}
}


/**
 * Write a (unsigned) value of 32 bit as 4 bytes in a byte[] array, little endian (intel) style.
 *
 * @param b byte array to write to.
 * @param startPos position of first byte to write to.
 * @param data the value to write, in a long.
 */
public static void storeInByteArrayAs4BytesIntelEndian(byte[] b, int startPos, long data) {
	for (int i = 3; i >= 0; i--) { // intel does things backwards... :-)
		b[startPos + i] = (byte) ((data >>> i * 8) & 0xFF);
	}
}


/**
 * Write a byte in hexadecimal form, as two ASCII bytes, e.g. "5F" for 0x5F, to an OutputStream.
 *
 * @param b the byte to write.
 * @param out the output stream to write to.
 */
public static void writeHexByte(byte b, OutputStream out) throws IOException {
	char c = (char)((b >> 4) & 0xF);
	if (c > 9)
		c = (char)((c - 10) + 'A');
	else
		c = (char)(c + '0');
	out.write(c);
	
	c = (char)(b & 0xF);
	if (c > 9)
		c = (char)((c - 10) + 'A');
	else
		c = (char)(c + '0');
	out.write(c);
}


/**
 * Write a byte in hexadecimal form, as two ASCII bytes, e.g. "5F" for 0x5F, to a PrintWriter.
 *
 * @param b the byte to write.
 * @param out the PrintWriter to write to.
 */
public static void writeHexByte(byte b, PrintWriter writer) {
	char c = (char)((b >> 4) & 0xF);
	if (c > 9)
		c = (char)((c - 10) + 'A');
	else
		c = (char)(c + '0');
	writer.print(c);
	
	c = (char)(b & 0xF);
	if (c > 9)
		c = (char)((c - 10) + 'A');
	else
		c = (char)(c + '0');
	writer.print(c);
}


/** 
 * Takes an int value, and writes this to an OutputStream as a unsigned 16-bit, big endian, value.
 * Note! The value must be between 0 and 2^16.
 *
 * @param out the output stream to write to.
 * @param value the value to write.
 *
 * @see #readUint16(InputStream)
 * @see #storeInByteArrayAs2Bytes(byte[], int, int)
 */
public static void writeUint16(OutputStream out, int value) throws IOException {
	byte[] buffer = new byte[2];

	storeInByteArrayAs2Bytes(buffer, 0, value);
	out.write(buffer);
}


/** 
 * Takes an int value, and writes this to an OutputStream as a unsigned 32-bit, big endian, value.
 * Note! The value must be between 0 and 2^32.
 *
 * @param out the output stream to write to.
 * @param value the value to write.
 *
 * @see #readUint32(InputStream)
 * @see #storeInByteArrayAs4Bytes(byte[], int, long)
 */
public static void writeUint32(OutputStream out, long value) throws IOException {
	byte[] buffer = new byte[4];

	storeInByteArrayAs4Bytes(buffer, 0, value);
	out.write(buffer);
}


/** 
 * Takes an int value, and writes this to an OutputStream as a unsigned 32-bit, little endian, value.
 * Note! The value must be between 0 and 2^32.
 *
 * @param out the output stream to write to.
 * @param value the value to write.
 *
 * @see #readUint32(InputStream)
 * @see #storeInByteArrayAs4Bytes(byte[], int, long)
 */
public static void writeUint32IntelEndian(OutputStream out, long value) throws IOException {
	byte[] buffer = new byte[4];

	storeInByteArrayAs4BytesIntelEndian(buffer, 0, value);
	out.write(buffer);
}
}