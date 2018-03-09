package com.bluebrim.gemstone.client.command;

import java.text.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.transact.shared.*;
/**
 * NOTE: Bad class! Do not use! /Markus 2002-09-18
 * 
	Abstract commando class which models a transaction and implements most of the methods that make up the
	algorithm, eg <code>prepare</code>, <code>abort</code> and <code>finish</code>. The actual transaction 
	is performed with the help of a command that is executed in <code>doExecute</code>.
	The creation (or accessing) of this commando is a handled by a concrete subclass which implements 
	<code>getCommand</code>. This commando can very well be accessed by a method in a business object on
	the server.
 */
public abstract class CoAbstractTransaction extends CoAbstractCommand {
	protected Object transactionSource;
	protected Object transactionTarget;

protected CoAbstractTransaction(String name, Object target, Object source) {
	super(name);
	transactionSource 	= source;
	setTransactionTarget(target);
}

protected void abort() {
	CoTransactionService.abort();
}

protected void commitTransaction() {
	if (CoAssertion.TRACE) {
		CoAssertion.trace("COMMIT: "+getName() + " Target: "+getTransactionTarget() );
	}
	if (CoAssertion.SIMULATION_SUPPORT) {
		CoAssertion.addChangedObject(getTransactionTarget());
	}
	try {
		CoTransactionService.commit();
	} catch (CoBadTransactionStateException e) {
	}
}

protected String createErrorMessage(Exception e)
{
	String errorMsg 		= e.getMessage();
	if (CoBaseUtilities.stringIsEmpty(errorMsg))
	{
		MessageFormat tFormat 	= new MessageFormat(CoGsUIStringResources.getName(CoGemStoneUIConstants.UNABLE_TO_COMMIT_NO_ERROR));
		return tFormat.format(new String[] {getName()});
	}
	else
	{
		MessageFormat tFormat 	= new MessageFormat(CoGsUIStringResources.getName(CoGemStoneUIConstants.UNABLE_TO_COMMIT_1));
		return tFormat.format(new String[] {getName(), errorMsg});
	}
}

protected boolean doExecute(CoCommand command) {
	try
	{
		command.doExecute();
		commitTransaction();
		return true;
	}
	catch (Exception e)
	{
		handleTransactionException(e);
		return false;
	}		
}
public void execute()
{
	execute(getCommand());
}
protected void execute (CoCommand command )
{
	CoTransactionUtilities.execute(command, getTransactionTarget());
}
/**
 */
protected void finish() {
}
protected abstract CoCommand getCommand ();
/**
 */
public  String getName() {
	String tName = super.getName();
	return (tName.length() > 0) ? tName : CoGsUIStringResources.getName("REQUESTED_COMMAND");
}
public Object getTransactionSource()
{
	return transactionSource;
}
/**
 */
protected Object getTransactionTarget() {
	return transactionTarget;
}
/**
 */
protected void handleTransactionException(Exception e)
{
	abort();
	JOptionPane.showMessageDialog(new JFrame(""), createErrorMessage(e), "", JOptionPane.INFORMATION_MESSAGE);
	e.printStackTrace();
}

protected void prepare() {
	getCommand().prepare();
	try {
		CoTransactionService.begin();
	} catch (CoBadTransactionStateException e) {
	}
}

public  void setTransactionTarget(Object aTarget) {
	transactionTarget	= aTarget;
}
}
