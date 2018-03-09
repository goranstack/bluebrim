package com.bluebrim.xml.shared;

/**
 * Insert the type's description here.
 * Creation date: (1999-09-28 11:43:03)
 * @author:  
 */
public abstract class CoStateModelBuilder extends CoSimpleModelBuilder {
	private Object m_state;

/**
 * CoStrokeModelBuilder constructor comment.
 * @param parser com.bluebrim.xml.shared.CoXmlParserIF
 */
public CoStateModelBuilder(CoXmlParserIF parser) {
	super(parser);
}
public Object getState() {
	return m_state;
}
	public void initializeFrom(CoModelBuilderIF builder) {
		if(builder instanceof CoStateModelBuilder) {
			m_state = ((CoStateModelBuilder)builder).getState();
		}
	}
	public void setState(Object obj) {
		m_state = obj;
	}
}
