package com.flurry.example.container;

public interface IContainer
{
	public void loadModule(ClassLoader moduleLoader, String moduleClass) throws Exception;
	public void clearModules();
}
