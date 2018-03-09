package com.bluebrim.text.shared;

import java.util.*;

import com.bluebrim.xml.shared.*;

/**
 * Constants for XML parsing/creating
 * Creation date: (1999-09-17 14:38:09)
 * @author: 
 */
public class CoXmlTextConstants implements CoXmlConstantsIF, CoTextConstants {
	// A hashmap for mapping keywords to integers.
	private static HashMap words = new HashMap();
	private static HashMap m_textMap = new HashMap();
	static {
		// ...and a map for keywords used when importing/exporting text as XML		
		m_textMap.put(ALIGN_CENTER, "center");
		m_textMap.put(ALIGN_FORCED, "forced");
		m_textMap.put(ALIGN_JUSTIFIED, "justified");
		m_textMap.put(ALIGN_LEFT, "left");
		m_textMap.put(ALIGN_RIGHT, "right");

		m_textMap.put("center", ALIGN_CENTER);
		m_textMap.put("forced", ALIGN_FORCED);
		m_textMap.put("justified", ALIGN_JUSTIFIED);
		m_textMap.put("left", ALIGN_LEFT);
		m_textMap.put("right", ALIGN_RIGHT);
		
	}

	public static Object getMappedForText(Object obj) {
		Object o = m_textMap.get(obj);
		return o == null ? obj : o;
	}
	public static Object getMappedObjectFor(Object obj) {
		return words.get(obj);
	}
	public static String getMappedStringFor(Object obj) {
		Object ret = words.get(obj);
		if(ret != null) {
			return ret.toString();
		} else {
			return "none";
			//return "no value mapped for obj " + obj.toString();
		}
	}
}