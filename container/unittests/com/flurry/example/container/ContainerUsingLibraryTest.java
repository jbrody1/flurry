package com.flurry.example.container;

import com.flurry.example.container.ContainerUsingLibrary;
import com.flurry.example.container.IContainer;


public class ContainerUsingLibraryTest extends ContainerTest
{
	// fully qualified class name of the module to load
	private static final String moduleClass = flurryPrefix + "module.ModuleUsingLibrary";

	@Override
	protected String getModuleClass()
	{
		return moduleClass;
	}

	@Override
	protected IContainer buildContainer()
	{
		return new ContainerUsingLibrary();
	}
}
