package com.traderobot.logic.systemtrading.examples;

import com.traderobot.platform.logic.systemtrading.AbstractSystemTradingLogic;

public class ExampleLogic2 extends AbstractSystemTradingLogic
{
	private static final String INPUT_TICK = "Ticks";

	public ExampleLogic2() {
		super();
	}

	@Override
	protected void createInputs()
	{
		addInput(INPUT_TICK, "2");
	}
	
	@Override
	protected void enableFeedPoints() {
	}	

	@Override
	protected void createFeedHandlers() {
	}

	@Override
	public void onBacktestBeginDate(String yyyymmdd) {
	}

	@Override
	public void onBacktestEndDate(String yyyymmdd) {
	}

	@Override
	protected void onPrepare() {
	}

	@Override
	protected void onStopped() {
	}
}
