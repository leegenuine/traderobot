package com.traderobot.ui.workbench.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.osgi.service.prefs.BackingStoreException;

import com.traderobot.platform.order.OrderManagerDescriptor;
import com.traderobot.platform.order.OrderManagerExtensions;
import com.traderobot.platform.order.Parameter;

/**
 * 주문관리자에 대한 설정 정보를 관리한다.
 * @author ty
 *
 */
public class OrderManagerSettingDialog extends TitleAreaDialog
{	
	private static final int IDX_NAME = 0;
	private static final int IDX_LABEL = 1;
	private static final int IDX_VALUE = 2;
	
	private OrderManagerDescriptor[] descriptors;
	private OrderManagerDescriptor selected;
	private TableViewer paramViewer;
	private Table paramTable;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public OrderManagerSettingDialog(Shell parentShell)
	{
		super(parentShell);
		OrderManagerExtensions extensions = OrderManagerExtensions.getInstance();
		descriptors = extensions.getAllDescriptors();
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		setMessage("\uC8FC\uBB38\uC811\uC18D\uC744 \uC704\uD55C \uD30C\uB77C\uBBF8\uD130\uB97C \uC124\uC815\uD558\uC138\uC694.");
		setTitle("\uC8FC\uBB38\uC811\uC18D \uC124\uC815");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		FillLayout fl_container = new FillLayout(SWT.HORIZONTAL);
		fl_container.marginWidth = 10;
		fl_container.marginHeight = 10;
		container.setLayout(fl_container);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new FormLayout());
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_composite_1 = new FormData();
		fd_composite_1.right = new FormAttachment(100);
		fd_composite_1.top = new FormAttachment(0);
		fd_composite_1.left = new FormAttachment(0);
		composite_1.setLayoutData(fd_composite_1);
		
		Group group = new Group(composite_1, SWT.NONE);
		group.setText("\uC2DC\uC2A4\uD15C\uC5D0\uC11C \uC0AC\uC6A9\uD560 \uAE30\uBCF8 \uC811\uC18D\uC790");
		FillLayout fl_group = new FillLayout(SWT.HORIZONTAL);
		fl_group.marginWidth = 5;
		fl_group.marginHeight = 5;
		group.setLayout(fl_group);
		
		Combo defaultOrderManagerCombo = new Combo(group, SWT.NONE);
		for(int i=0; i<descriptors.length; i++)
			defaultOrderManagerCombo.add(descriptors[i].getName());
				
		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayout(new FormLayout());
		FormData fd_composite_2 = new FormData();
		fd_composite_2.bottom = new FormAttachment(100);
		fd_composite_2.right = new FormAttachment(100);
		fd_composite_2.top = new FormAttachment(composite_1);
		fd_composite_2.left = new FormAttachment(0);
		composite_2.setLayoutData(fd_composite_2);
		
		Label lblNewLabel = new Label(composite_2, SWT.CENTER);
		lblNewLabel.setAlignment(SWT.CENTER);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.height = 20;
		fd_lblNewLabel.right = new FormAttachment(100);
		fd_lblNewLabel.top = new FormAttachment(0, 5);
		fd_lblNewLabel.left = new FormAttachment(0);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("\uC8FC\uBB38\uC811\uC18D\uAD00\uB9AC\uC790");
		
		Composite composite_3 = new Composite(composite_2, SWT.NONE);
		composite_3.setLayout(new GridLayout(2, false));
		FormData fd_composite_3 = new FormData();
		fd_composite_3.bottom = new FormAttachment(100);
		fd_composite_3.top = new FormAttachment(lblNewLabel, 5);
		fd_composite_3.right = new FormAttachment(100);
		fd_composite_3.left = new FormAttachment(0);
		composite_3.setLayoutData(fd_composite_3);
		
		List orderManagerList = new List(composite_3, SWT.BORDER);
		GridData gd_orderManagerList = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gd_orderManagerList.widthHint = 200;
		orderManagerList.setLayoutData(gd_orderManagerList);
		for(int i=0; i<descriptors.length; i++)
			orderManagerList.add(descriptors[i].getName());
		orderManagerList.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				selected = descriptors[orderManagerList.getSelectionIndex()];
				paramViewer.setInput(selected);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{	
				selected = descriptors[orderManagerList.getSelectionIndex()];
				paramViewer.setInput(selected);
			}
		});
		
		paramViewer = new TableViewer(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		paramTable = paramViewer.getTable();
		paramTable.addMouseListener(new MouseAdapter() {			
			@Override
			public void mouseDoubleClick(MouseEvent e)
			{
				int index = paramTable.getSelectionIndex();
				String name = paramTable.getItem(index).getText(IDX_NAME);
				String initValue = paramTable.getItem(index).getText(IDX_VALUE);
				InputDialog dialog = new InputDialog(parent.getShell(), "변수갑 설정", "변수값을 설정하십시오.", initValue, null);
				if ( dialog.open() == InputDialog.OK ) {
					selected.setParameter(name, dialog.getValue());
					paramViewer.refresh();
				}
			}			
		});
		paramViewer.setContentProvider(new ContentProvider());		
		paramTable.setLinesVisible(true);
		paramTable.setHeaderVisible(true);
		paramTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn paramNameColumn = new TableViewerColumn(paramViewer, SWT.NONE);
		paramNameColumn.setLabelProvider(new ParameterColumnLabelProvider(IDX_NAME));
		paramNameColumn.getColumn().setWidth(0);
		paramNameColumn.getColumn().setResizable(false);		
		paramNameColumn.getColumn().setText("변수명");
		
		TableViewerColumn paramLabelColumn = new TableViewerColumn(paramViewer, SWT.NONE);
		paramLabelColumn.setLabelProvider(new ParameterColumnLabelProvider(IDX_LABEL));
		paramLabelColumn.getColumn().setWidth(130);
		paramLabelColumn.getColumn().setText("\uD30C\uB77C\uBBF8\uD130");
		
		TableViewerColumn paramValueColumn = new TableViewerColumn(paramViewer, SWT.NONE);
		paramValueColumn.setLabelProvider(new ParameterColumnLabelProvider(IDX_VALUE));
		paramValueColumn.getColumn().setWidth(150);
		paramValueColumn.getColumn().setText("\uC124\uC815\uAC12");
		
		if ( descriptors.length > 0 )
		{
			orderManagerList.select(0);
			selected = descriptors[0];
			paramViewer.setInput(selected);
		}

		return area;
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
		return new Point(532, 486);
	}
	
	@Override
	protected void okPressed()
	{
		for(int i=0; i<descriptors.length; i++) {
			try {
				descriptors[i].saveParameter();
			} catch ( BackingStoreException bse ) {
			}
		}
		super.okPressed();
	}
	
	
	private static class ContentProvider implements IStructuredContentProvider {
		
		private OrderManagerDescriptor desc;
		
		public Object[] getElements(Object inputElement) {
			return desc.getParameters();
		}
		
		public void dispose() {
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			desc = (OrderManagerDescriptor) newInput;
		}
	}
		
	private class ParameterColumnLabelProvider extends ColumnLabelProvider {
		
		private int index;
		
		public ParameterColumnLabelProvider(int columnIndex)
		{
			this.index = columnIndex;
		}
		
		@Override
		public String getText(Object element)
		{
			Parameter param = (Parameter) element;			
			
			switch(index)
			{
			case IDX_NAME:
				return param.getName();
			case IDX_LABEL:
				return param.getLabel();
			case IDX_VALUE:
				return param.getValue();
			}
			return "";
		}
	}	
}
