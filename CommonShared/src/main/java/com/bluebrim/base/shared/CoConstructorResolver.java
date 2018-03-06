package com.bluebrim.base.shared;

import java.lang.reflect.*;

/**
 * Helper class to find the most suitable constructor
 * by reflection.
 * 
 * @author Markus Persson 2002-08-16
 */
public class CoConstructorResolver {
	private Constructor[] m_constructors;
	private Class[][] m_paramLists;

	public CoConstructorResolver(Class type) {
		m_constructors = type.getConstructors();
		int len = m_constructors.length;
		m_paramLists = new Class[len][];
		for (int i = 0; i < len; i++) {
			Constructor constructor = m_constructors[i];
			m_paramLists[i] = constructor.getParameterTypes();
		}
	}

	/**
	 * Create an instance by passing the given </code>args to a constructor
	 * following.
	 * NOTE: It is the length of </code>types<code> that is important.
	 * </code>args<code> may be longer, but not shorter.
	 */
	public Object createWith(Object[] args, Class[] types) {
		Class[][] paramLists = m_paramLists;

		int best = -1;
		int numParams = types.length;
		int len = paramLists.length;
		
		outer:
		for (int i = 0; i < len; i++) {
			Class[] params = paramLists[i];
			if (params.length == numParams) {
				for (int p = 0; p < numParams; p++) {
					Class param = params[p];
					if (!param.isAssignableFrom((args[p] != null)
						? args[p].getClass() : types[p])) {
						 	continue outer;
					}
				}
				best = i;
				break;
			}
		}
		
		if (best >= 0) {
			if (args.length != numParams) {
				Object[] oldArgs = args;
				args = new Object[numParams];
				System.arraycopy(oldArgs, 0, args, 0, numParams);
			}

			try {
				return m_constructors[best].newInstance(args);
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}

		return null;
	}
}
