package com.traderobot.ui.workbench.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.traderobot.platform.koscom.feed.FeedPointDescriptor;
import com.traderobot.platform.koscom.feed.FeedPointExtensions;

public class FeedPointInformationDialog extends Dialog
{
	private static final int IDX_NAME = 0;
	private static final int IDX_DESCRIPTION = 1;
	private static final int IDX_GROUP_IP = 2;
	private static final int IDX_GROUP_PORT = 3;
	private static final int IDX_ID = 4;
	
	
	private FeedPointDescriptor[] descriptors;
	
	private TableViewer viewer;
	private Table table;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public FeedPointInformationDialog(Shell parentShell)
	{
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.RESIZE | SWT.TITLE);
		FeedPointExtensions extensions = FeedPointExtensions.getInstance();
		descriptors = extensions.getAllDescriptors();		
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite container = (Composite) super.createDialogArea(parent);		
		viewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		createColumns(parent, viewer);
		viewer.setContentProvider(new ContentProvider());
		
		table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		viewer.setInput(descriptors);
		
		getShell().setText("FeedPoint정보 리스트 조회");
		return container;
	}
	
	private void createColumns(final Composite parent, final TableViewer viewer)
	{	
		TableViewerColumn nameColumn = createTableViewerColumn("FeedPoint명", 200);
		nameColumn.setLabelProvider(new FeedPointColumnLabelProvider(IDX_NAME));
		
		TableViewerColumn descriptionColumn = createTableViewerColumn("설명", 200);		
		descriptionColumn.setLabelProvider(new FeedPointColumnLabelProvider(IDX_DESCRIPTION));
		
		TableViewerColumn groupIpColumn = createTableViewerColumn("IP", 100);
		groupIpColumn.setLabelProvider(new FeedPointColumnLabelProvider(IDX_GROUP_IP));
				
		TableViewerColumn groupPortColumn = createTableViewerColumn("Port", 60);
		groupPortColumn.setLabelProvider(new FeedPointColumnLabelProvider(IDX_GROUP_PORT));
		
		TableViewerColumn idColumn = createTableViewerColumn("ID", 200);
		idColumn.setLabelProvider(new FeedPointColumnLabelProvider(IDX_ID));
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
		return new Point(650, 479);
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
			default:
				return "";
			}
		}
	}
}
