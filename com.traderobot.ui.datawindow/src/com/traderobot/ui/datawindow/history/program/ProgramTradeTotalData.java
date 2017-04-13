package com.traderobot.ui.datawindow.history.program;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.traderobot.platform.TradePlatformConfiguration;
import com.traderobot.platform.koscom.transaction.ITransactionData;


public class ProgramTradeTotalData {
	
	public static final String NAME = "프로그램전체집계";
	public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	public static String separator = ",";
	
	private int 	indexArbitrageSellQty;		// 지수차익매도수량
	private int 	indexArbitrageBuyQty;		// 지수차익매수수량
	private int 	indexNoArbitrageSellQty;	// 지수비차익매도수량
	private int 	indexNoArbitrageBuyQty;		// 지수비차익매수수량
	private double 	indexArbitrageSellAmount;	// 지수차익매도금액
	private double 	indexArbitrageBuyAmount;	// 지수차익매수금액
	private double 	indexNoArbitrageSellAmount;	// 지수비차익매도금액
	private double 	indexNoArbitrageBuyAmount;	// 지수비차익매수금액
	
	public ProgramTradeTotalData() {
	}
	
	public String getName()
	{
		return NAME;
	}
	
	public String getFileName()
	{
		return getName() + ".hst";
	}
	
	public void update(ITransactionData rtd) {		
		byte[] data = rtd.getBytes();
		indexArbitrageSellQty = com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexArbitrageTrustSellQty(data) + 
				com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexArbitragePropSellQty(data);
		indexArbitrageBuyQty = com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexArbitrageTrustBuyQty(data) + 
				com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexArbitragePropBuyQty(data);
		indexNoArbitrageSellQty = com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexNoArbitrageTrustSellQty(data) + 
				com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexNoArbitragePropSellQty(data);
		indexNoArbitrageBuyQty = com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexNoArbitrageTrustBuyQty(data) + 
				com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexNoArbitragePropBuyQty(data);		
		
		indexArbitrageSellAmount = com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexArbitrageTrustSellAmount(data) + 
				com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexArbitragePropSellAmount(data);
		indexArbitrageBuyAmount = com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexArbitrageTrustBuyAmount(data) + 
				com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexArbitragePropBuyAmount(data);
		indexNoArbitrageSellAmount = com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexNoArbitrageTrustSellAmount(data) + 
				com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexNoArbitragePropSellAmount(data);
		indexNoArbitrageBuyAmount = com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexNoArbitrageTrustBuyAmount(data) + 
				com.traderobot.platform.koscom.transaction.udp.information.ProgramTradeTotalData.getIndexNoArbitragePropBuyAmount(data);
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(DATE_FORMAT.format(Calendar.getInstance().getTime()));
		sb.append(separator);
		sb.append(String.format("%d", indexArbitrageSellQty));
		sb.append(separator);
		sb.append(String.format("%d", indexArbitrageBuyQty));
		sb.append(separator);
		sb.append(String.format("%d", indexNoArbitrageSellQty));
		sb.append(separator);
		sb.append(String.format("%d", indexNoArbitrageBuyQty));
		sb.append(separator);
		sb.append(String.format("%.0f", indexArbitrageSellAmount));
		sb.append(separator);
		sb.append(String.format("%.0f", indexArbitrageBuyAmount));
		sb.append(separator);
		sb.append(String.format("%.0f", indexNoArbitrageSellAmount));
		sb.append(separator);
		sb.append(String.format("%.0f", indexNoArbitrageBuyAmount));
		return sb.toString();
	}
	
	public void save() throws IOException
	{
		//Preferences configurationStore = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		//String historyFilePath = configurationStore.get("historyFilePath", "");
		String historyFilePath = TradePlatformConfiguration.getInstance().getHistoryPath();
		File f = new File(historyFilePath + "//" + getFileName());		
		FileWriter writer = new FileWriter(f, true);
		writer.write(toString());
		writer.write("\r\n");
		writer.close();
	}
}
