 
package com.traderobot.ui.test.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.traderobot.platform.backtest.BacktestMatchingService;
import com.traderobot.platform.backtest.BacktestOrderManager;
import com.traderobot.platform.order.OrderRecord;
import com.traderobot.platform.order.OrderTableManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class BacktestView {
	
	public static final int IDX_SHORT_CODE = 0;
	public static final int IDX_SHORT_NAME = 1;
	public static final int IDX_LAST_JOB = 2;
	public static final int IDX_BUY_SELL_TYPE = 3;
	public static final int IDX_ORDER_QTY = 4;
	public static final int IDX_ORDER_PRICE = 5;
	public static final int IDX_SETTLE_QTY = 6;
	public static final int IDX_SETTLE_PRICE = 7;
	public static final int IDX_UNSETTLE_QTY = 8;				// 잔량	
	public static final int IDX_ORDER_TIME = 9;
	public static final int IDX_LOGIC_NAME = 10;
	public static final int IDX_ORDER_NUMBER = 11;
	public static final int IDX_ORIGINAL_ORDER_NUMBER = 12;
	public static final int IDX_ORDER_KIND = 13;
	public static final int IDX_ORDER_CONDITION = 14;
	
	private Text shortCodeText;
	private Text standardCodeText;
	private Text logicNameText;
	private Text orderPriceText;
	private Text orderQtyText;
	private Table orderTable;
	private Table matchTable;
	private Text modifyOrderPriceText;
	private Text modifyOriginalOrderNumber;
	private Text cancelOriginalOrderNumber;
	
	private TableViewer orderTableViewer;
	private TableViewer matchTableViewer;
	
	private BacktestOrderManager 		orderManager;
	private BacktestMatchingService		matchingService;
	
	
	@Inject
	public BacktestView() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		
		try {
			matchingService = new BacktestMatchingService();
			matchingService.start();
		} catch (Exception e) {
			MessageDialog.open(MessageDialog.ERROR, parent.getShell(), "매칭서비스생성 오류", e.getMessage(), SWT.NONE);
		}		
		
		orderManager = new BacktestOrderManager();
		orderManager.attachMatchingService(matchingService);
		
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm sashForm = new SashForm(parent, SWT.NONE);		
		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(new FormLayout());
		
		Group group = new Group(composite, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.top = new FormAttachment(0);
		fd_group.left = new FormAttachment(0, 10);
		fd_group.bottom = new FormAttachment(0, 222);
		fd_group.right = new FormAttachment(100, -5);
		group.setLayoutData(fd_group);
		group.setLayout(new FillLayout());
		
		Composite composite_2 = new Composite(group, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(composite_2, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("\uB2E8\uCD95\uCF54\uB4DC");
		
		shortCodeText = new Text(composite_2, SWT.BORDER);
		shortCodeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		shortCodeText.setText("101LC");
		
		Label lblNewLabel_1 = new Label(composite_2, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("\uD45C\uC900\uCF54\uB4DC");
		
		standardCodeText = new Text(composite_2, SWT.BORDER);
		standardCodeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		standardCodeText.setText("KR4101LC0006");
		
		Label lblNewLabel_2 = new Label(composite_2, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("\uB85C\uC9C1\uBA85");
		
		logicNameText = new Text(composite_2, SWT.BORDER);
		logicNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		logicNameText.setText("TEST.LOGIC");
		
		Label label = new Label(composite_2, SWT.NONE);
		label.setText("\uC0C1\uD488\uC720\uD615");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		Combo goodsTypeCombo = new Combo(composite_2, SWT.NONE);
		goodsTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		goodsTypeCombo.add("선물");
		goodsTypeCombo.add("옵션");
		goodsTypeCombo.select(0);
		
		Label lblNewLabel_3 = new Label(composite_2, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("\uB9E4\uB9E4\uAD6C\uBD84");
		
		Combo buySellTypeCombo = new Combo(composite_2, SWT.NONE);
		buySellTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		buySellTypeCombo.add("매수");
		buySellTypeCombo.add("매도");
		buySellTypeCombo.select(0);
		
		Label lblNewLabel_4 = new Label(composite_2, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("\uC8FC\uBB38\uAC00\uACA9");
		
		orderPriceText = new Text(composite_2, SWT.BORDER);
		orderPriceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		orderPriceText.setText("255.80");
		
		Label lblNewLabel_5 = new Label(composite_2, SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_5.setText("\uC8FC\uBB38\uC218\uB7C9");
		
		orderQtyText = new Text(composite_2, SWT.BORDER);
		orderQtyText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		orderQtyText.setText("1");
		
		new Label(composite_2, SWT.NONE);
		
		Button newButton = new Button(composite_2, SWT.NONE);
		newButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				int buySellType;
				String goodsType;
				if ( goodsTypeCombo.getSelectionIndex() == 0 )
					goodsType = OrderRecord.GOODS_TYPE_KOSPI200_FUTURE;
				else
					goodsType = OrderRecord.GOODS_TYPE_KOSPI200_OPTION;
				
				if ( buySellTypeCombo.getSelectionIndex() == 0)
					buySellType = OrderRecord.BUY;
				else
					buySellType = OrderRecord.SELL;
				
				orderManager.orderLimit(logicNameText.getText(), standardCodeText.getText(), 
										shortCodeText.getText(), goodsType, buySellType, 
										Double.parseDouble(orderPriceText.getText()),
										Integer.parseInt(orderQtyText.getText()));
				
			}
		});
		newButton.setText("\uC2E0\uADDC\uC8FC\uBB38");
		
		Group group_1 = new Group(composite, SWT.NONE);
		group_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_group_1 = new FormData();
		fd_group_1.bottom = new FormAttachment(group, 104, SWT.BOTTOM);
		fd_group_1.top = new FormAttachment(group, 5);
		fd_group_1.right = new FormAttachment(group, 0, SWT.RIGHT);
		fd_group_1.left = new FormAttachment(0, 10);
		group_1.setLayoutData(fd_group_1);
		
		Composite composite_5 = new Composite(group_1, SWT.NONE);
		composite_5.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel_6 = new Label(composite_5, SWT.NONE);
		lblNewLabel_6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_6.setText("\uC815\uC815\uAC00\uACA9");
		
		modifyOrderPriceText = new Text(composite_5, SWT.BORDER);
		modifyOrderPriceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_7 = new Label(composite_5, SWT.NONE);
		lblNewLabel_7.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_7.setText("\uC6D0\uC8FC\uBB38\uBC88\uD638");
		
		modifyOriginalOrderNumber = new Text(composite_5, SWT.BORDER);
		modifyOriginalOrderNumber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_5, SWT.NONE);
		
		Button modifyButton = new Button(composite_5, SWT.NONE);
		modifyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = orderTable.getSelectionIndex();
				String orderNumber = orderTable.getItem(index).getText(IDX_ORDER_NUMBER);
				OrderRecord record = orderManager.getOrderTableManager().get(orderNumber);
				if ( record != null )
				{
					orderManager.orderModify(Double.parseDouble(modifyOrderPriceText.getText()), record);
				}
			}
		});
		modifyButton.setText("\uC815\uC815\uC8FC\uBB38");
		
		Group group_2 = new Group(composite, SWT.NONE);
		group_2.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_group_2 = new FormData();
		fd_group_2.bottom = new FormAttachment(group_1, 76, SWT.BOTTOM);
		fd_group_2.top = new FormAttachment(group_1, 5);
		fd_group_2.right = new FormAttachment(group, 0, SWT.RIGHT);
		fd_group_2.left = new FormAttachment(0, 10);
		group_2.setLayoutData(fd_group_2);
		
		Composite composite_6 = new Composite(group_2, SWT.NONE);
		composite_6.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel_8 = new Label(composite_6, SWT.NONE);
		lblNewLabel_8.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_8.setText("\uC6D0\uC8FC\uBB38\uBC88\uD638");
		
		cancelOriginalOrderNumber = new Text(composite_6, SWT.BORDER);
		cancelOriginalOrderNumber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_6, SWT.NONE);
		
		Button cancelButton = new Button(composite_6, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				int index = orderTable.getSelectionIndex();
				String orderNumber = orderTable.getItem(index).getText(IDX_ORDER_NUMBER);
				OrderRecord record = orderManager.getOrderTableManager().get(orderNumber);
				if ( record != null )
					orderManager.orderCancel(record);				
			}
		});
		cancelButton.setText("\uCDE8\uC18C\uC8FC\uBB38");
		
		Button refreshButton = new Button(composite, SWT.NONE);
		refreshButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				orderTableViewer.refresh();
				matchTableViewer.refresh();
			}
		});
		FormData fd_refreshButton = new FormData();
		fd_refreshButton.top = new FormAttachment(group_2, 8);
		fd_refreshButton.right = new FormAttachment(group, 0, SWT.RIGHT);
		refreshButton.setLayoutData(fd_refreshButton);
		refreshButton.setText("Refresh");		
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm sashForm_1 = new SashForm(composite_1, SWT.VERTICAL);
		Composite composite_3 = new Composite(sashForm_1, SWT.NONE);
		composite_3.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		orderTableViewer = new TableViewer(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		orderTableViewer.setContentProvider(new ContentProvider());
		orderTableViewer.setLabelProvider(new TableLabelProvider());
		orderTable = orderTableViewer.getTable();
		orderTable.setHeaderVisible(true);
		orderTable.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				int index = orderTable.getSelectionIndex();
				
				System.out.println("widgetSelected selection index: " + index);
				
				modifyOriginalOrderNumber.setText(orderTable.getItem(index).getText(IDX_ORDER_NUMBER));
				cancelOriginalOrderNumber.setText(orderTable.getItem(index).getText(IDX_ORDER_NUMBER));				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				System.out.println("widgetDefaultSelected");
				int index = orderTable.getSelectionIndex();
				modifyOriginalOrderNumber.setText(orderTable.getItem(index).getText(IDX_ORDER_NUMBER));
				cancelOriginalOrderNumber.setText(orderTable.getItem(index).getText(IDX_ORDER_NUMBER));
			}
			
		});
		
		Composite composite_4 = new Composite(sashForm_1, SWT.NONE);
		composite_4.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		matchTableViewer = new TableViewer(composite_4, SWT.BORDER | SWT.FULL_SELECTION);
		matchTableViewer.setContentProvider(new ContentProvider());
		matchTableViewer.setLabelProvider(new TableLabelProvider());
		matchTable = matchTableViewer.getTable();
		matchTable.setHeaderVisible(true);
		
		createTableColumns(orderTable);		
		createTableColumns(matchTable);
		
		sashForm_1.setWeights(new int[] {1, 1});
		sashForm.setWeights(new int[] {3, 7});
		
		orderTableViewer.setInput(orderManager.getOrderTableManager());
		matchTableViewer.setInput(matchingService.getOrderTableManager());		
		
		
		
	}
	
	private void createTableColumns(Table table)
	{
		{
			TableColumn shortCodeColumn = new TableColumn(table, SWT.NONE);
			shortCodeColumn.setWidth(70);
			shortCodeColumn.setText("\uC885\uBAA9\uCF54\uB4DC");
		}
		{
			TableColumn nameColumn = new TableColumn(table, SWT.NONE);
			nameColumn.setWidth(130);
			nameColumn.setText("\uC885\uBAA9\uBA85");
		}
		{
			TableColumn lastJobColumn = new TableColumn(table, SWT.NONE);
			lastJobColumn.setWidth(60);
			lastJobColumn.setText("\uCC98\uB9AC");
		}
		{
			TableColumn buySellType = new TableColumn(table, SWT.NONE);
			buySellType.setWidth(60);
			buySellType.setText("\uB9E4\uB9E4\uAD6C\uBD84");
		}
		{
			TableColumn orderQtyColumn = new TableColumn(table, SWT.NONE);
			orderQtyColumn.setWidth(60);
			orderQtyColumn.setText("\uC8FC\uBB38\uB7C9");
		}
		{
			TableColumn orderPriceColumn = new TableColumn(table, SWT.NONE);
			orderPriceColumn.setWidth(60);
			orderPriceColumn.setText("\uC8FC\uBB38\uAC00");
		}
		{
			TableColumn settleQtyColumn = new TableColumn(table, SWT.NONE);
			settleQtyColumn.setWidth(60);
			settleQtyColumn.setText("\uCCB4\uACB0\uB7C9");
		}
		{
			TableColumn settlePriceColumn = new TableColumn(table, SWT.NONE);
			settlePriceColumn.setWidth(60);
			settlePriceColumn.setText("\uCCB4\uACB0\uAC00");
		}
		{
			TableColumn unsettledQtyColumn = new TableColumn(table, SWT.NONE);
			unsettledQtyColumn.setWidth(70);
			unsettledQtyColumn.setText("\uC794\uB7C9");
		}
		{
			TableColumn orderTimeColumn = new TableColumn(table, SWT.NONE);
			orderTimeColumn.setWidth(70);
			orderTimeColumn.setText("\uC8FC\uBB38\uC2DC\uAC04");
		}
		{
			TableColumn logicNameColumn = new TableColumn(table, SWT.NONE);
			logicNameColumn.setWidth(80);
			logicNameColumn.setText("\uB85C\uC9C1");
		}	
		{
			TableColumn orderNumberColumn = new TableColumn(table, SWT.NONE);
			orderNumberColumn.setWidth(80);
			orderNumberColumn.setText("\uC8FC\uBB38\uBC88\uD638");
		}
		{
			TableColumn originalOrderNumberColumn = new TableColumn(table, SWT.NONE);
			originalOrderNumberColumn.setWidth(80);
			originalOrderNumberColumn.setText("\uC6D0\uC8FC\uBB38\uBC88\uD638");
		}
		{
			TableColumn orderKindColumn = new TableColumn(table, SWT.NONE);
			orderKindColumn.setWidth(60);
			orderKindColumn.setText("\uC8FC\uBB38\uC720\uD615");
		}
		{
			TableColumn orderConditionColumn = new TableColumn(table, SWT.NONE);
			orderConditionColumn.setWidth(100);
			orderConditionColumn.setText("\uC8FC\uBB38\uC870\uAC74");
		}
	}
	
	///////////////////// INNER CLASS 
	
	private static class ContentProvider implements IStructuredContentProvider {
		
		OrderTableManager manager;
		
		public Object[] getElements(Object inputElement) {
			return manager.getActiveOrderRecordList();
		}
		
		public void dispose() {
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			manager = (OrderTableManager) newInput;
		}
	}
	
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		public String getColumnText(Object element, int columnIndex) {
			
			OrderRecord record = (OrderRecord) element;
			
			switch(columnIndex)
			{
			case IDX_SHORT_CODE:
				return record.getShortCode();
			case IDX_LAST_JOB:
				return record.getStateMessage();
			case IDX_BUY_SELL_TYPE:
				return record.getBuySellTypeMessage();
			case IDX_ORDER_QTY:
				return String.format("%d", record.getOrderQty());
			case IDX_ORDER_PRICE:
				return String.format("%.2f", record.getOrderPrice());
			case IDX_SETTLE_QTY:
				return String.format("%d", record.getSettleQty());
			case IDX_SETTLE_PRICE:
				return String.format("%.2f", record.getSettlePrice());
			case IDX_UNSETTLE_QTY: 
				return String.format("%d", record.getOrderQty() - record.getCumulatedSettleQty());
			case IDX_ORDER_TIME:
				return record.getOrderTime();
			case IDX_LOGIC_NAME:
				return record.getLogicName();
			case IDX_ORDER_NUMBER:
				return record.getOrderNumber();
			case IDX_ORIGINAL_ORDER_NUMBER:
				return record.getOriginalOrderNumber();
			case IDX_ORDER_KIND:
				return record.getOrderKindMessage();
			case IDX_ORDER_CONDITION:
				return record.getOrderConditionMessage();
			}
			return "";
		}
	}
}