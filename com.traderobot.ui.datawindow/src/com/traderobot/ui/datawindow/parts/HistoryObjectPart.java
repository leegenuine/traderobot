 
package com.traderobot.ui.datawindow.parts;

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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.service.prefs.Preferences;

import com.traderobot.ui.datawindow.history.HistoryObjectDescriptor;
import com.traderobot.ui.datawindow.history.HistoryObjectManager;

public class HistoryObjectPart {
	
	private static final Color DISABLED_COLOR = SWTResourceManager.getColor(SWT.COLOR_RED);
	
	private static final String NODE_ROOT = "com.traderobot.ui.datawindow.historyobjects";
	private Hashtable<String, Boolean> endableHistoryObjectList;
	private static final int IDX_NAME = 0;
	private static final int IDX_DESCRIPTION = 1;	
	private static final int IDX_ENABLE = 2;
		
	private HistoryObjectDescriptor[] descriptors;
	
	private Preferences store;
	private TableViewer viewer;
	private Table table;
	
	@Inject
	public HistoryObjectPart() {
		store = ConfigurationScope.INSTANCE.getNode(NODE_ROOT);				
		endableHistoryObjectList = new Hashtable<String, Boolean>();
		try {
			String[] objectNames = store.keys();
			for(int i=0; i<objectNames.length; i++) {
				endableHistoryObjectList.put(objectNames[i], store.getBoolean(objectNames[i], false));
			}
		} catch (Exception e) {}		
		HistoryObjectManager manager = HistoryObjectManager.getInstance();
		descriptors = manager.getDescriptors();
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		viewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = viewer.getTable();
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
					String name = table.getItem(index).getText(IDX_NAME);
					endableHistoryObjectList.put(name, true);
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
					String name = table.getItem(index).getText(IDX_NAME);
					endableHistoryObjectList.put(name, false);
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
				
				endableHistoryObjectList.forEach(new BiConsumer<String, Boolean>() {
					@Override
					public void accept(String name, Boolean enable) {
						store.putBoolean(name, enable);
					}					
				});
				try {
					store.flush();
				} catch (Exception ex) {
					MessageDialog.open(MessageDialog.ERROR, parent.getShell(), "설정저장오류", "히스토리 오브젝트설정 저장실패", SWT.NONE);
				}
			}
		});
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.widthHint = 80;
		saveButton.setLayoutData(gd_btnNewButton);
		saveButton.setText("\uC800\uC7A5");
		viewer.setInput(descriptors);		
	}
	
	private void createColumns(final Composite parent, final TableViewer viewer)
	{	
		TableViewerColumn nameColumn = createTableViewerColumn("오브젝트명", 200);
		nameColumn.setLabelProvider(new HistoryColumnLabelProvider(IDX_NAME));
		
		TableViewerColumn descriptionColumn = createTableViewerColumn("설명", 400);		
		descriptionColumn.setLabelProvider(new HistoryColumnLabelProvider(IDX_DESCRIPTION));
		
		TableViewerColumn enableColumn = createTableViewerColumn("사용가능", 60);
		enableColumn.setLabelProvider(new HistoryColumnLabelProvider(IDX_ENABLE));
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
			return descriptors;
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//			this.manager = (IOrderManager) newInput;
//			currentManager = manager;
		}
	}
	
	public class HistoryColumnLabelProvider extends ColumnLabelProvider 
	{	
		private int index;
		
		public HistoryColumnLabelProvider(int index)
		{
			this.index = index;
		}
		
		@Override
		public String getText(Object element)
		{
			HistoryObjectDescriptor desc = (HistoryObjectDescriptor) element;			
			switch(index)
			{			
			case IDX_NAME:
				return desc.getName();					
			case IDX_DESCRIPTION:		
				return desc.getDescription();		
			case IDX_ENABLE:		
				if ( endableHistoryObjectList.get(desc.getName()) == null )
					return "사용안함";
				if ( endableHistoryObjectList.get(desc.getName()) )
					return "사용";
				else
					return "사용안함";
			default:
				return "";
			}
		}
		
		@Override
		public Color getForeground(Object element) {			
			HistoryObjectDescriptor desc = (HistoryObjectDescriptor) element;	
			boolean enable = false;
			if ( endableHistoryObjectList.get(desc.getName()) != null)
				enable = endableHistoryObjectList.get(desc.getName());			
			if (!enable)
				return DISABLED_COLOR;
			return super.getForeground(element);
		}

//		@Override
//		public Color getBackground(Object element) {			
//			FeedPointDescriptor desc = (FeedPointDescriptor) element;
//			boolean enable = false;
//			if ( enableFeedPointList.get(desc) != null)
//				enable = enableFeedPointList.get(desc);			
//			if (!enable)
//				return DISABLED_COLOR;
//			return super.getBackground(element);
//		}
	}
}