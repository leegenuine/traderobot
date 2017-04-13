 
package com.traderobot.ui.workbench.parts.market;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class TickerPart {
	@Inject
	public TickerPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Button btnNewButton = new Button(parent, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		btnNewButton.setText("New Button");
		
	}
	
	
	
	
}