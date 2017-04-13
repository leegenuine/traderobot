package com.traderobot.ui.datawindow.toolcontrols;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import com.traderobot.platform.IPlatformEventConstants;
import com.traderobot.platform.PlatformLogMessage;

public class StatusBarControl {
	
	private static SimpleDateFormat datetime_format = new SimpleDateFormat("HH:mm:ss.SSS");
	
	private Label lastTransactionTimeLabel;
	private Label messageLabel;
	
	@PostConstruct
	public void createControls(Composite parent) {
		
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));		
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.verticalSpacing = 2;
		gl_composite.marginWidth = 2;
		gl_composite.marginHeight = 2;
		gl_composite.horizontalSpacing = 2;
		composite.setLayout(gl_composite);
		
		messageLabel = new Label(composite, SWT.BORDER);
		messageLabel.setFont(SWTResourceManager.getFont("±¼¸²", 10, SWT.NORMAL));
		messageLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		lastTransactionTimeLabel = new Label(composite, SWT.BORDER | SWT.RIGHT);
		lastTransactionTimeLabel.setFont(SWTResourceManager.getFont("±¼¸²", 10, SWT.NORMAL));
		GridData gd_lastTransactionTimeLabel = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gd_lastTransactionTimeLabel.heightHint = 16;
		gd_lastTransactionTimeLabel.widthHint = 100;
		lastTransactionTimeLabel.setLayoutData(gd_lastTransactionTimeLabel);
	}
	
	@Inject @Optional
	public void onReceiveTransaction(
		@UIEventTopic(IPlatformEventConstants.TOPIC_TRANSACTION) 
		Object data)
	{
//		ITransactionData rtd = (ITransactionData) data;
		Calendar c = Calendar.getInstance();		
		String time = datetime_format.format(c.getTime());
//		lastTransactionTimeLabel.setText(time + " [" +  TransactionDescription.getDescription(rtd.getTransactionCode()) + "]");
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
}