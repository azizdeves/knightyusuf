package com.naitsoft.ihsan.entity;

import com.google.web.bindery.requestfactory.shared.Locator;


public class OrganizationLocator extends Locator<Organization, Void> {

	@Override
	public Organization create(Class<? extends Organization> clazz) {
		return new Organization();
	}

	@Override
	public Organization find(Class<? extends Organization> clazz, Void id) {
		return create(clazz);
	}

	@Override
	public Class<Organization> getDomainType() {
		return Organization.class;
	}

	@Override
	public Void getId(Organization domainObject) {
		return null;
	}

	@Override
	public Class<Void> getIdType() {
		return Void.class;
	}

	@Override
	public Object getVersion(Organization domainObject) {
		return null;
	}

}
