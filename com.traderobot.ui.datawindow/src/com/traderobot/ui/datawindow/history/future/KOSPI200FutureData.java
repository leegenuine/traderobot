package com.traderobot.ui.datawindow.history.future;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.traderobot.platform.TradePlatformConfiguration;
import com.traderobot.platform.market.Trade;

public class KOSPI200FutureData {

	public static final String NAME = "KOSPI200연결선물";
	public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	public static String separator = ",";
	
	private boolean initiated;
	private double 	open;
	private double 	high;
	private double 	low;
	private double 	close;	
	private int 	tradeQty;			// 거래량
	private double	tradeAmount;		// 거래대금
	
	public KOSPI200FutureData() {
		init();		
	}
	
	public String getName()
	{
		return NAME;
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
	
	public void update(Trade trade) {
		if ( !initiated )
			initiated = true;
		open = trade.getOpenPrice();
		high = trade.getHighPrice();
		low = trade.getLowPrice();
		close = trade.getTradePrice();
		tradeQty = trade.getCumulatedQty();
		tradeAmount = trade.getCumulatedAmount();
	}
	
	public void init()
	{
		initiated = false;
		open = 0;
		high = 0;
		low = 0;
		close = 0;
		tradeQty = 0;
		tradeAmount = 0;
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
