package com.bluebrim.text.impl.client;


import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;

//

public interface CoNumericComponentBuilder
{

CoTextField createLengthTextField( CoUserInterfaceBuilder b, String name, CoLengthUnitSet us );
}