 
package com.traderobot.ui.workbench.handlers;

import java.io.IOException;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import com.traderobot.platform.TradePlatform;
import com.traderobot.platform.TradePlatformConfiguration;
import com.traderobot.platform.order.ias.IAS_OrderManager;
import com.traderobot.ui.workbench.dialogs.IASLoginDialog;

/**
 * 접속환경으로 IAS를 선택하였음.
 * IAS접속을 위한 접속창을 열고, 주어진 조건으로 플랫폼을 변경한다.
 * @author ty
 *
 */
public class SelectIASHandler {
	
	@Inject
	EPartService partService;
	
	@Inject
	EModelService modelService;
	
	@Inject
	MApplication app;
	
	@Execute
	public void execute(Shell shell, MWindow window) {
		
		if ( TradePlatform.getPlatform().getConfiguration().getStartMode() == TradePlatformConfiguration.START_REAL )
			return;
		
		MPerspective element = (MPerspective) modelService.find("com.traderobot.ui.workbench.perspective.real", app);
		partService.switchPerspective(element);
		
		TradePlatform.getPlatform().restart(TradePlatformConfiguration.START_REAL);
		IASLoginDialog dialog = new IASLoginDialog(shell);
		if ( dialog.open() == Window.OK ) {
			TradePlatform.getPlatform().getOrderManager().setParameter(IAS_OrderManager.PARAM_TRADE_ID, dialog.getTradeId());
			TradePlatform.getPlatform().getOrderManager().setParameter(IAS_OrderManager.PARAM_PASSWORD, dialog.getPassword());
			try {
				TradePlatform.getPlatform().getOrderManager().connect();
			} catch (IOException ioe) {
				MessageDialog.open(MessageDialog.ERROR, shell, "IAS접속오류", ioe.getMessage(), SWT.NONE);
			}
		}		
		window.setLabel("TradeRobot [IAS-Real]");
		//System.out.println(TradePlatform.getPlatform().getOrderManager().toString());
	}	
	
}