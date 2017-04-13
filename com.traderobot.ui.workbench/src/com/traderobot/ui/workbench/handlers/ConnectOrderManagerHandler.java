 
package com.traderobot.ui.workbench.handlers;

import java.io.IOException;

import org.eclipse.e4.core.di.annotations.Execute;

import com.traderobot.platform.TradePlatform;

public class ConnectOrderManagerHandler {
	
	@Execute
	public void execute() {
		
		try
		{
			TradePlatform.getPlatform().getOrderManager().connect();
		} 
		catch (IOException e)
		{	
			e.printStackTrace();
		}
	}
		
}