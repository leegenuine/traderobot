 
package com.traderobot.ui.datawindow.parts;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.traderobot.platform.koscom.transaction.TransactionDescription;
import com.traderobot.ui.datawindow.misc.DataFileAnalysis;

public class DataFileAnalysisPart {
	
	private static NumberFormat nf = new DecimalFormat("#,###,##0");
	
	private static final int IDX_CODE = 0;
	private static final int IDX_COUNT = 1;
	private static final int IDX_SIZE = 2;
	
	
	@Inject
	Shell shell;
	
	private DataFileAnalysis analysis;
	
	private Text fileNameText;
	private TableViewer viewer;
	private Table table;
	
	@Inject
	public DataFileAnalysisPart() {
		analysis = new DataFileAnalysis();
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("\uBD84\uC11D\uD30C\uC775");
		
		fileNameText = new Text(composite, SWT.BORDER);
		fileNameText.setEditable(false);
		fileNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button findFileButton = new Button(composite, SWT.NONE);
		findFileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				FileDialog dialog = new FileDialog(shell);
				if ( dialog.open() != null ) 
					fileNameText.setText(dialog.getFilterPath() + "\\" + dialog.getFileName());
			}
		});
		findFileButton.setText("...");
		
		Button runButton = new Button(parent, SWT.NONE);
		runButton.setText("\uBD84\uC11D \uC2E4\uD589");
		runButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				Cursor cursor = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
				shell.setCursor(cursor);
				
				analysis.setFileName(fileNameText.getText());
				analysis.run();
				viewer.refresh();
				
				shell.setCursor(null);
				cursor.dispose();
			}
		});
		
		viewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table = viewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);		
		createColumns(parent, viewer);
		viewer.setContentProvider(new ContentProvider());
		viewer.setInput(analysis);
	}
	
	private void createColumns(final Composite parent, final TableViewer viewer)
	{	
		TableViewerColumn codeColumn = createTableViewerColumn("트랜잭션 코드", 300);
		codeColumn.setLabelProvider(new DataLogColumnLabelProvider(IDX_CODE));		
		
		TableViewerColumn countColumn = createTableViewerColumn("건수", 100);		
		countColumn.setLabelProvider(new DataLogColumnLabelProvider(IDX_COUNT));
		countColumn.getColumn().setAlignment(SWT.RIGHT);
		
		TableViewerColumn sizeColumn = createTableViewerColumn("사이즈", 100);
		sizeColumn.setLabelProvider(new DataLogColumnLabelProvider(IDX_SIZE));
		sizeColumn.getColumn().setAlignment(SWT.RIGHT);
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
	
	
	public class ContentProvider implements IStructuredContentProvider 
	{	
		DataFileAnalysis log;
		
		public Object[] getElements(Object inputElement) {
			return log.getLogs();
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			log = (DataFileAnalysis) newInput;
		}
	}
	
	public class DataLogColumnLabelProvider extends ColumnLabelProvider 
	{	
		private int index;
		
		public DataLogColumnLabelProvider(int index)
		{
			this.index = index;
		}
		
		@Override
		public String getText(Object element)
		{
			DataFileAnalysis.DataLog log = (DataFileAnalysis.DataLog) element;			
			switch(index)
			{
			case IDX_CODE:				
				return TransactionDescription.getDescription(log.getTransactionCode());
			case IDX_COUNT:
				return String.format("%d", log.getCount());
			case IDX_SIZE:			
				return nf.format(log.getTotalBytes()/1000) + "KB";
			default:
				return "";
			}
		}
	}
	
	
	
	
}