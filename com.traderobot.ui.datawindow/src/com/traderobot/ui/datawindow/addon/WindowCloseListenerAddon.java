 
package com.traderobot.ui.datawindow.addon;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.wb.swt.ResourceManager;
import org.osgi.service.event.Event;

import com.traderobot.ui.datawindow.actions.ShowAction;

public class WindowCloseListenerAddon {
	
	private TrayItem trayItem;	
	
	@Inject
	MApplication application;
	
	@Inject
	EModelService service;
	
	@Inject
	@Optional
	IWorkbench workbench;

	@Inject
	@Optional
	public void applicationStarted(
			@UIEventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event) {		
		if ( workbench != null )
			registerCloseHandler(application);
		
		MWindow window = (MWindow) service.find("com.traderobot.ui.datawindow.window.main", application);
		Shell shell = (Shell) window.getWidget();
		trayItem = initTaskItem(shell);
		if ( trayItem != null ) {
			hookPopupMenu(shell);
			hookMinimize(shell);
		}
	}
	
	private void registerCloseHandler(MApplication application) {
		MWindow window = (MWindow) service.find("com.traderobot.ui.datawindow.window.main", application);
		IWindowCloseHandler closeHandler = new IWindowCloseHandler() {
			@Override
			public boolean close(MWindow window) {
				Shell shell = (Shell) window.getWidget();
				return MessageDialog.openConfirm(shell, "프로그램 종료", "프로그램을 정말로 종료하시겠습니까?");
			}
		};
		if ( window.getContext() != null )
			window.getContext().set(IWindowCloseHandler.class, closeHandler);
	}
	
	private void hookMinimize(final Shell shell) {
		shell.addShellListener(new ShellAdapter() {
			public void shellIconified(ShellEvent e) {
				shell.setVisible(false);
			}
		});
		trayItem.addListener(SWT.DefaultSelection, new Listener(){
			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event event) {
				if ( !shell.isVisible() ) {
					shell.setVisible(true);
					shell.setMinimized(false);
				}
			}
		});
	}
	
	private void hookPopupMenu(final Shell shell) {
		trayItem.addListener(SWT.MenuDetect, new Listener() {			
			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event event) {
				MenuManager manager = new MenuManager();
				Menu menu = manager.createContextMenu(shell);
				manager.add(new ShowAction(shell));
				menu.setVisible(true);	
			}
		});
	}
	
	private TrayItem initTaskItem(Shell shell) {
		Tray tray = shell.getDisplay().getSystemTray();
		if ( tray == null )
			return null;
		TrayItem trayItem = new TrayItem(tray, SWT.NONE);
		Image trayImage = ResourceManager.getPluginImage("com.traderobot.ui.datawindow", "icons/sample.png");
		trayItem.setImage(trayImage);
		trayItem.setToolTipText("TradeRobot DataWindow");
		return trayItem;
	}
}
