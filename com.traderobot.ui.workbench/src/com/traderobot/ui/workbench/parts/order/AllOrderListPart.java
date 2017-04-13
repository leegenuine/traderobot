 
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
	
	public static final int IDX_SHORT_CODE = 0;					//�����ڵ�
	public static final int IDX_SHORT_NAME = 1;					//�����
	public static final int IDX_LAST_JOB = 2;					//ó������
	public static final int IDX_BUY_SELL_TYPE = 3;				//�Ÿű���
	public static final int IDX_ORDER_QTY = 4;					//�ֹ�����
	public static final int IDX_ORDER_PRICE = 5;				//�ֹ�����
	public static final int IDX_SETTLE_QTY = 6;					//ü�����
	public static final int IDX_SETTLE_PRICE = 7;				//ü�ᰡ��
	public static final int IDX_UNSETTLE_QTY = 8;				//�ܷ�	
	public static final int IDX_ORDER_TIME = 9;					//ó���ð�	
	public static final int IDX_ORDER_NUMBER = 10;				//�ֹ���ȣ
	public static final int IDX_ORIGINAL_ORDER_NUMBER = 11;		//���ֹ���ȣ
	public static final int IDX_LOGIC_NAME = 12;				//������
	public static final int IDX_ACCOUNT_NUMBER = 13;			//������
	public static final int IDX_ORDER_KIND = 14;				//�ֹ�����
	public static final int IDX_ORDER_CONDITION = 15;			//�ֹ�����
	
	private TableViewer viewer;
	private Table table;
	
	private static final int ALL = 0;							// ��系��
	private static final int FILLED = 1;						// ü�᳻��
	private static final int UNFILLED = 2;						// ��ü�᳻��
	
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
		TableViewerColumn shortCodeColumn = createTableViewerColumn("�����ڵ�", 70);
		shortCodeColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_SHORT_CODE));
		
		TableViewerColumn shortNameColumn = createTableViewerColumn("�����", 100);
		shortNameColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_SHORT_NAME));
		
		TableViewerColumn lastJobColumn = createTableViewerColumn("ó������", 60);
		lastJobColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_LAST_JOB));
				
		TableViewerColumn buySellTypeColumn = createTableViewerColumn("�Ÿű���", 60);
		buySellTypeColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_BUY_SELL_TYPE));
		
		TableViewerColumn orderQtyColumn = createTableViewerColumn("�ֹ�����", 60);
		orderQtyColumn.getColumn().setAlignment(SWT.RIGHT);
		orderQtyColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_ORDER_QTY));
		
		TableViewerColumn orderPriceColumn = createTableViewerColumn("�ֹ�����", 60);
		orderPriceColumn.getColumn().setAlignment(SWT.RIGHT);
		orderPriceColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_ORDER_PRICE));
		
		TableViewerColumn settleQtyColumn = createTableViewerColumn("ü�����", 60);
		settleQtyColumn.getColumn().setAlignment(SWT.RIGHT);
		settleQtyColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_SETTLE_QTY));
		
		TableViewerColumn settlePriceColumn = createTableViewerColumn("ü�ᰡ��", 60);
		settlePriceColumn.getColumn().setAlignment(SWT.RIGHT);
		settlePriceColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_SETTLE_PRICE));
		
		TableViewerColumn unsettleQtyColumn = createTableViewerColumn("�ܷ�", 60);
		unsettleQtyColumn.getColumn().setAlignment(SWT.RIGHT);
		unsettleQtyColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_UNSETTLE_QTY));
		
		TableViewerColumn orderTimeColumn = createTableViewerColumn("�ֹ��ð�", 70);
		orderTimeColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_ORDER_TIME));
		
		TableViewerColumn orderNumberColumn = createTableViewerColumn("�ֹ���ȣ", 100);
		orderNumberColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_ORDER_NUMBER));
		
		TableViewerColumn originalOrderNumberColumn = createTableViewerColumn("���ֹ���ȣ", 100);
		originalOrderNumberColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_ORIGINAL_ORDER_NUMBER));
		
		TableViewerColumn logicNameColumn = createTableViewerColumn("������", 100);
		logicNameColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_LOGIC_NAME));
		
		TableViewerColumn accountNumberColumn = createTableViewerColumn("���¹�ȣ", 100);
		accountNumberColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_ACCOUNT_NUMBER));
		
		TableViewerColumn orderKindColumn = createTableViewerColumn("�ֹ�����", 60);
		orderKindColumn.setLabelProvider(new OrderColumnLabelProvider(IDX_ORDER_KIND));
		
		TableViewerColumn orderConditionColumn = createTableViewerColumn("�ֹ�����", 60);
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
			case IDX_SHORT_CODE:				//�����ڵ�
				return record.getShortCode();
			case IDX_SHORT_NAME:				//�����
				IMasterData master = TradePlatform.getPlatform().getItemManager().get(record.getStandardCode());
				if ( master == null )
					return "������� �� �� ����";
				else
					return master.getShortName();
			case IDX_LAST_JOB:					//ó������
				return record.getStateMessage();
			case IDX_BUY_SELL_TYPE:				//�Ÿű���
				return record.getBuySellTypeMessage();
			case IDX_ORDER_QTY:					//�ֹ�����
				return String.format("%d", record.getOrderQty());
			case IDX_ORDER_PRICE:				//�ֹ�����
				return String.format("%.2f", record.getOrderPrice());
			case IDX_SETTLE_QTY:				//ü�����
				return String.format("%d", record.getSettleQty());
			case IDX_SETTLE_PRICE:				//ü�ᰡ��
				return  String.format("%.2f", record.getSettlePrice());
			case IDX_UNSETTLE_QTY:				//�ܷ�
//				System.out.println("�ֹ�����: " + record.getOrderQty() + ", ����ü�����: " + record.getCumulatedSettleQty());
				return String.format("%d", record.getOrderQty() - record.getCumulatedSettleQty());
			case IDX_ORDER_TIME:				//ó���ð�
				return record.getOrderTime();
			case IDX_LOGIC_NAME:				//������
				return record.getLogicName();
			case IDX_ACCOUNT_NUMBER:			//���¹�ȣ
				return record.getAccountNumber();
			case IDX_ORDER_NUMBER:				//�ֹ���ȣ
				return record.getOrderNumber();
			case IDX_ORIGINAL_ORDER_NUMBER:		//���ֹ���ȣ
				return record.getOriginalOrderNumber();
			case IDX_ORDER_KIND:				//�ֹ�����
				return record.getOrderKindMessage();
			case IDX_ORDER_CONDITION:			//�ֹ�����
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
			case IDX_BUY_SELL_TYPE:				//�Ÿű���
				if ( record.getBuySellType() == OrderRecord.BUY )
					return Display.getDefault().getSystemColor(SWT.COLOR_RED);
				else
					return Display.getDefault().getSystemColor(SWT.COLOR_BLUE);	
			}
			return super.getForeground(element);
		}
	}
	
}