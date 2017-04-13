package com.traderobot.ui.datawindow.history;

import java.io.IOException;

import com.traderobot.platform.koscom.feed.IFeedHandler;

public interface IHistoryObject extends IFeedHandler {
	
	/**
	 * �� ���� ���� �ʱ�ȭ �۾��� ���Ͽ� �ڵ�ȣ��ȴ�.
	 * �ʱ�ȭ �κп��� setDirty(false)�� ������ ���� ������ �ʱ�ȭ ���� ��!
	 * onFeed�κп��� setDirty(true)�� ���������ν� save()�۾��� ���������� �̷�������� �Ѵ�.  
	 */
	public void init();
	
	public boolean isDirty();
	
	public void setDirty(boolean dirty);
	
	/**
	 * �� ���� �Ŀ� ������ ������ ���Ͽ� �ڵ�ȣ��ȴ�. 
	 * isDirty()�� ���¸� üũ�� �Ŀ� �����Ѵ�.
	 */
	public void save();
	
	/**
	 * ���񽺸� ������ �� ȣ��ȴ�.
	 * @throws IOException
	 */
	public void start() throws IOException ;
	
	/**
	 * ���񽺸� ������ �� ȣ��ȴ�.
	 * @throws IOException
	 */
	public void stop() throws IOException ;
	
}
