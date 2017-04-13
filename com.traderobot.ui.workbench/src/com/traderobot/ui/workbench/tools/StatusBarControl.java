package com.traderobot.ui.workbench.tools;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import com.traderobot.platform.IPlatformEventConstants;
import com.traderobot.platform.PlatformLogMessage;
import com.traderobot.platform.TradePlatform;

public class StatusBarControl {
	
	private static SimpleDateFormat datetime_format = new SimpleDateFormat("HH:mm:ss.SSS");
	
	private Label lastTransactionTimeLabel;
	private Label messageLabel;
	private Label masterFileNameLabel;
	
	@PostConstruct
	public void createControls(Composite parent) {
		
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));		
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(3, false);
		gl_composite.verticalSpacing = 2;
		gl_composite.marginWidth = 2;
		gl_composite.marginHeight = 2;
		gl_composite.horizontalSpacing = 2;
		composite.setLayout(gl_composite);
		
		messageLabel = new Label(composite, SWT.BORDER);
		messageLabel.setFont(SWTResourceManager.getFont("굴림", 10, SWT.NORMAL));
		messageLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		masterFileNameLabel = new Label(composite, SWT.BORDER | SWT.CENTER);
		masterFileNameLabel.setFont(SWTResourceManager.getFont("굴림", 10, SWT.NORMAL));
		masterFileNameLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		GridData gd_masterFileNameLabel = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gd_masterFileNameLabel.widthHint = 150;
		masterFileNameLabel.setLayoutData(gd_masterFileNameLabel);
		
		lastTransactionTimeLabel = new Label(composite, SWT.BORDER | SWT.RIGHT);
		lastTransactionTimeLabel.setFont(SWTResourceManager.getFont("굴림", 10, SWT.NORMAL));
		GridData gd_lastTransactionTimeLabel = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gd_lastTransactionTimeLabel.heightHint = 16;
		gd_lastTransactionTimeLabel.widthHint = 100;
		lastTransactionTimeLabel.setLayoutData(gd_lastTransactionTimeLabel);
		
		try {
			TradePlatform.getPlatform().loadDefaultMaster();
		} catch (IOException ioe) {
			MessageDialog.open(MessageDialog.ERROR, parent.getShell(), "종목정보 파일 읽기 오류", ioe.getMessage(), SWT.NONE);
			masterFileNameLabel.setText("마스터파일 ERROR");
			masterFileNameLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
	}
	
	@Inject @Optional
	public void onReceiveTransaction(
		@UIEventTopic(IPlatformEventConstants.TOPIC_TRANSACTION) 
		Object data)
	{			
		Calendar c = Calendar.getInstance();		
		String time = datetime_format.format(c.getTime());
		lastTransactionTimeLabel.setText(time);
	}	
	
	@Inject @Optional
	public void onPlatformMessage(
		@UIEventTopic(IPlatformEventConstants.TOPIC_PLATFORM_MESSAGE) 
		Object data)
	{			
		PlatformLogMessage message = (PlatformLogMessage) data;
		messageLabel.setText(message.getMessage());
	}
	
	@Inject @Optional
	public void onPlatformMasterFileChanged(
		@UIEventTopic(IPlatformEventConstants.TOPIC_PLATFORM_MASTERFILE_CHANGED) 
		Object data)
	{	
//		String masterFileName = (String) data;
		masterFileNameLabel.setText("마스터파일 OK");
		masterFileNameLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
	}
	
	
}