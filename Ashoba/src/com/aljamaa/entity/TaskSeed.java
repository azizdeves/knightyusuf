package com.aljamaa.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.datepicker.client.CalendarUtil;

@Entity
public class TaskSeed implements IsSerializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	private String name;
	
	
	private String mominId;
	
	
	private int min;
	
	
	private int type;
	
	
	private int every;
	
	
	private int priority;
	/**
	 * si le type du seed est monthly alors:<br>
	 * param[0]=true --> repeat by day of the week<br>
	 * param[0]=false --> repeat by day of the month
	 */
	@Basic(fetch=FetchType.EAGER)
	private boolean[] param;
	
	
	private Date start;

	
	private Date end;
	
	
	private Date last;
	
	
	private Date update;

	
	private int group;

	
	private boolean alarm;
	
	
	private int duration;

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
		this.last=start;
	}

	public void setTaskAttributes(Task task){
		this.name=task.getName();
		this.min = task.getMin();
		this.priority = task.getPriority();
		this.group=task.getGroup();
		this.duration=task.getDuration();
		this.start = CalendarUtil.copyDate(task.getDate());
		this.last = CalendarUtil.copyDate(task.getDate());
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

	/**
	 * si le type du seed est monthly alors:<br>
	 * param[0]=true --> repeat by day of the week<br>
	 * param[0]=false --> repeat by day of the month
	 */
	public boolean[] getParam() {
		return param;
	}

	/**
	 * si le type du seed est monthly alors:<br>
	 * param[0]=true --> repeat by day of the week<br>
	 * param[0]=false --> repeat by day of the month
	 */
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

	public void setUpdate(Date update) {
		this.update = update;
	}

	public Date getUpdate() {
		return update;
	}

	
	public String toString(){
		return "id: "+id+" name: "+name+" Up:"+update+"  Last: "+last;
	}

}
