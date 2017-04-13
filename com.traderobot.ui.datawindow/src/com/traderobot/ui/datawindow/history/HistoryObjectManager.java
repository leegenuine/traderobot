package com.traderobot.ui.datawindow.history;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;

public class HistoryObjectManager
{	
	private static final String TAG_EXTENSION_POINT = "historyObjects";
	private static final String TAG_EXTENSION_POINT_ELEMENT = "historyObject";
	
	private static HistoryObjectManager singleton;	
	private static Hashtable<String, HistoryObjectDescriptor> extensionLogicTable;
	
	private HistoryObjectManager()
	{	
		initExtensionLogics();
	}
	
	public static HistoryObjectManager getInstance()
	{
		if ( singleton != null )
			return singleton;
		
		singleton = new HistoryObjectManager();
		return singleton;
	}
	
	private static void initExtensionLogics()
	{	
		if ( extensionLogicTable == null )
			extensionLogicTable = new Hashtable<String, HistoryObjectDescriptor>();		
		IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint("com.traderobot.ui.datawindow", TAG_EXTENSION_POINT).getExtensions();		
		for(int i=0; i<extensions.length; i++)
		{
			IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
			for(int j=0; j<configElements.length; j++)
			{
				if ( configElements[j].getName().equals(TAG_EXTENSION_POINT_ELEMENT) )
				{
					HistoryObjectDescriptor desc = new HistoryObjectDescriptor(configElements[j]);					
					extensionLogicTable.put(desc.getName(), desc);
				}
			}
		}
	}
	
	public HistoryObjectDescriptor[] getDescriptors()
	{
		ArrayList<HistoryObjectDescriptor> list = new ArrayList<HistoryObjectDescriptor>(extensionLogicTable.values());
		list.sort(new Comparator<HistoryObjectDescriptor>() {
			@Override
			public int compare(HistoryObjectDescriptor o1, HistoryObjectDescriptor o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return list.toArray(new HistoryObjectDescriptor[list.size()]);
	}
	
	public HistoryObjectDescriptor getDescriptor(String name)
	{
		return extensionLogicTable.get(name);
	}
}