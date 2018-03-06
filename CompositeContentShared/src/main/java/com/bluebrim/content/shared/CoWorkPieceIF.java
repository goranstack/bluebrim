package com.bluebrim.content.shared;
import java.rmi.*;
import java.util.*;

/**
 * Creation date: (2000-10-27 11:30:26)
 * @author G�ran St�ck
 */
public interface CoWorkPieceIF extends Remote, CoContentCollectionIF, CoContentIF  {

	String WORK_PIECE = "workpiece";
	String FACTORY_KEY = "workpiece";

	public List getImages();
	
	public List getLayouts();
	
	public List getTexts();
	
	public void attachLayoutProjector( CoWorkPieceProjector projector );
	
	public void dettachLayoutProjector( CoWorkPieceProjector projector );

}