 
package com.traderobot.ui.workbench.parts.book;

import java.text.DecimalFormat;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.traderobot.platform.IPlatformEventConstants;
import com.traderobot.platform.TradePlatform;
import com.traderobot.platform.book.Book;
import com.traderobot.platform.book.Position;
import com.traderobot.platform.market.MarketData;
import com.traderobot.platform.master.IMasterData;

public class BookListPart {
	
	private static final int IDX_LOGIC_NAME = 0;				//로직명
	private static final int IDX_ACCOUNT_NUMBER = 1;			//계좌번호
	private static final int IDX_STANDARD_CODE = 2;				//표준코드
	private static final int IDX_SHORT_NAME = 3;				//단축명	
	private static final int IDX_BOOK_QTY = 4;					//장부수량
	private static final int IDX_BOOK_AMOUNT = 5;				//장부금액
	private static final int IDX_BUY_QTY = 6;					//매수수량
	private static final int IDX_BUY_AMOUNT = 7;				//매수금액
	private static final int IDX_SELL_QTY = 8;					//매도수량
	private static final int IDX_SELL_AMOUNT = 9;				//매도금액
	private static final int IDX_TOTAL_PNL = 10;				//총손익	
	
	private TableViewer viewer;
	private Table table;
	private Book currentBook;
		
	@Inject
	public BookListPart() {		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {		
		viewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		viewer.setContentProvider(new ContentProvider());
		table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		createColumns(parent, viewer);
		
		viewer.setInput(TradePlatform.getPlatform().getBook());
	}
	
	private void createColumns(final Composite parent, final TableViewer viewer)
	{
		TableViewerColumn logicNameColumn = createTableViewerColumn("로직명", 100);
		logicNameColumn.setLabelProvider(new PositionColumnLabelProvider(IDX_LOGIC_NAME));
		
		TableViewerColumn accountNumberColumn = createTableViewerColumn("계좌번호", 80);
		accountNumberColumn.setLabelProvider(new PositionColumnLabelProvider(IDX_ACCOUNT_NUMBER));
		
		TableViewerColumn standardCodeColumn = createTableViewerColumn("표준코드", 0);
		standardCodeColumn.setLabelProvider(new PositionColumnLabelProvider(IDX_STANDARD_CODE));
		standardCodeColumn.getColumn().setResizable(false);
		
		TableViewerColumn shortNameColumn = createTableViewerColumn("종목명", 80);
		shortNameColumn.setLabelProvider(new PositionColumnLabelProvider(IDX_SHORT_NAME));
		
		TableViewerColumn bookQtyColumn = createTableViewerColumn("장부수량", 70);
		bookQtyColumn.setLabelProvider(new PositionColumnLabelProvider(IDX_BOOK_QTY));
		bookQtyColumn.getColumn().setAlignment(SWT.RIGHT);
		
		TableViewerColumn bookAmountColumn = createTableViewerColumn("장부금액", 120);
		bookAmountColumn.setLabelProvider(new PositionColumnLabelProvider(IDX_BOOK_AMOUNT));
		bookAmountColumn.getColumn().setAlignment(SWT.RIGHT);
		
		TableViewerColumn buyQtyColumn = createTableViewerColumn("매수수량", 70);
		buyQtyColumn.setLabelProvider(new PositionColumnLabelProvider(IDX_BUY_QTY));
		buyQtyColumn.getColumn().setAlignment(SWT.RIGHT);
		
		TableViewerColumn buyAmountColumn = createTableViewerColumn("매수금액", 120);
		buyAmountColumn.setLabelProvider(new PositionColumnLabelProvider(IDX_BUY_AMOUNT));
		buyAmountColumn.getColumn().setAlignment(SWT.RIGHT);
		
		TableViewerColumn sellQtyColumn = createTableViewerColumn("매도수량", 70);
		sellQtyColumn.setLabelProvider(new PositionColumnLabelProvider(IDX_SELL_QTY));
		sellQtyColumn.getColumn().setAlignment(SWT.RIGHT);
		
		TableViewerColumn sellAmountColumn = createTableViewerColumn("매도금액", 120);
		sellAmountColumn.setLabelProvider(new PositionColumnLabelProvider(IDX_SELL_AMOUNT));
		sellAmountColumn.getColumn().setAlignment(SWT.RIGHT);
		
		TableViewerColumn totalPnlAmountColumn = createTableViewerColumn("손익", 120);
		totalPnlAmountColumn.setLabelProvider(new PositionColumnLabelProvider(IDX_TOTAL_PNL));
		totalPnlAmountColumn.getColumn().setAlignment(SWT.RIGHT);
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
	
	@Inject @Optional
	public void onBookExecute(
		@UIEventTopic(IPlatformEventConstants.TOPIC_BOOK_EXECUTE) 
		Object data)
	{
		viewer.refresh();
	}
	
	@Inject @Optional
	public void onBookChanged(
		@UIEventTopic(IPlatformEventConstants.TOPIC_BOOK_CHANGED) 
		Object data)
	{
		currentBook = TradePlatform.getPlatform().getBook();
		viewer.setInput(currentBook);
	}
	
	
	public class ContentProvider implements IStructuredContentProvider 
	{	
		private Book book;
		
		public Object[] getElements(Object inputElement) {
			return book.getPositionList();
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			this.book = (Book) newInput;
			currentBook = book;
		}
	}
	
	public class PositionColumnLabelProvider extends ColumnLabelProvider 
	{	
		private int index;
		
		public PositionColumnLabelProvider(int index)
		{
			this.index = index;
		}
		
		@Override
		public String getText(Object element)
		{
			Position position = (Position) element;
			DecimalFormat df0 = new DecimalFormat("#,###,###,##0");
			
			switch(index)
			{
			case IDX_LOGIC_NAME:				//단축코드
				return position.getLogicName();			
			case IDX_ACCOUNT_NUMBER:			//처리상태
				return position.getAccountNumber();
			case IDX_STANDARD_CODE:				//표준코드
				return position.getStandardCode();
			case IDX_SHORT_NAME:				//단축명
				IMasterData master = TradePlatform.getPlatform().getItemManager().get(position.getStandardCode());
				if ( master != null )
					return master.getShortName();
				return "알 수 없는 종목";
			case IDX_BOOK_QTY:					//장부수량
				return df0.format(position.getBookQty());
			case IDX_BOOK_AMOUNT:				//장부금액
				return df0.format(position.getBookAmount());
			case IDX_BUY_QTY:					//총매수수량
				return df0.format(position.getBuyQty());
			case IDX_BUY_AMOUNT:				//총매수금액
				return df0.format(position.getBuyAmount());
			case IDX_SELL_QTY:					//총매도수량
				return df0.format(position.getSellQty());
			case IDX_SELL_AMOUNT:				//총매도금액
				return df0.format(position.getSellAmount());	
			case IDX_TOTAL_PNL:					//총매도금액
				if ( position.getBookQty() != 0 ) {
					double currentPrice = 0.0;
					MarketData md = TradePlatform.getPlatform().getMarketScreen().get(position.getStandardCode());
					if ( md != null )
						currentPrice = md.getTrade().getTradePrice();
					return df0.format(position.getTotalPnl(currentPrice));	
				} else {
					return df0.format(position.getTotalPnl(0.0));
				}
			default:
				return "";
			}
		}		
	}
}