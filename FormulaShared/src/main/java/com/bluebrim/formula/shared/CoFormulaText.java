package com.bluebrim.formula.shared;


public interface CoFormulaText 
{
    public void addString(String str);
    public int getLength();
    public void insertFormulaVariable(int offset, int length, String name);
}
