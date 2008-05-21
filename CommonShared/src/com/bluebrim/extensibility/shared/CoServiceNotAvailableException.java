package com.bluebrim.extensibility.shared;

/**
 * Execption indicating that a service is unavailable.
 * 
 * PENDING: Recreate the class object at deserialization if it is available?
 * @author Markus Persson 2002-03-14
 */
public class CoServiceNotAvailableException extends RuntimeException {
	private String m_spiName;
	private transient Class m_spi;

	public CoServiceNotAvailableException(Class spi) {
		m_spi = spi;
		m_spiName = spi.getName();
	}

    public String getMessage() {
    	// WARNING: Contains marketing language ...
        return "No service provider is available for " + m_spiName + ".\n"
			+ "Make sure that the relevant module is installed properly.";
    }

	/**
	 * Returns true if the reason this exception was thrown was that
	 * the service represented by <code>spi</code> wasn't available.
	 * NOTE: That this method returns false, doesn't mean that the
	 * <code>spi</code> service is available.
	 */
	public boolean wasUnavailableService(Class spi) {
		return (m_spi != null) ? (m_spi == spi) : m_spiName.equals(spi.getName());
	}

	/**
	 * Returns the name of the interface representing the service whose
	 * unavailablilty caused this exception to be thrown.
	 */
	public String getUnavailableServiceInterfaceName() {
		return m_spiName;
	}
}
