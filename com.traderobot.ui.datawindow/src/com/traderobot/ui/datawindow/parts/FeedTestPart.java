 
package com.traderobot.ui.datawindow.parts;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.traderobot.platform.koscom.feed.FeedPointDescriptor;
import com.traderobot.platform.koscom.feed.FeedPointExtensions;
import com.traderobot.platform.koscom.feed.FeedPointManager;
import com.traderobot.platform.koscom.feed.IFeedHandler;
import com.traderobot.platform.koscom.transaction.ITransactionData;
import com.traderobot.platform.koscom.transaction.TransactionDescription;

public class FeedTestPart {
	
	private FeedPointManager manager;
	private IFeedHandler handler;
	private FeedPointDescriptor[] descriptors;
	private Combo feedPointCombo;
	private Text receiveText;
	private boolean isRun;
	
	@Inject
	public FeedTestPart(Shell shell) {
		FeedPointExtensions extensions = FeedPointExtensions.getInstance();
		descriptors = extensions.getAllDescriptors();
		handler = new IFeedHandler() {
			@Override
			public void onFeed(String transactionCode, ITransactionData rtd) {
				shell.getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						receiveText.append("[" + transactionCode + ":" + TransactionDescription.getDescription(transactionCode) + "] " + new String(rtd.getBytes()) + "\n");
					}
				});
			}
		};
		manager = new FeedPointManager(handler);
		isRun = false;
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new GridLayout(3, false));
		
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("\uD53C\uB4DC\uD3EC\uC778\uD2B8 \uC120\uD0DD:");
		
		feedPointCombo = new Combo(parent, SWT.NONE);
		feedPointCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(int i=0; i<descriptors.length; i++)
			feedPointCombo.add(descriptors[i].getName() + ": " + descriptors[i].getDescription());
		if ( descriptors.length > 0 )
			feedPointCombo.select(0);
		
		Button testButton = new Button(parent, SWT.NONE);
		testButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				try {
					if ( isRun ) {
						manager.stopAll();
						testButton.setText("테스트시작");
					} else {
						manager.disableAll();
						manager.setEnable(descriptors[feedPointCombo.getSelectionIndex()].getName(), true);
						manager.startAll();
						testButton.setText("테스트중지");
					}
				} catch (IOException ioe) {
					MessageDialog.openError(parent.getShell(), "피드포인트 오류", ioe.getMessage());
				}
				isRun = !isRun;
			}
		});
		testButton.setText("\uD14C\uC2A4\uD2B8 \uC2DC\uC791");
		
		receiveText = new Text(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		receiveText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));		
	}
	
	
	
	
}