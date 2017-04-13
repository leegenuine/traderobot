package com.traderobot.ui.datawindow.history;

import java.io.IOException;

import com.traderobot.platform.koscom.feed.IFeedHandler;

public interface IHistoryObject extends IFeedHandler {
	
	/**
	 * 장 시작 전에 초기화 작업을 위하여 자동호출된다.
	 * 초기화 부분에서 setDirty(false)로 저장을 위한 정보를 초기화 해줄 것!
	 * onFeed부분에서 setDirty(true)를 설정함으로써 save()작업이 정상적으로 이루어지도록 한다.  
	 */
	public void init();
	
	public boolean isDirty();
	
	public void setDirty(boolean dirty);
	
	/**
	 * 장 마감 후에 데이터 저장을 위하여 자동호출된다. 
	 * isDirty()로 상태를 체크한 후에 저장한다.
	 */
	public void save();
	
	/**
	 * 서비스를 시작할 때 호출된다.
	 * @throws IOException
	 */
	public void start() throws IOException ;
	
	/**
	 * 서비스를 종료할 때 호출된다.
	 * @throws IOException
	 */
	public void stop() throws IOException ;
	
}
