package com.bluebrim.font.shared;
import java.awt.font.*;
import java.rmi.*;
import java.util.*;

import com.bluebrim.base.shared.debug.*;
import com.bluebrim.font.impl.shared.*;
import com.bluebrim.font.shared.metrics.*;

/**
 * Provides safe access to the font repository for read-only purposes. This is an abstract superclass
 * for the server and client version of the FontMapper.
 *
 * FontMapper is designed to facilitate access to existing font data, such as font metrics and font face
 * listings, primarily by the LayoutEditor.
 *
 * <p><b>Creation date:</b> 2001-04-25
 * <br><b>Documentation last updated:</b> 2001-09-18
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see com.bluebrim.font.impl.client.CoClientFontMapper
 * @see com.bluebrim.font.impl.server.CoFontRepository.ServerFontMapper
 */
public abstract class CoAbstractFontMapper {
	// NOTE: Creation moved here since static initialization is much less dangerous
	// than lazy initialization. /Markus
	// FontRenderContext(affineTransformation, isAntiAliased, usesFractionalMetrics) 
	private static FontRenderContext FRC = new FontRenderContext(null, false, false);

	// NOTE: Only used when on the client, but needs to be in a common place. /Markus
	private static CoAbstractFontMapper CLIENT_MAPPER;

	public final static String BASIC_FALLBACK_FONT = "sansserif";  // what to fallback to in worst case
/**
 * Returns true if the CoFontFace exists, i.e. is or has been used as the mapping for a CoFontFaceSpec
 * at any point in time, and has binary data associated with it, and false otherwise.
 *
 * @param face the font face to check for existance.
 *
 * @return true if the CoFontFace exists.
 *
 * @see #hasNewerVersion(CoFontFace)
 * @see #isRemoved(CoFontFace)
 */
public boolean exists(CoFontFace face) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(face, "face");
	
	return getFontFaces().contains(face);
}


/**
 * Finds the font face that is "closest" to matching the specified font face spec. The heuristics
 * used is based on the distanceTo method of the font face spec. This method returns null if
 * no fonts with that family name is installed, otherwise it is guaranteed to return a font face.
 *
 * @param spec the font face spec to match against.
 *
 * @return the closest font face, or null if no font family matches.
 */
public CoFontFace findClosestFontFace(CoFontFaceSpec spec) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(spec, "spec");

	// do a linear search for minimum distance in this font family
	CoFontFaceSpec closestMatch = null;
	int closestDistance = Integer.MAX_VALUE;
	
	Iterator i = getSpecsForFamily(spec.getFamilyName()).iterator();

	while (i.hasNext()) {
		CoFontFaceSpec thisSpec = (CoFontFaceSpec) i.next();
		int thisDistance = thisSpec.distanceTo(spec);
		if (thisDistance < closestDistance) {
			closestMatch = thisSpec;
			closestDistance = thisDistance;
		}
	}
	
	if (closestMatch == null) {
		return null;
	} else {
		return getFontFace(closestMatch);
	}
}


/**
 * Returns a Set of the names of all CoFontCatalogs.
 *
 * @return a Set of the names of all CoFontCatalogs.
 */
public abstract Set getAllCatalogs();


/**
 * Fetches the AwtData from the repository for the associated CoFontFace, 
 * or from the cache if possible and applicable.
 *
 * @param face the font face to get AwtData for.
 *
 * @return the AwtData associated with the specified CoFontFace.
 */
public abstract CoFontAwtData getAwtData(CoFontFace face);


/**
 * Returns the CoFontCatalog associated with this font catalog name, or null if none.
 *
 * @param name the name of the catalog to retrieve.
 *
 * @return the CoFontCatalog associated with this font catalog name, or null if none.
 */
public abstract CoFontCatalog getCatalog(String name);


/**
 * Returns the fallback font family name. This is the name of a font family that is guaranteed to exist.
 *It is recommended not to use this method, but to instead use
 * {@link #getRealFallbackFamily}. This method returns the same value as <code>getRealFallbackFamily</code>,
 * unless this value is <code>null</code>. In that case (i.e. if no explicitly definied fallback family 
 * exists), this method returns a predefined "hard-wired" default, that is guaranteed to exist.  
 *
 * @return the fallback font family name, guaranteed to exist.
 *
 * @see #getRealFallbackFamily
 */
public String getFallbackFontFamily() {
	String fallbackFamily = getRealFallbackFamily();

	return (fallbackFamily == null) ? BASIC_FALLBACK_FONT : fallbackFamily;
}



/**
 * Returns the CoFontFace currently associated with this <code>CoFontFaceSpec</code>, or null if none. This will
 * only return an exact match. See {@link:getFontFaceOrFallback} for a failsafe version, which always returns a
 * valid font face.
 *
 * @param spec the font face spec to retrieve a font face for.
 *
 * @return the CoFontFace currently associated with this <code>CoFontFaceSpec</code>, or null if none.
 *
 * @see #getFontFace(CoFontFaceSpec, int)
 * @see #getFontFaceOrFallback(CoFontFaceSpec)
 */
public abstract CoFontFace getFontFace(CoFontFaceSpec spec);


/**
 * Returns the CoFontFace associated with this <code>CoFontFaceSpec</code> and version. If no such CoFontFace has
 * ever been registered, this function will return null. (A new CoFontFace will _not_ be created.)
 * This is the preferred way of finding a CoFontFace of a specific version number.
 *
 * @param spec the font face spec to retrieve a font face for.
 * @param version the version number of the font face to retrieve.
 *
 * @return the CoFontFace currently associated with this font face spec and version, or null if no such font face exists. 
 *
 * @see #getFontFace(CoFontFaceSpec)
 * @see #getFontFaceOrFallback(CoFontFaceSpec)
 */
public CoFontFace getFontFace(CoFontFaceSpec spec, int version) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(spec, "spec");
	
	Iterator i = getFontFaces().iterator();
	CoFontFace face = new CoFontFace(spec, version);

	// loop through font faces to canonicalize
	while (i.hasNext()) {
		CoFontFace thisFace = (CoFontFace) i.next();
		if (thisFace.equals(face)) {
			return thisFace;
		}
	}

	return null;
}


/**
 * Returns the best-matching font face for a given font face spec. If a font face exists, which spec exactly
 * matches the given spec, then this is guaranteed to be returned. If no such font face exists, a fallback
 * font face is found using heuristics to get a close match as possible to the specified <code>CoFontFaceSpec</code>.
 * The method is guaranteed to always return an existing <code>CoFontFace</code>.
 *
 * @param spec the font face spec to retrieve the font face for.
 *
 * @return the best-matching font face for this spec.
 */
public CoFontFace getFontFaceOrFallback(CoFontFaceSpec spec) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(spec, "spec");
	
	// first try to get the fontface from the spec
	CoFontFace face = getFontFace(spec);
	
	if (face == null) {
		// not found, so try find closest match
		face = findClosestFontFace(spec);

		if (face == null) { // we don't even got this font family installed...
			
			// get the fallback spec for the fallback font family name
			face = findClosestFontFace(new CoFontFaceSpec(getFallbackFontFamily(), CoFontConstants.NORMAL_WEIGHT,
				CoFontConstants.NORMAL_STYLE, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH));
			
			// if this too is null, then we don't got one single font installed
			// this should just not happen
			com.bluebrim.base.shared.debug.CoAssertion.assertTrue(face != null, "No fallback font family specified.");
		}

		System.out.println("Returning fallback font face (" + face + ") for spec: " + spec);
	}

	return face;
}


/**
 * Returns a set of all font faces available.
 *
 * @return a set of all font faces available.
 */
public abstract Set getFontFaces();


/**
 * Returns a list of all available font families. The list is returned as a <code>CoFontCatalog</code>.
 *
 * @return a font catalog of all available font families.
 */
public abstract CoFontCatalog getFontFamilies();


/**
 * Fetch the CoFontFileContainer from the repository for the associated CoFontFace, 
 * or from the cache if possible and applicable.
 *
 * @param face the font face to get the file container for.
 *
 * @return the file container associated with the specified CoFontFace.
 */
public abstract CoFontFileContainer getFontFileContainer(CoFontFace face);


/**
 * Returns an instance of the current font mapper on this system. This is the correct way to access the font mapper.<p>
 * The methods dispatches the request to either the client-side or the server-side font mapper, depending 
 * on the environment. These methods in turn returns the singleton instance of their respective font mapper.
 *
 * @return the singleton instance of the font mapper to use.
 */
public static CoAbstractFontMapper getFontMapper() {
	try
    {
        return CoFontServerProvider.getFontServer().getFontRepository().getFontMapper();
    } catch (RemoteException e)
    {
        throw new RuntimeException(e);
    }
}


/**
 * Convenience method to get a correct FontRenderContext for AWT in certain methods.
 *
 * @return an instance of FontRenderContext to pass to AWT.
 */
public static FontRenderContext getFontRenderContext() {
	return FRC;
}

/**
 * Fetch the font metrics data from the repository for the associated CoFontFace, 
 * or from the cache if possible and applicable.
 *
 * @param face the font face to get the metrics for.
 *
 * @return the font metrics associated with the specified CoFontFace.
 */
public abstract CoFontMetricsData getMetricsData(CoFontFace face);


/**
 * Fetch the postscript data from the repository for the associated CoFontFace, 
 * or from the cache if possible and applicable.
 *
 * @param face the font face to get the postscript data for.
 *
 * @return the postscript data associated with the specified CoFontFace.
 */
public abstract CoFontPostscriptData getPostscriptData(CoFontFace face);


/**
 * Returns a set of all the font face specs that is available for a specified font family.
 *
 * @param familyName the font family to get specs for.
 *
 * @return a set of all the font face specs available for the specified font family.
 */
public abstract Set getSpecsForFamily(String familyName);


/**
 * Returns the stretch values available for a specified font family.
 *
 * @param familyName the font family.
 *
 * @return a set of all available stretch values for that family.
 */
public Set getStretchValuesForFamily(String familyName) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(familyName, "familyName");
	
	Set specs = getSpecsForFamily(familyName);
	Set values = new HashSet();

	Iterator i = specs.iterator();
	while (i.hasNext()) {
		CoFontFaceSpec spec = (CoFontFaceSpec) i.next();
		values.add(spec.getStretchAttribute()); // Sets only add if object does not already exist in the Set
	}
	
	return values;
}


/**
 * Returns the style values available for a specified font family.
 *
 * @param familyName the font family.
 *
 * @return a set of all available style values for that family.
 */
public Set getStyleValuesForFamily(String familyName) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(familyName, "familyName");
	
	Set specs = getSpecsForFamily(familyName);
	Set values = new HashSet();

	Iterator i = specs.iterator();
	while (i.hasNext()) {
		CoFontFaceSpec spec = (CoFontFaceSpec) i.next();
		values.add(spec.getStyleAttribute()); // Sets only add if object does not already exist in the Set
	}
	
	return values;
}


/**
 * Returns the variant values available for a specified font family.
 *
 * @param familyName the font family.
 *
 * @return a set of all available variant values for that family.
 */
public Set getVariantValuesForFamily(String familyName) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(familyName, "familyName");
	
	Set specs = getSpecsForFamily(familyName);
	Set values = new HashSet();

	Iterator i = specs.iterator();
	while (i.hasNext()) {
		CoFontFaceSpec spec = (CoFontFaceSpec) i.next();
		values.add(spec.getVariantAttribute()); // Sets only add if object does not already exist in the Set
	}
	
	return values;
}


/**
 * Returns the weight values available for a specified font family.
 *
 * @param familyName the font family.
 *
 * @return a set of all available wieght values for that family.
 */
public Set getWeightValuesForFamily(String familyName) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(familyName, "familyName");
	
	Set specs = getSpecsForFamily(familyName);
	Set values = new HashSet();

	Iterator i = specs.iterator();
	while (i.hasNext()) {
		CoFontFaceSpec spec = (CoFontFaceSpec) i.next();
		values.add(spec.getWeightAttribute()); // Sets only add if object does not already exist in the Set
	}
	
	return values;
}


/** 
 * Returns true if the font is not removed, and a newer version exists, and false otherwise.
 *
 * @param face the font face to check for new version.
 *
 * @return true if the font is not removed, and a newer version exists.
 *
 * @see #exists(CoFontFace)
 * @see #isRemoved(CoFontFace)
 */
public boolean hasNewerVersion(CoFontFace face) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(face, "face");
	
	CoFontFace mappedFace = getFontFace(face.getSpec());

	// is true if a fontface with the same mapping and a higher version number exists
	// the specMapping should contain the highest version for each existing spec.

	if (mappedFace != null && mappedFace.getVersion() > face.getVersion()) {
		return true;
	} else {
		return false;
	}
}


/**
 * Download and install the files needed by AWT to present a CoFontFace on the screen. This should only be
 * called if awtFontIsAvailable returns false, to avoid unnecessary file transfers. Also, the same problem
 * applies to this function as to awtFontIsAvailable (see the comments for that).
 *
 * <b>Note!</b> This function is not yet implemented! See notes at awtFontIsAvailable for details.<p>
 *
 * @param face the font face to install in the AWT environment.
 *
 * @throws CoFontException if the font, for any reason, could not be installed.
 *
 * @see #isAwtFontAvailable(CoFontFace)
 * @see #installAwtFontIfNeeded(CoFontFace)
 */
public void installAwtFont(CoFontFace face) throws CoFontException {
}


/**
 * Downloads and installs the files needed by AWT to present a CoFontFace on the screen, if not already present. 
 * Always call this method before trying to show a new font on the screen, to make sure it is correctly installed.
 *
 * @param face the font face to check for presence.
 *
 * @see #installAwtFont(CoFontFace)
 * @see #isAwtFontAvailable(CoFontFace)
 */

public void installAwtFontIfNeeded(CoFontFace face) {
	try {
		if (!isAwtFontAvailable(face)) {
			installAwtFont(face);
		}
	} catch (CoFontException e) {
		System.out.println("Warning: Exception occured during font transfer.");
		e.printStackTrace();
	}
}


/**
 * Returns true if the specified CoFontFace is available on this client for rendering on the
 * screen. If not, it should be requested from the server, and the client (actually, the local VM)
 * needs to be restarted.<p>
 * <b>Note!</b> Due to limitations in AWT, it is not possible to implement this method correctly on
 * the client. See {@link com.bluebrim.font.impl.client.CoClientFontMapper#isAwtFontAvailable(CoFontFace)}
 * for more details.
 *
 * @param face the font face to check for availability.
 *
 * @return true if the specified CoFontFace is available on this client.
 *
 * @see #installAwtFont(CoFontFace)
 * @see #installAwtFontIfNeeded(CoFontFace)
 */
public abstract boolean isAwtFontAvailable(CoFontFace face);


/**
 * Returns true if the CoFontFace is "removed". Note that "removed" actually means "hidden"; a CoFontFace
 * is normally never actually removed, but just made hidden so no new texts can be written with that font.
 * A complete removal would mean that old text no longer could be rendered correct, and that is an
 * unfortunate situation.
 *
 * @param face the font face to check if it's removed.
 *
 * @return true if the CoFontFace is "removed" (i.e. hidden).
 *
 * @see #exists(CoFontFace)
 * @see #hasNewerVersion(CoFontFace)
 */
public abstract boolean isRemoved(CoFontFace face);


/**
 * Returns the fallback font family name. This method returns null if no explicitly specified fallback font
 * family exists.
 *
 * @return the fallback font family name, or null if none.
 *
 * @see getFallbackFontFamily
 */
public abstract String getRealFallbackFamily();
}