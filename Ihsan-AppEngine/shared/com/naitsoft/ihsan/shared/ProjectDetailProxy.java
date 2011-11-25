package com.naitsoft.ihsan.shared;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName(value = "com.naitsoft.ihsan.entity.ProjectDetail", locator = "com.naitsoft.ihsan.entity.ProjectDetailLocator")
public interface ProjectDetailProxy extends ValueProxy {

	String getEmail();

	void setEmail(String email);

}
