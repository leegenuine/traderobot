package com.traderobot.ui.datawindow.history.index;

import java.io.IOException;
import java.util.Hashtable;
import java.util.function.BiConsumer;

import com.traderobot.platform.koscom.feed.FeedPointManager;
import com.traderobot.platform.koscom.transaction.ITransactionData;
import com.traderobot.platform.koscom.transaction.TransactionCode;
import com.traderobot.platform.koscom.transaction.udp.index.KOSPIIndexData;
//import com.traderobot.platform.master.MasterManager;
//import com.traderobot.platform.master.future.KOSPI200FutureMaster;
import com.traderobot.ui.datawindow.history.AbstractHistoryObject;

public class IndexHistory extends AbstractHistoryObject {
	
	private FeedPointManager manager;
	private Hashtable<String, IndexData> indexTable;
//	private MasterManager itemManager;
	
	public IndexHistory()
	{	
		manager = new FeedPointManager(this);
		manager.setEnable("KOSPI.INDEX", true);
		manager.setEnable("KOSDAQ.INDEX", true);
//		itemManager = new MasterManager();
		init();
	}
	
	@Override
	public void init() {
		
		indexTable = new Hashtable<String, IndexData>();
		setDirty(false);
		
		// check business date
//		try {
//			itemManager.loadDefault();
//			KOSPI200FutureMaster master = itemManager.getKOSPI200FutureItemManager().getLastestItem();
//			if ( master == null ) {
//				return;
//			}
//		} catch (Exception ex) {
//			return;
//		}
	}

	@Override
	public void save() {
		
		if (!isDirty())
			return;
		
		indexTable.forEach(new BiConsumer<String, IndexData>() {
			@Override
			public void accept(String t, IndexData id) {
				try {
					id.save();
				} catch (IOException ioe ) {
					ioe.printStackTrace();
				}
			}
		});
	}

	@Override
	public void start() throws IOException {		
		manager.startAll();
	}

	@Override
	public void stop() throws IOException  {		
		manager.stopAll();
	}
	
	@Override
	public void onFeed(String transactionCode, ITransactionData rtd) {
		
		if ( transactionCode.equals(TransactionCode.KOSPI_INDEX) || 
			 transactionCode.equals(TransactionCode.KOSPI200_INDEX) ||
			 transactionCode.equals(TransactionCode.KOSDAQ_INDEX) ) {
			
			setDirty(true);
			
			String marketCode = KOSPIIndexData.getMarketCode(rtd.getBytes());
			String upjongCode = KOSPIIndexData.getUpjongCode(rtd.getBytes());
			String name = getName(marketCode, upjongCode);
			double price = KOSPIIndexData.getIndexValue(rtd.getBytes());			
			int qty = KOSPIIndexData.getTradeQty(rtd.getBytes());
			double amount = KOSPIIndexData.getTradeAmount(rtd.getBytes());			
			IndexData d = indexTable.get(name);
			if ( d == null ) {
				d = new IndexData(marketCode, upjongCode);
				indexTable.put(name,  d);
			}
			d.update(price, qty, amount);
		}
	}
	
	public static String getName(String marketCode, String upjongCode)
	{
		return IndexData.PREFIX + marketCode + upjongCode;
	}
	
}
