package com.traderobot.ui.datawindow;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.EventAdmin;

public class Activator implements BundleActivator {

	public static String PLUGIN_ID = "com.traderobot.ui.datawindow";
	
	private static Service service;
	private static EventAdmin eventAdmin;
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}
	
	public static EventAdmin getEventAdmin() {
		return eventAdmin;
	}
	
	public static Service getService() {
		return service;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {		
		Activator.context = bundleContext;
		service = Service.getInstance();
		ServiceReference<EventAdmin> reference = context.getServiceReference(EventAdmin.class);
		eventAdmin = context.getService(reference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		if ( service != null)
			service.stop();
	}

}
