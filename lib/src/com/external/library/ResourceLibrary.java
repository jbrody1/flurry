package com.external.library;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ResourceLibrary
{
	private static final ConcurrentMap<Class<?>, Resource> cache = new ConcurrentHashMap<Class<?>, Resource>();

	public static Resource getResource(Class<?> clazz)
	{
		if (!cache.containsKey(clazz))
		{
			cache.putIfAbsent(clazz, new Resource());
		}

		return cache.get(clazz);
	}
}
