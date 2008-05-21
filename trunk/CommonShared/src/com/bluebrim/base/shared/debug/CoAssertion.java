package com.bluebrim.base.shared.debug;

import java.io.*;

import com.bluebrim.observable.*;

/**
 * Debugklass.
 * <code>CoAssertion</code> inneh�ller ett antal debugvariabler, PRE, POST, TRACE m fl. Till varje variabel finns en metod
 * som �r t�nkt att anv�ndas tillsammans med denna f�r att hj�lpa till med fels�kning. Observera att de fel som 
 * vi letar efter och vill f�nga upp �r av karakt�ren programmeringsfel och allts� inte �r fel som "naturligt" kan 
 * uppst� under exekveringen. F�r dessa typer av fel �r Java's vanliga exceptionhanteringen b�ttre. Finessen med CoAssertion
 * �r att vi genom att villkora anropen av dess metoder med hj�lp av motsvarande klassvariabler kan full kontroll under
 * utveckling men sedan st�nga allt (eller l�mpliga delar) innan leverans genom att s�tta v�rdet av variablerna till false.
 * Kompilatorerna �r d� s� smarta att de kompilererar bort hela anropet (enligt uppgift :-)). 
 * <br>
 * Detta bygger p� att klassvariablerna �r deklarerade som <code>final</code>. Av praktiska sk�l s�tts dock
 * i dagsl�get dessa variabler fr�n system properties.
 * <br>
 * Test av ing�ngsvillkor:
 * <pre>
 * 	public void foo(int x, int y)
 * 	{
 * 		if (CoAssertion.PRE)
 * 			CoAssertion.preCondition((x >= 0) && (y >=0), "x och/eller y inte giltiga: < 0");
 * 		....
 * 		....
 * 	}
 * </pre>
 * <br>	
 * 	Test av utg�ngsvillkor:
 * 	<pre>
 * 	public void foo(int x, int y)
 * 		{
 * 			int result;
 * 			...
 * 			...
 *  		if (CoAssertion.POST)
 * 				CoAssertion.postCondition((result > 0), "Resultatet <= 0");
 * 			return result;
 * 	}
 * 	</pre>
 * 	<code>invariant</code>:<br>
 * 	De klasser som vill g�ra en kontroll av hela instansen m�ste implementera CoInvariantIF och metoden #invariant
 * 	som skall svara med true om klassens tillst�nd �r "g�tt".
 * 	<pre>
 * 		public class Foo implements CoInvariantIF {
 * 			public void foo(int x, int y)
 * 	 		{
 * 	 			...
 * 	  			if (CoAssertion.INVARIANT)
 * 	  				CoAssertion.invariant(this);
 * 			}
 * 			public boolean invariant()
 * 	 		{
 * 	 			//Kontrollerar v�rdet av instansvariabler mm och svarar
 * 	 			//med true om instansen �r ok annars false.
 * 			}
 * 		}
 *  	</pre>
 *  <code>trace</code> �r t�nkt att anv�ndas n�r vi vill logga in- och urg�ng ur en metod eller del av metod.
 * 	<pre>
 *  	public void foo(int x, int y)
 * 		{
 *  			if (CoAssertion.TRACE)
 *   				CoAssertion.trace("ENTERING foo");
 * 			...
 * 			...
 * 			...
 *   			if (CoAssertion.TRACE)
 *   				CoAssertion.trace("LEAVING foo");
 * 		}
 *  	</pre>
 *  <code>assert</code> anv�nds f�r allm�nna kontroller som inte �r att rubricera som pre- eller postkontroller. 
 * 	<pre>
 *  	public void foo(int x, int y)
 *  	{
 *  			...
 * 			...
 * 			int tSaldo = getSaldo(x,y);
 *   			if (CoAssertion.ASSERT)
 *   				CoAssertion.assert((tSaldo > 0),"Saldo <= 0");
 *   			...
 *   			...
 * 		}
 * 	</pre>
 *  <code>hal</code> anv�nds f�r kunna s�tta brytpunkter i inre klasser. F�rhoppningsvis kommer behovet av denna variabel och metod
 *  att f�rsvinna n�r VAJ st�djer brytpunkter i inre klasser.
 * 	<pre>
 *  	public void foo(int x, int y)
 *  	{
 *  			...
 * 			...
 * 			ActionListener tListener = new ActionListener () {
 * 				public void actionPerformed(ActionEvent e)
 * 				{
 * 					if (CoAssertion.HALT)
 * 						CoAssertion.halt("");
 * 					...
 * 					...
 * 				}
 * 			}
 *   			...
 *   			...
 * 		}
 * 	</pre>
 */
public abstract class CoAssertion {

	// NOTE: These variables should be final (and false) in order to
	// compile code away. They are probably not final now since that
	// could possibly trigger heavy recompilation in VisualAge
	// whenever this class is changed (which is not often). /Markus
	// The values of these static variables are now set from 
	// system properties. This makes it possible to toggle the debugging 
	// by an ini-file or -D switches./Lasse

	public static boolean PRE;
	public static boolean POST;
	public static boolean INVARIANT;
	public static boolean ASSERT;
	public static boolean HALT;
	public static boolean TRACE;
	public static boolean SIMULATION_SUPPORT;
	public static long TIME_STAMP;

/*
	private static Set m_changedObjects = new TreeSet(new Comparator() {
		public int compare(Object o1, Object o2) {
			return (o1 == o2) ? 0 : o1.hashCode() - o2.hashCode();
		}
	});
*/
	static {
		initializeAssertions();
	}
	
	public static void addChangedObject(Object t) {
		CoObservable.objectChanged(t);
	}
	
	/**
	 * Renamed from assert to avoid conflict with the new
	 * assert keyword in JDK1.4. /Markus 2002-05-13
	 */
	public static void assertTrue(boolean booleanExpr, String message) {
		if (!booleanExpr)
			throw new CoAssertionFailedException("Assertion failed " + message);
	}
	
	public static Object fakeSerialization(Object o) {
		byte[] tmp = null;
		String ex = null;

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);

			oos.writeObject(o);
			oos.close();

			tmp = baos.toByteArray();
		} catch (java.io.IOException e) {
			ex = e.toString();
		} catch (SecurityException e) {
			ex = e.toString();
		}

		assertTrue(tmp != null, "CoAssertion.fakeSerialization: error while writing object to byte array \n" + o + "\n" + ex);

		Object O = null;

		java.io.ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new ByteArrayInputStream(tmp));
			O = ois.readObject();
			ois.close();
		} catch (StreamCorruptedException e) {
			ex = e.toString();
		} catch (ClassNotFoundException e) {
			ex = e.toString();
		} catch (IOException e) {
			ex = e.toString();
		}

		assertTrue(O != null, "CoAssertion.fakeSerialization: error while reading object from byte array \n" + o + "\n" + ex);

		return O;
	}
	
	public static void halt(String message) {
		System.out.println("HALT:" + message);
	}
	
	public static void invariant(CoInvariantIF object) {
		if (!object.invariant())
			throw new CoInvariantFailedException("Invariant failed for " + object);
	}
	
	public static void postCondition(boolean booleanExpr, String message) {
		if (!booleanExpr)
			throw new CoPostConditionFailedException("Postcondition failed " + message);
	}
	
	public static void preCondition(boolean booleanExpr, String message) {
		if (!booleanExpr)
			throw new CoPreConditionFailedException("Precondition failed " + message);
	}

	public static void startTimer() {
		TIME_STAMP = System.currentTimeMillis();
		System.out.println("Start timer");
	}

	public static void logTimer(String msg) {
		System.out.println(msg + (System.currentTimeMillis() - TIME_STAMP) / 1000);
	}
	
	public static void stopTimer() {
		logTimer("Stop timer: ");
		TIME_STAMP = 0L;
	}

	public static void trace(String message) {
		System.out.println("TRACE: " + message);
	}

	private static void initializeAssertions() {
		if (getBoolean("calvin.debug.assertions", true)) {
			PRE = getBoolean("calvin.debug.pre", true);
			POST = getBoolean("calvin.debug.post", true);
			ASSERT = getBoolean("calvin.debug.assert", true);
			INVARIANT = getBoolean("calvin.debug.invariant", true);
			HALT = getBoolean("calvin.debug.halt", true);
			TRACE = getBoolean("calvin.debug.trace", false);
			SIMULATION_SUPPORT = getBoolean("calvin.debug.simulation", true);
		}
	}

	private static boolean getBoolean(String key, boolean def) {
		return System.getProperty(key, def ? "true" : "false").equalsIgnoreCase("true");
	}

	public static void showAssertionStatus() {
		System.out.println("*************************");
		System.out.println(" PRE: " + PRE);
		System.out.println(" POST: " + POST);
		System.out.println(" INVARIANT: " + INVARIANT);
		System.out.println(" ASSERT: " + ASSERT);
		System.out.println(" HALT: " + HALT);
		System.out.println(" TRACE: " + TRACE);
		System.out.println(" SIMULATION_SUPPORT: " + SIMULATION_SUPPORT);
		System.out.println("*************************");
	}

	/**
	 * Tests if an Object is not null. If so, a CoAssertionFailedException is thrown. Recommended use:
	 * CoAssertion.notNull(myFoo, "myFoo");
	 *
	 * Creation date: (2001-08-27 13:47:17)
	 * @param o The object to test.
	 * @param name The name of the variable, to be used in the exception message. May be null.
	 * @author Magnus Ihse (magnus.ihse@appeal.se)
	 */
	public static void notNull(Object o, String name) {
		if (o == null) {
			throw new CoAssertionFailedException("Null not an allowed value for " + name);
		}
	}
}