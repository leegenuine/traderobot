 
package com.traderobot.ui.workbench.parts.systemtrading;

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
import com.traderobot.platform.logic.LogicMessage;

public class SystemTradingLogicMessagePart {
	
	private static final int IDX_LOGIC_NAME = 0;
	private static final int IDX_MESSAGE = 1;
	
	private Table table;
	
	@Inject
	public SystemTradingLogicMessagePart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		
		table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		
		TableColumn logicNameColumn = new TableColumn(table, SWT.NONE);
		logicNameColumn.setWidth(150);
		logicNameColumn.setText("\uB85C\uC9C1\uBA85");
		
		TableColumn messageColumn = new TableColumn(table, SWT.NONE);
		messageColumn.setWidth(800);
		messageColumn.setText("\uBA54\uC2DC\uC9C0");		
	}
	
	@Inject @Optional
	public void onSystemTradingLogicMessage(
		@UIEventTopic(IPlatformEventConstants.TOPIC_LOGIC_MESSAGE) 
		Object data)
	{
		LogicMessage message = (LogicMessage) data;		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(c.getTime());		
		TableItem item = new TableItem(table, SWT.NONE, 0);
		item.setText(IDX_LOGIC_NAME, message.getLogicName());
		item.setText(IDX_MESSAGE, "[" + time + "] " + message.getMessage());
	}	
}