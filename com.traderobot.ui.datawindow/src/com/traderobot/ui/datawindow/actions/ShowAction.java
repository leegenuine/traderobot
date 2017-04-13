package com.traderobot.ui.datawindow.actions;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

public class ShowAction extends Action {
	
	@Inject
	IWorkbench workbench;
	
	@Inject
	IEventBroker broker;
	
	Shell shell;
	
	public ShowAction(Shell shell) {
		this.shell = shell;
		setText("Show...");
		setToolTipText("Show TradeRobot DataWindow");
	}

	@Override
	public void run() {
		shell.setMinimized(false);
		shell.setVisible(true);
	}
}
