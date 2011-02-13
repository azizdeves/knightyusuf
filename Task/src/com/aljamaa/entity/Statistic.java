package com.aljamaa.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.aljamaa.server.StatisticTools;

@Entity
public class Statistic {

	//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	String key;

	@Transient
	String name;

	@Transient
	String momin;

	@Transient
	int count;

	@Transient
	int moy;

	@Transient
	char year;

	@Transient
	byte week;
	//	int oCount;
	//	int nCount;
	int valSum;
	String serializedData;
	long update;
	
	@Transient
	Date date;

	@Transient
	int[][] evals = new int[7][24];
	
	@Transient
	byte[][] groups = new byte[7][24];

	public Statistic (){
		moy=-1;
	}

//	public void init()
//	{
//		if(serializedData==null)
//			return;
//		StatisticTools.initStatistic(this);		
//	}

	public String getMomin() {
		return momin;
	}
	
	public void setMomin(String momin) {
		this.momin = momin;
	}
	//	public void update(Task tsk){
	//		count ++;
	//		if(tsk.getMin()>2){	
	//			valSum += tsk.getEval();
	//			moy = valSum/count;
	//			if(tsk.getEval()>-1)		
	//				if(tsk.getMin()>tsk.getEval())
	//					nCount++;
	//				else
	//					oCount++;
	//		}
	//		
	//	}

	public String getKey() {
//		if(key ==null)
//			StatisticinitKey();
		return key;
	}



	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setKey(String pkey) {
		this.key = pkey;
	}

	public long getUpdate() {
		return update;
	}

	public void setUpdate(long update) {
		this.update = update;
	}

	public byte[][] getGroups() {
		return groups;
	}

	public void setGroups(byte[][] groups) {
		this.groups = groups;
	}

	public String getName() {
		return name;
	}

	public int[][] getEvals() {
		return evals;
	}

	public void setEvals(int[][] evals) {
		this.evals = evals;
	}



	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count += count;
	}

	public int getMoy() {
		if(moy!=-1)
			return moy;
		moy=valSum/count;
		return moy;
	}

	public void setMoy(int moy) {
		this.moy = moy;
	}

	//	public int getoCount() {
	//		return oCount;
	//	}
	//
	//	public void setoCount(int oCount) {
	//		this.oCount = oCount;
	//	}
	//
	//	public int getnCount() {
	//		return nCount;
	//	}
	//
	//	public void setnCount(int nCount) {
	//		this.nCount = nCount;
	//	}

	public int getValSum() {
		return valSum;
	}

	public void setValSum(int valSum) {
		this.valSum = valSum;
	}

	public String getSerializedData() {
		return serializedData;
	}

	public void setSerializedData(String serializedData) {
		this.serializedData = serializedData;
	}

}
