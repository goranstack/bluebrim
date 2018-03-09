package com.bluebrim.xml.shared;
import java.lang.reflect.*;
import java.util.*;

import org.w3c.dom.*;

/**
 * This is a wrapper class.  It wraps a <code>Class</code> implementing the
 * <code>CoXmlEnabledIF</code> interface, and during XML import passes
 * calls on to the target class.
 * <p>
 * Creation date: (2001-06-18 15:03:32)
 * 
 * @author Johan Walles
 */
public class CoXmlImportEnabledModelBuilder extends CoModelBuilder {
	/** The class we are wrapping */
	private Class m_xmlImportEnabledClass = null;

	public CoXmlImportEnabledModelBuilder(CoXmlParserIF parser) {
		super(parser);
	}

	public void addNode(Node node, CoXmlContext context) {
		// We are not interested in any random nodes, so this method is intentionally left blank
	}

	public void addSubModel(String parameter, Object subModel, CoXmlContext context) throws CoXmlReadException {
		((CoXmlImportEnabledIF) m_model).xmlAddSubModel(parameter, subModel, context);
	}

	public Class classToBuild(final Object superModel, final Node node, final CoXmlContext context) {
		return m_xmlImportEnabledClass;
	}

	/**
	  * Create the new object using the class' implementation of the
	  * <code>CoXmlImportLazyCreationIF.xmlCreateModel</code> method.
	  */
	public void createModel(Object superModel, Collection subModels, Node node, CoXmlContext context) throws CoXmlReadException {
		// Try letting the super model of the current node creating this node
		m_model = superModelCreateModel(superModel, node, context);
		if (m_model != null) {
			return;
		}

		// Find the xmlCreateModel method
		Method createModel;
		Class argumentTypes[] = { Object.class, Collection.class, Node.class, CoXmlContext.class };

		try {
			createModel = m_xmlImportEnabledClass.getMethod("xmlCreateModel", argumentTypes);
		} catch (NoSuchMethodException e) {
			throw new CoXmlReadException(
				"Class "
					+ m_xmlImportEnabledClass
					+ " must implement public static method xmlCreateModel(Object, Collection, Node, CoXmlContext).\n"
					+ "For more information, see class javadoc for CoXmlImportLazyCreationIF.");
		}

		// Verify that the method is actually public static
		if ((!Modifier.isStatic(createModel.getModifiers())) || (!Modifier.isPublic(createModel.getModifiers()))) {
			throw new CoXmlReadException(
				m_xmlImportEnabledClass
					+ ".xmlCreateModel(Object, Collection, Node, CoXmlContext) must be public static.\n"
					+ "For more information, see class javadoc for CoXmlImportLazyCreationIF.");
		}

		// Call the xmlCreateModel method
		Object arguments[] = { unwrapParentObject(superModel), subModels, node, context };

		try {
			m_model = (CoXmlImportEnabledIF) createModel.invoke(null, arguments);
		} catch (IllegalAccessException e) {
			// We were not allowed to call xmlCreateModel
			throw new CoXmlReadException(
				"Class "
					+ m_xmlImportEnabledClass
					+ " must implement public static method xmlCreateModel(Object, Collection, Node, CoXmlContext),\n"
					+ "but the call to xmlCreateModel was not allowed.  For more information, see class javadoc for CoXmlImportLazyCreationIF.");
		} catch (InvocationTargetException e) {
			// xmlCreateModel threw an exception
			e.printStackTrace();
			throw (new CoXmlReadException("xmlCreateModel failed: " + e.getTargetException()));
		}
	}

	/**
	  * Create the new object using the class' implementation of the
	  * <code>CoXmlImportEnabledIF.xmlCreateModel</code> method.
	  */
	public void createModel(Object superModel, Node node, CoXmlContext context) throws CoXmlReadException {
		// Try letting the super model of the current node creating this node
		m_model = superModelCreateModel(superModel, node, context);
		if (m_model != null) {
			return;
		}

		// Find the xmlCreateModel method
		Method createModel;
		Class argumentTypes[] = { Object.class, Node.class, CoXmlContext.class };

		try {
			createModel = m_xmlImportEnabledClass.getMethod("xmlCreateModel", argumentTypes);
		} catch (NoSuchMethodException e) {
			throw new CoXmlReadException(
				"Class "
					+ m_xmlImportEnabledClass
					+ " must implement public static method xmlCreateModel(Object, Node, CoXmlContext).\n"
					+ "For more information, see class javadoc for CoXmlImportEnabledIF.");
		}

		// Verify that the method is actually public static
		if ((!Modifier.isStatic(createModel.getModifiers())) || (!Modifier.isPublic(createModel.getModifiers()))) {
			throw new CoXmlReadException(
				m_xmlImportEnabledClass
					+ ".xmlCreateModel(Object, Node, CoXmlContext) must be public static.\n"
					+ "For more information, see class javadoc for CoXmlImportEnabledIF.");
		}

		// Call the xmlCreateModel method
		Object arguments[] = { unwrapParentObject(superModel), node, context };

		try {
			m_model = (CoXmlImportEnabledIF) createModel.invoke(null, arguments);
		} catch (IllegalAccessException e) {
			// We were not allowed to call xmlCreateModel
			throw new CoXmlReadException(
				"Class "
					+ m_xmlImportEnabledClass
					+ " must implement public static method xmlCreateModel(Object, Node, CoXmlContext),\n"
					+ "but the call to xmlCreateModel was not allowed.  For more information, see class javadoc for CoXmlImportEnabledIF.");
		} catch (InvocationTargetException e) {
			// xmlCreateModel threw an exception
			e.printStackTrace();
			throw (new CoXmlReadException("xmlCreateModel failed: " + e.getTargetException()));
		}
	}

	public Object getModel(Node node, CoXmlContext context) {
		return m_model;
	}

	public void setXmlImportEnabled(Class xmlImportEnabledClass) {
		if (CoXmlImportEnabledIF.class.isAssignableFrom(xmlImportEnabledClass)) {
			m_xmlImportEnabledClass = xmlImportEnabledClass;
		} else {
			throw new IllegalArgumentException(xmlImportEnabledClass + " must implement CoXmlImportEnabledIF");
		}
	}

	/**
	  * Let the parent model create the current model if possible.  It is considered possible if
	  * the parent model implements a method with the following signature:
	  * <p>
	  * <code>public&nbsp;CoXmlImportEnabledIF&nbsp;xmlCreate<i>NameOfSubModelClass</i>(Node&nbsp;node,&nbsp;CoXmlContext&nbsp;context)</code>
	  */
	private CoXmlImportEnabledIF superModelCreateModel(Object superModel, Node node, CoXmlContext context) throws CoXmlReadException {
		Object parent = unwrapParentObject(superModel);

		if (parent == null) {
			return null;
		}
		
		// Find out the name of the factory method
		String createModelMethodName = m_xmlImportEnabledClass.getName();
		createModelMethodName = "xmlCreate" + createModelMethodName.substring(createModelMethodName.lastIndexOf(".") + 1);

		// Find the factory method
		Method createModel;
		Class argumentTypes[] = { Node.class, CoXmlContext.class };

		try {
			createModel = parent.getClass().getMethod(createModelMethodName, argumentTypes);
		} catch (NoSuchMethodException e) {
			return null;
		}

		// Verify that the method is actually public but not static
		if ((Modifier.isStatic(createModel.getModifiers())) || (!Modifier.isPublic(createModel.getModifiers()))) {
			String superModelClassName = parent.getClass().getName();

			System.out.println("Warning: " + superModelClassName + " has a " + createModelMethodName);
			System.out.println("       but it has the wrong signature (should be public non-static)");
			return null;
		}

		// Call the xmlCreateModel method
		Object arguments[] = { node, context };

		CoXmlImportEnabledIF returnMe = null;

		try {
			returnMe = (CoXmlImportEnabledIF) (createModel.invoke(parent, arguments));
		} catch (IllegalAccessException e) {
			String superModelClassName = parent.getClass().getName();

			// We were not allowed to call xmlCreateModel
			throw new CoXmlReadException("Failed to call " + superModelClassName + "." + createModelMethodName + "(Node, CoXmlContext)");
		} catch (InvocationTargetException e) {
			// xmlCreateModel threw an exception
			throw (new CoXmlReadException(createModelMethodName + " failed: " + e.getTargetException()));
		}

		return returnMe;
	}

	public void xmlImportFinished(Node node, CoXmlContext context) throws CoXmlReadException {
		((CoXmlImportEnabledIF) m_model).xmlImportFinished(node, context);
	}

	/**
	 * When the parent object is a <code>CoNamedObject</code> the parent object
	 * of real interest is the <code>CoNamedObject</code>'s parent.
	 */	
	private Object unwrapParentObject(Object object) {
		if (object instanceof CoNamedObject)
			return ((CoNamedObject)object).getParentObject();
		else
			return object;
	}
}