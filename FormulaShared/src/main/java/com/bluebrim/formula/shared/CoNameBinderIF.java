package com.bluebrim.formula.shared;


/**
 * Interface for CoNameBinder.
 * Creation date: (1999-12-14 23:58:13)
 * @author: G�ran St�ck
 */
public interface CoNameBinderIF extends java.rmi.Remote {
/*
 * Binds a name to a variable
 */
public CoAbstractVariableIF bindName(String name);
}
