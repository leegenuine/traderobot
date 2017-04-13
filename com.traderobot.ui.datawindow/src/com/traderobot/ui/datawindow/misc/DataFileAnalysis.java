package com.traderobot.ui.datawindow.misc;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;

public class DataFileAnalysis {
	
	public class DataLog {
		
		String transactionCode;
		long count;
		long totalBytes;
		
		public DataLog(String transactionCode) {
			this.transactionCode = transactionCode;
			count = 0;
			totalBytes = 0;
		}
		
		public String getTransactionCode() {
			return transactionCode;
		}		
		
		public long getCount() {
			return count;
		}
		
		public long getTotalBytes() {
			return totalBytes;
		}
		
		public void accept(long bytes) {
			count++;
			totalBytes += bytes;
		}
	}
	
	protected String fileName;		
	protected LineNumberReader reader;
	protected Hashtable<String, DataLog> logTable;
	
	public DataFileAnalysis() {
		logTable = new Hashtable<String, DataLog>();
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
		logTable.clear();
	}
	
	public void run() {
		
		if ( fileName == null )
			return;
		
		String line = new String();
		byte[] data = new byte[1024*2];
		try {
			File f = new File(fileName);
			reader = new LineNumberReader(new FileReader(f));
			while( (line = reader.readLine()) != null ) {
				data = line.getBytes();
				if ( data.length < 5 ) {
					System.out.println("invalid data: [" + new String(data) + "] at line: " + reader.getLineNumber());
					continue;
				}
				String code = new String(data, 0, 5);
				DataLog log = logTable.get(code);
				if ( log == null ) {
					log = new DataLog(code);
					logTable.put(code, log);
				}
				log.accept(data.length);
			}
			reader.close();			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public DataLog[] getLogs()
	{
		ArrayList<DataLog> list = new ArrayList<DataLog>(logTable.values());
		list.sort(new Comparator<DataLog>() {
			@Override
			public int compare(DataLog o1, DataLog o2) {
				if (o1.getTotalBytes() > o2.getTotalBytes())
					return -1;
				if (o1.getTotalBytes() < o2.getTotalBytes())
					return 1;
				else
					return 0;
			}
		});		
		return list.toArray(new DataLog[logTable.size()]);
	}
	
	public static void main(String args[]) {
		//String fileName = "F://log//data//20161222PM.log";
		String fileName = "F://log//data//20161223AM.log";
		DataFileAnalysis a = new DataFileAnalysis();
		a.setFileName(fileName);		
		a.run();
	}
	
}
