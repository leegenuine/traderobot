 
package com.traderobot.ui.workbench.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import com.traderobot.ui.workbench.dialogs.OrderManagerSettingDialog;

public class OpenOrderManagerSettingDialogHandler {
	
	@Execute
	public void execute(Shell shell) {
		OrderManagerSettingDialog dialog = new OrderManagerSettingDialog(shell);
		dialog.open();
	}	
}