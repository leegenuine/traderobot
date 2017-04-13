package com.traderobot.ui.datawindow.datacollector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;

public class DataCollectorManager
{	
	private static final String TAG_EXTENSION_POINT = "dataCollectors";
	private static final String TAG_EXTENSION_POINT_ELEMENT = "dataCollector";
	
	private static DataCollectorManager singleton;	
	private static Hashtable<String, DataCollectorDescriptor> extensionLogicTable;
	
	private DataCollectorManager()
	{	
		initExtensionLogics();
	}
	
	public static DataCollectorManager getInstance()
	{
		if ( singleton != null )
			return singleton;
		
		singleton = new DataCollectorManager();
		return singleton;
	}
	
	private static void initExtensionLogics()
	{	
		if ( extensionLogicTable == null )
			extensionLogicTable = new Hashtable<String, DataCollectorDescriptor>();		
		IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint("com.traderobot.ui.datawindow", TAG_EXTENSION_POINT).getExtensions();		
		for(int i=0; i<extensions.length; i++)
		{
			IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
			for(int j=0; j<configElements.length; j++)
			{
				if ( configElements[j].getName().equals(TAG_EXTENSION_POINT_ELEMENT) )
				{
					DataCollectorDescriptor desc = new DataCollectorDescriptor(configElements[j]);					
					extensionLogicTable.put(desc.getName(), desc);
				}
			}
		}
	}
	
	public DataCollectorDescriptor[] getDescriptors()
	{
		ArrayList<DataCollectorDescriptor> list = new ArrayList<DataCollectorDescriptor>(extensionLogicTable.values());
		list.sort(new Comparator<DataCollectorDescriptor>() {
			@Override
			public int compare(DataCollectorDescriptor o1, DataCollectorDescriptor o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return list.toArray(new DataCollectorDescriptor[list.size()]);
	}
	
	public DataCollectorDescriptor getDescriptor(String name)
	{
		return extensionLogicTable.get(name);
	}
}