package com.traderobot.ui.workbench.dialogs;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.TableColumn;

import com.traderobot.platform.logic.systemtrading.SystemTradingLogicDescriptor;
import com.traderobot.platform.logic.systemtrading.SystemTradingLogicManager;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class SystemTradingLogicSelectDialog extends Dialog
{
	private class ContentProvider implements IStructuredContentProvider {
		
		private SystemTradingLogicManager manager;
		
		public Object[] getElements(Object inputElement) {
			return manager.getAllExtensionLogics();
		}
		
		public void dispose() {
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			this.manager = (SystemTradingLogicManager) newInput;
		}
	}
	
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		
		public String getColumnText(Object element, int columnIndex) {
			
			SystemTradingLogicDescriptor desc = (SystemTradingLogicDescriptor) element;
			
			switch(columnIndex)
			{
			case IDX_ID:
				return desc.getId();
			case IDX_NAME:
				return desc.getName();
			case IDX_DEVELOPER_NAME:
				return desc.getDeveloperName();
			case IDX_VERSION:
				return desc.getVersion();
			}
			return null;
		}
	}	
	
	private static final int IDX_NAME = 0;
	private static final int IDX_DEVELOPER_NAME = 1;
	private static final int IDX_VERSION = 2;
	private static final int IDX_ID = 3;
	
	private SystemTradingLogicManager manager;
	private ArrayList<SystemTradingLogicDescriptor> selectedLogicExtensions;
	private Table table;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public SystemTradingLogicSelectDialog(Shell parentShell)
	{
		super(parentShell);		
		manager = SystemTradingLogicManager.getInstance();
		selectedLogicExtensions = new ArrayList<SystemTradingLogicDescriptor>(10);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("시스템 트레이딩 로직 선택");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TableViewer tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				int[] indices = table.getSelectionIndices();
				selectedLogicExtensions.clear();
				for(int i=0; i<indices.length; i++)
					selectedLogicExtensions.add(manager.getExtensionLogic(table.getItem(indices[i]).getText(IDX_ID)));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{	
				int[] indices = table.getSelectionIndices();
				selectedLogicExtensions.clear();
				for(int i=0; i<indices.length; i++)
					selectedLogicExtensions.add(manager.getExtensionLogic(table.getItem(indices[i]).getText(IDX_ID)));
			}
			
		});
		
		TableViewerColumn nameTableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn nameColumn = nameTableViewerColumn.getColumn();		
		nameColumn.setWidth(120);
		nameColumn.setText("\uB85C\uC9C1\uBA85");
		
		TableViewerColumn developerNameTableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn developerNameColumn = developerNameTableViewerColumn.getColumn();
		developerNameColumn.setWidth(100);
		developerNameColumn.setText("\uAC1C\uBC1C\uC790");
		
		TableViewerColumn versionTableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn versionColumn = versionTableViewerColumn.getColumn();
		versionColumn.setWidth(100);
		versionColumn.setText("\uBC84\uC804");	
		
		TableViewerColumn idTableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn idColumn = idTableViewerColumn.getColumn();
		idColumn.setWidth(300);
		idColumn.setText("ID");
		
		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());	
		tableViewer.setInput(manager);

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
		return new Point(675, 325);
	}
	
	public SystemTradingLogicDescriptor[] getSelectedSystemLogicDescriptors()
	{
		SystemTradingLogicDescriptor[] list = new SystemTradingLogicDescriptor[selectedLogicExtensions.size()];
		for(int i=0; i<list.length; i++)
			list[i] = selectedLogicExtensions.get(i);
		return list;
	}
}
