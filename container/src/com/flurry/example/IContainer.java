package com.flurry.example;

public interface IContainer
{
	public void loadModule(ClassLoader moduleLoader, String moduleClass) throws Exception;
	public void clearModules();
}
