package com.naitsoft.ihsan.entity;

import com.google.web.bindery.requestfactory.shared.Locator;


public class ProjectDetailLocator extends Locator<ProjectDetail, Void> {

	@Override
	public ProjectDetail create(Class<? extends ProjectDetail> clazz) {
		return new ProjectDetail();
	}

	@Override
	public ProjectDetail find(Class<? extends ProjectDetail> clazz, Void id) {
		return create(clazz);
	}

	@Override
	public Class<ProjectDetail> getDomainType() {
		return ProjectDetail.class;
	}

	@Override
	public Void getId(ProjectDetail domainObject) {
		return null;
	}

	@Override
	public Class<Void> getIdType() {
		return Void.class;
	}

	@Override
	public Object getVersion(ProjectDetail domainObject) {
		return null;
	}

}
