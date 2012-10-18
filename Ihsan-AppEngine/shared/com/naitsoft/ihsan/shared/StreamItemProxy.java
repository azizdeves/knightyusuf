package com.naitsoft.ihsan.shared;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName(value = "com.naitsoft.ihsan.entity.StreamItem", locator = "com.naitsoft.ihsan.entity.StreamItemLocator")
public interface StreamItemProxy extends ValueProxy {

	Long getId();

	String getSubject();

	void setSubject(String subject);

	String getActor();

	void setActor(String actor);

	int getAction();

	void setAction(int action);

	String getMetaData();

	void setMetaData(String metaData);

	Long getSubjectId();

	void setSubjectId(Long subjectId);

}
