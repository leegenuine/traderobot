package com.traderobot.ui.datawindow.datacollector;

import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.traderobot.platform.TradePlatformConfiguration;
import com.traderobot.platform.koscom.feed.IFeedHandler;
import com.traderobot.platform.koscom.transaction.ITransactionData;
import com.traderobot.ui.datawindow.Service;

public abstract class AbstractDataCollector implements IFeedHandler {
	
	protected String dataFilePath = ".";
	protected String currentFileName = "";
	protected FileWriter writer = null;
	protected Object lock;
	
	public AbstractDataCollector()
	{
		TradePlatformConfiguration configuration = TradePlatformConfiguration.getInstance();
		dataFilePath = configuration.getDataPath();
		try {
			createFile();
		} catch (Exception ignore) {
			Service.sendMessage(ignore.getMessage());
		}
		lock = new Object();
	}
	
	abstract public boolean canAccept(String code, ITransactionData rtd);
		
	@Override
	public void onFeed(String code, ITransactionData rtd) 
	{			
		synchronized(lock) {
			try
			{
				String dataLine;
				if ( !currentFileName.equals(getFileName()))
				{
					if ( writer != null)
						close();
					createFile();
				}
				if ( !canAccept(code, rtd) )
					return;
				StringBuffer sb = new StringBuffer();			
				dataLine = new String(rtd.getBytes());
				sb.append(dataLine);
				sb.append("[");
				sb.append(getDateTime());
				sb.append("]\r\n");
				writer.write(sb.toString());
				writer.flush();
			}
			catch (IOException ioe)
			{
				Service.sendMessage(ioe.getMessage());
			}
		}
	}
		
	/**
	 * YYYYMMDDhhmmssSSS
	 * @return
	 */
	private String getDateTime()
	{
		Calendar c = Calendar.getInstance();
		Format format = new SimpleDateFormat("YYYYMMddhhmmssSSS");
		return format.format(c.getTime());
	}
	
	private String getFileName()
	{
		String dateStr;
		String fileName;
		Calendar c = Calendar.getInstance();
		Format format = new SimpleDateFormat("YYYYMMdd");
		dateStr = format.format(c.getTime());
		fileName = dataFilePath + "\\" + dateStr + ".log";			
		return fileName;
	}
		
	private void createFile() throws IOException
	{
		currentFileName = getFileName();
		writer = new FileWriter(currentFileName, true);
		Service.sendMessage("DATA_LOG_FILE[" + currentFileName +"] CREATED.");
	}
		
	public void close()
	{
		if ( writer != null)
		{
			try {
				writer.close();
				writer = null;
			} catch(IOException ignore) {}			
		}
	}
}
