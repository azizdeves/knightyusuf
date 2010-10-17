package com.aljamaa.entity;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Task implements IsSerializable{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String name;
	
	@Persistent
	private String mominId;
	
	@Persistent
	private int min;
	
	@Persistent
	private int eval;
	
	@Persistent
	private int priority;
	
	@Persistent
	private long seedId;
	
	@Persistent
	private int group;

	@Persistent
	private Date date;

	@Persistent
	private boolean alarm;
	
	public Task() {
	}

	public Task(String name, int min, int priority, long seedId,
			Date date) {
		this.name = name;
		this.min = min;
		this.priority = priority;
		this.seedId = seedId;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getSeedId() {
		return seedId;
	}

	public void setSeedId(long seedId) {
		this.seedId = seedId;
	}

	public int getGroupId() {
		return group;
	}

	public void setGroupId(int groupId) {
		this.group = groupId;
	}

	public boolean isAlarm() {
		return alarm;
	}

	public void setAlarm(boolean alarm) {
		this.alarm = alarm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMominId() {
		return mominId;
	}

	public void setMominId(String mominId) {
		this.mominId = mominId;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getEval() {
		return eval;
	}

	public void setEval(int eval) {
		this.eval = eval;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public long getSeedKey() {
		return seedId;
	}

	public void setSeedKey(long seedKey) {
		this.seedId = seedKey;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
