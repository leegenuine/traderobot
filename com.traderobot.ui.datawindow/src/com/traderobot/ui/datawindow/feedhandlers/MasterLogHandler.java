package com.traderobot.ui.datawindow.feedhandlers;

import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.traderobot.platform.koscom.feed.IFeedHandler;
import com.traderobot.platform.koscom.transaction.ITransactionData;
import com.traderobot.ui.datawindow.Service;

public class MasterLogHandler implements IFeedHandler 
{	
	protected String logFilePath = ".";
	protected String currentFileName = "";
	protected FileWriter writer = null;
	
	public MasterLogHandler(String logPath)
	{		
		this.logFilePath = logPath;
		try {
			createFile();
		} catch (Exception ignore) {}
	}
		
	@Override
	public void onFeed(String code, ITransactionData rtd) 
	{
		String dataLine;		
		try
		{
			if ( !currentFileName.equals(getFileName()))
			{
				if ( writer != null)
					close();
				createFile();
			}
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
		
	/**
	 * YYYYMMDDhhmmssSSS
	 * @return
	 */
	protected String getDateTime()
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
		fileName = logFilePath + "\\" + dateStr + ".log";
		return fileName;
	}
		
	private void createFile() throws IOException
	{
		currentFileName = getFileName();
		writer = new FileWriter(currentFileName, true);
		Service.sendMessage("MASTER_LOG_FILE[" + currentFileName +"] CREATED.");
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
