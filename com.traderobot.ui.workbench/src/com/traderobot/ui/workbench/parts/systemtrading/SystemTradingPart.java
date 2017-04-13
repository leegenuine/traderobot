package com.traderobot.ui.workbench.parts.systemtrading;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import com.traderobot.platform.IPlatformEventConstants;
import com.traderobot.platform.logic.ITradingLogic;
import com.traderobot.platform.logic.systemtrading.SystemTradingLogicDescriptor;
import com.traderobot.platform.logic.systemtrading.SystemTradingLogicManager;
import com.traderobot.ui.workbench.IUIEventConstants;
import com.traderobot.ui.workbench.dialogs.SystemTradingLogicSelectDialog;

public class SystemTradingPart
{
	@Inject
	private ESelectionService selectionService;
	
	private static final int IDX_NAME = 0;	
	private static final int IDX_DEVELOPER_NAME = 1;
	private static final int IDX_VERSION = 2;
	private static final int IDX_ENABLE = 3;
	private static final int IDX_MESSAGE = 4;
	private static final int IDX_RUNNING = 5;
	private static final int IDX_ID = 6;
	
	private Table table;
	private TableViewer viewer;
	private SystemTradingLogicManager manager;

	public SystemTradingPart()
	{
		manager = SystemTradingLogicManager.getInstance();
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createPartControls(Composite parent, IEclipseContext ctx, EMenuService menuService)
	{	
		viewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		createColumns(parent, viewer);
		table = viewer.getTable();
		table.setLinesVisible(true);
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
		table.setHeaderVisible(true);
		viewer.setContentProvider(new ContentProvider());
		viewer.setInput(manager);
		viewer.refresh();
		menuService.registerContextMenu(viewer.getTable(), "com.traderobot.ui.workbench.popupmenu.systemtradingView");
	}
	
	@PreDestroy
	public void preDestroy() {
//		System.out.println("SystemTradingPart preDestroy");
		try {
			SystemTradingLogicDescriptor[] list = manager.getVisibleExtensionLogics();
			for(int i=0; i<list.length; i++) {
				list[i].getInstance().save();
			}
		} catch ( Exception e ) {}
	}
	
	private void createColumns(final Composite parent, final TableViewer viewer)
	{
		TableViewerColumn nameColumn = createTableViewerColumn("\uB85C\uC9C1\uBA85", 220);
		nameColumn.setLabelProvider(new SystemTradingColumnLabelProvider(IDX_NAME));
		
		TableViewerColumn developerNameColumn = createTableViewerColumn("\uAC1C\uBC1C\uC790", 0);		
		developerNameColumn.getColumn().setResizable(false);
		developerNameColumn.setLabelProvider(new SystemTradingColumnLabelProvider(IDX_DEVELOPER_NAME));
		
		TableViewerColumn versionColumn = createTableViewerColumn("\uBC84\uC804\uC815\uBCF4", 0);
		versionColumn.getColumn().setResizable(false);
		versionColumn.setLabelProvider(new SystemTradingColumnLabelProvider(IDX_VERSION));			
		
		TableViewerColumn enableColumn = createTableViewerColumn("\uC0AC\uC6A9\uAC00\uB2A5", 60);
		enableColumn.setLabelProvider(new SystemTradingColumnLabelProvider(IDX_ENABLE));
		
		TableViewerColumn messageColumn = createTableViewerColumn("\uBA54\uC2DC\uC9C0", 0);
		messageColumn.setLabelProvider(new SystemTradingColumnLabelProvider(IDX_MESSAGE));
		
		TableViewerColumn runningColumn = createTableViewerColumn("\uC2E4\uD589\uC0C1\uD0DC", 60);
		runningColumn.setLabelProvider(new SystemTradingColumnLabelProvider(IDX_RUNNING));
		
		TableViewerColumn idColumn = createTableViewerColumn("id", 0);
		idColumn.setLabelProvider(new SystemTradingColumnLabelProvider(IDX_ID));
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
	public void dispose()
	{
		try
		{
			manager.saveExtensionLogics();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Focus
	public void setFocus()
	{
		// TODO	Set the focus to control
	}	
	
	private static class ContentProvider implements IStructuredContentProvider {
		
		private SystemTradingLogicManager manager;
		
		public Object[] getElements(Object inputElement) {
			return manager.getVisibleExtensionLogics();
		}
		
		public void dispose() {
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			this.manager = (SystemTradingLogicManager) newInput;
		}
	}
	
	private class SystemTradingColumnLabelProvider extends ColumnLabelProvider {
		
		private int columnIndex;
		
		public SystemTradingColumnLabelProvider(int columnIndex)
		{
			this.columnIndex = columnIndex;
		}
		
		@Override
		public String getText(Object element)
		{
			SystemTradingLogicDescriptor desc = (SystemTradingLogicDescriptor) element;			
			
			switch(columnIndex)
			{
			case IDX_NAME:
				return desc.getName();
			case IDX_DEVELOPER_NAME:
				return desc.getDeveloperName();
			case IDX_VERSION:
				return desc.getVersion();
			case IDX_ENABLE:
				if ( desc.isEnable() )
					return "사용가능";
				else
					return "사용불가";
			case IDX_MESSAGE:
				try {					
					if ( desc.getInstance() != null )
						return desc.getInstance().getMessage();
				} catch (Exception e) {}
				return "";
			case IDX_RUNNING:
				try {
					if ( desc.getInstance().isRunning() )
						return "실행";
					else
						return "중지";
				} catch (Exception e) {}
			case IDX_ID:
				return desc.getId();
			default:
				return "";
			}
		}
		
		@Override
		public Color getForeground(Object element)
		{
			SystemTradingLogicDescriptor desc = (SystemTradingLogicDescriptor) element;
			if ( !desc.isEnable() )
				return SWTResourceManager.getColor(SWT.COLOR_GRAY);
			return super.getBackground(element);
		}
		
		@Override
		public Image getImage(Object element)
		{
			SystemTradingLogicDescriptor desc = (SystemTradingLogicDescriptor) element;			
			switch(columnIndex)
			{
			case IDX_NAME:
				try {
					if ( desc.getInstance().isRunning() )
						return ResourceManager.getPluginImage("com.traderobot.ui.workbench", "icons/forward_nav.png");
					else
						return ResourceManager.getPluginImage("com.traderobot.ui.workbench", "icons/stop.png");
				} catch (Exception e) { }
			default:
				return super.getImage(element);
			}
		}
		
	}
	
	
	
	///////////////// EVENT HANDLING
	@Inject @Optional
	public void onAddSystemTradingLogic(
		@UIEventTopic(IUIEventConstants.TOPIC_ADD_SYSTEMTRADING_LOGIC) 
		Object data, @Named(IServiceConstants.ACTIVE_SHELL) Shell shell)
	{
		SystemTradingLogicSelectDialog dialog = new SystemTradingLogicSelectDialog(shell);
		if ( dialog.open() == Window.OK )
		{
			SystemTradingLogicDescriptor[] items = dialog.getSelectedSystemLogicDescriptors();
			for(int i=0; i<items.length; i++)
				items[i].setVisible(true);
			viewer.refresh();
//			if ( items != null)
//				tableViewer.setSelection(new StructuredSelection(items[0]));
		}
	}
	
	@Inject @Optional
	public void onDeleteSystemTradingLogic(
		@UIEventTopic(IUIEventConstants.TOPIC_DELETE_SYSTEMTRADING_LOGIC) 
		Object data)
	{	
		if ( table.getSelectionCount() == 1 )
		{
			SystemTradingLogicDescriptor desc = (SystemTradingLogicDescriptor) table.getSelection()[0].getData();
			desc.setVisible(false);
			viewer.refresh();			
		}
		selectionService.setSelection(null);
	}
	
	@Inject @Optional
	public void onEnableSystemTradingLogic(
		@UIEventTopic(IUIEventConstants.TOPIC_ENABLE_SYSTEMTRADING_LOGIC) 
		Object data)
	{
		if ( table.getSelectionCount() == 1 )
		{
			SystemTradingLogicDescriptor desc = (SystemTradingLogicDescriptor) table.getSelection()[0].getData();
			desc.setEnable(!desc.isEnable());			
			viewer.refresh();
		}
	}
	
	@Inject @Optional
	public void onRunSystemTradingLogic(
		@UIEventTopic(IUIEventConstants.TOPIC_RUN_SYSTEMTRADING_LOGIC) 
		Object data)
	{	
		if ( table.getSelectionCount() == 1 )
		{
			SystemTradingLogicDescriptor desc = (SystemTradingLogicDescriptor) table.getSelection()[0].getData();
			try {
				if ( desc.getInstance() != null ) {
					if ( desc.getInstance().isRunning() )
						desc.getInstance().stop();
					else
						desc.getInstance().start(ITradingLogic.REALTIME);
					viewer.refresh();
				}
			} catch ( Exception ex ) {
				ex.printStackTrace();
			}
		}
	}
	
	@Inject @Optional
	public void onBacktestSystemTradingLogic(
		@UIEventTopic(IUIEventConstants.TOPIC_BACKTEST_SYSTEMTRADING_LOGIC) 
		Object data)
	{	
		if ( table.getSelectionCount() == 1 )
		{
			SystemTradingLogicDescriptor desc = (SystemTradingLogicDescriptor) table.getSelection()[0].getData();
			try {
				if ( desc.getInstance() != null ) {
					if ( desc.getInstance().isRunning() )
						desc.getInstance().stop();
					else
						desc.getInstance().start(ITradingLogic.BACKTEST);
					viewer.refresh();
				}
			} catch ( Exception ex ) {
				ex.printStackTrace();
			}
		}
	}
	
	@Inject @Optional
	public void onSystemTradingLogicStatusChanged(
		@UIEventTopic(IPlatformEventConstants.TOPIC_LOGIC_STATUS_CHANGED) 
		Object data)
	{
		viewer.refresh();
	}
}
