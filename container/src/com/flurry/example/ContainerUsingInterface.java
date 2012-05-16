package com.flurry.example;

import java.util.Collection;
import java.util.HashSet;

public class ContainerUsingInterface implements IContainer
{
	private final Collection<IModule> modules = new HashSet<IModule>();

	public synchronized void loadModule(ClassLoader moduleLoader, String moduleClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		@SuppressWarnings("unchecked")
		Class<? extends IModule> clazz = (Class<? extends IModule>) moduleLoader.loadClass(moduleClass);
		IModule module = clazz.newInstance();

		// keep a reference to the module interface; we can communicate with it via the java api
		modules.add(module);
	}

	public synchronized void clearModules()
	{
		modules.clear();
	}
}
