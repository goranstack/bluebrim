package com.bluebrim.base.shared.debug;

import java.util.*;

//

public class CoTimer
{
	private String m_name;

	private long m_t0;
	private long m_t;
	private long m_T;

	private List m_samples = new ArrayList();

	private class Sample
	{
		public String m_text;
		public long m_t;
		public Sample( String text, long t )
		{
			m_text = text; m_t = t;
		}
	}


	private static Map m_timers = new HashMap();
public CoTimer( String name )
{
	m_name = name;
}
public static CoTimer get( String name )
{
	return (CoTimer) m_timers.get( name );
}
public void sample( String text )
{
	long t = System.currentTimeMillis();
	m_samples.add( new Sample( text, t - m_t ) );
	m_t = t;
}
public void start()
{
	m_t0 = System.currentTimeMillis();
	m_t = m_t0;

	m_timers.put( m_name, this );
}
public void stop()
{
	m_timers.remove( m_name );
	
	m_T = System.currentTimeMillis() - m_t0;
}
public String toString()
{
	String str = "Timer " + m_name + ", total time: " + m_T;

	for
		( int i = 0; i < m_samples.size(); i++ )
	{
		str += "\n";
		Sample s = (Sample) m_samples.get( i );
		str += "  " + s.m_text + " : " + s.m_t + "  " + 100 * s.m_t / (float) m_T + " %";
	}

	return str;
}
}
