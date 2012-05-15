package com.external.library;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ResourceLibrary
{
	private static final ConcurrentMap<Class<?>, Resource> lookup = new ConcurrentHashMap<Class<?>, Resource>();

	public static Resource getResource(Class<?> clazz)
	{
		if (!lookup.containsKey(clazz))
		{
			lookup.putIfAbsent(clazz, new Resource());
		}

		return lookup.get(clazz);
	}
}
