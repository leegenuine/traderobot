 
package com.traderobot.ui.workbench.handlers;

import java.io.IOException;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.traderobot.platform.TradePlatform;
import com.traderobot.platform.TradePlatformConfiguration;

public class LoadMasterFileHandler {
	
	@Execute
	public void execute(Shell shell) {		
		String fileName;
		FileDialog dialog = new FileDialog(shell);
		dialog.setFilterPath(TradePlatformConfiguration.getInstance().getMasterPath());
		if ( (fileName = dialog.open()) != null ) {
			try {
				TradePlatform.getPlatform().loadMasterFile(fileName);
				MessageDialog.open(MessageDialog.INFORMATION, shell, "�������� �ε� ����", "[" + TradePlatform.getPlatform().getItemManager().getLoadFileName() + "]���� �ε� ����", SWT.NONE);
			} catch (IOException e) {
				MessageDialog.open(MessageDialog.ERROR, shell, "�������� ���� �б� ����", e.getMessage(), SWT.NONE);
			}
		}
	}
		
}