package com.naitsoft.ihsan.shared;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName(value = "com.naitsoft.ihsan.entity.Momin", locator = "com.naitsoft.ihsan.entity.MominLocator")
public interface MominProxy extends ValueProxy {

	String getId();

	String getName();

	void setName(String name);

	String getEmail();

	void setEmail(String email);

}
