package com.traderobot.ui.workbench.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.traderobot.platform.TradePlatformConfiguration;

public class PlatformConfigurationDialog extends TitleAreaDialog {
	
	private TradePlatformConfiguration config;	
	private Text masterFilePathText;
	private Text dataFilePathText;
	private Text historyFilePathText;
	private Text feedPointFileText;
	private Text koscomIpText;
	private Text stockAccountNumberText;
	private Text foAccountNumberText;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public PlatformConfigurationDialog(Shell parentShell) {
		super(parentShell);
		config = TradePlatformConfiguration.getInstance();
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("\uD658\uACBD\uC124\uC815\uC744 \uC704\uD55C \uD56D\uBAA9\uAC12\uC744 \uC785\uB825\uD558\uC2ED\uC2DC\uC624.");
		setTitle("TradeRobot \uD658\uACBD\uC124\uC815");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		FillLayout fl_container = new FillLayout(SWT.HORIZONTAL);
		fl_container.marginWidth = 15;
		fl_container.marginHeight = 15;
		container.setLayout(fl_container);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("\uB9C8\uC2A4\uD130\uD30C\uC77C \uB514\uB809\uD1A0\uB9AC:");
		
		masterFilePathText = new Text(composite, SWT.BORDER);
		masterFilePathText.setEditable(false);
		masterFilePathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		masterFilePathText.setText(config.getMasterPath());
		
		Button masterFilePathButton = new Button(composite, SWT.NONE);
		masterFilePathButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				dialog.setText("마스터 파일 경로 설정");
				dialog.setMessage("마스터 파일을 저장할 경로를 설정하십시오.");
				String path;
				if ( (path=dialog.open()) != null )
					masterFilePathText.setText(path);
			}
		});
		masterFilePathButton.setText("...");
		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("\uB370\uC774\uD130\uD30C\uC77C \uB514\uB809\uD1A0\uB9AC:");
		
		dataFilePathText = new Text(composite, SWT.BORDER);
		dataFilePathText.setEditable(false);
		dataFilePathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dataFilePathText.setText(config.getDataPath());
		
		Button dataFilePathButton = new Button(composite, SWT.NONE);
		dataFilePathButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				dialog.setText("데이터 파일 경로 설정");
				dialog.setMessage("데이터 파일을 저장할 경로를 설정하십시오.");
				String path;
				if ( (path=dialog.open()) != null )
					dataFilePathText.setText(path);
			}
		});
		dataFilePathButton.setText("...");
		
		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("\uD788\uC2A4\uD1A0\uB9AC\uD30C\uC77C \uB514\uB809\uD1A0\uB9AC:");
		
		historyFilePathText = new Text(composite, SWT.BORDER);
		historyFilePathText.setEditable(false);
		historyFilePathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		historyFilePathText.setText(config.getHistoryPath());
		
		Button historyFilePathButton = new Button(composite, SWT.NONE);
		historyFilePathButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				dialog.setText("히스토리 파일 경로 설정");
				dialog.setMessage("히스토리 파일을 저장할 경로를 설정하십시오.");
				String path;
				if ( (path=dialog.open()) != null )
					historyFilePathText.setText(path);
			}
		});
		historyFilePathButton.setText("...");
		
		Label lblNewLabel_3 = new Label(composite, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("FeedPoint \uD30C\uC77C:");
		
		feedPointFileText = new Text(composite, SWT.BORDER);
		feedPointFileText.setEditable(false);
		feedPointFileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		feedPointFileText.setText(config.getFeedPointFile());
		
		Button feedPoitFileButton = new Button(composite, SWT.NONE);
		feedPoitFileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(parent.getShell());
				dialog.setText("피드포인트 파일 설정");
				String path;
				if ( (path=dialog.open()) != null )
					feedPointFileText.setText(path);
			}
		});
		feedPoitFileButton.setText("...");
		
		Label lblNewLabel_5 = new Label(composite, SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_5.setText("KOSCOM \uC804\uC6A9 IP:");
		
		koscomIpText = new Text(composite, SWT.BORDER);
		koscomIpText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		koscomIpText.setText(config.getKoscomNetworkIp());
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel_4 = new Label(composite, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("\uC8FC\uC2DD\uACC4\uC88C\uBC88\uD638:");
		
		stockAccountNumberText = new Text(composite, SWT.BORDER);
		stockAccountNumberText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		stockAccountNumberText.setText(config.getStockAccountNumber());
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel_6 = new Label(composite, SWT.NONE);
		lblNewLabel_6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_6.setText("\uD30C\uC0DD\uACC4\uC88C\uBC88\uD638:");
		
		foAccountNumberText = new Text(composite, SWT.BORDER);
		foAccountNumberText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		foAccountNumberText.setText(config.getFoAccountNumber());
		new Label(composite, SWT.NONE);
		
		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(512, 405);
	}

	@Override
	protected void okPressed() {		
		
		config.setMasterPath(masterFilePathText.getText());
		config.setDataPath(dataFilePathText.getText());
		config.setHistoryPath(historyFilePathText.getText());
		config.setFeedPointFile(feedPointFileText.getText());
		config.setKoscomNetworkIp(koscomIpText.getText());
		config.setStockAccountNumber(stockAccountNumberText.getText());		
		config.setFoAccountNumber(foAccountNumberText.getText());
		try {
			config.save();
		} catch (Exception e) {
			MessageDialog.open(MessageDialog.ERROR, getShell(), "환경설정 저장 오류", e.getMessage(), SWT.NONE);
		}
		super.okPressed();
	}
}
