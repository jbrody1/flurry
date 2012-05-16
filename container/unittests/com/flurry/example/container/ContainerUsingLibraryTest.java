package com.flurry.example.container;

public class ContainerUsingLibraryTest extends ContainerUsingInterfaceTest
{
	// fully qualified class name of the module to load
	private static final String moduleClass = "com.flurry.example.module.ModuleUsingLibrary";

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
