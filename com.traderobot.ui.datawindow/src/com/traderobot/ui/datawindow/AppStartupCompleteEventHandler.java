package com.traderobot.ui.datawindow;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.action.MenuManager;
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
import org.osgi.service.event.EventHandler;

import com.traderobot.ui.datawindow.actions.ShowAction;

public class AppStartupCompleteEventHandler implements EventHandler {
	
	private TrayItem trayItem;	
	private final IEclipseContext context;
	private final IEventBroker broker;
	
	public AppStartupCompleteEventHandler(IEclipseContext context, IEventBroker broker) {				
		this.context = context;
		this.broker = broker;
	}

	@Override
	public void handleEvent(Event event) {
		broker.unsubscribe(this);
		Shell shell = (Shell) context.get(IServiceConstants.ACTIVE_SHELL);
		trayItem = initTaskItem(shell);
		if ( trayItem != null ) {
			hookPopupMenu(shell);
			hookMinimize(shell);
		}
//		IWindowCloseHandler handler = new IWindowCloseHandler() {
//			@Override
//			public boolean close(MWindow window) {
//				return MessageDialog.openConfirm(shell, "프로그램 종료", "정말로 프로그램을 종료하시겠습니까?");
//			}
//		};
//		context.set(IWindowCloseHandler.class, handler);
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
