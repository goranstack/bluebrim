package com.bluebrim.base.shared;


public abstract class CoObjectPool {


	private class PoolElement {
		public boolean m_isFree = true;
		public Object m_object;

		public PoolElement(Object obj)
		{
			m_object = obj;
		}
	}
	
	private Object[] 		m_objectPool;
	private int  			m_initialCapacity;
	private int				m_poolSize;
 
	public CoObjectPool(int initialCapacity)
	{
		m_initialCapacity 	= initialCapacity;
		m_poolSize			= initialCapacity;
		m_objectPool 		= new Object[initialCapacity];
		for (int i=0; i<m_poolSize; i++)
			m_objectPool[i] = createObject();
	}

	public final Object getObject()
	{
		for (int i=0; i<m_poolSize; i++)
		{
			Object obj = m_objectPool[i];
			if (obj != null)
			{
				m_objectPool[i] = null;
				return prepareObject(obj);
			}
		}
		return expandCapacity();
	}
	public final void releaseObject(Object obj)  {
		for (int i=0; i<m_poolSize; i++)
		{
			Object object = m_objectPool[i];
			if (object == null)
			{
				m_objectPool[i] = obj;
				return;
			}
		}
	}

	protected abstract Object createObject();


protected Object expandCapacity()
{
	int         oldSize		= m_poolSize;
	int 		newCapacity = m_poolSize+m_initialCapacity;
	Object 		objects[]	= new Object[newCapacity];
	System.arraycopy(m_objectPool, 0, objects,0, m_poolSize);
	for (int i=m_poolSize; i<newCapacity; i++)
	{
		objects[i] = createObject();
	}
	m_objectPool 	= objects;
	m_poolSize 		= newCapacity;
	return prepareObject(m_objectPool[oldSize]);
}


	protected Object prepareObject(Object obj)
	{
		return obj;
	}
}