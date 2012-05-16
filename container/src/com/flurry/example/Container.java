package com.flurry.example;

import java.util.Collection;
import java.util.HashSet;

public class Container implements IContainer
{
	private final Collection<ClassLoader> loaders = new HashSet<ClassLoader>();

	public synchronized void loadModule(ClassLoader moduleLoader, String moduleClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		Class<?> clazz = moduleLoader.loadClass(moduleClass);
		
		// create the module, but don't do anything with it
		clazz.newInstance();

		// keep a reference to the class loader so the module sticks around for a bit
		loaders.add(moduleLoader);
	}

	public synchronized void clearModules()
	{
		loaders.clear();
	}
}
