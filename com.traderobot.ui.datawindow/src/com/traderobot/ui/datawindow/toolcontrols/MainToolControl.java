package com.traderobot.ui.datawindow.toolcontrols;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.traderobot.ui.datawindow.Activator;

public class MainToolControl {
	
	@Inject
	Shell shell;
	
	private boolean isRun = false;
	private Label statusLabel;
	
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setLayout(new GridLayout(2, false));
		
		Button runButton = new Button(composite, SWT.NONE);
		runButton.setFont(SWTResourceManager.getFont("Consolas", 25, SWT.BOLD));
		runButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				isRun = !isRun;
				if ( isRun ) {
					
					Cursor cursor = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
					shell.setCursor(cursor);
					Activator.getService().start();
					runButton.setText("STOP");
					statusLabel.setText("System is running!");
					statusLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					runButton.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					shell.setCursor(null);
					cursor.dispose();
					
				}
				else {
					Activator.getService().stop();
					runButton.setText("RUN");
					statusLabel.setText("System was stopped!");
					statusLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
					runButton.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
				}
			}
		});
		GridData gd_runButton = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gd_runButton.heightHint = 50;
		gd_runButton.widthHint = 150;
		runButton.setLayoutData(gd_runButton);
		runButton.setSize(330, 70);
		runButton.setText("RUN");
		
		statusLabel = new Label(composite, SWT.NONE);
		statusLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		statusLabel.setFont(SWTResourceManager.getFont("±¼¸²", 30, SWT.BOLD));
		statusLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		statusLabel.setText("System was stopped!");		
	}
	
	
}