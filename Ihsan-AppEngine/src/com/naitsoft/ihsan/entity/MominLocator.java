package com.naitsoft.ihsan.entity;

import com.google.web.bindery.requestfactory.shared.Locator;


public class MominLocator extends Locator<Momin, Void> {

	@Override
	public Momin create(Class<? extends Momin> clazz) {
		return new Momin();
	}

	@Override
	public Momin find(Class<? extends Momin> clazz, Void id) {
		return create(clazz);
	}

	@Override
	public Class<Momin> getDomainType() {
		return Momin.class;
	}

	@Override
	public Void getId(Momin domainObject) {
		return null;
	}

	@Override
	public Class<Void> getIdType() {
		return Void.class;
	}

	@Override
	public Object getVersion(Momin domainObject) {
		return null;
	}

}
