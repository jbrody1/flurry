package com.flurry.example;


public class ContainerUsingInterfaceTest extends ContainerTest
{
	@Override
	protected IContainer buildContainer(IClassLoaderFactory factory)
	{
		return new ContainerUsingInterface(factory);
	}
}
