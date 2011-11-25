package com.naitsoft.ihsan.entity;

import com.google.web.bindery.requestfactory.shared.Locator;


public class VolunteerDetailLocator extends Locator<VolunteerDetail, Void> {

	@Override
	public VolunteerDetail create(Class<? extends VolunteerDetail> clazz) {
		return new VolunteerDetail();
	}

	@Override
	public VolunteerDetail find(Class<? extends VolunteerDetail> clazz, Void id) {
		return create(clazz);
	}

	@Override
	public Class<VolunteerDetail> getDomainType() {
		return VolunteerDetail.class;
	}

	@Override
	public Void getId(VolunteerDetail domainObject) {
		return null;
	}

	@Override
	public Class<Void> getIdType() {
		return Void.class;
	}

	@Override
	public Object getVersion(VolunteerDetail domainObject) {
		return null;
	}

}
