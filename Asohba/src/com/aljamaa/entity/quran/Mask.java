package com.aljamaa.entity.quran; 

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;


@Entity
public class Mask implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	Date date;
	int page;
	String momin;
	String encodBM;
	
	@Transient
	ArrayList<BoxMask> boxMasks = new ArrayList<BoxMask>();
	
	public Mask(){
		 
	}
	
	public void encode(){
		encodBM = encodeMask(this);
	}
	public void decode(){
		boxMasks = decodeMask(encodBM);
	}
	public void addBoxMask(BoxMask b){
		boxMasks.add(b);
	}
	public static ArrayList<BoxMask> decodeMask(String enc){
//		Mask msk = new Mask();
		ArrayList<BoxMask> bms = new ArrayList<BoxMask>();
//		StringTokenizer tknzr =new StringTokenizer(enc, ";");
//		msk.setDate(new Date(Base64Utils.longFromBase64(tknzr.nextToken())));
		String[] splt = enc.split(";");
		for(int i = 0;i<splt.length; i++){
			bms.add( BoxMask.decodeBox(splt[i]));			
		}
		return bms;
	}
	
	public static  String encodeMask(Mask msk){
		StringBuffer code= new StringBuffer();
//		code.append(msk.getDate());
		for(BoxMask bm : msk.getBoxMasks()){
			code.append(bm.codeBoxMask());
			code.append(";");
		}
		return code.toString();
	}
	public BoxMask getBoxMask(int x, int y){
		for(BoxMask bm : boxMasks)
		{
			if((bm.w>=0 && bm.x<= x && bm.x+bm.w >= x) || (bm.w<0 && bm.x>= x && bm.x+bm.w <= x))
				if((bm.h>=0 && bm.y<= y && bm.y+bm.h >= y) || (bm.h<0 && bm.y>= y && bm.y+bm.h <= y))
					return bm;
		}
		return null;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getMomin() {
		return momin;
	}

	public void setMomin(String momin) {
		this.momin = momin;
	}

	public String getEncodBM() {
		return encodBM;
	}

	public void setEncodBM(String encodBM) {
		this.encodBM = encodBM;
	}

	public ArrayList<BoxMask> getBoxMasks() {
		return boxMasks;
	}
	public void setBoxMasks(ArrayList<BoxMask> boxMasks) {
		this.boxMasks = boxMasks;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	

}