 
package com.traderobot.ui.test.parts;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.UnknownHostException;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Spinner;

import com.traderobot.platform.backtest.BacktestFeedPointService;
import com.traderobot.platform.backtest.BacktestMatchingService;

public class BacktestFeedPointServiceView {
	
	BacktestFeedPointService service;
	List fileList;
	
	@Inject
	public BacktestFeedPointServiceView() {
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		
		try {
			InetAddress group = InetAddress.getByName(BacktestMatchingService.BACKTEST_FEED_POINT_GROUP);		
			service = new BacktestFeedPointService(group,
											   	   BacktestMatchingService.BACKTEST_FEED_POINT_PORT);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Composite composite = new Composite(parent, SWT.NONE);		
		composite.setLayout(new GridLayout(1, false));
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new FormLayout());
		composite_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.left = new FormAttachment(5);
		fd_lblNewLabel.right = new FormAttachment(0, 70);		
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("Feed\uAC04\uACA9");
		
		Spinner spinner = new Spinner(composite_1, SWT.BORDER);
		fd_lblNewLabel.top = new FormAttachment(spinner, 3, SWT.TOP);
		FormData fd_spinner = new FormData();
		fd_spinner.left = new FormAttachment(0, 90);
		fd_spinner.top = new FormAttachment(5);
		spinner.setLayoutData(fd_spinner);
		
		Button selectFileButton = new Button(composite_1, SWT.NONE);
		selectFileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(parent.getShell(), SWT.MULTI);
				dialog.setFilterPath("F:\\");
				dialog.open();
				String[] list = dialog.getFileNames();				
				Arrays.sort(list);
				for(int i=0; i<list.length; i++)
					fileList.add(dialog.getFilterPath() + "\\" + list[i]);
			}
		});
		FormData fd_selectFileButton = new FormData();
		fd_selectFileButton.width = 80;
		fd_selectFileButton.bottom = new FormAttachment(spinner, 0, SWT.BOTTOM);
		fd_selectFileButton.left = new FormAttachment(spinner, 15);
		selectFileButton.setLayoutData(fd_selectFileButton);
		selectFileButton.setText("\uD30C\uC77C\uC120\uD0DD");
		
		Button runButton = new Button(composite_1, SWT.NONE);
		runButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				service.setFileNameList(fileList.getItems());
				try {
					service.start();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		FormData fd_runButton = new FormData();
		fd_runButton.width = 80;
		fd_runButton.bottom = new FormAttachment(spinner, 0, SWT.BOTTOM);
		fd_runButton.left = new FormAttachment(selectFileButton, 10);
		runButton.setLayoutData(fd_runButton);
		runButton.setText("\uC2E4\uD589");
		
		Button stopButton = new Button(composite_1, SWT.NONE);
		stopButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					service.stop();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		stopButton.setText("\uC911\uC9C0");
		FormData fd_stopButton = new FormData();
		fd_stopButton.left = new FormAttachment(runButton, 10);
		fd_stopButton.width = 80;
		stopButton.setLayoutData(fd_stopButton);
		
		fileList = new List(composite, SWT.BORDER);
		GridData gd_fileList = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		gd_fileList.heightHint = 800;
		gd_fileList.widthHint = 800;
		fileList.setLayoutData(gd_fileList);
	}
}