package com.aljamaa.entity;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class TaskSeed implements IsSerializable {
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
	private int type;
	
	@Persistent
	private int every;
	
	@Persistent
	private int priority;
	
	@Persistent
	private boolean[] param;
	
	@Persistent
	private Date start;

	@Persistent
	private Date end;
	
	@Persistent
	private Date last;
	
	@Persistent
	private int group;

	@Persistent
	private boolean alarm;

	public TaskSeed() {
	}

	public TaskSeed(String name, int type, int min, int every, int priority,
			boolean[] param, Date start, Date end, int groupId) {
		this.name = name;
		this.min = min;
		this.every = every;
		this.priority = priority;
		this.param = param;
		this.start = start;
		this.end = end;
		this.group = groupId;
		this.type = type;
	}

	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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


	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getEvery() {
		return every;
	}

	public void setEvery(int every) {
		this.every = every;
	}

	public boolean[] getParam() {
		return param;
	}

	public void setParam(boolean[] param) {
		this.param = param;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Date getLast() {
		return last;
	}

	public void setLast(Date last) {
		this.last = last;
	}

	public int getGroupId() {
		return group;
	}

	public void setGroupId(int groupId) {
		this.group = groupId;
	}

	
	

}
