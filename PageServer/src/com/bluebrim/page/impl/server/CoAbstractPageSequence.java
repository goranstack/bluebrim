package com.bluebrim.page.impl.server;

/**
 * The page sequence model is implemented as a composite pattern where this class is the abstract super class. 
 * A page sequence is a sequence of page place holders <code>CoPlaceHolder</code>. A page place holder
 * know its place in the sequence. That is useful for page numbering. An example where composite page sequence 
 * and page sequence is used is a newspaper with two sections A and B each containing 24 pages. In that case we have
 * one composite page sequence containing two page sequence objects representing the A and B section. These two page sequence objects
 * contains 24 page place holders each. 
 * Creation date: (2001-11-13 16:33:50)
 * @author: Göran Stäck 
 */
public abstract class CoAbstractPageSequence  {
}