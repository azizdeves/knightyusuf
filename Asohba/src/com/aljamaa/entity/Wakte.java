package com.aljamaa.entity;

public class Wakte {

	long time;
	int sec; //1 2
	int min;	//3 4
	int hour;// 5 6	
	int dayWeek;// 7
	int day;//8 9
	int month;//10 11 
	int week;//12 13
	int year; //14 17
	
	public void expand(){
		year = (int) (time/10*13);
		week = (int) (time/10*11)-year*10;
		//month = (int)(time / )
	//	week = (int) (time>> 5);
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getSec() {
		return sec;
	}
	public void setSec(int sec) {
		this.sec = sec;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getDayWeek() {
		return dayWeek;
	}
	public void setDayWeek(int dayWeek) {
		this.dayWeek = dayWeek;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
}
