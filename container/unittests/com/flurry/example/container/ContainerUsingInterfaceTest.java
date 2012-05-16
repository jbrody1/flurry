package com.flurry.example.container;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.flurry.example.container.classLoader.ConditionalDelegationClassLoader;
import com.flurry.example.container.classLoader.PostDelegationClassLoader;
import com.flurry.example.container.classLoader.StandAloneClassLoader;

public class ContainerUsingInterfaceTest extends ContainerTest
{
	// package prefix under which all internal code can be found
	private static final String flurryPrefix = "com.flurry.example.";

	@Override
	protected URL[] buildClassPath() throws MalformedURLException
	{
		return new URL[] { URLUtils.buildJarUrl(moduleJar),		// required
						   URLUtils.buildJarUrl(apiJar) };		// not required, but exposes a ClassCastException with post-delegation
	}

	@Override
	protected IContainer buildContainer()
	{
		return new ContainerUsingInterface();
	}

	@Test
	public void testStandAloneClassLoader() throws Exception
	{
		test(new IClassLoaderFactory()
    	{
			public ClassLoader factory() throws MalformedURLException
			{
				// don't load any classes from the parent
				return new StandAloneClassLoader(buildClassPath());
			}
		});
	}

	@Test
	public void testPostDelegationClassLoader() throws Exception
	{
		test(new IClassLoaderFactory()
    	{
			public ClassLoader factory() throws MalformedURLException
			{
				// load classes from the child before the parent
				return new PostDelegationClassLoader(buildClassPath());
			}
		});
	}

	@Test
	public void testConditionalDelegationClassLoader() throws Exception
	{
		test(new IClassLoaderFactory()
    	{
			public ClassLoader factory() throws MalformedURLException
			{
				// load Flurry classes from the parent before the child,
				// all other from the child before the parent
				return new ConditionalDelegationClassLoader(buildClassPath(), flurryPrefix);
			}
		});
	}
}
