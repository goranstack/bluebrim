package com.bluebrim.layout.impl.client.transfer;

public abstract class CoTopicOnSomethingOperation extends CoAbstractDropOperation {
	public String getDescription(String nameOfTarget) {
		return "Släpp avdelning på " + nameOfTarget;
	}
}