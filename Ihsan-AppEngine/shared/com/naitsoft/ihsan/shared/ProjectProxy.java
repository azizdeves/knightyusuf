package com.naitsoft.ihsan.shared;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName(value = "com.naitsoft.ihsan.entity.Project", locator = "com.naitsoft.ihsan.entity.ProjectLocator")
public interface ProjectProxy extends ValueProxy {

	String getName();

	void setName(String name);

	Long getId();

	String getOrg();

	void setOrg(String org);

	String getResume();

	void setResume(String resume);

	String getSkills();

	void setSkills(String skills);

	int getRating();

	void setRating(int rating);

	long getDetailId();

	void setDetailId(long detailId);

	public long getOrgId() ;
	public void setOrgId(long orgId) ;
}
