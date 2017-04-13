package com.traderobot.ui.datawindow.feedhandlers;

import com.traderobot.platform.koscom.transaction.ITransactionData;
import com.traderobot.ui.datawindow.datacollector.AbstractDataCollector;

public class DefaultDataCollector extends AbstractDataCollector {

	@Override
	public boolean canAccept(String code, ITransactionData rtd) {
		return true;
	}
	
}
