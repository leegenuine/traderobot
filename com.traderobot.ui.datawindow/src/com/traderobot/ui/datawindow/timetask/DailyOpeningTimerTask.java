package com.traderobot.ui.datawindow.timetask;

import java.util.Hashtable;
import java.util.TimerTask;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.Preferences;

import com.traderobot.ui.datawindow.Service;
import com.traderobot.ui.datawindow.history.HistoryObjectManager;


public class DailyOpeningTimerTask extends TimerTask {
	
	private static final String NODE_ROOT = "com.traderobot.ui.datawindow.historyobjects";
	protected static final String KOSPI200_FUTURE_HISTORY_OBJECT_NAME = "KOSPI200연결선물관리";
	
	@Override
	public void run() {
		
		try {
			
			Service.sendMessage("Opening Task 시작");
			
//			Service.getInstance().getItemManager().loadDefault();
//			String standardCode = Service.getInstance().getItemManager().getKOSPI200FutureItemManager().getLastestItem().getStandardCode();		
//			HistoryObjectManager manager = HistoryObjectManager.getInstance();
//			HistoryObjectDescriptor desc = manager.getDescriptor(KOSPI200_FUTURE_HISTORY_OBJECT_NAME);
//			if ( desc != null ) {
//				KOSPI200FutureHistory obj = (KOSPI200FutureHistory) desc.getHistoryObject();
//				obj.setStandardCode(standardCode);
//			}
			
			// 모든 사용가능 OBJECT를 저장한다.
			Preferences store;
			HistoryObjectManager hom = HistoryObjectManager.getInstance();
			Hashtable<String, Boolean> endableHistoryObjectList;
			
			store = ConfigurationScope.INSTANCE.getNode(NODE_ROOT);				
			endableHistoryObjectList = new Hashtable<String, Boolean>();
			try {
				String[] objectNames = store.keys();
				for(int i=0; i<objectNames.length; i++)
					endableHistoryObjectList.put(objectNames[i], store.getBoolean(objectNames[i], false));
			} catch (Exception e) {
				Service.sendMessage(e.getMessage());
			}
			
			endableHistoryObjectList.forEach(new BiConsumer<String, Boolean>() {
				@Override
				public void accept(String name, Boolean enable) {
					if ( enable ) {
						try {
							hom.getDescriptor(name).getHistoryObject().init();
						} catch (Exception ex) {
							Service.sendMessage(ex.getMessage());
						}
					}
				}
			});
			
			Service.sendMessage("Opening Task 종료");
			
		} catch (Exception e) {
			Service.sendMessage(e.getMessage());
		}
		
	}
}
