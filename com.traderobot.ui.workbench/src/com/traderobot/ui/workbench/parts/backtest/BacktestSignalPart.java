package com.traderobot.ui.workbench.parts.backtest;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.traderobot.platform.IPlatformEventConstants;
import com.traderobot.platform.logic.systemtrading.SystemSignal;
import com.traderobot.platform.logic.systemtrading.SystemSignalRecord;

public class BacktestSignalPart {
	
	@Inject
	MApplication app;
	
	@Inject
	EModelService modelService;	
	
	@Inject
	EPartService partService;
	
	public static final int IDX_TIME = 0;						//신호발생시간
	public static final int IDX_LOGIC_NAME = 1;					//로직명	
	public static final int IDX_SIGNAL_ID = 2;					//신호ID
	public static final int IDX_ENTRY_EXIT = 3;					//ENTRY_EXIT구분
	public static final int IDX_BUY_SELL = 4;					//BUY/SELL구분
	public static final int IDX_PRICE = 5;						//가격
	public static final int IDX_QTY = 6;						//수량
	
	private Vector<SystemSignalRecord> recordList;
	private TableViewer viewer;
	private Table table;
	
	@Inject
	public BacktestSignalPart() {
		recordList = new Vector<SystemSignalRecord>();
	}
	
	@PostConstruct
	public void postConstruct(Composite parent, UISynchronize sync) {		
		viewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		viewer.setContentProvider(new ContentProvider());
		table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		createColumns(parent, viewer);
		viewer.setInput(recordList);
	}
	
	private void createColumns(final Composite parent, final TableViewer viewer)
	{
		TableViewerColumn timeColumn = createTableViewerColumn("시각", 80);
		timeColumn.setLabelProvider(new SignalRecordColumnLabelProvider(IDX_TIME));
		
		TableViewerColumn logicNameColumn = createTableViewerColumn("로직명", 150);
		logicNameColumn.setLabelProvider(new SignalRecordColumnLabelProvider(IDX_LOGIC_NAME));
		
		TableViewerColumn signalIdColumn = createTableViewerColumn("시그널ID", 80);
		signalIdColumn.setLabelProvider(new SignalRecordColumnLabelProvider(IDX_SIGNAL_ID));
				
		TableViewerColumn entryExitColumn = createTableViewerColumn("진입구분", 80);
		entryExitColumn.setLabelProvider(new SignalRecordColumnLabelProvider(IDX_ENTRY_EXIT));
		
		TableViewerColumn buySellColumn = createTableViewerColumn("매매구분", 80);		
		buySellColumn.setLabelProvider(new SignalRecordColumnLabelProvider(IDX_BUY_SELL));
		
		TableViewerColumn priceColumn = createTableViewerColumn("가격", 80);
		priceColumn.getColumn().setAlignment(SWT.RIGHT);
		priceColumn.setLabelProvider(new SignalRecordColumnLabelProvider(IDX_PRICE));
		
		TableViewerColumn qtyColumn = createTableViewerColumn("수량", 80);
		qtyColumn.getColumn().setAlignment(SWT.RIGHT);
		qtyColumn.setLabelProvider(new SignalRecordColumnLabelProvider(IDX_QTY));
	}
	
	private TableViewerColumn createTableViewerColumn(String title, int width)
	{
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(width);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}
	
	@Inject @Optional
	public void onSystemSignal(
		@UIEventTopic(IPlatformEventConstants.TOPIC_SYSTEM_SIGNAL) 
		Object data)
	{	
		SystemSignalRecord record = (SystemSignalRecord) data;
		recordList.add(record);
		viewer.refresh();
	}
	
	public String timeToString(SystemSignalRecord record)
	{
		int year = record.getDate().get(Calendar.YEAR);
		int month = record.getDate().get(Calendar.MONTH)+1;
		int day = record.getDate().get(Calendar.DAY_OF_MONTH);
		
		int hhmmssSS = record.getTime();
		int hh = hhmmssSS/1000000;
		int mm = hhmmssSS/10000 - hh*100;
		int ss = hhmmssSS/100 - hh*10000 - mm*100;
		int SS = hhmmssSS - hh*1000000 - mm*10000 - ss*100;		
		return String.format("%04d-%02d-%02d %02d:%02d:%02d.%02d", year, month, day, hh, mm, ss, SS);
	}
	
	public class ContentProvider implements IStructuredContentProvider 
	{	
		private Vector<SystemSignalRecord> list;
		
		public Object[] getElements(Object inputElement) {
			
			list.sort(new Comparator<SystemSignalRecord>() {

				@Override
				public int compare(SystemSignalRecord o1, SystemSignalRecord o2) {
					int ret = o2.getDate().compareTo(o1.getDate());
					if ( ret != 0 )
						return ret;
					return (o2.getTime() - o1.getTime());
				}
			});
			
			return list.toArray(new SystemSignalRecord[list.size()]);
		}
		
		@SuppressWarnings("unchecked")
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			this.list = (Vector<SystemSignalRecord>) newInput;
		}
	}
	
	public class SignalRecordColumnLabelProvider extends ColumnLabelProvider 
	{	
		private int index;
		
		public SignalRecordColumnLabelProvider(int index)
		{
			this.index = index;
		}
		
		@Override
		public String getText(Object element)
		{
			SystemSignalRecord record = (SystemSignalRecord) element;			
			switch(index)
			{
			case IDX_TIME:
				return timeToString(record);
			case IDX_LOGIC_NAME:
				return record.getLogicName();
			case IDX_SIGNAL_ID:
				return record.getSignalId();
			case IDX_ENTRY_EXIT:
				if ( record.getEntryExit() == SystemSignal.ENTRY)
					return "진입";
				else
					return "청산";
			case IDX_BUY_SELL:
				if ( record.getBuySell() == SystemSignal.BUY)
					return "매수";
				else
					return "매도";
			case IDX_PRICE:
				return String.format("%.2f", record.getPrice());
			case IDX_QTY:
				return String.format("%d", record.getQty());
			default:
				return "";
			}
		}

		@Override
		public Color getForeground(Object element)
		{
			SystemSignalRecord record = (SystemSignalRecord) element;	
			switch(index)
			{
			case IDX_BUY_SELL:				//매매구분
				if ( record.getBuySell() == SystemSignal.BUY )
					return Display.getDefault().getSystemColor(SWT.COLOR_RED);
				else
					return Display.getDefault().getSystemColor(SWT.COLOR_BLUE);	
			}
			return super.getForeground(element);
		}
	}

}
