package com.traderobot.ui.workbench;

public class IUIEventConstants
{
	/**
	 * 시스템트레이딩로직뷰에 등록 변경 작업을 수행 
	 */
	public static final String TOPIC_ADD_SYSTEMTRADING_LOGIC = "TRADEROBOT/WORKBENCH/onAddSystemTradingLogic";	
	public static final String TOPIC_DELETE_SYSTEMTRADING_LOGIC = "TRADEROBOT/WORKBENCH/onDeleteSystemTradingLogic";
	public static final String TOPIC_ENABLE_SYSTEMTRADING_LOGIC = "TRADEROBOT/WORKBENCH/onEnableSystemTradingLogic";
	public static final String TOPIC_RUN_SYSTEMTRADING_LOGIC = "TRADEROBOT/WORKBENCH/onRunSystemTradingLogic";	
	public static final String TOPIC_BACKTEST_SYSTEMTRADING_LOGIC = "TRADEROBOT/WORKBENCH/onBacktestSystemTradingLogic";
	public static final String TOPIC_SYSTEMTRADING_LOGIC_SELECTION_CHANGED = "TRADEROBOT/WORKBENCH/onSystemTradingLogicSelectionChanged";
	
	public static final String TOPIC_ADD_SIGNAL_ORDER_ITEM = "TRADEROBOT/WORKBENCH/SIGNAL/onAddSignalOrderItem";
	public static final String TOPIC_EDIT_SIGNAL_ORDER_ITEM = "TRADEROBOT/WORKBENCH/SIGNAL/onEditSignalOrderItem";
	public static final String TOPIC_DELETE_SIGNAL_ORDER_ITEM = "TRADEROBOT/WORKBENCH/SIGNAL/onDeleteSignalOrderItem";
	
	public static final String TOPIC_ADD_MARKET_MONITOR_ITEM = "TRADEROBOT/WORKBENCH/MARKET/onAddMarketMonitorItem";	
	public static final String TOPIC_DELETE_MARKET_MONITOR_ITEM = "TRADEROBOT/WORKBENCH/MARKET/onDeleteMarketMonitorItem";	
	
	public static final String TOPIC_OPEN_ORDER_MANAGER_SETTING = "TRADEROBOT/WORKBENCH/ORDER/onOpenOrderManagerSetting";
	
}
