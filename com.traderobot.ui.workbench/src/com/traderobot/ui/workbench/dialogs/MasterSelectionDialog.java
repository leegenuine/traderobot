package com.traderobot.ui.workbench.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.traderobot.platform.TradePlatform;
import com.traderobot.platform.master.IMasterData;
import com.traderobot.platform.master.MasterManager;
import com.traderobot.platform.master.future.CommodityFutureMaster;
import com.traderobot.platform.master.future.KOSPI200FutureMaster;
import com.traderobot.platform.master.future.MiniFutureMaster;
import com.traderobot.platform.master.option.KOSPI200OptionMaster;
import com.traderobot.platform.master.option.MiniOptionMaster;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class MasterSelectionDialog extends Dialog
{
	private IMasterData selected;
	
	private MasterManager manager;
	private KOSPI200FutureMaster kospi200Futures[];
	private KOSPI200OptionMaster kospi200Calls[];
	private KOSPI200OptionMaster kospi200Puts[];
	private MiniFutureMaster miniFutures[];
	private MiniOptionMaster miniCalls[];
	private MiniOptionMaster miniPuts[];
	private CommodityFutureMaster commodityFutures[];
	
	private String kospi200OptionDates[];
	private int selectedKOSPI200OptionMonth;
	
	private String miniOptionDates[];
	private int selectedMiniOptionMonth;
	
	private String productIds[];
	private String commodityFutureDates[];
	private String selectedProductId;
	private int selectedCommodityFutureMonth;
	
	
	private List kospi200CallList;
	private List kospi200PutList;
	private List miniCallList;
	private List miniPutList;
	private List commodityFutureList;
	
	private Text selectedText;
	Combo commodityFutureMaturityCombo;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public MasterSelectionDialog(Shell parentShell)
	{
		super(parentShell);
		manager = TradePlatform.getPlatform().getItemManager();
		kospi200Futures = manager.getKOSPI200FutureItemManager().getExcludeSpreadList();		
		kospi200OptionDates = manager.getKOSPI200OptionItemManager().getLastTradeDates();
		miniFutures = manager.getMiniFutureItemManager().getExcludeSpreadList();
		miniOptionDates = manager.getMiniOptionItemManager().getLastTradeDates();
		
		selectedKOSPI200OptionMonth = 0;
		selectedMiniOptionMonth = 0;
		selectedCommodityFutureMonth = 0;
		
		productIds = manager.getCommodityFutureItemManager().getProductIds();
		//commodityFutureDates = manager.getCommodityFutureItemManager().getLastTradeDates();
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		Composite baseComposite = new Composite(container, SWT.NONE);
		baseComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		baseComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabFolder tabFolder = new TabFolder(baseComposite, SWT.NONE);
		
		TabItem kospi200TabItem = new TabItem(tabFolder, SWT.NONE);
		kospi200TabItem.setText("KOSPI200\uC120\uBB3C\uC635\uC158");
		
		Composite kospi200Pane = new Composite(tabFolder, SWT.NONE);
		kospi200TabItem.setControl(kospi200Pane);
		kospi200Pane.setLayout(new GridLayout(1, false));
		
		Composite kospi200FuturePane = new Composite(kospi200Pane, SWT.NONE);
		kospi200FuturePane.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_kospi200FuturePane = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_kospi200FuturePane.heightHint = 50;
		kospi200FuturePane.setLayoutData(gd_kospi200FuturePane);
		
		Group kospi200FutureGroup = new Group(kospi200FuturePane, SWT.NONE);
		kospi200FutureGroup.setText("\uC120\uBB3C");
		
		Combo kospi200FutureCombo = new Combo(kospi200FutureGroup, SWT.NONE);
		kospi200FutureCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelected(kospi200Futures[kospi200FutureCombo.getSelectionIndex()]);
			}
		});
		for(int i=0; i<kospi200Futures.length; i++)
			kospi200FutureCombo.add(kospi200Futures[i].getName());
		if ( kospi200Futures.length > 0 )
			kospi200FutureCombo.select(0);
		kospi200FutureCombo.setBounds(10, 19, 352, 20);
		
		Composite kospi200OptionPane = new Composite(kospi200Pane, SWT.NONE);
		kospi200OptionPane.setLayout(new FillLayout(SWT.HORIZONTAL));
		kospi200OptionPane.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group kospi200OptionGroup = new Group(kospi200OptionPane, SWT.NONE);
		kospi200OptionGroup.setText("\uC635\uC158");
		kospi200OptionGroup.setLayout(new GridLayout(1, false));
		
		Composite kospi200OptionMaturityPane = new Composite(kospi200OptionGroup, SWT.NONE);
		kospi200OptionMaturityPane.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		kospi200OptionMaturityPane.setLayout(new GridLayout(2, false));
		
		Label kospi200OptionMaturityLabel = new Label(kospi200OptionMaturityPane, SWT.NONE);
		kospi200OptionMaturityLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		kospi200OptionMaturityLabel.setText("\uC635\uC158\uB9CC\uAE30");
		
		Combo kospi200OptionMaturityCombo = new Combo(kospi200OptionMaturityPane, SWT.NONE);
		kospi200OptionMaturityCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedKOSPI200OptionMonth = kospi200OptionMaturityCombo.getSelectionIndex();
				refreshKospi200OptionList();
			}
		});
		for(int i=0; i<kospi200OptionDates.length; i++)
			kospi200OptionMaturityCombo.add(kospi200OptionDates[i]);
		if ( kospi200OptionDates.length > 0)
			kospi200OptionMaturityCombo.select(0);
		kospi200OptionMaturityCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite kospi200OptionListPane = new Composite(kospi200OptionGroup, SWT.NONE);
		kospi200OptionListPane.setLayout(new GridLayout(2, true));
		kospi200OptionListPane.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		kospi200CallList = new List(kospi200OptionListPane, SWT.BORDER | SWT.V_SCROLL);
		kospi200CallList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelected(kospi200Calls[kospi200CallList.getSelectionIndex()]);
			}
		});
		kospi200CallList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		kospi200PutList = new List(kospi200OptionListPane, SWT.BORDER | SWT.V_SCROLL);
		kospi200PutList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelected(kospi200Puts[kospi200PutList.getSelectionIndex()]);
			}
		});
		kospi200PutList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		
		///////////////////////// MINI
		TabItem miniTabItem = new TabItem(tabFolder, SWT.NONE);
		miniTabItem.setText("\uBBF8\uB2C8\uC120\uBB3C\uC635\uC158");
		
		Composite miniPane = new Composite(tabFolder, SWT.NONE);
		miniTabItem.setControl(miniPane);
		miniPane.setLayout(new GridLayout(1, false));
		
		Composite miniFuturePane = new Composite(miniPane, SWT.NONE);
		miniFuturePane.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_miniFuturePane = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_miniFuturePane.heightHint = 50;
		miniFuturePane.setLayoutData(gd_miniFuturePane);
		
		Group miniFutureGroup = new Group(miniFuturePane, SWT.NONE);
		miniFutureGroup.setText("\uC120\uBB3C");
		
		Combo miniFutureCombo = new Combo(miniFutureGroup, SWT.NONE);
		miniFutureCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelected(miniFutures[miniFutureCombo.getSelectionIndex()]);
			}
		});
		for(int i=0; i<miniFutures.length; i++)
			miniFutureCombo.add(miniFutures[i].getName());
		if ( miniFutures.length > 0 )
			miniFutureCombo.select(0);
		miniFutureCombo.setBounds(10, 19, 352, 20);
		
		Composite miniOptionPane = new Composite(miniPane, SWT.NONE);
		miniOptionPane.setLayout(new FillLayout(SWT.HORIZONTAL));
		miniOptionPane.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group miniOptionGroup = new Group(miniOptionPane, SWT.NONE);
		miniOptionGroup.setText("\uC635\uC158");
		miniOptionGroup.setLayout(new GridLayout(1, false));
		
		Composite miniOptionMaturityPane = new Composite(miniOptionGroup, SWT.NONE);
		miniOptionMaturityPane.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		miniOptionMaturityPane.setLayout(new GridLayout(2, false));
		
		Label miniOptionMaturityLabel = new Label(miniOptionMaturityPane, SWT.NONE);
		miniOptionMaturityLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		miniOptionMaturityLabel.setText("\uC635\uC158\uB9CC\uAE30");
		
		Combo miniOptionMaturityCombo = new Combo(miniOptionMaturityPane, SWT.NONE);
		miniOptionMaturityCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedMiniOptionMonth = miniOptionMaturityCombo.getSelectionIndex();
				refreshMiniOptionList();
			}
		});
		for(int i=0; i<miniOptionDates.length; i++)
			miniOptionMaturityCombo.add(miniOptionDates[i]);
		if ( miniOptionDates.length > 0 )
			miniOptionMaturityCombo.select(0);
		miniOptionMaturityCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite miniOptionListPane = new Composite(miniOptionGroup, SWT.NONE);
		miniOptionListPane.setLayout(new GridLayout(2, true));
		miniOptionListPane.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		miniCallList = new List(miniOptionListPane, SWT.BORDER | SWT.V_SCROLL);
		miniCallList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelected(miniCalls[miniCallList.getSelectionIndex()]);
			}
		});
		miniCallList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		miniPutList = new List(miniOptionListPane, SWT.BORDER | SWT.V_SCROLL);
		miniPutList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelected(miniPuts[miniPutList.getSelectionIndex()]);
			}
		});
		miniPutList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// 상품선물		
		TabItem commodityTabItem = new TabItem(tabFolder, SWT.NONE);
		commodityTabItem.setText("\uC0C1\uD488\uC120\uBB3C");
		
		Composite commodityPane = new Composite(tabFolder, SWT.NONE);
		commodityTabItem.setControl(commodityPane);
		commodityPane.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(commodityPane, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group group = new Group(composite, SWT.NONE);
		group.setLayout(new GridLayout(4, false));
		
		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("\uC0C1\uD488\uC720\uD615");
		
		Combo productIdCombo = new Combo(group, SWT.NONE);
		productIdCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				selectedProductId = productIdCombo.getText();
				refreshCommodityFutureMaturity();
			}
		});
		for(int i=0; i<productIds.length; i++) 
			productIdCombo.add(productIds[i]);
		if ( productIds.length > 0 )
			productIdCombo.select(0);
		productIdCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setBounds(0, 0, 61, 12);
		lblNewLabel.setText("\uB9CC\uAE30\uC77C:");
		
		commodityFutureMaturityCombo = new Combo(group, SWT.NONE);
		commodityFutureMaturityCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedCommodityFutureMonth = commodityFutureMaturityCombo.getSelectionIndex();
				refreshCommodityFutureList();
			}
		});
		commodityFutureMaturityCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite_1 = new Composite(commodityPane, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		commodityFutureList = new List(composite_1, SWT.BORDER | SWT.V_SCROLL);
		commodityFutureList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelected(commodityFutures[commodityFutureList.getSelectionIndex()]);
			}
		});
		
		selectedText = new Text(container, SWT.BORDER);
		selectedText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		selectedText.setEnabled(false);
		selectedText.setEditable(false);
		
//		for(int i=0; i<kospi200OptionDates.length; i++)
//			optionMaturityCombo.add(kospi200OptionDates[i]);
		if ( kospi200OptionDates.length > 0 )
			refreshKospi200OptionList();
		if ( miniOptionDates.length > 0 )
			refreshMiniOptionList();
		if ( productIds.length > 0 )
			refreshCommodityFutureMaturity();
		
		getShell().setText("종목선택");				
		return container;
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
		return new Point(406, 548);
	}
	
	protected void refreshKospi200OptionList()
	{	
		kospi200Calls = manager.getKOSPI200OptionItemManager().find(kospi200OptionDates[selectedKOSPI200OptionMonth], 2);
		if ( kospi200Calls == null)
			return;
		kospi200CallList.removeAll();
		for(int i=0; i<kospi200Calls.length; i++)
			kospi200CallList.add(kospi200Calls[i].getName());
		
		kospi200Puts = manager.getKOSPI200OptionItemManager().find(kospi200OptionDates[selectedKOSPI200OptionMonth], 3);
		if ( kospi200Puts == null)
			return;
		kospi200PutList.removeAll();
		for(int i=0; i<kospi200Puts.length; i++)
			kospi200PutList.add(kospi200Puts[i].getName());
	}
	
	protected void refreshMiniOptionList()
	{
		int month = selectedMiniOptionMonth+1;
		miniCalls = manager.getMiniOptionItemManager().find(month, 2);
		if ( miniCalls == null)
			return;
		miniCallList.removeAll();
		for(int i=0; i<miniCalls.length; i++)
			miniCallList.add(miniCalls[i].getName());
		
		miniPuts = manager.getMiniOptionItemManager().find(month, 3);
		if ( miniPuts == null)
			return;
		miniPutList.removeAll();
		for(int i=0; i<miniPuts.length; i++)
			miniPutList.add(miniPuts[i].getName());
	}
	
	protected void refreshCommodityFutureMaturity()
	{	
		commodityFutureMaturityCombo.removeAll();		
		commodityFutureDates = manager.getCommodityFutureItemManager().getLastTradeDatesByProductId(selectedProductId);
		if ( commodityFutureDates == null )
			return;
		for(int i=0; i<commodityFutureDates.length; i++)
			commodityFutureMaturityCombo.add(commodityFutureDates[i]);
		commodityFutureList.removeAll();
	}
	
	protected void refreshCommodityFutureList()
	{	
		commodityFutures = manager.getCommodityFutureItemManager().find(selectedProductId, commodityFutureDates[selectedCommodityFutureMonth]);
		if ( commodityFutures == null )
			return;
		commodityFutureList.removeAll();
		for(int i=0; i<commodityFutures.length; i++)
			commodityFutureList.add(commodityFutures[i].getName());
	}
	
	
	
	protected void setSelected(IMasterData master) {
		selected = master;
		selectedText.setText(selected.getName());
	}
 	
	
	public IMasterData getSelectedMaster()
	{
		return selected;
	}

	@Override
	protected void okPressed()
	{
		if ( selected == null )
		{
			MessageDialog.open(MessageDialog.INFORMATION, getShell(), "종목미선택", "종목을 선택하지 않았습니다.", SWT.NONE);
			return;
		}		
		super.okPressed();
	}
}
