package com.bluebrim.content.shared;

import com.bluebrim.image.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Temporay hack until <code>CoContentReceiver</code> don't have separate add
 * methods for different kind of content
 */
public class CoContentUtility {
	public static boolean addContent(CoContentReceiver receiver,
			CoContentIF content) {
		if (content instanceof CoWorkPieceIF)
			return receiver.add((CoWorkPieceIF) content);

		if (content instanceof CoImageContentIF)
			return receiver.add((CoImageContentIF) content);

		if (content instanceof CoTextContentIF)
			return receiver.add((CoTextContentIF) content);

		if (content instanceof CoLayoutContentIF)
			return receiver.add((CoLayoutContentIF) content);

		throw new IllegalArgumentException("Unknown content class");
	}

}