package com.naitsoft.ihsan.shared;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName(value = "com.naitsoft.ihsan.entity.Organization", locator = "com.naitsoft.ihsan.entity.OrganizationLocator")
public interface OrganizationProxy extends ValueProxy {

	String getName();

	void setName(String name);

	Long getId();

	String getResume();

	void setResume(String resume);

	String getInterest();

	void setInterest(String interest);

	int getRating();

	void setRating(int rating);

	long getDetailId();

	void setDetailId(long detailId);

}
