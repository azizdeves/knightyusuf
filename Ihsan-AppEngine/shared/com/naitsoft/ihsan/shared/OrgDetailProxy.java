package com.naitsoft.ihsan.shared;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName(value = "com.naitsoft.ihsan.entity.OrgDetail", locator = "com.naitsoft.ihsan.entity.OrgDetailLocator")
public interface OrgDetailProxy extends ValueProxy {

	String getEmail();

	void setEmail(String email);

}
