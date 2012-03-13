package com.aljamaa.entity.quran;


import com.aljamaa.shared.Base64Utils;


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
//		StringTokenizer tknzr = new StringTokenizer(enc, "-");
		String[] splt = enc.split("-");
		BoxMask bm=new BoxMask();
		bm.setX(Base64Utils.intFromBase64(splt[0]));
		bm.setY(Base64Utils.intFromBase64(splt[1]));
		bm.setW(Base64Utils.intFromBase64(splt[2]));
		bm.setH(Base64Utils.intFromBase64(splt[3]));
		return bm;		
	}
	
	public String codeBoxMask() {		
		return Base64Utils.toBase64(x)+"-"+Base64Utils.toBase64(y)+"-"+Base64Utils.toBase64(w)+"-"+Base64Utils.toBase64(h);
	}
	
	public void setHeightWidth(int a,int b){
		w=a-x;
		h=b-y;
	}
	
	public String toString (){
		return "x:"+x+" y:"+y+" h:"+h+" w:"+w;
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