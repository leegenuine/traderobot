package com.traderobot.ui.datawindow.timetask;

import java.util.Hashtable;
import java.util.TimerTask;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.Preferences;

import com.traderobot.ui.datawindow.Service;
import com.traderobot.ui.datawindow.history.HistoryObjectManager;

public class DailyClosingTimerTask extends TimerTask {
	
	private static final String NODE_ROOT = "com.traderobot.ui.datawindow.historyobjects";

	@Override
	public void run() {
		
		Service.sendMessage("Closing Task 시작");
		
		// 모든 사용가능 OBJECT를 저장한다.
		Preferences store;
		HistoryObjectManager hom = HistoryObjectManager.getInstance();
		Hashtable<String, Boolean> endableHistoryObjectList;
		
		store = ConfigurationScope.INSTANCE.getNode(NODE_ROOT);				
		endableHistoryObjectList = new Hashtable<String, Boolean>();
		try {
			String[] objectNames = store.keys();
			for(int i=0; i<objectNames.length; i++) {
				endableHistoryObjectList.put(objectNames[i], store.getBoolean(objectNames[i], false));
			}
		} catch (Exception e) {
			Service.sendMessage(e.getMessage());
		}
		endableHistoryObjectList.forEach(new BiConsumer<String, Boolean>() {
			@Override
			public void accept(String name, Boolean enable) {
				if ( enable ) {
					try {
						hom.getDescriptor(name).getHistoryObject().save();
					} catch (Exception ex) {
						Service.sendMessage(ex.getMessage());
					}
				}
			}
		});
		
		Service.sendMessage("Closing Task 종료");
	}
}
