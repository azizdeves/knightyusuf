package com.naitsoft.ihsan.shared;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName(value = "com.naitsoft.ihsan.entity.Project", locator = "com.naitsoft.ihsan.entity.ProjectLocator")
public interface ProjectProxy extends ValueProxy {

	String getId();

	String getName();

	void setName(String name);

}
