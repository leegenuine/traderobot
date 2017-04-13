 
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
				MessageDialog.open(MessageDialog.INFORMATION, shell, "종목정보 로드 성공", "[" + TradePlatform.getPlatform().getItemManager().getLoadFileName() + "]파일 로드 성공", SWT.NONE);
			} catch (IOException e) {
				MessageDialog.open(MessageDialog.ERROR, shell, "종목정보 파일 읽기 오류", e.getMessage(), SWT.NONE);
			}
		}
	}
		
}