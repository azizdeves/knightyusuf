package com.naitsoft.ihsan.entity;

import com.google.web.bindery.requestfactory.shared.Locator;


public class OrgDetailLocator extends Locator<OrgDetail, Void> {

	@Override
	public OrgDetail create(Class<? extends OrgDetail> clazz) {
		return new OrgDetail();
	}

	@Override
	public OrgDetail find(Class<? extends OrgDetail> clazz, Void id) {
		return create(clazz);
	}

	@Override
	public Class<OrgDetail> getDomainType() {
		return OrgDetail.class;
	}

	@Override
	public Void getId(OrgDetail domainObject) {
		return null;
	}

	@Override
	public Class<Void> getIdType() {
		return Void.class;
	}

	@Override
	public Object getVersion(OrgDetail domainObject) {
		return null;
	}

}
