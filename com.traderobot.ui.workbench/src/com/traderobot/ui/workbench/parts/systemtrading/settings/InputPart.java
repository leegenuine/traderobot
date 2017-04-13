 
package com.traderobot.ui.workbench.parts.systemtrading.settings;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.traderobot.platform.logic.systemtrading.ISystemTradingLogic;
import com.traderobot.platform.logic.systemtrading.Input;
import com.traderobot.platform.logic.systemtrading.SystemTradingLogicDescriptor;
import org.eclipse.swt.layout.FillLayout;

public class InputPart {
	
	@Inject
	private ESelectionService selectionService;
	
	private static final int IDX_INPUT_NAME = 0;
	private static final int IDX_INPUT_VALUE = 1;
	
	private TableViewer viewer;
	private Table table;
	private ISystemTradingLogic input;
	
	@Inject
	public InputPart() {
	}
	
	@PostConstruct
	public void postConstruct(Composite parent, Shell shell) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		Composite tableComposite = new Composite(parent, SWT.NONE);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);
		
		viewer = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		viewer.setContentProvider(new ContentProvider());
		
		table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseDoubleClick(MouseEvent e)
			{
				int index = table.getSelectionIndex();
				String name = table.getItem(index).getText(IDX_INPUT_NAME);
				String initValue = table.getItem(index).getText(IDX_INPUT_VALUE);
				InputDialog dialog = new InputDialog(shell, "변수갑 설정", "변수값을 설정하십시오.", initValue, null);
				if ( dialog.open() == InputDialog.OK ) {
					input.setInputValue(name, dialog.getValue());
					viewer.refresh();
				}
			}			
		});
				
		TableViewerColumn nameColumn = new TableViewerColumn(viewer, SWT.NONE);
		nameColumn.setLabelProvider(new InputColumnLabelProvider(IDX_INPUT_NAME));
		TableColumn tblclmnNewColumn = nameColumn.getColumn();
		tblclmnNewColumn.setText("\uBCC0\uC218\uBA85");
						
		TableViewerColumn valueColumn = new TableViewerColumn(viewer, SWT.NONE);
		valueColumn.setLabelProvider(new InputColumnLabelProvider(IDX_INPUT_VALUE));
		TableColumn tblclmnNewColumn_1 = valueColumn.getColumn();
		tblclmnNewColumn_1.setText("\uBCC0\uC218\uAC12");
		tableColumnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(50, 100, true));
		tableColumnLayout.setColumnData(valueColumn.getColumn(), new ColumnWeightData(50, 100, true));
	}
	
	@Inject
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object o)
	{	
		SystemTradingLogicDescriptor desc;
		try {
			if ( o instanceof SystemTradingLogicDescriptor )
			{
				desc = (SystemTradingLogicDescriptor) selectionService.getSelection();
				if ( desc.getInstance() != null && input != desc.getInstance() )
				{
					input = desc.getInstance();
					viewer.setInput(input);
				}
			}
		} catch (Exception e) {}
	}
	
	private static class ContentProvider implements IStructuredContentProvider {
		
		private ISystemTradingLogic logic;
		
		public Object[] getElements(Object inputElement) {
			return logic.getInputs();
		}
		
		public void dispose() {
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			logic = (ISystemTradingLogic) newInput;
		}
	}
		
	private class InputColumnLabelProvider extends ColumnLabelProvider {
		
		private int columnIndex;
		
		public InputColumnLabelProvider(int columnIndex)
		{
			this.columnIndex = columnIndex;
		}
		
		@Override
		public String getText(Object element)
		{
			Input input = (Input) element;			
			
			switch(columnIndex)
			{
			case IDX_INPUT_NAME:
				return input.getName();
			case IDX_INPUT_VALUE:
				return input.getValue();
			}
			return "";
		}		
	}
}