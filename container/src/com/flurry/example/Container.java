package com.flurry.example;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.external.library.Resource;
import com.external.library.ResourceLibrary;

@SuppressWarnings("unused")
public class Container
{
	private final ClassLoaderFactory factory;
	private final Collection<IModule> modules = new ConcurrentLinkedQueue<IModule>();

	public Container(ClassLoaderFactory factory)
	{
		this.factory = factory;
	}

	public void loadModule(String moduleClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		ClassLoader moduleLoader = factory.factory();

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
