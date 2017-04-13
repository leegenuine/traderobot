package com.traderobot.ui.datawindow.history;

public abstract class AbstractHistoryObject implements IHistoryObject {
	
	private boolean dirty;	
	
	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
}
