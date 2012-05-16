package com.flurry.example.module;

import com.flurry.example.IModule;


@SuppressWarnings("unused")
public class Module implements IModule
{
	/**
	 * Allocate a big chunk of static memory to expose class leaks
	 * (otherwise class leaks manifest over time as PermGen errors)
	 */
	private static final byte[] bytes = new byte[1024 * 1024 * 50];
}
