package com.flurry.example;

import java.util.Collection;
import java.util.HashSet;

public class ContainerUsingInterface implements IContainer
{
	private final IClassLoaderFactory factory;
	private final Collection<IModule> modules = new HashSet<IModule>();

	public ContainerUsingInterface(IClassLoaderFactory factory)
	{
		this.factory = factory;
	}

	public synchronized void loadModule(String moduleClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		ClassLoader moduleLoader = factory.factory();

		@SuppressWarnings("unchecked")
		Class<? extends IModule> clazz = (Class<? extends IModule>) moduleLoader.loadClass(moduleClass);
		IModule module = clazz.newInstance();

		// keep a reference to the module interface; we can communicate with it via the java api
		modules.add(module);
	}

	public void clearModules()
	{
		modules.clear();
	}
}
