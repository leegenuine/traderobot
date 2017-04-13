package com.traderobot.ui.test.windows;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TestShell extends Shell
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
			TestShell shell = new TestShell(display);
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
					System.out.println(rect.toString());
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
	public TestShell(Display display)
	{
		super(display, (SWT.CLOSE | SWT.MIN | SWT.TITLE) & ~SWT.RESIZE);
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents()
	{
		setText("SWT Application");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
	
	
}
