package com.bluebrim.paint.impl.shared;
import java.awt.*;

//

public interface CoRegistrationColorIF extends CoEditableColorIF {
	public static final String REGISTRATION_COLOR = "registration-color";
	public abstract void setColor(Color c);
}