package com.external.library;

import java.util.HashMap;
import java.util.Map;

public class ResourceLibrary
{
	public static class Resource
	{
	}

	// simulate a 3rd party library that holds references to its clients
	private static final Map<Class<?>, Resource> lookup = new HashMap<Class<?>, Resource>();

	public static synchronized Resource getResource(Class<?> clazz)
	{
		if (!lookup.containsKey(clazz))
		{
			lookup.put(clazz, new Resource());
		}

		return lookup.get(clazz);
	}
}
