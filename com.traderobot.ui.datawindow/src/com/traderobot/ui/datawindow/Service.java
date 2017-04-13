package com.traderobot.ui.datawindow;

//import java.io.IOException;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Timer;
import java.util.function.BiConsumer;

import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.e4.core.services.events.IEventBroker;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.prefs.Preferences;

import com.traderobot.platform.TradePlatformConfiguration;
import com.traderobot.platform.koscom.feed.FeedBranchHandler;
import com.traderobot.platform.koscom.feed.FeedPointExtensions;
import com.traderobot.platform.koscom.feed.FeedPointManager;
import com.traderobot.platform.koscom.transaction.TransactionCode;
//import com.traderobot.platform.master.MasterManager;
import com.traderobot.ui.datawindow.datacollector.AbstractDataCollector;
import com.traderobot.ui.datawindow.datacollector.DataCollectorManager;
import com.traderobot.ui.datawindow.feedhandlers.DefaultDataCollector;
import com.traderobot.ui.datawindow.feedhandlers.MasterLogHandler;
import com.traderobot.ui.datawindow.history.HistoryObjectManager;
import com.traderobot.ui.datawindow.timetask.DailyClosingTimerTask;
import com.traderobot.ui.datawindow.timetask.DailyOpeningTimerTask;

public class Service {
	
	@Inject
	Shell shell;
	
	private static final String FEEDPOINT_NODE = "com.traderobot.ui.datawindow.feedpoints";
	private static final String TRANSACTION_NODE = "com.traderobot.ui.datawindow.transactions";
	private static final String HISTORY_OBJECT_NODE = "com.traderobot.ui.datawindow.historyobjects";
	
	//private static final int DEFAULT_CLOSING_HOUR_MINUTE = 1510;
	private static final int DEFAULT_OPENING_HOUR = 8;
	private static final int DEFAULT_OPENING_MINUTE = 40;	
	private static final int DEFAULT_CLOSING_HOUR = 17;
	private static final int DEFAULT_CLOSING_MINUTE = 00;
	private static final int DAY_INTERVAL = 24*60*60*1000;
	
	private Calendar closingTime;
	private Timer closingTimer;
	private long interval;
	private DailyClosingTimerTask closingTask;
	
	private Calendar openingTime;
	private Timer openingTimer;
	private DailyOpeningTimerTask openingTask;
	
	private String masterFilePath;	
//	private String historyFilePath;
	private String dataCollectorName;
	
	private Hashtable<String, Boolean> enableFeedPointList;			// 수신할 FeedPoint list - id, enable
	private Hashtable<String, Boolean> enableTransactionList;		// 기록할 Transaction list
	private Hashtable<String, Boolean> enableHistoryObjectList;		// 히스토리오브젝트 list
	
	private TradePlatformConfiguration configuration;
	
	private FeedPointManager 		feedManager;
	private FeedPointExtensions 	extensions;
	
	private MasterLogHandler 		masterHandler;	
	private AbstractDataCollector 	dataHandler;
	private FeedBranchHandler 		handler;
//	private MasterManager			itemManager;
	
	private static Service instance;
	
	private Service() {
		initConfigurations();
//		itemManager = new MasterManager();
//		try {
//			itemManager.loadDefault();
//		} catch (IOException ignore ) {}
	}
	
	public static Service getInstance() {
		if ( instance != null )
			return instance;
		
		instance = new Service();
		return instance;
	}
	
//	public MasterManager getItemManager() {
//		return itemManager;
//	}
	
	private void initConfigurations() {		
		configuration = TradePlatformConfiguration.getInstance();		
//		dataFilePath = configurationStore.get("dataFilePath", "");
//		historyFilePath = configurationStore.get("historyFilePath", "");		
	}
	
	private void initFeedPoints() {
		
		extensions = FeedPointExtensions.getInstance();
		handler = new FeedBranchHandler();
		feedManager = new FeedPointManager(handler);
		feedManager.disableAll();
		
		Preferences feedPointStore = ConfigurationScope.INSTANCE.getNode(FEEDPOINT_NODE);
		enableFeedPointList = new Hashtable<String, Boolean>();
		try {
			String[] feedPointIds = feedPointStore.keys();
			for(int i=0; i<feedPointIds.length; i++) {
				enableFeedPointList.put(feedPointIds[i], feedPointStore.getBoolean(feedPointIds[i], false));
			}
		} catch (Exception e) {}
		enableFeedPointList.forEach(new BiConsumer<String, Boolean>() {
			@Override
			public void accept(String id, Boolean enable) {
				if ( enable ) {
//					Service.sendMessage("피드포인트[" + extensions.getDescriptor(id).getName() +"]에 대하여 실시간자료를 수집함");
					feedManager.setEnable(extensions.getDescriptor(id).getName(), true);
				}
			}
		});
	}
	
	private void initFeedHandlers() {
		
		Preferences configurationStore = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);		
		dataCollectorName = configurationStore.get("dataCollector", "");
		masterFilePath = configuration.getMasterPath();
		masterHandler = new MasterLogHandler(masterFilePath);
		try {			
			dataHandler = DataCollectorManager.getInstance().getDescriptor(dataCollectorName).getDataCollector();
		} catch (CoreException ce) {
			dataHandler = new DefaultDataCollector();
		}
		
		// MASTER HANDLER
		handler.addHandler(TransactionCode.KOSPI_MASTER_DATA, masterHandler); 						// KOSPI종목정보
		handler.addHandler(TransactionCode.ELW_MASTER_DATA, masterHandler); 						// ELW종목정보
		handler.addHandler(TransactionCode.MEMBER_DATA, masterHandler);								// 회원사 정보
		handler.addHandler(TransactionCode.KOSPI200_FUTURE_MASTER_DATA, masterHandler);				// KOSPI200선물 종목
		handler.addHandler(TransactionCode.KOSPI200_OPTION_MASTER_DATA, masterHandler);				// KOSPI200옵션 종목
		handler.addHandler(TransactionCode.MINI_FUTURE_MASTER_DATA, masterHandler);					// MINI선물 종목
		handler.addHandler(TransactionCode.MINI_OPTION_MASTER_DATA, masterHandler);					// MINI옵션 종목
		handler.addHandler(TransactionCode.COMMODITY_FUTURE_MASTER_DATA, masterHandler);			// 상품선물 종목
		
		// DATA HANDLER
		Preferences transactionStore = ConfigurationScope.INSTANCE.getNode(TRANSACTION_NODE);
		enableTransactionList = new Hashtable<String, Boolean>();
		try {
			String[] codes = transactionStore.keys();
			for(int i=0; i<codes.length; i++) {
				enableTransactionList.put(codes[i], transactionStore.getBoolean(codes[i], false));
			}
		} catch (Exception e) {}
		enableTransactionList.forEach(new BiConsumer<String, Boolean>() {
			@Override
			public void accept(String code, Boolean enable) {
				if ( enable )
					handler.addHandler(code, dataHandler);
			}
		});

//		handler.addHandler(TransactionCode.KOSPI200_FUTURE_CLOSE_DATA, dataHandler);				// KOSPI200선물 종목마감		
//		handler.addHandler(TransactionCode.KOSPI200_FUTURE_MARKET_CLOSE_DATA, dataHandler);			// KOSPI200선물 시장마감
//		handler.addHandler(TransactionCode.KOSPI200_OPTION_CLOSE_DATA, dataHandler);				// KOSPI200옵션 종목마감		
//		handler.addHandler(TransactionCode.KOSPI200_OPTION_MARKET_CLOSE_DATA, dataHandler);			// KOSPI200옵션 시장마감
	}
	
	private void initHistoryObjects() {
		
		HistoryObjectManager hom = HistoryObjectManager.getInstance();		
		Preferences store = ConfigurationScope.INSTANCE.getNode(HISTORY_OBJECT_NODE);
		enableHistoryObjectList = new Hashtable<String, Boolean>();
		try {
			String[] objectNames = store.keys();
			for(int i=0; i<objectNames.length; i++) {
				enableHistoryObjectList.put(objectNames[i], store.getBoolean(objectNames[i], false));
			}
		} catch (Exception e) {}
		enableHistoryObjectList.forEach(new BiConsumer<String, Boolean>() {
			@Override
			public void accept(String name, Boolean enable) {
				if ( enable ) {
					try {
						hom.getDescriptor(name).getHistoryObject().start();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}
	
	private void stopHistoryObjects() {
		
		enableHistoryObjectList.forEach(new BiConsumer<String, Boolean>() {
			@Override
			public void accept(String name, Boolean enable) {
				if ( enable ) {
					try {
						HistoryObjectManager hom = HistoryObjectManager.getInstance();
						hom.getDescriptor(name).getHistoryObject().stop();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}
	
	private void initTimer()
	{
		Preferences store = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		int openingHour = store.getInt("openingTaskHour", DEFAULT_OPENING_HOUR);
		int openingMinute = store.getInt("openingTaskMinute", DEFAULT_OPENING_MINUTE);
		int closingHour = store.getInt("closingTaskHour", DEFAULT_CLOSING_HOUR);
		int closingMinute = store.getInt("closingTaskMinute", DEFAULT_CLOSING_MINUTE);

		openingTime = Calendar.getInstance();
		int hour = openingTime.get(Calendar.HOUR_OF_DAY);
		int minute = openingTime.get(Calendar.MINUTE);
		int hourminute = Integer.parseInt(String.format("%02d%02d", hour, minute));
		
		int openingHourMinute = Integer.parseInt(String.format("%02d%02d", openingHour, openingMinute));
		if ( hourminute >= openingHourMinute )
			openingTime.roll(Calendar.DAY_OF_YEAR, true);
		openingTime.set(Calendar.HOUR_OF_DAY, openingHour);
		openingTime.set(Calendar.MINUTE, openingMinute);
		openingTime.set(Calendar.SECOND, 0);
		openingTimer = new Timer();
		interval = DAY_INTERVAL;					// millseconds
		openingTask = new DailyOpeningTimerTask();
		openingTimer.scheduleAtFixedRate(openingTask, openingTime.getTime(), interval);
		
		// 16시로 설정한다.
		closingTime = Calendar.getInstance();
		hour = closingTime.get(Calendar.HOUR_OF_DAY);
		minute = closingTime.get(Calendar.MINUTE);
		hourminute = Integer.parseInt(String.format("%02d%02d", hour, minute));
		int closingHourMinute = Integer.parseInt(String.format("%02d%02d", closingHour, closingMinute));
		if ( hourminute >= closingHourMinute )
			closingTime.roll(Calendar.DAY_OF_YEAR, true);
		closingTime.set(Calendar.HOUR_OF_DAY, closingHour);
		closingTime.set(Calendar.MINUTE, closingMinute);
		closingTime.set(Calendar.SECOND, 0);
		closingTimer = new Timer();
		interval = DAY_INTERVAL;					// millseconds
		closingTask = new DailyClosingTimerTask();
		closingTimer.scheduleAtFixedRate(closingTask, closingTime.getTime(), interval);
	}
	
	public void start() {
		try {	
			
			initFeedPoints();
			initFeedHandlers();
			initHistoryObjects();
			initTimer();
			feedManager.startAll();
			Service.sendMessage("서비스가 정상적으로 가동되었습니다.");
			
		} catch (Exception e) {
			MessageDialog.open(MessageDialog.ERROR, shell, "서비스 시작 오류", e.getMessage(), SWT.NONE);
		}
	}
	
	public void stop() {
		try {
			feedManager.stopAll();
			stopHistoryObjects();
			enableFeedPointList = null;
			enableHistoryObjectList = null;
			enableTransactionList = null;
			feedManager = null;
			extensions = null;
			masterHandler = null;
			dataHandler = null;
			handler = null;
			closingTask.cancel();
			openingTask.cancel();
			Service.sendMessage("서비스가 정상적으로 중지되었습니다.");
			
		} catch (Exception e) {
			if ( shell != null)
				MessageDialog.open(MessageDialog.ERROR, shell, "서비스 중지 오류", e.getMessage(), SWT.NONE);
		}
	}
	
	public static void sendMessage(String message) {
		Dictionary<String, Object> data = new Hashtable<String, Object>(2);
		LogMessage lm = new LogMessage(message);
		data.put(IEventBroker.DATA, lm);
		Event event = new Event(IEventConstants.TOPIC_MESSAGE, data);
		EventAdmin eventAdmin = Activator.getEventAdmin();
		if ( eventAdmin != null )
			eventAdmin.postEvent(event);
	}
	
	public static void sendMessage(String message, Object obj) {
		sendMessage(message + " [" + obj.getClass().getName() + "]");
	}
}
