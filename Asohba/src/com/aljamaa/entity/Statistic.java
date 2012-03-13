package com.aljamaa.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.aljamaa.server.StatisticTools;
import com.google.gwt.user.client.rpc.GwtTransient;

@Entity
public class Statistic implements Serializable{

	//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@GwtTransient
	String key;

	@Transient
	@GwtTransient
	String name;

	@Transient
	@GwtTransient
	String momin;

	@GwtTransient
	@Transient
	int count;

	@GwtTransient
	@Transient
	int moy;

	@GwtTransient
	@Transient
	char year;

	@GwtTransient
	@Transient
	byte week;
	
	//	int oCount;
	//	int nCount;
	
	@GwtTransient
	int valSum;
	String serializedData;

	@GwtTransient
	long update;
	
	@GwtTransient
	@Transient
	Date date;

	@GwtTransient
	@Transient
	HashMap<Integer, Integer> evals = new HashMap<Integer, Integer>();
//	int[][] evals = new int[7][24];
	
	@GwtTransient
	@Transient
	byte[][] groups = new byte[7][24];

	public Statistic (){
//		moy=-1;
	}

	public void init()
	{
		initStatistic(this);
	}
	
	public static void initStatistic(Statistic stat)
	{
		int d,h,g,i=0;
		char cgdh;
		int[] gdh;
		String srl = stat.getSerializedData();
		for(String snum=null;i<srl.length() ; ){
			cgdh = srl.charAt(i++);
			gdh = decodeGroupDayHour(cgdh);
			snum=stat.getSerializedData().substring(i, i+=2);
			stat.getEvals().put(gdh[1]*100+gdh[2], decodeInteger(snum));
			stat.getGroups()[gdh[1]][gdh[2]] = (byte) gdh[0];
		}
	}
	public static int decodeInteger(String code){
		char c[]=code.toCharArray();
		int integer = c[0];
		integer = integer << 16;
		integer += (int)c[1];
		return integer;
	}

	/**
	 *  g:15--11    d:10--8   h:0--7
	 * 
	 */
	public static int[] decodeGroupDayHour(char car){
		int[] hd = new int[3];
		hd[2]=(byte)car;					//hour
		hd[1]=(car<<21)>>>29;		//day
		hd[0]=car>>>11;				//group
		return hd;
	}

	@Override
	public String toString() {
		String s="";
		char[] c = serializedData.toCharArray();
		for(int i= 0; i < c.length; i++){
		//	s+="("+(int) c[i]+"-"+(c[++i]<<16+c[++i])+")";
			s+=(int)c[i]+" ";
		
		}
		return s;
	}

	public HashMap<Integer, Integer> getEvals() {
		return evals;
	}

	public void setEvals(HashMap<Integer, Integer> evals) {
		this.evals = evals;
	}

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

//	public int[][] getEvals() {
//		return evals;
//	}
//
//	public void setEvals(int[][] evals) {
//		this.evals = evals;
//	}



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
