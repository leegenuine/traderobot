package com.traderobot.ui.workbench.dialogs.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

public class MasterItemSelectPane extends Composite
{
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MasterItemSelectPane(Composite parent, int style)
	{
		super(parent, SWT.NONE);
		Composite composite = new Composite(parent, style);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		composite.setLayout(gridLayout);
		
		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		GridData gd_nameLabel = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_nameLabel.heightHint = 20;
		nameLabel.setLayoutData(gd_nameLabel);
		
		Button findButton = new Button(composite,SWT.NONE);
		findButton.setImage(ResourceManager.getPluginImage("com.traderobot.ui.workbench", "icons/help_search.png"));
		GridData gd_findButton = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_findButton.widthHint = 25;
		findButton.setLayoutData(gd_findButton);
	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
