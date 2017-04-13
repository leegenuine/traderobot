 
package com.traderobot.ui.datawindow.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.Preferences;

import com.traderobot.platform.TradePlatformConfiguration;
import com.traderobot.ui.datawindow.Activator;
import com.traderobot.ui.datawindow.datacollector.DataCollectorDescriptor;
import com.traderobot.ui.datawindow.datacollector.DataCollectorManager;


//import com.traderobot.platform.Activator;

public class ConfigurationPart {
	
	private Preferences store;
	private TradePlatformConfiguration configuration;
	
	private DataCollectorDescriptor[] descriptors;
	
	private Text masterFilePathText;
	private Text rawDataFilePathText;
	private Text historyFilePathText;
	private Combo dataCollectorCombo;	
	private Text koscomIpText;
	private Spinner openingTaskHourSpinner;
	private Spinner openingTaskMinuteSpinner;
	private Spinner closingTaskHourSpinner;
	private Spinner closingTaskMinuteSpinner;
	
	@Inject
	public ConfigurationPart() {
		store = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		configuration = TradePlatformConfiguration.getInstance();
		DataCollectorManager manager = DataCollectorManager.getInstance();
		descriptors = manager.getDescriptors();
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(3, false));
		
		Label masterFileDirLabel = new Label(composite, SWT.NONE);
		masterFileDirLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		masterFileDirLabel.setText("\uB9C8\uC2A4\uD130 \uD30C\uC77C \uACBD\uB85C:");
		
		masterFilePathText = new Text(composite, SWT.BORDER);
		masterFilePathText.setEditable(false);
		masterFilePathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		masterFilePathText.setText(configuration.getMasterPath());
				
		Button masterFilePathButton = new Button(composite, SWT.NONE);
		masterFilePathButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				dialog.setText("마스터 파일 경로 설정");
				dialog.setMessage("마스터 파일을 저장할 경로를 설정하십시오.");
				String path;
				if ( (path=dialog.open()) != null ) {
					masterFilePathText.setText(path);
					configuration.setMasterPath(masterFilePathText.getText());					
					try {
						configuration.save();
					} catch (Exception ex) {
						MessageDialog.open(MessageDialog.ERROR, parent.getShell(), "설정저장오류", "마스터파일경로 저장 실패", SWT.NONE);
					}
				}
			}
		});
		masterFilePathButton.setText("...");
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("\uC2E4\uC2DC\uAC04 \uB370\uC774\uD130 \uD30C\uC77C \uACBD\uB85C:");
		
		rawDataFilePathText = new Text(composite, SWT.BORDER);
		rawDataFilePathText.setEditable(false);
		rawDataFilePathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		rawDataFilePathText.setText(configuration.getDataPath());
		
		Button rawDataFilePathButton = new Button(composite, SWT.NONE);
		rawDataFilePathButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				dialog.setText("데이터 파일 경로 설정");
				dialog.setMessage("데이터 파일을 저장할 경로를 설정하십시오.");
				String path;
				if ( (path=dialog.open()) != null ) {
					rawDataFilePathText.setText(path);
					configuration.setDataPath(rawDataFilePathText.getText());					
					try {
						configuration.save();
					} catch (Exception ex) {
						MessageDialog.open(MessageDialog.ERROR, parent.getShell(), "설정저장오류", "데이터파일경로 저장 실패", SWT.NONE);
					}
				}
			}
		});
		rawDataFilePathButton.setText("...");
		
		Label lblNewLabel_3 = new Label(composite, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("\uC2E4\uC2DC\uAC04 \uB370\uC774\uD130 \uC218\uC9D1\uAE30:");
		
		dataCollectorCombo = new Combo(composite, SWT.NONE);
		dataCollectorCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				store.put("dataCollector", dataCollectorCombo.getText());
				try {
					store.flush();
				} catch (Exception ex) {
					MessageDialog.open(MessageDialog.ERROR, parent.getShell(), "설정저장오류", "데이터수집기 저장 실패", SWT.NONE);
				}
			}
		});
		dataCollectorCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(int i=0; i<descriptors.length; i++)
			dataCollectorCombo.add(descriptors[i].getName());
		dataCollectorCombo.setText(store.get("dataCollector", ""));
		
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("\uD788\uC2A4\uD1A0\uB9AC \uD30C\uC77C \uACBD\uB85C:");
		
		historyFilePathText = new Text(composite, SWT.BORDER);
		historyFilePathText.setEditable(false);
		historyFilePathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		historyFilePathText.setText(configuration.getHistoryPath());
		
		Button historyFilePathButton = new Button(composite, SWT.NONE);
		historyFilePathButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				dialog.setText("히스토리 파일 경로 설정");
				dialog.setMessage("히스토리 파일을 저장할 경로를 설정하십시오.");
				String path;
				if ( (path=dialog.open()) != null ) {
					historyFilePathText.setText(path);
					configuration.setHistoryPath(historyFilePathText.getText());					
					try {
						configuration.save();
					} catch (Exception ex) {
						MessageDialog.open(MessageDialog.ERROR, parent.getShell(), "설정저장오류", "히스토리 파일경로 저장 실패", SWT.NONE);
					}
				}
			}
		});
		historyFilePathButton.setText("...");
		
		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("KOSCOM \uC804\uC6A9 IP:");
		
		koscomIpText = new Text(composite, SWT.BORDER);
		koscomIpText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		koscomIpText.setText(configuration.getKoscomNetworkIp());
		
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel_4 = new Label(composite, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("\uC77C \uAC1C\uC2DC\uC791\uC5C5 \uC218\uD589\uC2DC\uAC01:");
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(4, false);
		gl_composite_1.marginWidth = 0;
		gl_composite_1.marginHeight = 0;
		composite_1.setLayout(gl_composite_1);
		
		openingTaskHourSpinner = new Spinner(composite_1, SWT.BORDER);
		openingTaskHourSpinner.setMaximum(24);
		openingTaskHourSpinner.setBounds(0, 0, 40, 21);
		openingTaskHourSpinner.setSelection(store.getInt("openingTaskHour", 8));
		Label lblNewLabel_5 = new Label(composite_1, SWT.NONE);
		lblNewLabel_5.setBounds(0, 0, 61, 12);
		lblNewLabel_5.setText("\uC2DC");
		
		openingTaskMinuteSpinner = new Spinner(composite_1, SWT.BORDER);		
		openingTaskMinuteSpinner.setMaximum(60);
		openingTaskMinuteSpinner.setSelection(store.getInt("openingTaskMinute", 40));
		Label lblNewLabel_6 = new Label(composite_1, SWT.NONE);
		lblNewLabel_6.setText("\uBD84");
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel_7 = new Label(composite, SWT.NONE);
		lblNewLabel_7.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_7.setText("\uC77C \uB9C8\uAC10\uC791\uC5C5 \uC218\uD589\uC2DC\uAC01:");
		
		Composite composite_2 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout(4, false);
		gl_composite_2.marginHeight = 0;
		gl_composite_2.marginWidth = 0;
		composite_2.setLayout(gl_composite_2);
		
		closingTaskHourSpinner = new Spinner(composite_2, SWT.BORDER);
		closingTaskHourSpinner.setMaximum(24);
		closingTaskHourSpinner.setBounds(0, 0, 40, 21);
		closingTaskHourSpinner.setSelection(store.getInt("closingTaskHour", 17));
		
		Label lblNewLabel_8 = new Label(composite_2, SWT.NONE);
		lblNewLabel_8.setBounds(0, 0, 61, 12);
		lblNewLabel_8.setText("\uC2DC");
		
		closingTaskMinuteSpinner = new Spinner(composite_2, SWT.BORDER);
		closingTaskMinuteSpinner.setMaximum(60);
		closingTaskMinuteSpinner.setSelection(store.getInt("closingTaskMinute", 0));
		
		Label lblNewLabel_9 = new Label(composite_2, SWT.NONE);
		lblNewLabel_9.setText("\uBD84");
		new Label(composite, SWT.NONE);
		
		Composite composite_3 = new Composite(parent, SWT.NONE);
		composite_3.setLayout(new GridLayout(1, false));
		composite_3.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		
		Button saveButton = new Button(composite_3, SWT.NONE);
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				try {
					configuration.setMasterPath(masterFilePathText.getText());
					configuration.setDataPath(rawDataFilePathText.getText());
					configuration.setHistoryPath(historyFilePathText.getText());
					configuration.setKoscomNetworkIp(koscomIpText.getText());
					configuration.save();
					
					store.put("dataCollector", dataCollectorCombo.getText());
					store.putInt("openingTaskHour", openingTaskHourSpinner.getSelection());
					store.putInt("openingTaskMinute", openingTaskMinuteSpinner.getSelection());
					store.putInt("closingTaskHour", closingTaskHourSpinner.getSelection());
					store.putInt("closingTaskMinute", closingTaskMinuteSpinner.getSelection());					
					store.flush();
					
				} catch (Exception ex) {
					MessageDialog.open(MessageDialog.ERROR, parent.getShell(), "설정저장오류", "설정정보를 저장하는 도중에 오류가 발생하였습니다.", SWT.NONE);
				}
			}
		});
		GridData gd_saveButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_saveButton.widthHint = 80;
		saveButton.setLayoutData(gd_saveButton);
		saveButton.setText("\uC800\uC7A5");
		
	}
}