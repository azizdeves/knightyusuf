package com.aljamaa.entity;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.datepicker.client.CalendarUtil;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Task implements IsSerializable, Comparable{
	static public  final int EVERY_DAY = 1;
	static public  final int EVERY_WEEK = 2;
	static public  final int EVERY_MONTH =3 ;
	static public  final int EVERY_YEAR = 4;
	
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
	private Long seedId;
	
	@Persistent
	private int group;
	
	@Persistent
	private int duration;

	@Persistent
	private Date date;

	@Persistent
	private boolean alarm;
	
	public Task() {
		eval=-1;
	}

	public Task(String name, int min, int priority, Long seedId,String momin,
			Date date) {
		this();
		this.name = name;
		this.min = min;
		this.priority = priority;
		this.seedId = seedId;
		this.date = date;
		this.mominId=momin;
	}
	public Task(String name, int min, int priority, Long seedId,
			Date date) {
		this.name = name;
		this.min = min;
		this.priority = priority;
		this.seedId = seedId;
		this.date = date;
	}
	
	public Task(TaskSeed seed){
		this(seed.getName(),
				seed.getMin(),
				seed.getPriority(),
				seed.getId(), 
				seed.getMominId(),
				null);
	}

	/**
	 * obtenir la date du jour i suivant la date d
	 * @param d
	 * @param i numero dur jour dans la semaine(1-7)
	 * @return
	 */
	@SuppressWarnings("deprecation")
	static public Date moveToDay(Date d,int i)
	{
		if(i>0){
			i--;
			CalendarUtil.addDaysToDate(d, (i>=d.getDay()?0:7)+i-d.getDay());
		}
		else {
			i=-i-1;
			
			CalendarUtil.addDaysToDate(d, (i<=d.getDay()?0:-7)+i-d.getDay());
		}
			
		return d;
			
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSeedId() {
		return seedId;
	}

	public void setSeedId(Long seedId) {
		this.seedId = seedId;
	}



	@Override
	public boolean equals(Object obj) {
		return obj != null && this.id == ((Task) obj).getId();
	}	

	@Override
	public int hashCode() {		
		return 0;
	}

	@Override
	public String toString() {		
		return "id: "+id+"  name: "+name+"  eval:"+eval+" min:"+min;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
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

//	public long getSeedKey() {
//		return seedId;
//	}
//
//	public void setSeedKey(long seedKey) {
//		this.seedId = seedKey;
//	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = CalendarUtil.copyDate(date);
	}

	@Override
	public int compareTo(Object o) {
		return id.compareTo(((Task) o).getId());
	}
	
	
	

}
