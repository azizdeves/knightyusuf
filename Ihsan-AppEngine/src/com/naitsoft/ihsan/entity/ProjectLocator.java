package com.naitsoft.ihsan.entity;

import com.google.web.bindery.requestfactory.shared.Locator;


public class ProjectLocator extends Locator<Project, Void> {

	@Override
	public Project create(Class<? extends Project> clazz) {
		return new Project();
	}

	@Override
	public Project find(Class<? extends Project> clazz, Void id) {
		return create(clazz);
	}

	@Override
	public Class<Project> getDomainType() {
		return Project.class;
	}

	@Override
	public Void getId(Project domainObject) {
		return null;
	}

	@Override
	public Class<Void> getIdType() {
		return Void.class;
	}

	@Override
	public Object getVersion(Project domainObject) {
		return null;
	}

}
