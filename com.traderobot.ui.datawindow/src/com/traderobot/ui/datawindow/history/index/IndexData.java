package com.traderobot.ui.datawindow.history.index;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.traderobot.platform.TradePlatformConfiguration;


public class IndexData {
	
	public static final String PREFIX = "IDX";
	public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	public static String separator = ",";
	
	private String	marketCode;
	private String	upjongCode;
	private boolean initiated;
	private double 	open;
	private double 	high;
	private double 	low;
	private double 	close;	
	private int 	tradeQty;
	private double	tradeAmount;
	
	public IndexData(String marketCode, String upjongCode) {
		this.marketCode = marketCode;
		this.upjongCode = upjongCode;
		init();
	}
	
	public String getName()
	{
		return PREFIX + marketCode + upjongCode;
	}
	
	public String getFileName()
	{
		return getName() + ".hst";
	}
	
	public boolean isInitiated() {
		return initiated;
	}
	
	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}
	
	public int getTradeQty() {
		return tradeQty;
	}

	public void setTradeQty(int tradeQty) {
		this.tradeQty = tradeQty;
	}

	public double getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(double tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public String getMarketCode() {
		return marketCode;
	}

	public String getUpjongCode() {
		return upjongCode;
	}
	
	public void init() {
		initiated = false;
		open = 0;
		high = 0;
		low = 0;
		close = 0;
		tradeQty = 0;
		tradeAmount = 0;
	}

	public void update(double price, int tradeQty, double tradeAmount) {
		
		if ( !initiated ) {
			open = price;
			high = price;
			low = price;
			close = price;	
			this.tradeQty = tradeQty;
			this.tradeAmount = tradeAmount;
			initiated = true;
			return;
		}		
		if ( price > high )
			high = price;		
		if ( price < low )
			low = price;
		close = price;		
		this.tradeQty = tradeQty;
		this.tradeAmount = tradeAmount;		
	}
	
	public void save() throws IOException
	{
		String historyFilePath = TradePlatformConfiguration.getInstance().getHistoryPath();
		File f = new File(historyFilePath + "//" + getFileName());		
		FileWriter writer = new FileWriter(f, true);		
		StringBuffer sb = new StringBuffer();
		sb.append(DATE_FORMAT.format(Calendar.getInstance().getTime()));
		sb.append(separator);
		sb.append(String.format("%.2f", open));
		sb.append(separator);
		sb.append(String.format("%.2f", high));
		sb.append(separator);
		sb.append(String.format("%.2f", low));
		sb.append(separator);
		sb.append(String.format("%.2f", close));
		sb.append(separator);		
		sb.append(String.format("%d", tradeQty));
		sb.append(separator);
		sb.append(String.format("%.0f", tradeAmount));
		writer.write(sb.toString());
		writer.write("\r\n");
		writer.close();
	}
}
