 
package com.traderobot.ui.workbench.parts.systemtrading.settings;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.traderobot.platform.TradePlatform;
import com.traderobot.platform.logic.systemtrading.ISignal;
import com.traderobot.platform.logic.systemtrading.ISignalOrderItem;
import com.traderobot.platform.logic.systemtrading.ISystemTradingLogic;
import com.traderobot.platform.logic.systemtrading.SignalOrderItem;
import com.traderobot.platform.logic.systemtrading.SystemTradingLogicDescriptor;
import com.traderobot.ui.workbench.IUIEventConstants;
import com.traderobot.ui.workbench.dialogs.SignalOrderItemInputDialog;

public class SignalPart {
	
	private static final int IDX_STANDARD_CODE = 0;
	private static final int IDX_ITEM_NAME = 1;
	private static final int IDX_TYPE = 2;
	private static final int IDX_BUY_SELL = 3;
	private static final int IDX_QTY = 4;
	private static final int IDX_RULE = 5;
	private static final int IDX_OFFSET = 6;	
	
	@Inject
	private ESelectionService selectionService;	
	private ISystemTradingLogic selectionLogic;
	private ISignal selectedSignal;
	private TableViewer viewer;
	private Table table;
	private Combo signalCombo;
	
	@Inject
	public SignalPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent, Shell shell) {
		
		parent.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FormLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group group = new Group(composite, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		FormData fd_group = new FormData();
		fd_group.top = new FormAttachment(0);
		fd_group.left = new FormAttachment(0);
		fd_group.bottom = new FormAttachment(0, 50);
		fd_group.right = new FormAttachment(100);
		group.setLayoutData(fd_group);
		
		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setText("\uC124\uC815\uD560 \uC2E0\uD638\uB97C \uC120\uD0DD:");
		
		signalCombo = new Combo(group, SWT.READ_ONLY);
		signalCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		signalCombo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e)
			{
//				System.out.println("SignalPart widgetSelected");
				ISignal signal = selectionLogic.getSignal(signalCombo.getText());
				viewer.setInput(signal);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{	
//				System.out.println("SignalPart widgetDefaultSelected");
				ISignal signal = selectionLogic.getSignal(signalCombo.getText());
				viewer.setInput(signal);
			}
		});
		
		viewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		viewer.setContentProvider(new ContentProvider());		
		table = viewer.getTable();
		table.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				selectionService.setSelection(e.item.getData());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{	
				selectionService.setSelection(e.item.getData());
			}
		});
		
		table.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseDoubleClick(MouseEvent e)
			{
				ISignalOrderItem item = (ISignalOrderItem) table.getItem(table.getSelectionIndex()).getData();
				SignalOrderItemInputDialog dialog = new SignalOrderItemInputDialog(shell);
				dialog.setInput(item);
				if ( dialog.open() == Window.OK )
				{
					//ISignalOrderItem item = new SignalOrderItem(selectedSignal);
					item.setStandardCode(dialog.getStandardCode());
					item.setType(dialog.getType());
					item.setBuySellType(dialog.getBuySellCode());
					item.setOrderQty(dialog.getOrderQty());
					item.setOrderPriceCalculationRule(dialog.getOrderPriceCalculationRule());
					item.setOrderPriceCalculationOffsetTick(dialog.getOrderPriceCalculationOffsetTick());
					//selectedSignal.addOrderItem(item);
					viewer.refresh();
				}
			}			
		});
		
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(100, -5);
		fd_table.top = new FormAttachment(group, 10);
		fd_table.right = new FormAttachment(100);
		fd_table.left = new FormAttachment(group, 0, SWT.LEFT);
		table.setLayoutData(fd_table);
		
		TableViewerColumn standardCodeViewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		standardCodeViewerColumn.setLabelProvider(new SignalColumnLabelProvider(IDX_STANDARD_CODE));
		TableColumn standardCodeColumn = standardCodeViewerColumn.getColumn();
		standardCodeColumn.setResizable(false);
		standardCodeColumn.setText("\uC885\uBAA9\uCF54\uB4DC");
		
		TableViewerColumn itemNameViewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		itemNameViewerColumn.setLabelProvider(new SignalColumnLabelProvider(IDX_ITEM_NAME));
		TableColumn itemNameColumn = itemNameViewerColumn.getColumn();
		itemNameColumn.setResizable(false);
		itemNameColumn.setWidth(100);
		itemNameColumn.setText("\uC885\uBAA9\uBA85");
		
		TableViewerColumn typeViewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		typeViewerColumn.setLabelProvider(new SignalColumnLabelProvider(IDX_TYPE));
		TableColumn typeColumn = typeViewerColumn.getColumn();
		typeColumn.setResizable(false);
		typeColumn.setText("\uC885\uBAA9\uC720\uD615");
		
		TableViewerColumn buySellViewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		buySellViewerColumn.setLabelProvider(new SignalColumnLabelProvider(IDX_BUY_SELL));
		TableColumn buySellColumn = buySellViewerColumn.getColumn();
		buySellColumn.setResizable(false);
		buySellColumn.setWidth(60);
		buySellColumn.setText("\uB9E4\uB9E4\uAD6C\uBD84");
		
		TableViewerColumn qtyViewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		qtyViewerColumn.setLabelProvider(new SignalColumnLabelProvider(IDX_QTY));
		TableColumn qtyColumn = qtyViewerColumn.getColumn();
		qtyColumn.setResizable(false);
		qtyColumn.setWidth(60);
		qtyColumn.setText("\uC8FC\uBB38\uC218\uB7C9");
		
		TableViewerColumn ruleViewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		ruleViewerColumn.setLabelProvider(new SignalColumnLabelProvider(IDX_RULE));
		TableColumn ruleColumn = ruleViewerColumn.getColumn();
		ruleColumn.setResizable(false);
		ruleColumn.setWidth(70);
		ruleColumn.setText("\uAC00\uACA9\uACB0\uC815");
		
		TableViewerColumn offsetViewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		offsetViewerColumn.setLabelProvider(new SignalColumnLabelProvider(IDX_OFFSET));
		TableColumn offsetColumn = offsetViewerColumn.getColumn();
		offsetColumn.setResizable(false);
		offsetColumn.setWidth(60);
		offsetColumn.setText("\uD2F1\uC870\uC815");
		
		try {
			// 기존의 시스템트레이딩이 선택되어있으면, selection을 설정하고 refresh한다.
			if ( selectionService.getSelection() instanceof SystemTradingLogicDescriptor ) {
				SystemTradingLogicDescriptor desc = (SystemTradingLogicDescriptor)selectionService.getSelection();
				selectionLogic = desc.getInstance();
				refresh();
			}
		} catch (Exception ex) {}
		
		// viewer input설정
		
	}
	
	@Inject
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object o)
	{
		try {
			if ( o instanceof SystemTradingLogicDescriptor )
			{
				SystemTradingLogicDescriptor desc = ((SystemTradingLogicDescriptor) o);
				if ( desc.getInstance() != null && selectionLogic != desc.getInstance() )
				{
					selectionLogic = desc.getInstance();
					refresh();
				}
			}
		} catch (Exception e) {}
	}
	
	public void refresh()
	{
		signalCombo.removeAll();
		//signalCombo.deselectAll();
		ISignal[] signals = selectionLogic.getRegisteredSignals();
		for(int i=0; i<signals.length; i++)
			signalCombo.add(signals[i].getId());
		if ( signalCombo.getItemCount() > 0 ) {
			signalCombo.select(0);
			viewer.setInput(signals[0]);
		}
	}
	
	private class ContentProvider implements IStructuredContentProvider {
		
		private ISignal signal;
		
		public Object[] getElements(Object inputElement) {
			return signal.getSignalOrderItems();
		}
		
		public void dispose() {
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//			System.out.println("SignalPart inputChanged");			
			signal = (ISignal) newInput;
			selectedSignal = signal; 
		}
	}
		
	private class SignalColumnLabelProvider extends ColumnLabelProvider {
		
		private int columnIndex;
		
		public SignalColumnLabelProvider(int columnIndex)
		{
			this.columnIndex = columnIndex;
		}
		
		@Override
		public String getText(Object element)
		{
			ISignalOrderItem item = (ISignalOrderItem) element;			
			
			switch(columnIndex)
			{
			case IDX_STANDARD_CODE:
				return item.getStandardCode();
			case IDX_ITEM_NAME:
				if ( TradePlatform.getPlatform().getItemManager().contains(item.getStandardCode()) )
					return TradePlatform.getPlatform().getItemManager().get(item.getStandardCode()).getShortName();
				else
					return "삭제된 종목";
			case IDX_TYPE:
				return item.getType();
			case IDX_BUY_SELL:
				if ( item.getBuySellType() == ISignal.SELL )
					return "매도";
				else if ( item.getBuySellType() == ISignal.BUY )
					return "매수";
				else
					return "없음";
			case IDX_QTY:
				return Integer.toString(item.getOrderQty());
			case IDX_RULE:
				switch(item.getOrderPriceCalculationRule()) {
				case ISignalOrderItem.ASK:
					return "매도호가";
				case ISignalOrderItem.BID:
					return "매수호가";
				case ISignalOrderItem.LAST:
					return "현재가";
//				case ISignalOrderItem.MARKET:
//					return "시장가주문";
				default:
					return "알수없음";
				}				
			case IDX_OFFSET:
				return Integer.toString(item.getOrderPriceCalculationOffsetTick());
			}
			return "";
		}
	}
	
	
	///////////////// EVENT HANDLING
	@Inject @Optional
	public void onAddSignalOrderItem(@UIEventTopic(IUIEventConstants.TOPIC_ADD_SIGNAL_ORDER_ITEM) Object data, 
									 @Named(IServiceConstants.ACTIVE_SHELL) Shell shell)
	{
		SignalOrderItemInputDialog dialog = new SignalOrderItemInputDialog(shell);
		if ( dialog.open() == Window.OK )
		{
			ISignalOrderItem item = new SignalOrderItem(selectedSignal);
			item.setStandardCode(dialog.getStandardCode());
			item.setType(dialog.getType());
			item.setBuySellType(dialog.getBuySellCode());
			item.setOrderQty(dialog.getOrderQty());
			item.setOrderPriceCalculationRule(dialog.getOrderPriceCalculationRule());
			item.setOrderPriceCalculationOffsetTick(dialog.getOrderPriceCalculationOffsetTick());
			selectedSignal.addOrderItem(item);
			viewer.refresh();
		}
	}
	
	@Inject @Optional
	public void onEditSignalOrderItem(
		@UIEventTopic(IUIEventConstants.TOPIC_EDIT_SIGNAL_ORDER_ITEM) Object data,
		@Named(IServiceConstants.ACTIVE_SHELL) Shell shell)
	{
		ISignalOrderItem item = (ISignalOrderItem) table.getItem(table.getSelectionIndex()).getData();
		SignalOrderItemInputDialog dialog = new SignalOrderItemInputDialog(shell);
		dialog.setInput(item);
		if ( dialog.open() == Window.OK )
		{
			//ISignalOrderItem item = new SignalOrderItem(selectedSignal);
			item.setStandardCode(dialog.getStandardCode());
			item.setType(dialog.getType());
			item.setBuySellType(dialog.getBuySellCode());
			item.setOrderQty(dialog.getOrderQty());
			item.setOrderPriceCalculationRule(dialog.getOrderPriceCalculationRule());
			item.setOrderPriceCalculationOffsetTick(dialog.getOrderPriceCalculationOffsetTick());
			//selectedSignal.addOrderItem(item);
			viewer.refresh();
		}
	}
	
	@Inject @Optional
	public void onDeleteSystemTradingLogic(
		@UIEventTopic(IUIEventConstants.TOPIC_DELETE_SIGNAL_ORDER_ITEM) 
		Object data)
	{
		ISignalOrderItem item = (ISignalOrderItem) table.getItem(table.getSelectionIndex()).getData();
		ISignal signal = selectionLogic.getSignal(signalCombo.getText());
		signal.removeOrderItem(item);
		viewer.refresh();
	}
	
}