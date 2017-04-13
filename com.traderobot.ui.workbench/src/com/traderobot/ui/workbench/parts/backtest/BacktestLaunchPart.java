 
package com.traderobot.ui.workbench.parts.backtest;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import com.traderobot.platform.IPlatformEventConstants;
import com.traderobot.platform.TradePlatform;
import com.traderobot.platform.backtest.BacktestFeedPointService;
import com.traderobot.platform.backtest.BacktestOrderManager;
import com.traderobot.platform.backtest.BacktestProgress;

public class BacktestLaunchPart {
	
	@Inject
	Shell shell;
	
	private BacktestFeedPointService feedService;			
	private List fileList;
	private ProgressBar progressBar;
	
	private Button selectFileButton;
	private Button runButton;
	private Button stopButton;
	
	@Inject
	public BacktestLaunchPart() {
	}
	
	@PostConstruct
	public void postConstruct(Composite parent, UISynchronize sync) {
		
		try {
			InetAddress group = InetAddress.getByName(BacktestFeedPointService.BACKTEST_FEED_POINT_GROUP);		
			feedService = new BacktestFeedPointService(group, BacktestFeedPointService.BACKTEST_FEED_POINT_PORT);
		} catch (Exception e) {
			MessageDialog.open(MessageDialog.ERROR, parent.getShell(), "백테스트서비스 생성오류", e.getMessage(), SWT.NONE);
		}
		
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FormLayout());
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.right = new FormAttachment(100);
		fd_composite_1.bottom = new FormAttachment(0, 30);
		fd_composite_1.top = new FormAttachment(0, 5);
		fd_composite_1.left = new FormAttachment(0, 5);
		composite_1.setLayoutData(fd_composite_1);
		composite_1.setLayout(new FormLayout());
		
		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(0, 4);
		fd_lblNewLabel.left = new FormAttachment(0);
		fd_lblNewLabel.right = new FormAttachment(0, 70);		
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("Feed\uAC04\uACA9");
		
		Spinner spinner = new Spinner(composite_1, SWT.BORDER);
		spinner.setMaximum(10000);
		spinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				feedService.setInterval(spinner.getSelection());
			}
		});
		FormData fd_spinner = new FormData();
		fd_spinner.right = new FormAttachment(lblNewLabel, 86, SWT.RIGHT);
		fd_spinner.left = new FormAttachment(lblNewLabel, 6);
		fd_spinner.top = new FormAttachment(5);
		spinner.setLayoutData(fd_spinner);
		
		selectFileButton = new Button(composite_1, SWT.NONE);
		selectFileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(parent.getShell(), SWT.MULTI);
				dialog.setFilterPath("F:\\");
				if ( dialog.open() != null ) {
					fileList.removeAll();
					String[] list = dialog.getFileNames();				
					Arrays.sort(list);
					for(int i=0; i<list.length; i++)
						fileList.add(dialog.getFilterPath() + "\\" + list[i]);					
					runButton.setEnabled(true);
				}
			}
		});
		FormData fd_selectFileButton = new FormData();
		fd_selectFileButton.top = new FormAttachment(0);
		fd_selectFileButton.left = new FormAttachment(spinner, 15);
		fd_selectFileButton.width = 80;
		selectFileButton.setLayoutData(fd_selectFileButton);
		selectFileButton.setText("\uD30C\uC77C\uC120\uD0DD");
		
		runButton = new Button(composite_1, SWT.NONE);
		runButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				feedService.setFileNameList(fileList.getItems());
				try {
					if ( !(TradePlatform.getPlatform().getOrderManager() instanceof BacktestOrderManager) ) {
						MessageDialog.open(MessageDialog.INFORMATION, parent.getShell(), "접속오류", "[백테스트 접속]으로 접속선택을 변경하십시오.", SWT.NONE);
						return;
					}
					feedService.start();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		FormData fd_runButton = new FormData();
		fd_runButton.top = new FormAttachment(0);
		fd_runButton.width = 80;
		fd_runButton.left = new FormAttachment(selectFileButton, 10);
		runButton.setLayoutData(fd_runButton);
		runButton.setText("\uC2E4\uD589");
		runButton.setEnabled(false);
		
		stopButton = new Button(composite_1, SWT.NONE);
		stopButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
//					handler.unsubscribe();
					feedService.stop();					
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		stopButton.setText("\uC911\uC9C0");
		stopButton.setEnabled(false);
		FormData fd_stopButton = new FormData();
		fd_stopButton.left = new FormAttachment(runButton, 10);
		fd_stopButton.width = 80;
		stopButton.setLayoutData(fd_stopButton);
		
		fileList = new List(composite, SWT.BORDER);
		FormData fd_fileList = new FormData();
		fd_fileList.top = new FormAttachment(composite_1);
		
		progressBar = new ProgressBar(composite_1, SWT.SMOOTH);
		FormData fd_progressBar = new FormData();
		fd_progressBar.height = 18;
		fd_progressBar.right = new FormAttachment(100, -5);
		fd_progressBar.left = new FormAttachment(stopButton, 10);
		progressBar.setLayoutData(fd_progressBar);
		
		fd_fileList.bottom = new FormAttachment(100, -5);
		fd_fileList.right = new FormAttachment(100, -5);
		fd_fileList.left = new FormAttachment(0, 5);
		fileList.setLayoutData(fd_fileList);
	}
	
	@Inject @Optional
	public void onBacktestProgress(
		@UIEventTopic(IPlatformEventConstants.TOPIC_BACKTEST_PROGRESS) 
		Object data)
	{	
		BacktestProgress progress = (BacktestProgress) data;
		//progressBar.setMaximum(100);
		int value = (int)(100*(progress.getSentBytes()*1.0)/(progress.getTotalBytes()*1.0));
//		System.out.println("PROGRESS...[" + value + "] sent: " + progress.getSentBytes() + ", total: " + progress.getTotalBytes());
		progressBar.setSelection(value);		
	}
	
	@Inject @Optional
	public void onBacktestStart(
		@UIEventTopic(IPlatformEventConstants.TOPIC_BACKTEST_START) 
		Object data)
	{	
//		System.out.println("BACKTEST START "+ Calendar.getInstance().getTime().toString());
		progressBar.setSelection(0);
		selectFileButton.setEnabled(false);
		runButton.setEnabled(false);
		stopButton.setEnabled(true);
	}
	
	@Inject @Optional
	public void onBacktestEnd(
		@UIEventTopic(IPlatformEventConstants.TOPIC_BACKTEST_END) 
		Object data)
	{
//		System.out.println("BACKTEST END " + Calendar.getInstance().getTime().toString());
		progressBar.setSelection(100);
		selectFileButton.setEnabled(true);
		runButton.setEnabled(true);
		stopButton.setEnabled(false);
		MessageDialog.open(MessageDialog.INFORMATION, shell, "백테스트 완료", "백테스트가 완료되었습니다.", SWT.NONE);
	}
	
	
}