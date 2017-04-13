 
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.service.prefs.Preferences;

import com.traderobot.platform.koscom.feed.FeedPointDescriptor;
import com.traderobot.platform.koscom.feed.FeedPointExtensions;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class FeedPointPart {
	
	private static final Color DISABLED_COLOR = SWTResourceManager.getColor(SWT.COLOR_RED);
	private static final Color ENABLED_BACKGROUND_COLOR = SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN);
	
	private static final String NODE_ROOT = "com.traderobot.ui.datawindow.feedpoints";
	private Hashtable<String, Boolean> enableFeedPointList;
	private static final int IDX_NAME = 0;
	private static final int IDX_DESCRIPTION = 1;
	private static final int IDX_GROUP_IP = 2;
	private static final int IDX_GROUP_PORT = 3;
	private static final int IDX_ID = 4;
	private static final int IDX_ENABLE = 5;
		
	private FeedPointDescriptor[] descriptors;
	
	private Preferences store;
	private TableViewer viewer;
	private Table table;
	private Text searchText;
	
	@Inject
	public FeedPointPart() {
		store = ConfigurationScope.INSTANCE.getNode(NODE_ROOT);
		enableFeedPointList = new Hashtable<String, Boolean>();
		try {
			String[] feedPointIds = store.keys();
			for(int i=0; i<feedPointIds.length; i++) {
				enableFeedPointList.put(feedPointIds[i], store.getBoolean(feedPointIds[i], false));
			}
		} catch (Exception e) {}
		FeedPointExtensions extensions = FeedPointExtensions.getInstance();
		descriptors = extensions.getAllDescriptors();
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("\uAC80\uC0C9:");
		
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
					String id = table.getItem(index).getText(IDX_ID);
					enableFeedPointList.put(id, true);
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
					String id = table.getItem(index).getText(IDX_ID);
					enableFeedPointList.put(id, false);
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
				
				enableFeedPointList.forEach(new BiConsumer<String, Boolean>() {
					@Override
					public void accept(String id, Boolean enable) {
						store.putBoolean(id, enable);
					}					
				});
				try {
					store.flush();
				} catch (Exception ex) {
					MessageDialog.open(MessageDialog.ERROR, parent.getShell(), "설정저장오류", "피드포인트설정 저장 실패", SWT.NONE);
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
		TableViewerColumn nameColumn = createTableViewerColumn("FeedPoint명", 200);
		nameColumn.setLabelProvider(new FeedPointColumnLabelProvider(IDX_NAME));
		
		TableViewerColumn descriptionColumn = createTableViewerColumn("설명", 400);		
		descriptionColumn.setLabelProvider(new FeedPointColumnLabelProvider(IDX_DESCRIPTION));
		
		TableViewerColumn groupIpColumn = createTableViewerColumn("IP", 0);
		groupIpColumn.getColumn().setResizable(false);
		groupIpColumn.getColumn().setMoveable(false);
		groupIpColumn.setLabelProvider(new FeedPointColumnLabelProvider(IDX_GROUP_IP));
				
		TableViewerColumn groupPortColumn = createTableViewerColumn("Port", 0);
		groupPortColumn.getColumn().setResizable(false);
		groupPortColumn.getColumn().setMoveable(false);
		groupPortColumn.setLabelProvider(new FeedPointColumnLabelProvider(IDX_GROUP_PORT));
		
		TableViewerColumn idColumn = createTableViewerColumn("ID", 0);
		idColumn.getColumn().setResizable(false);
		idColumn.getColumn().setMoveable(false);
		idColumn.setLabelProvider(new FeedPointColumnLabelProvider(IDX_ID));
		
		TableViewerColumn enableColumn = createTableViewerColumn("사용가능", 60);
		enableColumn.setLabelProvider(new FeedPointColumnLabelProvider(IDX_ENABLE));
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
			String search = searchText.getText();
			if ( search == null || search.length() == 0)
				return descriptors;
			
			ArrayList<FeedPointDescriptor> found = new ArrayList<FeedPointDescriptor>();
			for(int i=0; i<descriptors.length; i++) {
				String desc =  descriptors[i].getDescription();
				if ( descriptors[i].getName().contains(search) || desc.contains(search) )
					found.add(descriptors[i]);
			}
			if ( found.size() > 0)
				return found.toArray(new FeedPointDescriptor[found.size()]);
			else
				return descriptors;
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//			this.manager = (IOrderManager) newInput;
//			currentManager = manager;
		}
	}
	
	public class FeedPointColumnLabelProvider extends ColumnLabelProvider 
	{	
		private int index;
		
		public FeedPointColumnLabelProvider(int index)
		{
			this.index = index;
		}
		
		@Override
		public String getText(Object element)
		{
			FeedPointDescriptor desc = (FeedPointDescriptor) element;			
			switch(index)
			{
			case IDX_ID:				
				return desc.getId();
			case IDX_NAME:
				return desc.getName();
			case IDX_GROUP_IP:			
				return desc.getGroupIp();
			case IDX_GROUP_PORT:		
				return Integer.toString(desc.getGroupPort());
			case IDX_DESCRIPTION:		
				return desc.getDescription();		
			case IDX_ENABLE:		
				if ( enableFeedPointList.get(desc.getId()) == null )
					return "사용안함";
				if ( enableFeedPointList.get(desc.getId()) )
					return "사용";
				else
					return "사용안함";
			default:
				return "";
			}
		}
		
		@Override
		public Color getForeground(Object element) {			
			FeedPointDescriptor desc = (FeedPointDescriptor) element;
			boolean enable = false;
			if ( enableFeedPointList.get(desc.getId()) != null)
				enable = enableFeedPointList.get(desc.getId());			
			if (!enable)
				return DISABLED_COLOR;
			return super.getForeground(element);
		}

		@Override
		public Color getBackground(Object element) {			
			FeedPointDescriptor desc = (FeedPointDescriptor) element;
			boolean enable = false;
			if ( enableFeedPointList.get(desc) != null)
				enable = enableFeedPointList.get(desc);			
			if (enable)
				return ENABLED_BACKGROUND_COLOR;
			return super.getBackground(element);
		}
	}
}