 
package com.traderobot.ui.workbench.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import com.traderobot.platform.TradePlatform;
import com.traderobot.platform.TradePlatformConfiguration;

public class SelectBacktestHandler {
	
	@Inject
	MApplication app;
	
	@Inject
	EModelService modelService;	
	
	@Inject
	EPartService partService;
	
	@Execute
	public void execute(MWindow window) {
		
		TradePlatform.getPlatform().restart(TradePlatformConfiguration.START_BACKTEST);
		window.setLabel("TradeRobot [백테스트]");
		
		MPerspective element = (MPerspective) modelService.find("com.traderobot.ui.workbench.perspective.backtest", app);
		partService.switchPerspective(element);
	}	
}