package com.bluebrim.gui.client;

import java.util.Comparator;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import com.bluebrim.base.shared.CoObjectIF;

/**
	An abstract aspectadaptor which implements <code>CoListValueable</code> and whose value is 
	a collection or list, i e concrete subclasses are responsible for implementing <code>get</code> 
	so it answers with a collection. Most of the methods dispatch to the listmodel, i e an instance of a subclass
	to <code>CoAbstractListModel</code> that implements the <code>ListModel</code> protocol. 
	<code>CoAbstractListModel</code> has separate subclasses for different types of collections, i e
	<ul> 
	<li><code>CoVectorModel</code> for <code>Vector</code>
	<li><code>CoGsVectorModel</code> for <code>GsVector</code>, i e the GemStone implementation of a remote, optimized <code>Vector</code>
	<li><code>CoCollectionListModel<code> for <code>List</code>, i e a collection class that implements the <code>List</code> protocol.
	</ul>
	A concrete subclass to <code>CoAbstractListAspectAadptor</code> must implement <code>getDefaultListModel</code> to answer with
	an instance of the right subclass of <code>CoAbstractListModel</code>. It must also implement <code>get</code> to answer with
	the actual collection in the business object.
	<br>
	[As a convenience there're two abstract subclasses, <code> CoListAspectAdaptor</code> and <code>CoGsListAspectAdaptor</code>, 
	that implemnt <code>getDefaultListModel<code> to answer with a <code>CoVectorModel</code> and a <code>CoGsVectorModel</code>
	respectively. These classes still need to be subclasses to implement <code>get</code> though.
	(This seems to be outdated information. /Magnus Ihse (magnus.ihse@appeal.se) (2001-05-17 13:30:57)]
	<br>
	<code>CoAbstractListAspectAdaptor</code> is immutable in the sense of not implementing any methods for add and delete.
	An abstract inner subclass, <code>Mutable</code> defines these operations by dispatching to a mutable subclass of
	<code>CoAbstractListModel</code>.
	<br>
	<code>CoAbstractListAspectAdaptor.Mutable</code> implements <code>getDefaultListModel</code> by calling another method,
	<code>createListModel</code> which should be implemented to answer an instance of a subclass 
	to <code>CoAbstractListModel.Mutabloe</code>.
	<br>
	Examples:
	<pre><code>
	Immutable:

		aBuilder.createListBoxAdaptor(aBuilder.addListAspectAdaptor( new CoAbstractListAspectAdaptor.Default(this, FROM_TO) {
			protected Object get(CoObjectIF subject) 
			{
				return ((com.bluebrim.subscription.shared.CoInterruptionIF) subject).getTimePeriods();
			}
		}), (CoListBox) getNamedWidget(FROM_TO));
	
	
	
	Mutable:
	
		protected CoAbstractListAspectAdaptor.Mutable createCatalogHolder() {
			return new CoGsListAspectAdaptor.Mutable(this, "ELEMENTS") {
				protected Object get(CoObjectIF subject) 
				{
					return ((CoFeeHolderIF) subject).getFees();
				}
				private CoFeeHolderIF getFeeHolder()
				{
					return (CoFeeHolderIF )getSubject();
				}
				public CoAbstractListModel.Mutable createListModel() 
				{
					return new CoCollectionListModel.Mutable(this) {
						protected boolean doRemoveElement(Object element)
						{
							return getFeeHolder().removeFee((CoFeeIF) element) != null;
						}
						protected void doAddElement(Object element)
						{
							getFeeHolder().addFee((CoFeeIF) element);
						}
					};
				}
		};
	}
	
	</code></pre>
 */
public abstract class CoAbstractListAspectAdaptor extends CoAspectAdaptor  implements CoListValueable
{
	private CoAbstractListModel m_listModel;
	
	public static abstract class Mutable extends CoAbstractListAspectAdaptor implements CoListValueable.Mutable {
		public Mutable (CoValueable context, String name) 
		{
			super(context, name);
		}
		public void addElement(Object element)
		{
			getMutableListModel().addElement(element);
		}
		public void addElements(Object elements[])
		{
			getMutableListModel().addElements(elements);		
		}
		public void removeElement(Object element)
		{
			getMutableListModel().removeElement(element);
		}
		public void removeElements(Object elements[])
		{
			getMutableListModel().removeElements(elements);
		}
		protected final CoAbstractListModel.Mutable getMutableListModel()
		{
			return (CoAbstractListModel.Mutable )this.getListModel();
		}
		public final CoAbstractListModel getDefaultListModel()
		{
			return createListModel();
		}
		protected abstract CoAbstractListModel.Mutable createListModel();
	};

	public static abstract class Default extends CoAbstractListAspectAdaptor {
		public Default(CoValueable context, String name) {
			super(context, name);
		}
		public Default(CoValueable context, String name, boolean subjectFiresChange) {
			super(context, name, subjectFiresChange);
		}
		public CoAbstractListModel getDefaultListModel() {
			return new CoCollectionListModel.Default(this);
		}
	}

/**
 */
public CoAbstractListAspectAdaptor(CoValueable context, String name) 
{
	this(context, name,false);
}
/**
 */
public CoAbstractListAspectAdaptor(CoValueable context, String name, boolean subjectFiresChange) 
{
	super(context, name,subjectFiresChange);
}
protected void _setListModel(CoAbstractListModel model)
{
	CoAbstractListModel tOldModel = listModel();
	if (tOldModel != null)
	{
		removeValueListener(tOldModel);
	}
	m_listModel = model;
	if (model != null)
	{
		addValueListener(model);
	}
}
/**
 * @param l CoSelectionListener
 */
public void addSelectionListener ( CoSelectionListener l)
{
	listenerList.add(CoSelectionListener.class, l);
}
public boolean contains(Object element) {
 return (m_listModel != null) ? m_listModel.contains(element) : false;
}
public void elementHasChanged(Object source, Object element) {
	getListModel().elementHasChanged(source, element);
}
public abstract CoAbstractListModel getDefaultListModel();
/**
	Svarar med objekt som ligger på position 'index' i listan.
 */
public Object getElementAt(int index) {
	return getListModel().getElementAt(index);
}
/**
 * getSize method comment.
 */
public final CoAbstractListModel getListModel() {
	if (m_listModel == null)
		setListModel(getDefaultListModel());
	return m_listModel;
}
/**
 * getSize method comment.
 */
public int getSize() {
	return getListModel().getSize();
}
protected Object getValueFor(CoObjectIF aSubject)
{
	return (aSubject != null) ? get(aSubject): null;
}
public int indexOf(Object element) {
 return (m_listModel != null) ? m_listModel.indexOf(element) : -1;
}
public boolean isHandlingTransactions()
{
	return false;
}
public void listHasChanged(Object source) 
{
	resetCachedValue();
	getListModel().listHasChanged(source);
}
/**
 * getSize method comment.
 */
protected final CoAbstractListModel listModel() {
	return m_listModel;
}
private void propagateSelectionChanged(CoSelectionEvent e)
{

	Object tListeners[] 		= listenerList.getListenerList();
	Class tListenerClass		= CoSelectionListener.class;
	CoSelectionEvent tEvent 	= null;
	for (int k = tListeners.length - 2; k >= 0; k -= 2)
	{
	   if (tListeners[k] == tListenerClass)
	   {
				if (tEvent == null)
					tEvent = new CoSelectionEvent(this, e.getListSelectionModel());
				((CoSelectionListener)tListeners[k + 1]).selectionChange(tEvent);
	   }
	}
}
private void propagateValueChanged(ListSelectionEvent e)
{

	Object tListeners[] 		= listenerList.getListenerList();
	Class tListenerClass		= CoSelectionListener.class;
	CoSelectionEvent tEvent 	= null;
	for (int k = tListeners.length - 2; k >= 0; k -= 2)
	{
	   if (tListeners[k] == tListenerClass)
	   {
				if (tEvent == null)
					tEvent = new CoSelectionEvent(this, (ListSelectionModel )e.getSource());
				((CoSelectionListener)tListeners[k + 1]).selectionChange(tEvent);
	   }
	}
}
/**
 * @param l CoSelectionListener
 */
public void removeSelectionListener (CoSelectionListener l)
{
	listenerList.remove(CoSelectionListener.class, l);
}
/**
 * As default it's not possible to change the list in 
 * the domain object via the aspect adaptor.
 */
public void set(CoObjectIF subject, Object value)
{
	// NOP
}
/**
 * getSize method comment.
 */
public void setListModel(CoAbstractListModel model) {
	if (model != m_listModel)
	{
		_setListModel(model != null ? model : getDefaultListModel());
	}
}
/**
 */
public void setModelFor(JList list) {
	list.setModel(getListModel());
}
/**
 * Support for com.bluebrim.base.client.CoTileMapPanel.
 */
public void setModelFor(com.bluebrim.gui.client.CoTileMapPanel tilePanel) {
	tilePanel.setModel(getListModel());
}
public void sort(Comparator c)
{
	getListModel().sort(c);
}
public void valueChanged(ListSelectionEvent e) 
{
	propagateValueChanged(e);
}
}
