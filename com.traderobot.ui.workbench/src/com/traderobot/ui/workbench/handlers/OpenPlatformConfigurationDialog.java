 
package com.traderobot.ui.workbench.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import com.traderobot.ui.workbench.dialogs.PlatformConfigurationDialog;

public class OpenPlatformConfigurationDialog {
	
	@Execute
	public void execute(Shell shell) {
		PlatformConfigurationDialog dialog = new PlatformConfigurationDialog(shell);
		dialog.open();
	}
		
}