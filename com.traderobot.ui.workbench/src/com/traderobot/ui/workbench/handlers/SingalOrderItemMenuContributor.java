 
package com.traderobot.ui.workbench.handlers;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

import com.traderobot.platform.logic.systemtrading.ISignalOrderItem;

public class SingalOrderItemMenuContributor {
	
	@Inject 
	ESelectionService selectionService;
	
	@AboutToShow
	public void aboutToShow(List<MMenuElement> items) {
		try {			
			if ( selectionService.getSelection() instanceof ISignalOrderItem ) {				
				MDirectMenuItem dynamicItem = MMenuFactory.INSTANCE.createDirectMenuItem();
				dynamicItem.setLabel("연결주문 편집");
				dynamicItem.setContributionURI("bundleclass://com.traderobot.ui.workbench/com.traderobot.ui.workbench.handlers.EditSignalOrderItemHandler");
				items.add(dynamicItem);
				
				dynamicItem = MMenuFactory.INSTANCE.createDirectMenuItem();
				dynamicItem.setLabel("연결주문 삭제");			
				dynamicItem.setContributionURI("bundleclass://com.traderobot.ui.workbench/com.traderobot.ui.workbench.handlers.DeleteSignalOrderItemHandler");
				items.add(dynamicItem);				
			}
		} catch (Exception e ) {}
	}
		
}