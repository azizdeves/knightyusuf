package com.aljamaa.entity.quran;

import java.util.StringTokenizer;

import com.naitsoft.quran.shared.Base64Utils;

public class BoxMask{
//	static enum Status{drawing,deactive,active};
	int x,y,w,h;
	BMaskStatus status=BMaskStatus.active;
	public BoxMask() {	}
	public BoxMask(int x2, int y2) {
		x=x2;
		y=y2;
	}
	public BoxMask(int x2, int y2, int h1, int w1) {
		x=x2;
		y=y2;
		h=h1;
		w=w1;
	}
	

	public static BoxMask decodeBox(String enc){
		StringTokenizer tknzr = new StringTokenizer(enc, "-");
		BoxMask bm=new BoxMask();
		bm.setX(Base64Utils.intFromBase64(tknzr.nextToken()));
		bm.setY(Base64Utils.intFromBase64(tknzr.nextToken()));
		bm.setW(Base64Utils.intFromBase64(tknzr.nextToken()));
		bm.setH(Base64Utils.intFromBase64(tknzr.nextToken()));
		return bm;		
	}
	public void setHeightWidth(int a,int b){
		w=a-x;
		h=b-y;
	}
	
	public BMaskStatus getStatus() {
		return status;
	}
	public void setStatus(BMaskStatus status) {
		this.status = status;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	
}