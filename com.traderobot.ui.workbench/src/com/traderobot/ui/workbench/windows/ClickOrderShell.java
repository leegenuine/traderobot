package com.traderobot.ui.workbench.windows;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.traderobot.ui.workbench.dialogs.MasterSelectionDialog;

public class ClickOrderShell extends Shell
{
	private static final int FIXED_WIDTH = 400;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[])
	{
		try
		{
			Display display = Display.getDefault();
			ClickOrderShell shell = new ClickOrderShell(display);
			shell.addControlListener(new ControlListener(){

				@Override
				public void controlMoved(ControlEvent e)
				{					
				}

				@Override
				public void controlResized(ControlEvent e)
				{
					Shell s = (Shell) e.getSource();
					Rectangle rect = s.getBounds();
					s.setBounds(rect.x, rect.y, FIXED_WIDTH, rect.height);
				}
				
			});
			shell.open();
			shell.layout();
			while (!shell.isDisposed())
			{
				if (!display.readAndDispatch())
				{
					display.sleep();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public ClickOrderShell(Display display)
	{
		super(display, SWT.CLOSE | SWT.MIN | SWT.RESIZE | SWT.TITLE);
		
		Button findButton = new Button(this, SWT.NONE);
		findButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MasterSelectionDialog dialog = new MasterSelectionDialog(ClickOrderShell.this);
				if ( dialog.open() == Window.OK ) {
					System.out.println("selected: " + dialog.getSelectedMaster().getName());
				}	
			}
		});
		findButton.setBounds(305, 10, 77, 22);
		findButton.setText("New Button");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents()
	{
		setText("SWT Application");
		setSize(FIXED_WIDTH, 600);

	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
