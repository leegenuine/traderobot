package com.traderobot.ui.workbench.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;

import com.traderobot.ui.workbench.IUIEventConstants;

public class BacktestSystemTradingLogic {
	
	@Execute
	public void execute(IEventBroker broker) {
		broker.send(IUIEventConstants.TOPIC_BACKTEST_SYSTEMTRADING_LOGIC, null);
	}

}
