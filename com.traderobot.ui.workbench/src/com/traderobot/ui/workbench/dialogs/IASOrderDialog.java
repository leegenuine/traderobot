package com.traderobot.ui.workbench.dialogs;

import java.io.IOException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import com.traderobot.platform.TradePlatform;
import com.traderobot.platform.TradePlatformConfiguration;
import com.traderobot.platform.koscom.transaction.TransactionCode;
import com.traderobot.platform.logic.systemtrading.SystemTradingLogicDescriptor;
import com.traderobot.platform.logic.systemtrading.SystemTradingLogicManager;
import com.traderobot.platform.master.IMasterData;
import com.traderobot.platform.order.OrderRecord;

public class IASOrderDialog extends Dialog {
	
	private SystemTradingLogicDescriptor[] logics;
	private IMasterData selectedMaster;	
	private Text masterNameText;
	private Text shortCodeText;
	private Text goodsTypeText;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public IASOrderDialog(Shell parentShell) {
		super(parentShell);
		SystemTradingLogicManager manager = SystemTradingLogicManager.getInstance();
		logics = manager.getEnableExtensionLogics();
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group group = new Group(container, SWT.NONE);
		group.setLayout(new FillLayout());
		
		Composite composite_2 = new Composite(group, SWT.NONE);
		composite_2.setLayout(new GridLayout(3, false));
		
		Label nameLabel = new Label(composite_2, SWT.NONE);
		nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		nameLabel.setText("종목명:");
		
		masterNameText = new Text(composite_2, SWT.BORDER);
		masterNameText.setEditable(false);
		masterNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		masterNameText.setText("101LC");
		
		Button findItemButton = new Button(composite_2, SWT.NONE);
		findItemButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MasterSelectionDialog dialog = new MasterSelectionDialog(getShell());
				if ( dialog.open() == Window.OK ) {
					selectedMaster = dialog.getSelectedMaster();
					masterNameText.setText(selectedMaster.getName());
					shortCodeText.setText(selectedMaster.getShortCode());
					if ( selectedMaster.getType().equals(TransactionCode.KOSPI200_FUTURE_MASTER_DATA) )
						goodsTypeText.setText("KOSPI200선물");
					if ( selectedMaster.getType().equals(TransactionCode.KOSPI200_OPTION_MASTER_DATA) )
						goodsTypeText.setText("KOSPI200옵션");
				}
			}
		});
		findItemButton.setText("...");
		
		Label shortCodeLabel = new Label(composite_2, SWT.NONE);
		shortCodeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		shortCodeLabel.setText("단축코드:");
		
		shortCodeText = new Text(composite_2, SWT.BORDER);
		shortCodeText.setEditable(false);
		shortCodeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		shortCodeText.setText("KR4101LC0006");
		new Label(composite_2, SWT.NONE);
		
		Label goodsTypeLabel = new Label(composite_2, SWT.NONE);
		goodsTypeLabel.setText("상품유형:");
		goodsTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		goodsTypeText = new Text(composite_2, SWT.BORDER);
		goodsTypeText.setEditable(false);
		goodsTypeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_2, SWT.NONE);
		
		Label logicNameLabel = new Label(composite_2, SWT.NONE);
		logicNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		logicNameLabel.setText("로직명:");
		
		Combo logicCombo = new Combo(composite_2, SWT.NONE);
		logicCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(int i=0; i<logics.length; i++)
			logicCombo.add(logics[i].getName());
		new Label(composite_2, SWT.NONE);
		
		Label buySellTypeLabel = new Label(composite_2, SWT.NONE);
		buySellTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		buySellTypeLabel.setText("매매구분:");
		
		Combo buySellTypeCombo = new Combo(composite_2, SWT.NONE);
		buySellTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		buySellTypeCombo.add("매수");
		buySellTypeCombo.add("매도");
		buySellTypeCombo.select(0);
		new Label(composite_2, SWT.NONE);
		
		Label orderPriceLabel = new Label(composite_2, SWT.NONE);
		orderPriceLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		orderPriceLabel.setText("주문가격:");
		
		Spinner orderPriceSpinner = new Spinner(composite_2, SWT.BORDER);
		orderPriceSpinner.setIncrement(5);
		orderPriceSpinner.setMaximum(100000);
		GridData gd_orderPriceSpinner = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_orderPriceSpinner.widthHint = 50;
		orderPriceSpinner.setLayoutData(gd_orderPriceSpinner);
		orderPriceSpinner.setDigits(2);
		new Label(composite_2, SWT.NONE);
		
		Label orderQtyLabel = new Label(composite_2, SWT.NONE);
		orderQtyLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		orderQtyLabel.setText("주문수량:");
		
		Spinner orderQtySpinner = new Spinner(composite_2, SWT.BORDER);
		GridData gd_orderQtySpinner = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_orderQtySpinner.widthHint = 50;
		orderQtySpinner.setLayoutData(gd_orderQtySpinner);
		orderQtySpinner.setSelection(1);
		new Label(composite_2, SWT.NONE);
		
		new Label(composite_2, SWT.NONE);		
		Button newButton = new Button(composite_2, SWT.NONE);
		newButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		newButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				int buySellType;
				String goodsType = OrderRecord.GOODS_TYPE_KOSPI200_FUTURE;
				if ( selectedMaster.getType().equals(TransactionCode.KOSPI200_FUTURE_MASTER_DATA) )
					goodsType = OrderRecord.GOODS_TYPE_KOSPI200_FUTURE;
				else if ( selectedMaster.getType().equals(TransactionCode.KOSPI200_OPTION_MASTER_DATA) )
					goodsType = OrderRecord.GOODS_TYPE_KOSPI200_OPTION;
				else if ( selectedMaster.getType().equals(TransactionCode.MINI_FUTURE_MASTER_DATA) )
					goodsType = OrderRecord.GOODS_TYPE_MINI_FUTURE;
				else if ( selectedMaster.getType().equals(TransactionCode.MINI_OPTION_MASTER_DATA) )
					goodsType = OrderRecord.GOODS_TYPE_MINI_OPTION;				
				if ( buySellTypeCombo.getSelectionIndex() == 0)
					buySellType = OrderRecord.BUY;
				else
					buySellType = OrderRecord.SELL;
				OrderRecord record = OrderRecord.create();
				record.setOrderType(OrderRecord.NEW);
				record.setLogicName(logicCombo.getText());
				record.setAccountNumber(TradePlatformConfiguration.getInstance().getFoAccountNumber());
				record.setStandardCode(selectedMaster.getStandardCode());
				record.setShortCode(selectedMaster.getShortCode());
				record.setGoodsType(goodsType);
				record.setBuySellType(buySellType);
				record.setOrderPrice(Double.parseDouble(orderPriceSpinner.getText()));
				record.setOrderQty(Integer.parseInt(orderQtySpinner.getText()));
				try {
					TradePlatform.getPlatform().getOrderManager().sendNew(record);
				} catch(IOException ioe) {
					MessageDialog.openError(parent.getShell(), "주문오류", ioe.getMessage());
				}
			}
		});
		newButton.setText("신규주문");
		new Label(composite_2, SWT.NONE);
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
//		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
//		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(349, 283);
	}

}
