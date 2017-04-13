 
package com.traderobot.ui.workbench.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

import com.traderobot.ui.workbench.windows.ClickOrderShell;

public class OpenClickOrder {
	@Execute
	public void execute() {
		ClickOrderShell.main(null);
	}
		
}