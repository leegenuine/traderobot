 
package com.traderobot.ui.datawindow.parts;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.function.BiConsumer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.service.prefs.Preferences;

import com.traderobot.platform.koscom.transaction.TransactionDescription;
import org.eclipse.swt.widgets.Label;

public class TransactionPart {
	
	private static final Color DISABLED_COLOR = SWTResourceManager.getColor(SWT.COLOR_RED);
	private static final Color ENABLED_BACKGROUND_COLOR = SWTResourceManager.getColor(SWT.COLOR_GREEN);
	
	private static final String NODE_ROOT = "com.traderobot.ui.datawindow.transactions";
	private Hashtable<String, Boolean> enableTransactionList;
	private static final int IDX_CODE = 0;
	private static final int IDX_DESCRIPTION = 1;	
	private static final int IDX_ENABLE = 2;
		
	private String[] codes;
	
	private Preferences store;
	private TableViewer viewer;
	private Table table;
	private Text searchText;
	
	@Inject
	public TransactionPart() {
		store = ConfigurationScope.INSTANCE.getNode(NODE_ROOT);
		enableTransactionList = new Hashtable<String, Boolean>();
		try {
			String[] transactionCodes = store.keys();
			for(int i=0; i<transactionCodes.length; i++) {
				enableTransactionList.put(transactionCodes[i], store.getBoolean(transactionCodes[i], false));
			}
		} catch (Exception e) {}
		codes = TransactionDescription.getCodes();
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("검색:");
		
		searchText = new Text(composite, SWT.BORDER);
		searchText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				viewer.refresh();
			}
		});
		searchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		viewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = viewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		createColumns(parent, viewer);
		viewer.setContentProvider(new ContentProvider());
		
		Composite composite_1 = new Composite(parent, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		
		Button enableButton = new Button(composite_1, SWT.NONE);		
		enableButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = table.getSelectionIndex();
				if ( index >= 0 ) {
					String code = table.getItem(index).getText(IDX_CODE);
					enableTransactionList.put(code, true);
					viewer.refresh();
				}
			}
		});
		GridData gd_enableButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_enableButton.widthHint = 80;
		enableButton.setLayoutData(gd_enableButton);
		enableButton.setText("사용가능");
		
		Button disableButton = new Button(composite_1, SWT.NONE);
		disableButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = table.getSelectionIndex();
				if ( index >= 0 ) {
					String code = table.getItem(index).getText(IDX_CODE);
					enableTransactionList.put(code, false);
					viewer.refresh();
				}
			}
		});
		GridData gd_disableButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_disableButton.widthHint = 80;
		disableButton.setLayoutData(gd_disableButton);
		disableButton.setText("사용불가");
		
		Button saveButton = new Button(composite_1, SWT.NONE);
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				enableTransactionList.forEach(new BiConsumer<String, Boolean>() {
					@Override
					public void accept(String code, Boolean enable) {
						store.putBoolean(code, enable);
					}					
				});
				try {
					store.flush();
				} catch (Exception ex) {
					MessageDialog.open(MessageDialog.ERROR, parent.getShell(), "설정저장오류", "데이터코드설정 저장 실패", SWT.NONE);
				}
			}
		});
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.widthHint = 80;
		saveButton.setLayoutData(gd_btnNewButton);
		saveButton.setText("\uC800\uC7A5");
		viewer.setInput(codes);		
	}
	
	private void createColumns(final Composite parent, final TableViewer viewer)
	{	
		TableViewerColumn nameColumn = createTableViewerColumn("코드명", 200);
		nameColumn.setLabelProvider(new TransactionColumnLabelProvider(IDX_CODE));
		
		TableViewerColumn descriptionColumn = createTableViewerColumn("설명", 400);		
		descriptionColumn.setLabelProvider(new TransactionColumnLabelProvider(IDX_DESCRIPTION));
		
		TableViewerColumn enableColumn = createTableViewerColumn("사용가능", 60);
		enableColumn.setLabelProvider(new TransactionColumnLabelProvider(IDX_ENABLE));
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
	
	@PreDestroy
	public void preDestroy() {
		try {
			store.flush();
		} catch (Exception e) {}
	}
	
	public class ContentProvider implements IStructuredContentProvider 
	{	
		//private IOrderManager manager;
		
		public Object[] getElements(Object inputElement) {			
			String search = searchText.getText().trim();
			if ( search == null || search.length() == 0)
				return codes;
			
			ArrayList<String> found = new ArrayList<String>();
			for(int i=0; i<codes.length; i++) {
				String desc =  TransactionDescription.getDescription(codes[i]);
				if ( desc.contains(search) || codes[i].contains(search) )
					found.add(codes[i]);
			}
			if ( found.size() > 0)
				return found.toArray(new String[found.size()]);
			else
				return codes;
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//			this.manager = (IOrderManager) newInput;
//			currentManager = manager;
		}
	}
	
	public class TransactionColumnLabelProvider extends ColumnLabelProvider 
	{	
		private int index;
		
		public TransactionColumnLabelProvider(int index)
		{
			this.index = index;
		}
		
		@Override
		public String getText(Object element)
		{
			String code = (String) element;			
			switch(index)
			{			
			case IDX_CODE:
				return code;					
			case IDX_DESCRIPTION:		
				return TransactionDescription.getDescription(code);		
			case IDX_ENABLE:		
				if ( enableTransactionList.get(code) == null )
					return "사용안함";
				if ( enableTransactionList.get(code) )
					return "사용";
				else
					return "사용안함";
			default:
				return "";
			}
		}
		
		@Override
		public Color getForeground(Object element) {			
			String code = (String) element;
			boolean enable = false;
			if ( enableTransactionList.get(code) != null)
				enable = enableTransactionList.get(code);			
			if (!enable)
				return DISABLED_COLOR;
			return super.getForeground(element);
		}

		@Override
		public Color getBackground(Object element) {			
			String code = (String) element;
			boolean enable = false;
			if ( enableTransactionList.get(code) != null)
				enable = enableTransactionList.get(code);			
			if (enable)
				return ENABLED_BACKGROUND_COLOR;
			return super.getForeground(element);
		}
	}
}