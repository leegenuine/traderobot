package com.traderobot.ui.workbench.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import com.traderobot.platform.TradePlatform;
import com.traderobot.platform.koscom.transaction.TransactionCode;
import com.traderobot.platform.logic.systemtrading.ISignalOrderItem;
import com.traderobot.platform.master.IMasterData;

import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SignalOrderItemInputDialog extends TitleAreaDialog
{
	private IMasterData selectedMaster;
	private int buySellCode;
	private int qty;
	private int rule;
	private int tick;
	
	private Combo typeCombo;
	private Combo buySellCombo;
	private Spinner qtySpinner;
	private Combo ruleCombo;
	private Spinner tickSpinner;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public SignalOrderItemInputDialog(Shell parentShell)
	{
		super(parentShell);
		selectedMaster = null;
		buySellCode = ISignalOrderItem.BUY;
		qty = 0;
		rule = ISignalOrderItem.ASK;
		tick = 0;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		setMessage("\uD574\uB2F9 \uC2DC\uADF8\uB110\uACFC \uC5F0\uACC4\uB420 \uC8FC\uBB38 \uC885\uBAA9\uC5D0 \uB300\uD574\uC11C \uC544\uB798\uC758 \uC815\uBCF4\uB97C \uC785\uB825\uD558\uC2ED\uC2DC\uC624.");
		setTitle("\uC2DC\uADF8\uB110 \uC5F0\uACC4 \uC8FC\uBB38 \uC885\uBAA9 \uC124\uC815");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		GridLayout gl_container = new GridLayout(2, false);
		gl_container.verticalSpacing = 10;
		container.setLayout(gl_container);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);		
		gd_lblNewLabel.horizontalIndent = 10;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("\uC885\uBAA9\uC120\uD0DD");
		
		Composite composite = new Composite(container, SWT.NONE);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		gl_composite.verticalSpacing = 1;
		gl_composite.horizontalSpacing = 1;
		composite.setLayout(gl_composite);
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite.heightHint = 23;
		composite.setLayoutData(gd_composite);
		
		Text masterNameText = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		masterNameText.setEditable(true);
		masterNameText.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
		masterNameText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		Button findButton = new Button(composite, SWT.NONE);
		findButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				MasterSelectionDialog dialog = new MasterSelectionDialog(getShell());
				if ( dialog.open() == Window.OK ) {
					selectedMaster = dialog.getSelectedMaster();
					masterNameText.setText(selectedMaster.getName());
					if ( selectedMaster.getType().equals(TransactionCode.KOSPI200_FUTURE_MASTER_DATA) )
						typeCombo.select(0);
					if ( selectedMaster.getType().equals(TransactionCode.KOSPI200_OPTION_MASTER_DATA) )
						typeCombo.select(1);
				}
			}
		});
		findButton.setImage(ResourceManager.getPluginImage("com.traderobot.ui.workbench", "icons/newannotation_wiz.png"));
		GridData gd_findButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_findButton.heightHint = 20;
		gd_findButton.widthHint = 25;
		findButton.setLayoutData(gd_findButton);		
		
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		GridData gd_lblNewLabel_1 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_1.horizontalIndent = 10;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		lblNewLabel_1.setAlignment(SWT.RIGHT);
		lblNewLabel_1.setText("\uC885\uBAA9\uC720\uD615");
		
		typeCombo = new Combo(container, SWT.READ_ONLY);		
		typeCombo.setEnabled(false);
		typeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		typeCombo.add("선물");
		typeCombo.add("옵션");		
		typeCombo.setText("\uC120\uBB3C\r\n\uC635\uC158");
		
		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		GridData gd_lblNewLabel_2 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_2.horizontalIndent = 10;
		lblNewLabel_2.setLayoutData(gd_lblNewLabel_2);
		lblNewLabel_2.setAlignment(SWT.RIGHT);
		lblNewLabel_2.setText("\uB9E4\uC218/\uB9E4\uB3C4");
		
		buySellCombo = new Combo(container, SWT.NONE);		
		buySellCombo.add("매수");
		buySellCombo.add("매도");		
		
		Label lblNewLabel_3 = new Label(container, SWT.NONE);
		GridData gd_lblNewLabel_3 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_3.horizontalIndent = 10;
		lblNewLabel_3.setLayoutData(gd_lblNewLabel_3);
		lblNewLabel_3.setAlignment(SWT.RIGHT);
		lblNewLabel_3.setText("\uC8FC\uBB38\uC218\uB7C9");
		
		qtySpinner = new Spinner(container, SWT.BORDER);
		qtySpinner.setMaximum(1000);
		qtySpinner.setMinimum(1);
		
		Label lblNewLabel_4 = new Label(container, SWT.NONE);
		GridData gd_lblNewLabel_4 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_4.horizontalIndent = 10;
		lblNewLabel_4.setLayoutData(gd_lblNewLabel_4);
		lblNewLabel_4.setAlignment(SWT.RIGHT);
		lblNewLabel_4.setText("\uC8FC\uBB38\uAC00\uACA9 \uACB0\uC815\uBC29\uC2DD");
		
		ruleCombo = new Combo(container, SWT.NONE);		
		ruleCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		ruleCombo.add("매도호가");
		ruleCombo.add("매수호가");
		ruleCombo.add("현재가");
//		ruleCombo.add("시장가");		
		
		Label lblNewLabel_5 = new Label(container, SWT.NONE);
		GridData gd_lblNewLabel_5 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_5.horizontalIndent = 10;
		lblNewLabel_5.setLayoutData(gd_lblNewLabel_5);
		lblNewLabel_5.setAlignment(SWT.RIGHT);
		lblNewLabel_5.setText("\uC8FC\uBB38\uAC00\uACA9 \uD2F1\uC870\uC815");
		
		tickSpinner = new Spinner(container, SWT.BORDER);
		tickSpinner.setMaximum(20);
		tickSpinner.setMinimum(-20);
		
		if ( selectedMaster != null ) {
			masterNameText.setText(selectedMaster.getName());
			if ( selectedMaster.getType().equals(ISignalOrderItem.KOSPI200_FUTURE) )
				typeCombo.select(0);
			else
				typeCombo.select(1);			
		}
		if ( buySellCode == ISignalOrderItem.BUY )
			buySellCombo.select(0);
		else
			buySellCombo.select(1);
		qtySpinner.setSelection(qty);
		ruleCombo.select(rule);
		tickSpinner.setSelection(tick);						
		return area;
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(354, 324);
	}
	
	public String getStandardCode() 
	{
		if ( selectedMaster != null )
			return selectedMaster.getStandardCode();
		return "";
	}
	
	public String getType()
	{
		return selectedMaster.getType();
	}
	
	public int getBuySellCode()
	{
		return buySellCode;
	}
	
	public int getOrderQty()
	{
		return qty;
	}
	
	public int getOrderPriceCalculationRule()
	{
		return rule;
	}
	
	public int getOrderPriceCalculationOffsetTick()
	{
		return tick;
	}
	
	public void setInput(ISignalOrderItem item)
	{
		selectedMaster = TradePlatform.getPlatform().getItemManager().get(item.getStandardCode());		
		buySellCode = item.getBuySellType();
		qty = item.getOrderQty();
		rule = item.getOrderPriceCalculationRule();
		tick = item.getOrderPriceCalculationOffsetTick();
	}

	@Override
	protected void okPressed()
	{
		if ( selectedMaster == null )
		{
			MessageDialog.open(MessageDialog.INFORMATION, getShell(), "종목미선택", "종목을 선택하지 않았습니다.", SWT.NONE);
			
			return;
		}
		
		if ( buySellCombo.getSelectionIndex() == 0)
			buySellCode = ISignalOrderItem.BUY;
		else
			buySellCode = ISignalOrderItem.SELL;		
		qty = qtySpinner.getSelection();		
		rule = ruleCombo.getSelectionIndex();
		tick = tickSpinner.getSelection();
		super.okPressed();
	}
	
	
}
