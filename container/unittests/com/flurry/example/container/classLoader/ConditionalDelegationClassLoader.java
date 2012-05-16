package com.flurry.example.container.classLoader;

import java.net.URL;
import java.net.URLClassLoader;

public class ConditionalDelegationClassLoader extends URLClassLoader
{
	private final String parentPrefix;
	private final String childPrefix;

	public ConditionalDelegationClassLoader(URL[] urls, String parentPrefix)
	{
		this(urls, parentPrefix, null);
	}

	public ConditionalDelegationClassLoader(URL[] urls, String parentPrefix, String childPrefix)
	{
		super(urls);

		this.parentPrefix = parentPrefix;
		this.childPrefix = childPrefix;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException
	{
		Class<?> c = findLoadedClass(name);

		if (c == null)
		{
			boolean parentFirst = (parentPrefix == null || name.startsWith(parentPrefix)) && // load from parent
								  (childPrefix == null || !name.startsWith(childPrefix));	 // don't load from child

			if (parentFirst)
			{
				try
				{
					c = super.loadClass(name);
				}
				catch (ClassNotFoundException e)
				{
				}
			}
			
			if (c == null)
			{
				try
				{
					c = findClass(name);
				}
				catch (ClassNotFoundException e)
				{
					if (parentFirst)
					{
						throw e; // we didn't find it in the parent or the child
					}
					else
					{
						c = super.loadClass(name); // load from the parent now
					}
				}
			}
		}
		
		return c;
	}
}
