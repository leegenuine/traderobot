package com.traderobot.ui.datawindow.parts;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.traderobot.platform.IPlatformEventConstants;
import com.traderobot.platform.PlatformLogMessage;
import com.traderobot.ui.datawindow.IEventConstants;
import com.traderobot.ui.datawindow.LogMessage;
import com.traderobot.ui.datawindow.Service;

public class MessageLogPart {

	private static final int IDX_TIME = 0;
	private static final int IDX_MESSAGE = 1;
	
	private Table table;
	
	@Inject
	public MessageLogPart() {
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		
		table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		
		TableColumn timeColumn = new TableColumn(table, SWT.NONE);
		timeColumn.setWidth(150);
		timeColumn.setText("시각");
		
		TableColumn messageColumn = new TableColumn(table, SWT.NONE);
		messageColumn.setWidth(800);
		messageColumn.setText("메시지");		
	}
	
	@Inject @Optional
	public void onLogMessage(
		@UIEventTopic(IEventConstants.TOPIC_MESSAGE) 
		Object data)
	{
		LogMessage message = (LogMessage) data;
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		String time = sdf.format(c.getTime());
		TableItem item = new TableItem(table, SWT.NONE, 0);
		item.setText(IDX_TIME, time);
		item.setText(IDX_MESSAGE, message.getMessage());
	}
	
	@Inject @Optional
	public void onPlatformMessage(
		@UIEventTopic(IPlatformEventConstants.TOPIC_PLATFORM_MESSAGE) 
		Object data)
	{
		PlatformLogMessage plm = (PlatformLogMessage) data;
		Service.sendMessage(plm.getMessage());
	}
}
