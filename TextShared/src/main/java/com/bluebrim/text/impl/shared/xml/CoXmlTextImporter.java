package com.bluebrim.text.impl.shared.xml;

import java.io.*;
import java.util.*;

import javax.swing.text.*;

import org.apache.crimson.tree.*;
import org.w3c.dom.*;

import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Imports formatted text from an XML representation to a com.bluebrim.text.shared.CoStyledDocument. Importing is done
 * using a two step process:
 * 1 - Traversing the XML tree, computing bounds (in the character data) for all nodes and storing the bounds
 *     keyed on the XML node. During the traversal of the tree, the characters (text) are also stored. The character buffer
 *     is added to the styled document (so far unformatted)
 * 2 - For each node encountered during traversal, apply (to the styled document) the appropriate formatting on the node's bounds
 *     in the styled document.
 * Creation date: (1999-10-12 18:23:29)
 * @author: Mikael Printz
 */
public class CoXmlTextImporter extends CoAbstractTextImporter {



	private HashMap m_boundsMap = new HashMap();
	private StringBuffer m_characters = new StringBuffer();

	/**
	 * Holds bounds for a node
	 */
	public class NodeBounds {
		private int m_lower;
		private int m_upper;

		public NodeBounds(int lower, int upper) {
			setLower(lower);
			setUpper(upper);
		}

		public void setLower(int lower) {
			m_lower = lower;
		}

		public void setUpper(int upper) {
			m_upper = upper;
		}

		public int getLower() {
			return m_lower;
		}

		public int getUpper() {
			return m_upper;
		}

		public NodeBounds envelope(NodeBounds nb) {
			if(nb == null) {
				return this;
			}
			return new NodeBounds(Math.min(nb.getLower(), getLower()), Math.max(nb.getUpper(), getUpper()));
		}

		public String toString() {
			return "(" + m_lower + "-" + m_upper + ")";
		}
	}


	/**
	 *	Applies formatting to the styled document. The bounds for the different formattings has been
	 *  computed earlier in the traversion of the XML document
	 *
	 */
	private void applyFormatting(CoStyledDocument doc) {
		// Iterate through the nodes encountered in the XML representation
		Iterator iter = m_boundsMap.keySet().iterator();

		while(iter.hasNext()) {
			Node curNode = (Node)iter.next();
			NodeBounds curBounds = (NodeBounds)m_boundsMap.get(curNode);
			if(curBounds == null) {
				// No bounds have been stored for this node. Probably one of the top nodes. Not interesting formatting-wise - try next node
				continue;
			}
			int offset = curBounds.getLower();
			int length = curBounds.getUpper() - offset;
			formatNode(curNode, offset, length, doc);
		}
	}
	/**
	 *	Augments the XML with a header in order for it to be a valid standalone XML-document
	 */
	private String augmentXML(String xml) {
		// Add a header to the XML to make it a valid XML doc. 
		// Right now the DTD for the formatted text is also included in the generated document
		// in an effort to minimize the number of external files needed.
		// The DTD, as it is specified here, is extremely verbose since internal dtd:s do not
		// support parameter entitities, which made it necessary to expand the dtd with regard to
		// nested elements
//		m_xml = "<?xml version=\"1.0\" encoding=\"iso8859-1\"?> <!DOCTYPE text PUBLIC '' 'file:c:/xml/text.dtd' []>" + xml;
//		xml = "<?xml version=\"1.0\" encoding=\"iso8859-1\"?> <!DOCTYPE text [  <!ELEMENT text (p)* > <!ATTLIST text > <!ELEMENT p (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST p class CDATA \"normal\" align CDATA #IMPLIED > <!ELEMENT b (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST b > <!ELEMENT un_b (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_b > <!ELEMENT i (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST i > <!ELEMENT un_i (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_i > <!ELEMENT u (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST u val CDATA \"UNDERLINE_NORMAL\" > <!ELEMENT st (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST st > <!ELEMENT un_st (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_st > <!ELEMENT ol (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST ol > <!ELEMENT un_ol (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_ol > <!ELEMENT sh (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST sh > <!ELEMENT un_sh (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_sh > <!ELEMENT ac (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST ac > <!ELEMENT un_ac (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_ac > <!ELEMENT sc (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST sc > <!ELEMENT un_sc (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_sc > <!ELEMENT su (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST su > <!ELEMENT un_su (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_su > <!ELEMENT font (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST font face CDATA #IMPLIED size CDATA #IMPLIED color CDATA #IMPLIED shade CDATA #IMPLIED >]>" + xml;
// 		xml = "<?xml version=\"1.0\" encoding=\"iso8859-1\"?> <!DOCTYPE text [<!ELEMENT text (p)* > <!ATTLIST text > <!ELEMENT p (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST p class CDATA \"normal\" align CDATA #IMPLIED foreground_color CDATA #IMPLIED last_in_column CDATA #IMPLIED leading CDATA #IMPLIED space_above CDATA #IMPLIED underline CDATA #IMPLIED lines_together CDATA #IMPLIED line_breaker CDATA #IMPLIED drop_caps_count CDATA #IMPLIED small_caps CDATA #IMPLIED space_below CDATA #IMPLIED outline CDATA #IMPLIED float_font_size CDATA #IMPLIED superior CDATA #IMPLIED shadow CDATA #IMPLIED bold CDATA #IMPLIED foreground_shade CDATA #IMPLIED font_family CDATA #IMPLIED auto_leading CDATA #IMPLIED strike_thru CDATA #IMPLIED italic CDATA #IMPLIED left_indent CDATA #IMPLIED top_of_column CDATA #IMPLIED one_liner CDATA #IMPLIED vertical_position CDATA #IMPLIED track_amount CDATA #IMPLIED first_line_indent CDATA #IMPLIED all_caps CDATA #IMPLIED right_indent CDATA #IMPLIED drop_caps CDATA #IMPLIED drop_caps_height CDATA #IMPLIED > <!ELEMENT b (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST b > <!ELEMENT un_b (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_b > <!ELEMENT i (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST i > <!ELEMENT un_i (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_i > <!ELEMENT u (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST u val CDATA \"UNDERLINE_NORMAL\" > <!ELEMENT st (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST st > <!ELEMENT un_st (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_st > <!ELEMENT ol (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST ol > <!ELEMENT un_ol (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_ol > <!ELEMENT sh (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST sh > <!ELEMENT un_sh (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_sh > <!ELEMENT ac (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST ac > <!ELEMENT un_ac (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_ac > <!ELEMENT sc (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST sc > <!ELEMENT un_sc (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_sc > <!ELEMENT su (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST su > <!ELEMENT un_su (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_su > <!ELEMENT font (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST font face CDATA #IMPLIED size CDATA #IMPLIED color CDATA #IMPLIED shade CDATA #IMPLIED >]>" + xml;
//		xml = "<?xml version=\"1.0\" encoding=\"iso8859-1\"?> <!DOCTYPE text [<!ELEMENT text (p)* > <!ATTLIST text > <!ELEMENT p (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST p class CDATA \"normal\" align CDATA #IMPLIED font-color CDATA #IMPLIED lc CDATA #IMPLIED ld CDATA #IMPLIED sa CDATA #IMPLIED u CDATA #IMPLIED lt CDATA #IMPLIED line_breaker CDATA #IMPLIED dcc CDATA #IMPLIED sc CDATA #IMPLIED sb CDATA #IMPLIED ol CDATA #IMPLIED font-size CDATA #IMPLIED su CDATA #IMPLIED sh CDATA #IMPLIED b CDATA #IMPLIED font-shade CDATA #IMPLIED font-family CDATA #IMPLIED al CDATA #IMPLIED st CDATA #IMPLIED i CDATA #IMPLIED li CDATA #IMPLIED tc CDATA #IMPLIED ub CDATA #IMPLIED vp CDATA #IMPLIED ta CDATA #IMPLIED fli CDATA #IMPLIED ac CDATA #IMPLIED ri CDATA #IMPLIED dc CDATA #IMPLIED dcln CDATA #IMPLIED> <!ELEMENT b (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST b > <!ELEMENT un_b (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_b > <!ELEMENT i (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST i > <!ELEMENT un_i (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_i > <!ELEMENT u (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST u val CDATA \"UNDERLINE_NORMAL\" > <!ELEMENT st (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST st > <!ELEMENT un_st (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_st > <!ELEMENT ol (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST ol > <!ELEMENT un_ol (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_ol > <!ELEMENT sh (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST sh > <!ELEMENT un_sh (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_sh > <!ELEMENT ac (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST ac > <!ELEMENT un_ac (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_ac > <!ELEMENT sc (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST sc > <!ELEMENT un_sc (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_sc > <!ELEMENT su (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST su > <!ELEMENT un_su (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST un_su > <!ELEMENT font (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font)* > <!ATTLIST font face CDATA #IMPLIED size CDATA #IMPLIED color CDATA #IMPLIED shade CDATA #IMPLIED >]>" + xml;
		// ENCODING???
		//xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <!DOCTYPE text [<!ENTITY % subnode 'b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag' > <!ELEMENT text (p)* > <!ELEMENT p (#PCDATA | ta | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ATTLIST p class CDATA \"normal\" align CDATA #IMPLIED font-color CDATA #IMPLIED lc CDATA #IMPLIED ld CDATA #IMPLIED sa CDATA #IMPLIED u CDATA #IMPLIED lt CDATA #IMPLIED line_breaker CDATA #IMPLIED dcc CDATA #IMPLIED sc CDATA #IMPLIED sb CDATA #IMPLIED ol CDATA #IMPLIED font-size CDATA #IMPLIED su CDATA #IMPLIED sh CDATA #IMPLIED b CDATA #IMPLIED font-shade CDATA #IMPLIED font-family CDATA #IMPLIED al CDATA #IMPLIED st CDATA #IMPLIED i CDATA #IMPLIED li CDATA #IMPLIED tc CDATA #IMPLIED ub CDATA #IMPLIED vp CDATA #IMPLIED ta CDATA #IMPLIED fli CDATA #IMPLIED ac CDATA #IMPLIED ri CDATA #IMPLIED dc CDATA #IMPLIED dcln CDATA #IMPLIED > <!ELEMENT b (#PCDATA | ta | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT un_b (#PCDATA | ta | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT i (#PCDATA | ta | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT un_i (#PCDATA | ta | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT u (#PCDATA | ta | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ATTLIST u val CDATA \"UNDERLINE_NORMAL\" > <!ELEMENT st (#PCDATA | ta | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT un_st (#PCDATA | ta | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT ol (#PCDATA | ta | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT un_ol (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT sh (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT un_sh (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT ac (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT un_ac (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT sc (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT un_sc (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT su (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT un_su (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ELEMENT ctag (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ATTLIST ctag style CDATA #IMPLIED > <!ELEMENT font (#PCDATA | ta | b | un_b | i | un_i | u | st | un_st | ol | un_ol | sh | un_sh | ac | un_ac | sc | un_sc | su | un_su | font | ctag)* > <!ATTLIST font face CDATA #IMPLIED size CDATA #IMPLIED color CDATA #IMPLIED shade CDATA #IMPLIED >]>" + xml;
		xml = "<?xml version=\"1.0\" encoding=\"iso8859-1\" standalone=\"yes\"?>" + xml;

		return xml;
	}

	/**
	 *	Computes bounds for the nodes in the XML representation. The encountered
	 *  character data is also saved in the m_characters string buffer. The computed
	 *  bounds are later used to apply the character formats represented by the
	 *  XML nodes
	 */
	private NodeBounds computeBounds(Node node) {
		NodeBounds bounds = null;
		if(node instanceof Text) {
			String data = ((Text)node).getData();

			// The current position in the character stringbuffer is the beginning of the node's bounds
			int beg = m_characters.length();
			m_characters.append(data);

			if (node.getParentNode().getNodeName().equals("p")) {
				// Append a linefeed to the end of all paragraphs
				m_characters.append("\n");
			}
			
			bounds = new NodeBounds(beg, beg + data.length());
			
		} else {
			// Iterate through the children and compute the bounds for the node
			NodeList list = node.getChildNodes();
			int top = list.getLength();
			for(int idx = 0; idx < top; idx++) {
				Node currentNode = list.item(idx);

				if(bounds == null) {
					bounds = computeBounds(currentNode);
				} else {
					bounds = bounds.envelope(computeBounds(currentNode));
				}
			}
		}

		// Save the bounds for the node (to be used later when applying formats)
		m_boundsMap.put(node, bounds);
		return bounds;
	}
/**
 * This method is a placeholder needed because the <code>abstract</code> class
 * <code>CoAbstractTextImporter</code> requires it to be present.
 *
 * @return This method has no side effects and always returns <code>null</code>.
 *
 * @deprecated Use {@link doImport(Node, CoStyledDocument)} instead, this method
 * is broken.
 */
public CoStyledDocument doImport( Reader r, CoStyledDocument doc )
{
	return null;
}

/**
 *	Applies formatting to characters
 */
private void formatCharacters(Node node, int offset, int length, CoStyledDocument doc) {
	boolean isInverse;
	String tag = node.getNodeName();
	// Check if it is a character tag node
	NamedNodeMap nodeAttrs = node.getAttributes();
	if(tag.equals("ctag")) {
		Attr attr = (Attr)nodeAttrs.getNamedItem("style");
		if(attr != null) {
			String val = attr.getValue();
			doc.setCharacterTag(offset, length, val);
			return;
		}
	}
	// Check if it is an 'un_'-tag
	if (isInverse = isInverseTagName(tag)) {
		tag = stripTagName(tag);
	}

	// Check if there are any attributes associated with the tag
	boolean hasAttribute = nodeAttrs != null && nodeAttrs.getLength() != 0;
	
	CoAttributeTranslator translator = null;
	// Handle font. A bit ugly mapping between the format of the XML and the format for character styles
	if(tag.equals("font")) {
		String f;
		Attr attr = (Attr)nodeAttrs.getNamedItem("face");
		f = "font-family";
		if(attr == null) {
			attr = (Attr)nodeAttrs.getNamedItem("size");
			f = "font-size";
		}
		if(attr == null) {
			attr = (Attr)nodeAttrs.getNamedItem("color");
			f = "font-color";
		}
		if(attr == null) {
			attr = (Attr)nodeAttrs.getNamedItem("shade");
			f = "font-shade";
		}
		tag = f;
		translator = CoAttributeTranslator.getTranslator(f);
		tag = attr.getValue();
	} else {
		translator = CoAttributeTranslator.getTranslator(tag);
	}

	MutableAttributeSet as = new CoSimpleAttributeSet();
	// Determine the value to set for the character attribute 
	if (translator != null) {
		Object val = null;
		if(!hasAttribute) {
			// The node has no attribute. It is a character tag that can have value true or false (e.g. b or un_b)
			// If it is an 'un_'-tag, the value is set to false otherwise true; b -> true, un_b -> false
			val = isInverse ? Boolean.FALSE : Boolean.TRUE;
		} else {
			// Generic handling of tags with a single attribute
			// A tag with an attribute. Try to get the value using the first attribute
			Attr attr = (Attr)nodeAttrs.item(0);
			val = translator.string2value(attr.getValue());
		}
		if (val != null) {
			as.addAttribute(translator.getAttribute(), val);
		}
	}
	doc.setCharacterAttributes(offset, length, as, false);
}
/**
 *	Applies formatting to nodes
 */
private void formatNode(Node node, int offset, int length, CoStyledDocument doc) {
	// Nodes are either paragraph nodes or nodes within a paragraph that specifies
	// style for characters
	String tag = node.getNodeName();
	if(tag.equals("p")) {
		formatParagraph(node, offset, length, doc);
	} else {
		formatCharacters(node, offset, length, doc);
	}
}
/**
 *	Applies formatting on the paragraph level
 */
private void formatParagraph(Node node, int offset, int length, CoStyledDocument doc) {
	NamedNodeMap map = node.getAttributes();
	Attr attr = (Attr) map.getNamedItem("class");
	// It is a paragraph - set paragraph attributes... Could be no tag
	if(attr != null) {
		doc.setParagraphTag(offset, length - 1, attr.getValue());
	}
	// Handle optional properties
	int top = map.getLength();
	MutableAttributeSet as = new CoSimpleAttributeSet();
	for (int idx = 0; idx < top; idx++) {
		attr = (Attr) map.item(idx);
		String aName = attr.getName();
		if (!aName.equals("class")) {
			CoAttributeTranslator translator = CoAttributeTranslator.getTranslator(aName);
			if (translator != null) {
				as.addAttribute(translator.getAttribute(), translator.string2value(CoXmlTextConstants.getMappedForText(attr.getValue()).toString()));
			} else {
				System.out.println("*-*-*-*-* Could not get translator for " + aName);
			}
		}
		doc.setParagraphAttributes(offset, length - 1, as, false);
	}
}
	private boolean isInverseTagName(String tag) {
		return tag.startsWith("un_");
	}
	/**
	 *	Strips 'un_' from the tagname if present
	 */
	private String stripTagName(String tag) {
		if(isInverseTagName(tag)) {
			// If it is an inverse tag, we know it starts with 'un_'
			return tag.substring(3);
		}
		return tag;
	}

	private CoStyledDocument buildStyledDocument(CoStyledDocument doc, Node startNode) throws CoXmlReadException {
		if ( doc == null ) doc = new CoStyledDocument();

		// Iterate through xml document saving the encountered text nodes hashed on their nodes in the XML tree
		TreeWalker walker = new TreeWalker(startNode);
		// Traverse the xml-document, computing bounds 
		computeBounds(startNode);

		if ((m_characters != null) &&
			(m_characters.length() > 0) &&
			(m_characters.charAt(m_characters.length() - 1) == '\n'))
		{
			m_characters.deleteCharAt(m_characters.length() - 1);
		}
		
		try {
			doc.insertString(0, m_characters.toString(), null);
		} catch(BadLocationException ble) {
			ble.printStackTrace(System.out);
			throw new CoXmlReadException("Error when inserting text into styled document");
		}
		// Apply the formatting for paragraphs and characters
		applyFormatting(doc);
		Node curNode = null;
	
		return doc;
	}

public CoStyledDocument doImport( Node startNode, CoStyledDocument doc ) throws CoXmlReadException {
	return buildStyledDocument( doc, startNode );
}
}