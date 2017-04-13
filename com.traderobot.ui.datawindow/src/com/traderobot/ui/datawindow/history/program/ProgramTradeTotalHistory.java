package com.traderobot.ui.datawindow.history.program;

import java.io.IOException;

import com.traderobot.platform.koscom.feed.FeedPointManager;
import com.traderobot.platform.koscom.transaction.ITransactionData;
import com.traderobot.platform.koscom.transaction.TransactionCode;
import com.traderobot.ui.datawindow.history.AbstractHistoryObject;

public class ProgramTradeTotalHistory extends AbstractHistoryObject {
	
	private FeedPointManager manager;
	private ProgramTradeTotalData data;
	
	public ProgramTradeTotalHistory() {
		manager = new FeedPointManager(this);
		manager.setEnable("PROGRAM.INFORMATION", true);
		init();
	}

	@Override
	public void onFeed(String transactionCode, ITransactionData rtd) {		
		if ( transactionCode.equals(TransactionCode.PROGRAM_TRADE_TOTAL)) {
			setDirty(true);
			data.update(rtd);
		}
	}
	
	@Override
	public void init() {
		setDirty(false);
		data = new ProgramTradeTotalData();
	}

	@Override
	public void save() {
		try {
			if (isDirty())
				data.save();
		} catch (IOException ioe ) {
			ioe.printStackTrace();
		}
	}

	@Override
	public void start() throws IOException {
		manager.startAll();
	}

	@Override
	public void stop() throws IOException {
		manager.stopAll();
	}

}
