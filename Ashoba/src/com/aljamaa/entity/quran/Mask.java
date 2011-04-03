package com.aljamaa.entity.quran;

import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import com.naitsoft.quran.shared.Base64Utils;


public class Mask{
	ArrayList<BoxMask> boxMasks = new ArrayList<BoxMask>();
	Date date;
	public void addBoxMask(BoxMask b){
		boxMasks.add(b);
	}
	public static Mask decodeMask(String enc){
		Mask msk = new Mask();
		StringTokenizer tknzr =new StringTokenizer(enc, ";");
		msk.setDate(new Date(Base64Utils.longFromBase64(tknzr.nextToken())));
		while(tknzr.hasMoreTokens()){
			msk.getBoxMasks().add( BoxMask.decodeBox(tknzr.nextToken()));
			
		}
		return msk;
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