package com.traderobot.ui.datawindow.history.future;

import java.io.IOException;

import com.traderobot.platform.koscom.feed.FeedPointManager;
import com.traderobot.platform.koscom.transaction.ITransactionData;
import com.traderobot.platform.market.Trade;
import com.traderobot.platform.master.MasterManager;
import com.traderobot.platform.master.future.KOSPI200FutureMaster;
import com.traderobot.ui.datawindow.Service;
import com.traderobot.ui.datawindow.history.AbstractHistoryObject;

public class KOSPI200FutureHistory extends AbstractHistoryObject {
	
	private FeedPointManager 	manager;
	private String				standardCode;
	private KOSPI200FutureData	data;
	private MasterManager		itemManager;
	
	public KOSPI200FutureHistory() {
		manager = new FeedPointManager(this);
		manager.setEnable("KOSPI200.FUTURE.TRADE", true);
		itemManager  = new MasterManager();
		init();
	}
	
	public void setStandardCode(String standardCode) {		
		this.standardCode = standardCode;
	}
	
	@Override
	public void init() {
		data = new KOSPI200FutureData();
		setDirty(false);
		try {
			itemManager.loadDefault();
			KOSPI200FutureMaster master = itemManager.getKOSPI200FutureItemManager().getLastestItem();
			if ( master != null ) {
				standardCode = master.getStandardCode();
				Service.sendMessage("KOSPI200선물 최근월물 종목 변경[" + standardCode + "]", this);
			} else {
				standardCode = "";
				Service.sendMessage("KOSPI200선물 최근월물 종목을 찾을 수 없습니다.", this);
			}
		} catch (Exception ex) {
			standardCode = "";
		}
	}

	@Override
	public void save() {
		try {
			if ( isDirty() ) {
				data.save();
			}
		} catch (IOException ioe ) {
			ioe.printStackTrace();
		}
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
		
		if ( rtd.hasStandardCode() && rtd.getStandardCode().equals(standardCode) ) {			
			if ( rtd.hasTrade() ) {
				setDirty(true);
				Trade t = rtd.getTrade();
				data.update(t);
			}
		}
	}
}
