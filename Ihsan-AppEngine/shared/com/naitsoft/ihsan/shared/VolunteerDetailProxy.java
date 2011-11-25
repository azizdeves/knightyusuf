package com.naitsoft.ihsan.shared;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName(value = "com.naitsoft.ihsan.entity.VolunteerDetail", locator = "com.naitsoft.ihsan.entity.VolunteerDetailLocator")
public interface VolunteerDetailProxy extends ValueProxy {

	String getId();

}
