 
package com.traderobot.ui.workbench.handlers;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.ui.di.AboutToHide;
import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

import com.traderobot.platform.logic.ITradingLogic;
import com.traderobot.platform.logic.systemtrading.SystemTradingLogicDescriptor;

public class SystemTradingLogicMenuContributor {
	
	@Inject 
	ESelectionService selectionService;
	
	@AboutToShow
	public void aboutToShow(List<MMenuElement> items) {
		
		try {
			Object selection = selectionService.getSelection();		
//			System.out.println("aboutToShow: " + selection);
			
			if ( selection instanceof SystemTradingLogicDescriptor ) {
				
				SystemTradingLogicDescriptor desc = (SystemTradingLogicDescriptor) selectionService.getSelection();
				MDirectMenuItem dynamicItem = MMenuFactory.INSTANCE.createDirectMenuItem();
				dynamicItem.setLabel("로직삭제");
				dynamicItem.setContributionURI("bundleclass://com.traderobot.ui.workbench/com.traderobot.ui.workbench.handlers.DeleteSystemTradingLogic");
				items.add(dynamicItem);
				
				dynamicItem = MMenuFactory.INSTANCE.createDirectMenuItem();
				if ( desc.isEnable() )
					dynamicItem.setLabel("사용불가");
				else
					dynamicItem.setLabel("사용가능");			
				dynamicItem.setContributionURI("bundleclass://com.traderobot.ui.workbench/com.traderobot.ui.workbench.handlers.EnableSystemTradingLogic");
				items.add(dynamicItem);
				
				if ( !desc.getInstance().isRunning() ) {
					dynamicItem = MMenuFactory.INSTANCE.createDirectMenuItem();
					dynamicItem.setLabel("실시간 실행");			
					dynamicItem.setContributionURI("bundleclass://com.traderobot.ui.workbench/com.traderobot.ui.workbench.handlers.RunSystemTradingLogic");
					items.add(dynamicItem);
					
					dynamicItem = MMenuFactory.INSTANCE.createDirectMenuItem();
					dynamicItem.setLabel("BACKTEST 실행");			
					dynamicItem.setContributionURI("bundleclass://com.traderobot.ui.workbench/com.traderobot.ui.workbench.handlers.BacktestSystemTradingLogic");
					items.add(dynamicItem);
				} else {
					if ( desc.getInstance().getStartMode() == ITradingLogic.REALTIME ) {
						dynamicItem = MMenuFactory.INSTANCE.createDirectMenuItem();
						dynamicItem.setLabel("실행 중지");			
						dynamicItem.setContributionURI("bundleclass://com.traderobot.ui.workbench/com.traderobot.ui.workbench.handlers.RunSystemTradingLogic");
						items.add(dynamicItem);
					} else {
						dynamicItem = MMenuFactory.INSTANCE.createDirectMenuItem();
						dynamicItem.setLabel("BACKTEST 중지");			
						dynamicItem.setContributionURI("bundleclass://com.traderobot.ui.workbench/com.traderobot.ui.workbench.handlers.BacktestSystemTradingLogic");
						items.add(dynamicItem);
					}
				}
			}
		} catch (Exception e ) {}
	}
	
	
	@AboutToHide
	public void aboutToHide(List<MMenuElement> items) {
	}
	
		
}