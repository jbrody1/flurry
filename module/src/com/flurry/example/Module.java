package com.flurry.example;

import com.external.library.Resource;
import com.external.library.ResourceLibrary;

@SuppressWarnings("unused")
public class Module implements IModule
{
	/**
	 * Allocate a big chunk of static memory to expose class leaks
	 * (otherwise class leaks manifest over time as PermGen errors)
	 */
	private static final byte[] bytes = new byte[1024 * 1024 * 100];

	private final Resource resource = ResourceLibrary.getResource(Module.class);

	public Module()
	{
	}
}
