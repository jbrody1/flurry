package com.flurry.example;

import java.util.Collection;
import java.util.HashSet;

public class Container implements IContainer
{
	private final IClassLoaderFactory factory;
	private final Collection<ClassLoader> loaders = new HashSet<ClassLoader>();

	public Container(IClassLoaderFactory factory)
	{
		this.factory = factory;
	}

	public synchronized void loadModule(String moduleClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		ClassLoader moduleLoader = factory.factory();
		Class<?> clazz = moduleLoader.loadClass(moduleClass);
		
		// don't cast this reference to anything, not even Object
		clazz.newInstance();

		// keep a reference to the class loader so the module sticks around for a bit
		loaders.add(moduleLoader);
	}

	public void clearModules()
	{
		loaders.clear();
	}
}
