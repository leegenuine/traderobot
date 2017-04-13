 
package com.traderobot.ui.workbench.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import com.traderobot.ui.workbench.dialogs.IASOrderDialog;

public class OpenIASOrderDialogHandler {
	@Execute
	public void execute(Shell shell) {
		IASOrderDialog dialog = new IASOrderDialog(shell);
		dialog.open();
	}
	
}