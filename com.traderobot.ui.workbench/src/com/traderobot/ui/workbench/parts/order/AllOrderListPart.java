 
package com.traderobot.ui.workbench.parts.order;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
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
import com.traderobot.platform.TradePlatform;
import com.traderobot.platform.master.IMasterData;
import com.traderobot.platform.order.IOrderManager;
import com.traderobot.platform.order.OrderRecord;

public class AllOrderListPart {
	
	public static final int IDX_SHORT_CODE = 0;					//단축코드
	public static final int IDX_SHORT_NAME = 1;					//단축명
	public static final int IDX_LAST_JOB = 2;					//처리상태
	public static final int IDX_BUY_SELL_TYPE = 3;				//매매구분
	public static final int IDX_ORDER_QTY = 4;					//주문수량
	public static final int IDX_ORDER_PRICE = 5;				//주문가격
	public static final int IDX_SETTLE_QTY = 6;					//체결수량
	public static final int IDX_SETTLE_PRICE = 7;				//체결가격
	public static final int IDX_UNSETTLE_QTY = 8;				//잔량	
	public static final int IDX_ORDER_TIME = 9;					//처리시각	
	public static final int IDX_ORDER_NUMBER = 10;				//주문번호
	public static final int IDX_ORIGINAL_ORDER_NUMBER = 11;		//원주문번호
	public static final int IDX_LOGIC_NAME = 12;				//로직명
	public static final int IDX_ACCOUNT_NUMBER = 13;			//로직명
	public static final int IDX_ORDER_KIND = 14;				//주문유형
	public static final int IDX_ORDER_CONDITION = 15;			//주문조건
	
	private TableViewer viewer;
	private Table table;
	
	private static final int ALL = 0;							// 모든내역
	private static final int FILLED = 1;						// 체결내역
	private static final int UNFILLED = 2;						// 매체결내역
	
	private int filter;
	private IOrderManager currentManager;
	
	@Inject
	public AllOrderListPart() {
		filter = ALL;
		currentManager = TradePlatform.getPlatform().getOrderManager();		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent, UISynchronize sync) {		
		viewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		viewer.setContentProvider(new ContentProvider());
		table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		createColumns(parent, viewer);
		viewer.setInput(currentManager);
	}
	
	private void createColumns(final Composite parent, final TableViewer viewer)
	{
		TableViewerColumn shortCodeColumn = createTableViewerColumn("단축코드", 70);
		shortCodeColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_SHORT_CODE));
		
		TableViewerColumn shortNameColumn = createTableViewerColumn("종목명", 100);
		shortNameColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_SHORT_NAME));
		
		TableViewerColumn lastJobColumn = createTableViewerColumn("처리상태", 60);
		lastJobColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_LAST_JOB));
				
		TableViewerColumn buySellTypeColumn = createTableViewerColumn("매매구분", 60);
		buySellTypeColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_BUY_SELL_TYPE));
		
		TableViewerColumn orderQtyColumn = createTableViewerColumn("주문수량", 60);
		orderQtyColumn.getColumn().setAlignment(SWT.RIGHT);
		orderQtyColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_ORDER_QTY));
		
		TableViewerColumn orderPriceColumn = createTableViewerColumn("주문가격", 60);
		orderPriceColumn.getColumn().setAlignment(SWT.RIGHT);
		orderPriceColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_ORDER_PRICE));
		
		TableViewerColumn settleQtyColumn = createTableViewerColumn("체결수량", 60);
		settleQtyColumn.getColumn().setAlignment(SWT.RIGHT);
		settleQtyColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_SETTLE_QTY));
		
		TableViewerColumn settlePriceColumn = createTableViewerColumn("체결가격", 60);
		settlePriceColumn.getColumn().setAlignment(SWT.RIGHT);
		settlePriceColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_SETTLE_PRICE));
		
		TableViewerColumn unsettleQtyColumn = createTableViewerColumn("잔량", 60);
		unsettleQtyColumn.getColumn().setAlignment(SWT.RIGHT);
		unsettleQtyColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_UNSETTLE_QTY));
		
		TableViewerColumn orderTimeColumn = createTableViewerColumn("주문시각", 70);
		orderTimeColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_ORDER_TIME));
		
		TableViewerColumn orderNumberColumn = createTableViewerColumn("주문번호", 100);
		orderNumberColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_ORDER_NUMBER));
		
		TableViewerColumn originalOrderNumberColumn = createTableViewerColumn("원주문번호", 100);
		originalOrderNumberColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_ORIGINAL_ORDER_NUMBER));
		
		TableViewerColumn logicNameColumn = createTableViewerColumn("로직명", 100);
		logicNameColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_LOGIC_NAME));
		
		TableViewerColumn accountNumberColumn = createTableViewerColumn("계좌번호", 100);
		accountNumberColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_ACCOUNT_NUMBER));
		
		TableViewerColumn orderKindColumn = createTableViewerColumn("주문유형", 60);
		orderKindColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_ORDER_KIND));
		
		TableViewerColumn orderConditionColumn = createTableViewerColumn("주문조건", 60);
		orderConditionColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_ORDER_CONDITION));
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
	public void onOrderConfirmNew(
		@UIEventTopic(IPlatformEventConstants.TOPIC_ORDER_CONFIRM_NEW) 
		Object data)
	{
		viewer.refresh();
	}
	
	@Inject @Optional
	public void onOrderConfirmModify(
		@UIEventTopic(IPlatformEventConstants.TOPIC_ORDER_CONFIRM_MODIFY) 
		Object data)
	{
		viewer.refresh();
	}
	
	
	@Inject @Optional
	public void onOrderConfirmCancel(
		@UIEventTopic(IPlatformEventConstants.TOPIC_ORDER_CONFIRM_CANCEL) 
		Object data)
	{
		viewer.refresh();
	}
	
	@Inject @Optional
	public void onOrderExecute(
		@UIEventTopic(IPlatformEventConstants.TOPIC_ORDER_EXECUTE) 
		Object data)
	{
		viewer.refresh();
	}
	
	@Inject @Optional
	public void onOrderManagerChanged(
		@UIEventTopic(IPlatformEventConstants.TOPIC_ORDERMANAGER_CHANGED) 
		Object data)
	{
		currentManager = TradePlatform.getPlatform().getOrderManager();
		viewer.setInput(currentManager);
	}
	
	public class ContentProvider implements IStructuredContentProvider 
	{	
		private IOrderManager manager;
		
		public Object[] getElements(Object inputElement) {
			switch(filter)
			{			
			case FILLED:
				return manager.getFilledOrderRecordList();
			case UNFILLED:
				return manager.getUnfilledOrderRecordList();
			default:
				return manager.getOrderRecordList();
			}
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			this.manager = (IOrderManager) newInput;
			currentManager = manager;
		}
	}
	
	public class OrderColumnLabelProvider extends ColumnLabelProvider 
	{	
		private int index;
		
		public OrderColumnLabelProvider(int index)
		{
			this.index = index;
		}
		
		@Override
		public String getText(Object element)
		{
			OrderRecord record = (OrderRecord) element;			
			switch(index)
			{
			case IDX_SHORT_CODE:				//단축코드
				return record.getShortCode();
			case IDX_SHORT_NAME:				//단축명
				IMasterData master = TradePlatform.getPlatform().getItemManager().get(record.getStandardCode());
				if ( master == null )
					return "종목명을 알 수 없음";
				else
					return master.getShortName();
			case IDX_LAST_JOB:					//처리상태
				return record.getStateMessage();
			case IDX_BUY_SELL_TYPE:				//매매구분
				return record.getBuySellTypeMessage();
			case IDX_ORDER_QTY:					//주문수량
				return String.format("%d", record.getOrderQty());
			case IDX_ORDER_PRICE:				//주문가격
				return String.format("%.2f", record.getOrderPrice());
			case IDX_SETTLE_QTY:				//체결수량
				return String.format("%d", record.getSettleQty());
			case IDX_SETTLE_PRICE:				//체결가격
				return  String.format("%.2f", record.getSettlePrice());
			case IDX_UNSETTLE_QTY:				//잔량
//				System.out.println("주문수량: " + record.getOrderQty() + ", 누적체결수량: " + record.getCumulatedSettleQty());
				return String.format("%d", record.getOrderQty() - record.getCumulatedSettleQty());
			case IDX_ORDER_TIME:				//처리시각
				return record.getOrderTime();
			case IDX_LOGIC_NAME:				//로직명
				return record.getLogicName();
			case IDX_ACCOUNT_NUMBER:			//계좌번호
				return record.getAccountNumber();
			case IDX_ORDER_NUMBER:				//주문번호
				return record.getOrderNumber();
			case IDX_ORIGINAL_ORDER_NUMBER:		//원주문번호
				return record.getOriginalOrderNumber();
			case IDX_ORDER_KIND:				//주문유형
				return record.getOrderKindMessage();
			case IDX_ORDER_CONDITION:			//주문조건
				return record.getOrderConditionMessage();
			default:
				return "";
			}
		}

		@Override
		public Color getForeground(Object element)
		{
			OrderRecord record = (OrderRecord) element;			
			switch(index)
			{
			case IDX_BUY_SELL_TYPE:				//매매구분
				if ( record.getBuySellType() == OrderRecord.BUY )
					return Display.getDefault().getSystemColor(SWT.COLOR_RED);
				else
					return Display.getDefault().getSystemColor(SWT.COLOR_BLUE);	
			}
			return super.getForeground(element);
		}
	}
	
}