package com.bluebrim.base.shared;

import java.awt.geom.*;
import java.io.*;
import java.text.*;
import java.util.*;

/**
 *	Utility class with static methods for string 
 *	manipulation and file creation.
 * @author ?
 * @author Marcus Hirt
 */
public class CoBaseUtilities {
	private CoBaseUtilities() {
	}
	
	/**
	 * Returns a canonicalized form of the phone number in <code>number</code>.
	 * If country code and/or area code is missing the default values are added.
	 * The canonicalized answer will be in form of +46-13-112233
	 * This method also handles mobile numbers assuming that <code>number</code>
	 * contains the operator code, e g it's an error to provide a mobile number w/o 
	 * this information. That it's a mobile number is indicated by a null value for
	 * <code>defaultAreaCode</code>.
	 */
	public static String canonicalizePhoneNumber(String number, String defaultCountryCode, String defaultAreaCode, String delimiter) throws ParseException {
		if (CoBaseUtilities.stringIsEmpty(number))
			return null;
		String canonString = CoBaseUtilities.removeAllWhitespaces(number);
		if (CoBaseUtilities.stringIsEmpty(canonString))
			return null;
		int index = canonString.lastIndexOf(delimiter);
		if (defaultAreaCode == null) {
			if (index == -1)
				throw new ParseException("Wrong format", 0);
		}
		String phoneNumber = null;
		String areaCode = null;
		String countryCode = null;
		boolean hasPhoneNumber = index + 1 < canonString.length();

		CoReversedStringTokenizer tokenizer = new CoReversedStringTokenizer(canonString, delimiter);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (phoneNumber == null && hasPhoneNumber)
				phoneNumber = token;
			else if (areaCode == null)
				areaCode = token;
			else if (countryCode == null)
				countryCode = token;
			else
				throw new ParseException("Wrong format", 0);
		}
		countryCode = countryCode == null ? "+" + defaultCountryCode : (countryCode.startsWith("+") ? countryCode : "+" + countryCode);
		StringBuffer buffer = new StringBuffer(countryCode);
		buffer.append(delimiter);
		buffer.append(CoBaseUtilities.removeFromBeginning((areaCode == null ? defaultAreaCode : areaCode), '0'));
		buffer.append(delimiter);
		buffer.append(phoneNumber == null ? (hasPhoneNumber ? canonString : "") : phoneNumber);
		return buffer.toString();
	}
	
	public static String[] concatenate(String[] s1, String[] s2) {
		String[] res = new String[s1.length + s2.length];
		String[][] ss = new String[][] { s1, s2 };
		int prevLen = 0;
		for (int idx = 0; idx < 2; idx++) {
			for (int idxx = 0; idxx < ss[idx].length; idxx++) {
				res[prevLen + idxx] = ss[idx][idxx];
			}
			prevLen = ss[idx].length;
		}
		return res;
	}
	
	/**
	 * Converts a string to iso646se char encoding
	 * 
	 * PENDING: Remove and fix the places where it is used by using
	 * the enhanced CharToByte converters in JDK 1.4. /Markus
	 */
	public static String convertToISO646SE(String str) {
		StringBuffer sb = new StringBuffer();
		int top = str.length();
		for (int idx = 0; idx < top; idx++) {
			char ch = str.charAt(idx);
			switch (ch) {
				case 'å' :
					ch = '}';
					break;
				case 'ä' :
					ch = '{';
					break;
				case 'ö' :
					ch = '|';
					break;
				case 'ü' :
					ch = '~';
					break;
				case 'é' :
					ch = '`';
					break;
				case 'Å' :
					ch = ']';
					break;
				case 'Ä' :
					ch = '[';
					break;
				case 'Ö' :
					ch = '\\';
					break;
				case 'Ü' :
					ch = '^';
					break;
				case 'É' :
					ch = '@';
					break;
				case '\u2212' : // More or less temporary for time periods. /Markus 2000-11-13
					ch = '-';
					break;
			}
			sb.append(ch);
		}
		return sb.toString();
	}
	
	/**
	 * A convenience method that ensures that all directories
	 * in the path exists by creating them. Returns a File for the directory if succeeded.
	 * The path should look like "dir\dir" or "dir/dir".
	 */
	public static File createDirectories(String pathWithOnlyDirs) {
		File dir;
		dir = new File(pathWithOnlyDirs);
		// Ooops, no directory to check found, return false
		if (dir == null)
			return null;
		// Check if the directory exists, if it does all is well.
		if (dir.exists())
			return dir;
		// Ok, did not exist, then we create any directories
		if (dir.mkdirs())
			return dir;
		return null;
		/*
		public static void main(String[] args) {
			// Everything missing
			createDirectoriesForFile("/gb/file.txt");
			// Directory is there
			createDirectoriesForFile("/gb/file2.txt");
			// Multiple missing
			createDirectoriesForFile("/gb3/gb4/file.txt");
			// Multiple present
			createDirectoriesForFile("/gb3/gb4/file2.txt");
			// Only directory
			createDirectories("/gb4/gb5");
			// Only directory
			createDirectories("/gb6");
		}*/
	}
	
	/**
	 * A convenience method that ensures that all directories in the path 
	 * to the file exists by creating them. Returns a valid File object for the file if succeeded,
	 * otherwise null. The path should look like "dir\dir\file" or "dir/dir/file".
	 */
	public static File createDirectoriesForFile(String pathToFile) {
		File file, dir;
		file = new File(pathToFile);
		if (createDirectories(file.getParent()) != null)
			return file;
		return null;
	}
	public static String createExclusiveKey(String key) {
		int lastPos = key.length() - 1;
		if (lastPos >= 0) {
			char[] chars = key.toCharArray();
			chars[lastPos]++;
			key = new String(chars);
		}

		return key;
	}
	
	/**
		Returns a file, if it exists,  whose pathname is the pathname of the 
		<code>directoryPath</code> argument,relative to either the current 
		working directory or one of the directories in the java class path,
		followed by the separator character, followed by the <code>fileName</code> 
		argument.
		<br>
		If the file doesn't exists, <code>null</code> is returned,
	 */
	public static File createFile(String directoryPath, String fileName) {
		if (fileName == null) {
			/* raise exception, per Java Language Spec
			 * 22.24.6 & 22.24.7
			 */
			throw new NullPointerException();
		}
		String tFilePath;
		if (directoryPath != null) {
			tFilePath = (directoryPath.endsWith(File.separator)) ? directoryPath + fileName : directoryPath + File.separator + fileName;
		} else
			tFilePath = fileName;

		String tClassPath = System.getProperty("user.dir") + ";" + System.getProperty("java.class.path");
		int i = 0;
		while (i <= tClassPath.length() && i != -1) {
			int index = tClassPath.indexOf(";", i);
			File tFile = new File(tClassPath.substring(i, index != -1 ? index : tClassPath.length()) + File.separator + tFilePath);
			if (tFile.exists())
				return tFile;
			i = index != -1 ? index + 1 : index;
		}
		return null;
	}
	
	/**
		Returns a file, if it exists,  whose pathname is the pathname of the classfile for
		<code>resourceAnchorName</code> argument,relative to either the current working directory
		or one of the directories in the java class path, followed by the separator character,
		followed by the <code>fileName</code> argument. First <code>resourceAnchorName</code> is 
		resolved as a directory path, then <code>createFile</code> is called with the directory path
		as the first argument.
		<pre><code>
			CoBaseUtilities.createFileFor("com.bluebrim.base.shared.CoObjectIF","Test.gif") 
			called from inside VAJ returns, if it exists, a file whose path is
			E:\IBMVJava\IDE\project_resources\CalvinBas\se\corren\calvin\base\test.gif
		</code></pre>
	 */
	public static File createFileFor(String resourceAnchorName, String fileName) {
		String tSeparator = File.separator;
		String tResourcePath = resourceAnchorName.substring(0, resourceAnchorName.lastIndexOf(".")).replace('.', File.separatorChar);
		return createFile(tResourcePath, fileName);
	}
	
	/**
		Returns a file, if it exists,  for the <code>fileName</code> argument, whose directory 
		pathname is the result of the concatenation of the pathname for the <code>className</code> 
		argument,relative to either the current working directory or one of the directories in the
		java class path,and the <code>directory</code>, relative to the class path for <code>className</code>.
		<pre><code>
			CoBaseUtilities.createFileFor("com.bluebrim.base.shared.CoObjectIF","images","Test.gif") 
			called from inside VAJ returns, if it exists, a file whose path is
			E:\IBMVJava\IDE\project_resources\CalvinBas\se\corren\calvin\base\images\test.gif
		</code></pre>
	 */
	public static File createFileFor(String className, String directory, String fileName) {
		String tSeparator = File.separator;
		String tClassPath = className.substring(0, className.lastIndexOf(".")).replace('.', File.separatorChar);
		String tDirectoryPath = tClassPath + tSeparator + directory;
		return createFile(tDirectoryPath, fileName);
	}
	
	/**
		Returns a string with the class name for the <code>object</code> argument,
		without the package name and the hash value in hex.
	*/
	public static String debugInfo(Object object) {
		StringBuffer tBuffer = new StringBuffer();
		if (object != null) {
			String tClassName = object.getClass().getName();
			int tLastDot = tClassName.lastIndexOf(".");
			tBuffer.append(tClassName.substring(tLastDot + 1));
			tBuffer.append("@" + Integer.toHexString(object.hashCode()));
		} else
			tBuffer.append("null");

		return tBuffer.toString();
	}
	
	/**
	 *	Adds a dot at the beginning of the string if necessary
	 *  if the string is empty, the empty string is returned
	 */
	public static String ensureInitialDot(String str) {
		if (stringIsEmpty(str))
			return str;
		if (!str.startsWith(".")) {
			return "." + str;
		}
		return str;
	}
	
	/**
	 * Ensures that the string ends with a backslash
	 */
	public static String ensureTrailingFileSeparator(String path) {
		if (CoBaseUtilities.stringIsEmpty(path))
			return path;
		if (!path.endsWith(File.separator)) {
			return path + File.separator;
		}
		return path;
	}
	
	/**
		Utility method that returns true if the two arguments are equal.
		Encapsulates test for null,
	*/
	public static boolean equal(Object element1, Object element2) {
		if (element1 != null)
			return element1.equals(element2);
		else if (element2 != null)
			return false;
		else
			return true;
	}
	
	/**
	 * Utility method that returns true if the two arguments are equal.
	 * Encapsulates test for null,
	 * <br>
	 * <code>ignoreCase</code> tells if case should be ignored when
	 * comparing.
	 */
	public static boolean equal(String str1, String str2, boolean ignoreCase) {
		if (str1 != null)
			return ignoreCase ? str1.equalsIgnoreCase(str2) : str1.equals(str2);
		else if (str2 != null)
			return false;
		else
			return true;
	}
	
	/**
	 * Take a String and expand any variables contained within.
	 * Variables are surrounded by the character '%' and are substituted
	 * for their values in the map. If a variable is not found or if it has value null
	 * it will be substituted with the empty string.
	 *
	 * In order for this method to be thread-safe the map must of course not
	 * change during the execution of ths method...
	 *
	 * See CoBaseUtilities.testExpand() for a small test.
	 */
	public static String expandVariables(String string, Map variables) {
		String variableName;
		Object value;
		StringBuffer result = new StringBuffer();
		StringTokenizer st = new StringTokenizer(string, "%", true);
		try {
			// A string "abra%cad%abra simsamala bim" is broken into:
			// "abra", "%", "cad", "%", "abra simsamala bim"
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if (token.equalsIgnoreCase("%")) {
					// Substitute it, make sure we have a variablename and an end marker "%"
					if (st.hasMoreTokens()) {
						variableName = st.nextToken();
						// Check for escaped marker like "%%"
						if (variableName.equalsIgnoreCase("%")) {
							result.append(variableName);
						} else {
							// Find end marker
							if (st.hasMoreTokens()) {
								token = st.nextToken(); // It MUST be a "%" since these are the only delims we use
								// Ok, do the correct substitution
								value = variables.get(variableName);
								result.append(value);
							} else {
								// Broken variable.
								return string;
							}
						}
					}
					// The string ended with a marker - substitute with ""
				} else {
					result.append(token);
				}
			}
		} catch (Exception e) {
			// Something went wrong during parsing, do nothing - return string "unharmed".
			return string;
		}
		return result.toString();
	}
	
	public static int firstDigitIn(String str) {
		char chars[] = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isDigit(chars[i]))
				return i;
		}
		return -1;
	}
	
	public static int firstNonDigitIn(String str) {
		char chars[] = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!Character.isDigit(chars[i]))
				return i;
		}
		return -1;
	}
	
	/**
	 * Returns a formatted value of the phone number in <code>number</code>.
	 * It's assumed that it's in canonicalized form, eg +46-13-112233.
	 */
	public static String formatPhoneNumber(String number, String defaultCountryCode, String defaultAreaCode, String delimiter) {
		if (CoBaseUtilities.stringIsEmpty(number) || number.length() < 3)
			return number;
		String canonString = CoBaseUtilities.removeAllWhitespaces(number);
		if (CoBaseUtilities.stringIsEmpty(canonString))
			return canonString;

		String phoneNumber = null;
		String areaCode = null;
		String countryCode = null;

		CoReversedStringTokenizer tokenizer = new CoReversedStringTokenizer(canonString, delimiter);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (phoneNumber == null)
				phoneNumber = token;
			else if (areaCode == null)
				areaCode = token;
			else if (countryCode == null)
				countryCode = token;
		}

		String formatted = new String();
		int len = phoneNumber.length();
		if (len % 2 != 0 && len >= 3) {
			formatted = phoneNumber.substring(0, 3) + " ";
			phoneNumber = phoneNumber.substring(3);
		}

		len = phoneNumber.length();
		if (len >= 2) {
			for (int i = 0; i < len; i += 2) {
				formatted += phoneNumber.substring(i, i + 2) + " ";
			}
		} else
			formatted += phoneNumber;

		if (countryCode != null && countryCode.endsWith(defaultCountryCode))
			countryCode = null;

		if (countryCode == null) {
			if (areaCode != null && defaultAreaCode != null && defaultAreaCode.endsWith(areaCode))
				areaCode = null;
			else if (areaCode != null) {
				if (!areaCode.startsWith("0"))
					areaCode = "0" + areaCode;
			}
		} else if (countryCode != null) {
			areaCode = CoBaseUtilities.removeFromBeginning(areaCode, '0');
		}

		StringBuffer buffer = new StringBuffer();
		if (countryCode != null) {
			buffer.append(countryCode);
			buffer.append(delimiter);
		}
		if (areaCode != null) {
			buffer.append(areaCode);
			buffer.append(delimiter);
		}
		buffer.append(formatted);
		return CoBaseUtilities.removeTrailingWhitespaces(buffer.toString());
	}
	
	/**
	 *	Formats a string with the first char upper case and
	 *  the rest lower case
	 */
	public static String getFirstCharUpperCaseString(String str) {
		if (stringIsEmpty(str))
			return str;
		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}
	
	/**
	 *	Formats a string according to the following rules:
	 *  - The first character is upper-cased
	 *  - Any character following a white-space or hyphen is upper-cased
	 *  - All other characters are lower-cased
	 */
	public static String getInitialUpperCaseString(String str) {
		if (str == null)
			return str;
		// initialize prev with a white space char to make the first
		// char upper case
		char prev = '\t';
		int top = str.length();
		char[] formatted = new char[top];
		for (int idx = 0; idx < top; idx++) {
			char ch = str.charAt(idx);
			if (Character.isWhitespace(prev) || prev == '-') {
				formatted[idx] = Character.toUpperCase(ch);
			} else {
				formatted[idx] = Character.toLowerCase(ch);
			}
			prev = ch;
		}
		return new String(formatted);
	}
	
	/**
	 * calculate the real x scale from an AffineTransform
	 */
	public static double getXScale(AffineTransform t) {
		double q0 = t.getScaleX();
		double q1 = t.getShearX();
		return Math.sqrt(q0 * q0 + q1 * q1);
	}
	
	
	/**
	 * calculate the real y scale from an AffineTransform
	 */
	public static double getYScale(AffineTransform t) {
		double q0 = t.getScaleY();
		double q1 = t.getShearY();
		return Math.sqrt(q0 * q0 + q1 * q1);
	}
	
	public static boolean isNullOrEmpty(Object[] array) {
		return array == null || array.length == 0;
	}
	
	/**
		Returns true if the <code>aChar>/code> argument is a vowel.
		The method handles the vowels defined in the swedish alphabet.
	*/
	public static boolean isVowel(char aChar) {
		return (aChar == 'a')
			|| (aChar == 'A')
			|| (aChar == 'e')
			|| (aChar == 'E')
			|| (aChar == 'i')
			|| (aChar == 'I')
			|| (aChar == 'o')
			|| (aChar == 'O')
			|| (aChar == 'u')
			|| (aChar == 'U')
			|| (aChar == 'y')
			|| (aChar == 'Y')
			|| (aChar == 'å')
			|| (aChar == 'Å')
			|| (aChar == 'ä')
			|| (aChar == 'Ä')
			|| (aChar == 'ö')
			|| (aChar == 'Ö');
	}
	
	public static int lastDigitIn(String str) {
		char chars[] = str.toCharArray();
		for (int i = chars.length - 1; i >= 0; i--) {
			if (Character.isDigit(chars[i]))
				return i;
		}
		return -1;
	}
	
	private static int letterToNumber(char letter) {
		switch (letter) {
			case 'I' :
				return 1;
			case 'V' :
				return 5;
			case 'X' :
				return 10;
			case 'L' :
				return 50;
			case 'C' :
				return 100;
			case 'D' :
				return 500;
			case 'M' :
				return 1000;
			default :
				return -1;
		}
	}
	
	public static Properties loadProperties() {
		return loadProperties(new Properties());
	}
	
	public static Properties loadProperties(Properties props) {
		String filePath = System.getProperty("inifile");
		if (filePath != null) {
			File file = new File(filePath);
			// look for the file
			if (file.exists()) {
				try {
					System.out.println("Loading properties from " + filePath + " ...");
					// load any properties specified in the startup file
					props.load(new FileInputStream(file));
				} catch (IOException e) {
					System.out.println("Error reading specified ini file!");
				}
			} else {
				System.out.println("Ini file " + filePath + " not found, using defaults.");
			}
		}
		return props;
	}
	
	public static void main(String[] args) {
		testExpand();
	}
	
	/**
		Returns a new string that is <i>source</i> padded with <i>withChar</i>
		upto <i>toLen</i> length.
	*/
	public static String padAtBeginning(String source, int toLen, char withChar) {
		if (source == null) {
			source = "";
		}
		int srclen = source.length();
		if (srclen >= toLen)
			return source;
		else {
			int padLen = toLen - srclen;
			char result[] = new char[toLen];
			System.arraycopy(source.toCharArray(), 0, result, padLen, srclen);
			for (int i = 0; i < padLen; i++)
				result[i] = withChar;
			return new String(result);
		}
	}
	
	/**
		Returns a new string that is <i>source</i> padded with <i>withChar</i>
		upto <i>toLen</i> length.
	*/
	public static String padAtEnd(String source, int toLen, char withChar) {
		if (source == null) {
			source = "";
		}
		int srclen = source.length();
		if (srclen >= toLen)
			return source;
		else {
			char result[] = new char[toLen];
			System.arraycopy(source.toCharArray(), 0, result, 0, srclen);
			for (int i = srclen; i < toLen; i++)
				result[i] = withChar;
			return new String(result);
		}
	}
	
	/**
		Returns a new string that is the first string without any whitespaces
	*/
	public static String removeAllWhitespaces(String source) {
		if (CoBaseUtilities.stringIsEmpty(source))
			return source;

		StringBuffer sb = new StringBuffer();
		int top = source.length();
		for (int idx = 0; idx < top; idx++) {
			char ch = source.charAt(idx);
			if (!Character.isWhitespace(ch)) {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
	
	public static String removeBeginningWhitespaces(String source) {
		int tLen = source.length();
		StringBuffer tBuffer = new StringBuffer(tLen);
		boolean foundNonWhiteSpace = false;
		for (int i = 0; i < tLen; i++) {
			char tChar = source.charAt(i);
			if (foundNonWhiteSpace || !Character.isWhitespace(tChar)) {
				tBuffer.append(tChar);
				foundNonWhiteSpace = true;
			}
		}

		return tBuffer.toString();
	}
	
	/**
		Returns a new string that is equal to <i>source</i> with all 
		occurences of<i>aChar</i> removed.
	*/
	public static String removeFromBeginning(String source, char aChar) {
		int tLen = source.length();
		StringBuffer tBuffer = new StringBuffer(tLen);
		boolean foundNonCharacter = false;
		for (int i = 0; i < tLen; i++) {
			char tChar = source.charAt(i);
			if (foundNonCharacter || aChar != tChar) {
				tBuffer.append(tChar);
				foundNonCharacter = true;
			}
		}

		return tBuffer.toString();
	}
	
	public static String removeFromEnd(String source, char aChar) {
		int len = source.length();
		int index = -1;
		for (int i = len - 1; i >= 0; i--) {
			char iChar = source.charAt(i);
			if (iChar != aChar) {
				index = i == len - 1 ? -1 : i;
				break;
			}
		}

		return index != -1 ? source.substring(0, index + 1) : source;
	}
	
	public static String removeFromEnd(String source, char aChar, int minLength) {
		int len = source.length();
		int index = -1;
		for (int i = len - 1; i >= 0; i--) {
			char iChar = source.charAt(i);
			if (iChar != aChar || minLength - i > 0) {
				index = i == len - 1 ? -1 : i;
				break;
			}
		}

		return index != -1 ? source.substring(0, index + 1) : source;
	}
	
	/**
		Returns a new string that is equal to <i>source</i> with all 
		occurences of<i>aChar</i> removed.
	*/
	public static String removeFromString(String source, char aChar) {
		StringBuffer tBuffer = CoStringBufferPool.getStringBuffer();
		int tLen = source.length();
		for (int i = 0; i < tLen; i++) {
			char tChar = source.charAt(i);
			if (tChar != aChar)
				tBuffer.append(tChar);
		}

		String str = tBuffer.toString();
		CoStringBufferPool.releaseStringBuffer(tBuffer);
		return str;
	}
	
	public static String removeTrailingWhitespaces(String source) {
		int tLen = source.length();
		StringBuffer tBuffer = new StringBuffer(tLen);
		boolean foundNonWhiteSpace = false;
		for (int i = tLen - 1; i >= 0; i--) {
			char tChar = source.charAt(i);
			if (foundNonWhiteSpace || !Character.isWhitespace(tChar)) {
				tBuffer.insert(0, tChar);
				foundNonWhiteSpace = true;
			}
		}

		return tBuffer.toString();
	}
	
	public static String replace(String source, char oldChar, String newString) {
		int len = source.length();
		StringBuffer buffer = new StringBuffer(len);

		for (int i = 0; i < len; i++) {
			char iChar = source.charAt(i);
			if (iChar == oldChar)
				buffer.append(newString);
			else
				buffer.append(iChar);
		}

		return buffer.toString();
	}
	
	public static String romanToArabic(String str) throws NumberFormatException {
		if (CoBaseUtilities.stringIsEmpty(str))
			return str;

		String roman = str.toUpperCase();

		int i = 0; // A position in the string, roman;
		int arabic = 0; // Arabic numeral equivalent of the part of the string that has been converted so far.

		while (i < roman.length()) {
			char letter = roman.charAt(i); // Letter at current position in string.
			int number = letterToNumber(letter); // Numerical equivalent of letter.

			if (number < 0)
				throw new NumberFormatException("Illegal character \"" + letter + "\" in roman numeral.");
			i++; // Move on to next position in the string

			if (i == roman.length()) {
				// There is no letter in the string following the one we have just processed.
				// So just add the number corresponding to the single letter to arabic.
				arabic += number;
			} else {
				// Look at the next letter in the string.  If it has a larger Roman numeral
				// equivalent than number, then the two letters are counted together as
				// a Roman numeral with value (nextNumber - number).
				letter = roman.charAt(i);
				int nextNumber = letterToNumber(letter);
				if (nextNumber > number) {
					arabic += (nextNumber - number);
					i++;
				} else
					arabic += number;
			}
		}

		return String.valueOf(arabic);
	}
	
	/**
	 * If <code>str</code> is null or empty, 
	 * a space is returned.
	 */
	public static String spaceIfEmpty(String str) {
		return stringIsEmpty(str) ? " " : str;
	}
	
	/**
		Return true if <code>string</code> is either null or
		empty, i e has a length equal to 0.
	*/
	public static boolean stringIsEmpty(String string) {
		return string == null || string.length() == 0;
	}
	
	/**
	 * Return true if <code>string</code> is not null and
	 * has a length > 0.
	 */
	public static boolean stringIsNotEmpty(String string) {
		return !stringIsEmpty(string);
	}
	public static void testExpand() {
		HashMap map = new HashMap();
		map.put("username", "Hedgehog");
		map.put("number", new Integer(17));
		map.put("nullvalue", null);
		map.get("nullvalue");
		System.out.println(expandVariables("A text with a no variable in it.", map).equalsIgnoreCase("A text with a no variable in it."));
		System.out.println(expandVariables("A text with a %username% in it.", map).equalsIgnoreCase("A text with a Hedgehog in it."));
		System.out.println(expandVariables("A text %% with a %username in it.", map).equalsIgnoreCase("A text %% with a %username in it."));
		System.out.println(expandVariables("%%A text with a %username% in it.%", map).equalsIgnoreCase("%A text with a Hedgehog in it."));
		System.out.println(expandVariables("A %% text with a %username% in it.%%", map).equalsIgnoreCase("A % text with a Hedgehog in it.%"));
		System.out.println(expandVariables("A %% text with a %number% and %nullvalue% in it.%%", map).equalsIgnoreCase("A % text with a 17 and null in it.%"));
	}
	
	/**
	* Convert a byte[] array to a readable string format.
	* @return result String buffer in String format 
	* @param in byte[] buffer to convert to string format
	*/
	public static String toHex(byte in[]) {
		byte ch = 0x00;
		int i = 0;
		if (in == null || in.length <= 0)
			return null;

		String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
		StringBuffer out = new StringBuffer(in.length * 2);

		while (i < in.length) {
			ch = (byte) (in[i] & 0xF0); // Strip off high nibble
			ch = (byte) (ch >>> 4); // shift the bits down
			ch = (byte) (ch & 0x0F);
			out.append(pseudo[(int) ch]); // convert the nibble to a String Character 
			ch = (byte) (in[i] & 0x0F); // Strip off low nibble 
			out.append(pseudo[(int) ch]); // convert the nibble to a String Character 
			i++;
		}
		return out.toString();
	}
	
	/** 
	 * Converts a byte to a readable string.
	 * @return result String byte in hex format 
	 * @param b byte Byte to convert to string (hex) format
	 */
	public static String toHex(byte b) {
		byte[] barr = new byte[1];
		barr[0] = b;
		return toHex(barr);
	}
	
	/**
		Returns a new string that is <i>source</i> with <i>charCount</i>
		number of characters removed from the end.
		If <i>charCount</i> >= length of <i>source</i> an empty string is returned.
	*/
	public static String truncateString(String source, int charCount) {
		int tNewLength = Math.max(0, source.length() - charCount);
		return source.substring(0, tNewLength);
	}
	
	/**
		Returns a new string that is <i>source</i> with <i>suffix</i>
		removed from the end.
		If <i>suffix</i> is not at the end of <i>source</i>, <i>source</i> is returned.
	*/
	public static String truncateString(String source, String suffix) {
		return (source.endsWith(suffix)) ? truncateString(source, suffix.length()) : source;
	}
	
	/**
		Returns a new string that is the first <i>len<i> chars of <i>source</i>
	*/
	public static String truncateStringAt(String source, int len) {
		if (source.length() < len) {
			return source;
		}
		return source.substring(0, len);
	}
}