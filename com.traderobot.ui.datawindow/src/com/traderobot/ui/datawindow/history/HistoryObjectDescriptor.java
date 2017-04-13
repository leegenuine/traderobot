package com.traderobot.ui.datawindow.history;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

public class HistoryObjectDescriptor {
	
	private static final String ATTR_NAME = "name";	
	private static final String ATTR_DESCRIPTION = "description";
	private static final String ATTR_CLASS = "class";
	
	protected IHistoryObject instance;
	protected IConfigurationElement configurationElement;	
	protected String name;
	protected String description;
	protected String className;
	
	public HistoryObjectDescriptor(IConfigurationElement configurationElement) {
		this.configurationElement = configurationElement;		
		name = getAttribute(configurationElement, ATTR_NAME, null);		
		description = getAttribute(configurationElement, ATTR_DESCRIPTION, "");
		className = getAttribute(configurationElement, ATTR_CLASS, null);
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public String getClassName()
	{
		return className;
	}
	
	public IHistoryObject getHistoryObject() throws CoreException
	{
		if ( instance != null )
			return instance;
		instance = ((IHistoryObject)configurationElement.createExecutableExtension(ATTR_CLASS));
		return instance;
	}
	
	private static String getAttribute(IConfigurationElement configurationElement, String name, String defaultValue)
	{
		String value = configurationElement.getAttribute(name);
		if ( value != null )
			return value;
		if ( defaultValue != null )
			return defaultValue;
		throw new IllegalArgumentException("Missing " + name + "attribute");
	}
}