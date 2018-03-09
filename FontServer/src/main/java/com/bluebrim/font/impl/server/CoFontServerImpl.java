package com.bluebrim.font.impl.server;

import com.bluebrim.base.shared.*;
import com.bluebrim.font.shared.*;

/**
 * @author Göran Stäck 2002-10-29
 */
public class CoFontServerImpl implements CoFontServer 
{
	private CoFontRepositoryIF m_fontRepository;

	public CoFontServerImpl()
	{
	    m_fontRepository = CoFontRepository.createFontRepository(); 
	    m_fontRepository.installInitialData(new CoStatusShower.StdOutShower());
	}
	
	public CoFontRepositoryIF getFontRepository() {
		return m_fontRepository;
	}

}
