 
package com.traderobot.ui.workbench.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import com.traderobot.ui.workbench.dialogs.FeedPointInformationDialog;

public class OpenFeedPointInformationHandler {
	
	@Execute
	public void execute(Shell shell) {
		FeedPointInformationDialog dialog = new FeedPointInformationDialog(shell);
		dialog.open();
	}	
		
}