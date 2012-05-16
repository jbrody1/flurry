package com.flurry.example;


public class ContainerUsingInterfaceTest extends ContainerTest
{
	@Override
	protected IContainer buildContainer()
	{
		return new ContainerUsingInterface();
	}
}
