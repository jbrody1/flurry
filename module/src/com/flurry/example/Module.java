package com.flurry.example;


@SuppressWarnings("unused")
public class Module implements IModule
{
	/**
	 * Allocate a big chunk of static memory to expose class leaks
	 * (otherwise class leaks manifest over time as PermGen errors)
	 */
	private static final byte[] bytes = new byte[1024 * 1024 * 50];
}
