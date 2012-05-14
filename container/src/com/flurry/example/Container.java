package com.flurry.example;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.external.library.Resource;
import com.external.library.ResourceLibrary;

@SuppressWarnings("unused")
public class Container
{
	private static final String modulePath = "../module/bin/";
	private static final String flurryPrefix = "com.flurry.example.";
	private static final String moduleClass = flurryPrefix + "Module";
	private static final int bytesPerMb = 1024 * 1024;

	public static void main(String[] args) throws Exception
	{
		Container container = new Container(getClassUrls(modulePath));

		for (int i=0; i<10; i++)
		{
			// load the module
			container.loadModule(moduleClass);

			// now free the module
			container.clearModules();

			printMemoryUsage();
		}
	}

	private static URL[] getClassUrls(String classpath) throws MalformedURLException
	{
		return new URL[] { new File(classpath).toURI().toURL() };
	}

	private static void printMemoryUsage()
	{
		System.gc();
		long memUsed = Runtime.getRuntime().totalMemory();
		System.out.println(memUsed / bytesPerMb + "MB");
	}

	/*
	 * Implementation of container
	 */

	private final URL[] classUrls;
	private final Collection<IModule> modules = new ConcurrentLinkedQueue<IModule>();

	public Container(URL[] classUrls)
	{
		this.classUrls = classUrls;
	}

	public void loadModule(String moduleClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		ClassLoader moduleLoader = new URLClassLoader(classUrls, null);
		//ClassLoader moduleLoader = new URLClassLoader(classUrls, Container.class.getClassLoader());
		//ClassLoader moduleLoader = new PostDelegationClassLoader(classUrls, Container.class.getClassLoader());
		//ClassLoader moduleLoader = new FilterClassLoader(classUrls, Container.class.getClassLoader(), flurryPrefix);

		@SuppressWarnings("unchecked")
		Class<? extends IModule> clazz = (Class<? extends IModule>) moduleLoader.loadClass(moduleClass);
		IModule module = clazz.newInstance();
		modules.add(module);
		
		Resource resource = ResourceLibrary.getResource(Container.class);
	}

	public void clearModules()
	{
		modules.clear();
	}
}
