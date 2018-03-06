package com.bluebrim.postscript.shared;

/**
 * Postscript target specifications. Includes specific requirements that affect the postscript
 * generation. The "target" is defined as the Postscript file with its properties. A typical
 * target property could be Postscript Language Level (2 or 3), or wether to include a EPS preview
 * or not.
 *
 * <p><b>Creation date:</b> 2001-07-24
 * <br><b>Documentation last updated:</b> 2001-10-31
 * 
 * PENDING: Change names containing "resolution" to DPI or similar!
 * Possibly write javadoc instead ... /Markus 2002-08-27
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoPostscriptTarget {
	public final static CoPostscriptTarget EPS_FILE = new EpsFile();
	public final static CoPostscriptTarget PS_FILE = new PostscriptFile();
	public final static CoPostscriptTarget DESKTOP_PRINTER = new DesktopPrinter();
	public final static CoPostscriptTarget TYPESETTER = new Typesetter();

	public final static CoPostscriptMedia DEFAULT_MEDIA = CoPostscriptMedia.A4;

	private boolean m_encapsulatable;
	private boolean m_includePreview;
	private boolean m_shrinkToFit;
	private boolean m_enlargeToFill;
	private boolean m_centerHorizontally;
	private boolean m_centerVertically;
	private int m_psLevel;
	private boolean m_requireHighPrecision;
	private int m_resolution;
	private int m_previewResolution;
	private String m_psHeader;
	private CoPostscriptMedia m_media;

	public static class EpsFile extends CoPostscriptTarget {
		public EpsFile() {
			super(true, true, 2, false, 600, " EPSF-3.0", CoPostscriptMedia.A4);
		}

		protected void initialize() {
			super.initialize();
			setPreviewResolution(36);
		}
	}

	public static class PostscriptFile extends CoPostscriptTarget {
		public PostscriptFile() {
			super(true, false, 2, true, 600, " EPSF-3.0", CoPostscriptMedia.NEWSPAPER);
		}
	}

	public static class Typesetter extends CoPostscriptTarget {
		public Typesetter() {
			super(false, false, 3, true, 1200, "", CoPostscriptMedia.NEWSPAPER);
		}

		protected void initialize() {
			super.initialize();
			setCenterHorizontally(false);
			setCenterVertically(false);
		}
	}

	public static class DesktopPrinter extends CoPostscriptTarget {
		public DesktopPrinter() {
			super(false, false, 2, false, 600, "", CoPostscriptMedia.A4);
		}
	}
	
	public boolean isEncapsulatable() {
		return m_encapsulatable;
	}

	public void setEncapsulatable(boolean encapsulatable) {
		m_encapsulatable = encapsulatable;
	}

	public int getIntendedResolution() {
		return m_resolution;
	}

	public void setIntendedResolution(int resolution) {
		m_resolution = resolution;
	}

	public int getLevel() {
		return m_psLevel;
	}

	public void setLevel(int psLevel) {
		m_psLevel = psLevel;
	}

	public String getTargetPostscriptHeader() {
		return m_psHeader;
	}

	public void setTargetPostscriptHeader(String psHeader) {
		m_psHeader = psHeader;
	}

	public boolean isCenterHorizontally() {
		return m_centerHorizontally;
	}

	public boolean isCenterVertically() {
		return m_centerVertically;
	}

	public boolean isEnlargeToFill() {
		return m_enlargeToFill;
	}

	public boolean isShrinkToFit() {
		return m_shrinkToFit;
	}

	public void setCenterHorizontally(boolean centerHorizontally) {
		m_centerHorizontally = centerHorizontally;
	}

	public void setCenterVertically(boolean centerVertically) {
		m_centerVertically = centerVertically;
	}

	public void setEnlargeToFill(boolean enlargeToFill) {
		m_enlargeToFill = enlargeToFill;
	}

	public void setShrinkToFit(boolean shrinkToFit) {
		m_shrinkToFit = shrinkToFit;
	}

	public CoPostscriptTarget() {
		this(false, false, 2, true, 600, "");
	}

	/**
	 * This method is guaranteed to be called from the constructor after default values have been assigned. 
	 * Use this as a replacement for the constructor when creating anonymous inner subclasses of
	 * CoPostscriptTarget.
	 * Creation date: (2001-08-07 13:16:17)
	 * @author Magnus Ihse (magnus.ihse@appeal.se)
	 */
	protected void initialize() {
	}

	public CoPostscriptTarget(boolean encapsulatable, boolean includePreview, int psLevel, boolean requireHighPrecision, int resolution, String psHeader) {
		this(encapsulatable, includePreview, psLevel, requireHighPrecision, resolution, psHeader, DEFAULT_MEDIA);
	}

	public CoPostscriptTarget(boolean encapsulatable, boolean includePreview, int psLevel, boolean requireHighPrecision, int resolution, String psHeader, CoPostscriptMedia media) {
		this(encapsulatable, includePreview, psLevel, requireHighPrecision, resolution, psHeader, media, false, false, true, true);
	}

	public CoPostscriptTarget(
		boolean encapsulatable,
		boolean includePreview,
		int psLevel,
		boolean requireHighPrecision,
		int resolution,
		String psHeader,
		CoPostscriptMedia media,
		boolean shrinkToFit,
		boolean enlargeToFill,
		boolean centerHorizontally,
		boolean centerVertically) {

		m_encapsulatable = encapsulatable;
		m_includePreview = includePreview;
		m_psLevel = psLevel;
		m_requireHighPrecision = requireHighPrecision;
		m_resolution = resolution;
		m_psHeader = psHeader;
		m_media = media;
		m_shrinkToFit = shrinkToFit;
		m_enlargeToFill = enlargeToFill;
		m_centerHorizontally = centerHorizontally;
		m_centerVertically = centerVertically;

		initialize();
	}

	public CoPostscriptMedia getMedia() {
		return m_media;
	}

	public int getPreviewResolution() {
		return m_previewResolution;
	}

	public boolean isIncludePreview() {
		return m_includePreview;
	}

	public boolean isRequireHighPrecision() {
		return m_requireHighPrecision;
	}

	public void setIncludePreview(boolean includePreview) {
		m_includePreview = includePreview;
	}

	public void setMedia(CoPostscriptMedia media) {
		m_media = media;
	}

	public void setPreviewResolution(int previewResolution) {
		m_previewResolution = previewResolution;
	}

	public void setRequireHighPrecision(boolean requireHighPrecision) {
		m_requireHighPrecision = requireHighPrecision;
	}
}