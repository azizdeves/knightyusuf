package com.naitsoft.ihsan.entity;

import com.google.web.bindery.requestfactory.shared.Locator;


public class StreamItemLocator extends Locator<StreamItem, Void> {

	@Override
	public StreamItem create(Class<? extends StreamItem> clazz) {
		return new StreamItem();
	}

	@Override
	public StreamItem find(Class<? extends StreamItem> clazz, Void id) {
		return create(clazz);
	}

	@Override
	public Class<StreamItem> getDomainType() {
		return StreamItem.class;
	}

	@Override
	public Void getId(StreamItem domainObject) {
		return null;
	}

	@Override
	public Class<Void> getIdType() {
		return Void.class;
	}

	@Override
	public Object getVersion(StreamItem domainObject) {
		return null;
	}

}
