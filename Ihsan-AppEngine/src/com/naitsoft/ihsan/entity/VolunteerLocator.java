package com.naitsoft.ihsan.entity;

import com.google.web.bindery.requestfactory.shared.Locator;


public class VolunteerLocator extends Locator<Volunteer, Void> {

	@Override
	public Volunteer create(Class<? extends Volunteer> clazz) {
		return new Volunteer();
	}

	@Override
	public Volunteer find(Class<? extends Volunteer> clazz, Void id) {
		return create(clazz);
	}

	@Override
	public Class<Volunteer> getDomainType() {
		return Volunteer.class;
	}

	@Override
	public Void getId(Volunteer domainObject) {
		return null;
	}

	@Override
	public Class<Void> getIdType() {
		return Void.class;
	}

	@Override
	public Object getVersion(Volunteer domainObject) {
		return null;
	}

}
