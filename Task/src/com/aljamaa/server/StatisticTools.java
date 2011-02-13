package com.aljamaa.server;

import java.util.Calendar;
import java.util.Date;

import com.aljamaa.entity.Statistic;
import com.aljamaa.entity.Task;


public class StatisticTools {
//	final static String alphaBits[] ={	"0000"	 , "0001"	 , "0010"	 , "0011"	 , "0100"	 , "0101"	 , "0110"	 , "0111"	 , "1000"	 , "1001"
//		, "1010"	 , "1011"	 , "1100"	 , "1101"	 , "1110"	 , "1111"};
//	final static char alpha[]={ 				'0',		'1',			'2',			'3',				'4',			'5',			'6',			'7',			'8',			'9',
//		'u',				'k',				'm',			'?',				'n',			'y'};

	public static String encodeInteger(int num){
		char a,b;
		a=(char) (num>>>16);
		b=(char) num;
		return ""+a+b;
	}

	public static int decodeInteger(String code){
		char c[]=code.toCharArray();
		int integer = c[0];
		integer = integer << 16;
		integer += c[1];
		return integer;
	}

	/**
	 *  encode group , hour et day dans un caractère = g5d3h5
	 *  g:15--11    d:10--8   h:0--7
	 */
	public static char encodeDayGroupHour(int d1, int h1, int g){
		char car = (char) (d1<<8);
		car+=g<<11;
		car += (char) h1;
		return car;
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

	public static Statistic getStatistic(Task ... tsks)
	{
		int[][] evals = new int[7][24];
		byte[][] groups = new byte[7][24];
		int day ,hour;
		for(int d=0; d<7;d++)
			for(int h=0; h<24;h++)
				evals[d][h]=Integer.MIN_VALUE+1;
		for(Task tsk : tsks)
		{
			day=tsk.getDate().getDay();
			hour=tsk.getDate().getHours();
			// type boolean
			if(tsk.getMin()<1){
				if(tsk.getEval()==-1){
					evals[day][hour]=Integer.MIN_VALUE;
					groups[day][hour]=(byte) tsk.getGroup();
				}
				else
					if(tsk.getEval()==1){
						evals[day][hour]=Integer.MAX_VALUE;
						groups[day][hour]=(byte) tsk.getGroup();
					}
				//					else//?
				//						evals[day][hour]=Integer.MIN_VALUE+1;
			}// type numeric
			else{
				// if ?
				if(tsk.getEval()==-1)
					evals[day][hour]=Integer.MIN_VALUE+1;
				if(tsk.getEval()>=tsk.getMin()){
					evals[day][hour]=tsk.getEval();
					groups[day][hour]=(byte) tsk.getGroup();
				}
				else {
					evals[day][hour]=-tsk.getEval();
					groups[day][hour]=(byte) tsk.getGroup();
				}
			}

		}
		Statistic stat = new  Statistic();
		int valSum=0;
		stat.setCount(tsks.length);
		stat.setValSum(valSum);
		StringBuffer buf = new StringBuffer();
		for(int d=0; d<7;d++){
			for(int h=0; h<24;h++){
				if(evals[d][h]==Integer.MIN_VALUE+1)
					continue;
				buf.append(encodeDayGroupHour(d, h, groups[d][h]));
				buf.append(encodeInteger(evals[d][h]));							
			}
		}
		stat.setSerializedData(buf.toString());
		return stat;		
	}
	/**
	 * renseigne evals[] , done[] à partir de serielizedData
	 */
	public static void initStatistic(Statistic stat)
	{
		int d,h,g,i=0;
		char cgdh;
		int[] gdh;
		String srl = stat.getSerializedData();
		for(String snum=null;i<28 ; ){
			cgdh = srl.charAt(i++);
			gdh = decodeGroupDayHour(cgdh);
			snum=stat.getSerializedData().substring(i, i+=2);
			stat.getEvals()[gdh[1]][gdh[2]]=decodeInteger(snum);
			stat.getGroups()[gdh[1]][gdh[2]] = (byte) gdh[0];
		}

	}
	public  static void main(String arg[]){
		//	while(true)
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.WEEK_OF_YEAR, 6);
		cal.set(Calendar.YEAR, 2011);
		cal.get(Calendar.MONTH);
		int a[]= decodeGroupDayHour(encodeDayGroupHour(6, 23,9));
		Integer in = new Integer(1987654147);
		encodeInteger(1987654147);

		Task t = new Task("name", 100, 1, null, new Date());
		Task tt = new Task("name", 100, 1, null, new Date());
		Task ts = new Task("name", -1 , 1, null, new Date());
		Task tr = new Task("name", -1 , 1, null, new Date());
		t.setEval(150);
		tt.setEval(987654);
		ts.setEval(0);
		tr.setEval(1);
		Statistic stat = getStatistic(t,tt,ts,tr);
		//		stat.init();
		//			decondenseNbr(decodeCondsNbr(deserialize(serialize(encdCondesNbr(StatisticTools.condenseNumber(9876))))));
	}

//	public static Statistic getStatistics(Task ... tsks){
//		int evals[] = new int[7];
//		// ? -1    y 0      n1
//		char[] done = new char[7];
//		for(int i= 0;i<7;i++)done[i]= '?';
//		int day ;
//		for(Task tsk : tsks)
//		{
//			day=tsk.getDate().getDay();
//			// type boolean
//			if(tsk.getMin()<2){
//				setDone(done,day,tsk.getEval());
//			}// type numeric
//			else{
//				// if ?
//				if(tsk.getEval()==-1){
//					setDone(done,day,-1);
//					continue;
//				}else{
//					if(tsk.getEval()>=tsk.getMin())
//						setDone(done, day, 0);
//					else
//						setDone(done, day, 1);
//
//					evals[day]+=tsk.getEval();
//				}
//			}
//		}
//		Statistic stat = new  Statistic();
//		int valSum=0;
//		stat.setCount(tsks.length);
//		stat.setValSum(valSum);
//		StringBuffer buf = new StringBuffer();
//		StringBuffer doneBuf = new StringBuffer();
//		for(int i=0; i<7;i++){
//			valSum+=evals[i];
//			buf.append(encodeInteger(evals[i]));		
//			doneBuf.append(done[i]);
//			//			doneBuf.append(encodeDone(done[i]));
//		}
//		//		doneBuf.append("00");
//		buf.append(doneBuf);
//		stat.setSerializedData(buf.toString());
//		//		stat.setSerializedData(serialize(buf.toString()));
//		return stat;
//	}
//	/**
//	 * renseigne evals[] , done[] à partir de serielizedData
//	 */
//	public static void initStatistic(Statistic stat)
//	{
//		int day = 0; int i =0;
//		for(String cnum=null;i<28 ; ){
//			cnum=stat.getSerializedData().substring(i, i+=4);
//			stat.getEvals()[day++]=decodeInteger(cnum);
//		}
//		for(day=0;i<35;){
//			stat.getDone()[day++]=stat.getSerializedData().charAt(i++);
//		}
//	}
//	/**
//	 * dans le cas d'un boolean y+n=n ; ?+n = n;  y+?=y
//	 */
//	private static void setDone(char[] done , int i , int val)
//	{
//		// ? -1    y 0      n1
//		if(val==1){
//			done[i]='n';
//			return ;
//		}
//		if(val ==0 && done[i]=='?'){
//			done[i]='y';
//			return;
//		}
//	}
	//	private static void setEval(int[] done , int i , int val)
	//	{
	//		// ? -1    y -2      n-3
	//		if(val==-3){
	//			done[i]=val;
	//			return ;
	//		}
	//		if(val ==-2 && done[i]==-1){
	//			done[i]=-2;
	//			return;
	//		}
	//		if(val == -1 && done[i]==0)
	//			done[i]=-1;
	//	}

	//
	//	/**
	//	 * 9871 ==> 9k87 
	//	 */
	//	private static String condenseNumber(int num){
	//		char unit='u';
	//		String  sn="";
	//		if(num > 999999)
	//		{
	//			unit='m';
	//			sn= String.valueOf((double)(num / 1000000.0));
	//		}else 
	//			if(num > 999)
	//			{
	//				unit='k';
	//				sn= String.valueOf((double)(num / 1000.0));
	//			}
	//			else 
	//				sn = num+".0";
	//			if(sn.length()>4)
	//				sn = sn.substring(0,4); 
	//			else
	//				sn='0'+sn;
	//			sn=sn.replace('.',unit);
	//			return sn;		
	//	}
	//	/**
	//	 * 9k87 ==> 9870
	//	 */
	//	public static int decondenseNbr(String cdsNum){
	//		int nbr=0;
	//		int multi = 1;
	//		int idx ;
	//		if((idx = cdsNum.indexOf('k'))!=-1)
	//			multi = 1000;
	//		else
	//				if((idx = cdsNum.indexOf('m'))!=-1)
	//					multi = 1000000;
	//		cdsNum = cdsNum.replaceFirst("[k,m,u]", ".");
	//		nbr = (int) (Float.parseFloat(cdsNum)*multi);
	//		return nbr;
	//	}
	//	
	//	public static String encodeChar(char carac){
	//		for(int i = 0;i<alpha.length;i++)
	//		{
	//			if(carac == alpha[i])
	//				return alphaBits[i];
	//		}
	//		return null;
	//	}
	//	public static char decodeChar(String bits){
	//		
	//		for(int i = 0;i<alphaBits.length;i++)
	//		{
	//			if(alphaBits[i].equals(bits))
	//				return alpha[i];
	//		}
	//		return ' ';
	//	}
	//
	//	/**
	//	 * 9k ==> 10011011
	//	 */
	//	public static String encdCondesNbr(String cnum){
	//		StringBuffer encd=new StringBuffer();
	//		for(int i=0;i<cnum.length();i++)
	//		{
	//			encd.append(encodeChar(cnum.charAt(i)));
	//		}
	//		return encd.toString() ;
	//	}
	//	/**
	//	 * 9870 ==> 101100101
	//	 */
	//	public static String encodeNbr(int num){
	//		return encdCondesNbr(condenseNumber(num));
	//	}
	//	
	//	/**
	//	 *	10011011 ==>  9k
	//	 */
	//	public static String decodeCondsNbr(String cnum){
	//		String dcd = "";
	//		for(int i=0; i<cnum.length();){
	//			dcd+=decodeChar(cnum.substring(i,i+=4));
	//		}
	//		return dcd;
	//	}
	//	public static String encodeDone(char done){
	//		if(done == 'y')
	//			return "01";
	//		if(done == 'n')
	//			return "10";
	//		return "00";
	//	}
	//	public static char decodeDone (String code)
	//	{
	//		if("01".equals(code))
	//			return 'y';
	//		if("10".equals(code))
	//			return 'n';
	//		return '?';
	//	}
	//	
	//	public static String serialize(String bits)
	//	{
	//		int begin , end;
	//		char c;
	//		StringBuffer buf=new   StringBuffer();
	//		for(int i=0 ;i<bits.length();){
	//			begin = i; i +=16;
	//			end = i<bits.length()? i : bits.length();
	//			c = (char) Integer.valueOf(bits.substring(begin , end ), 2).intValue();
	//			buf.append(c);
	//		}
	//		return buf.toString(); 
	//	}
	//	/**
	//	 * ç_èç_ ==> 0101101101
	//	 */
	//	public static String deserialize(String srlz){
	//		StringBuffer bits= new StringBuffer();
	//		char carac;
	//		for(int i = 0; i<srlz.length();i++){
	//			carac = srlz.charAt(i);
	//			bits.append(Integer.toBinaryString(carac));			
	//		}		
	//		return bits.toString();
	//	}
	//	
}
//switch(carac){
//case '0' : r=alphaBits[0]; break;
//case '1' : r=alphaBits[1]; break;
//case '2' : r=alphaBits[2]; break;
//case '3' : r=alphaBits[3]; break;
//case '4' : r=alphaBits[4]; break;
//case '5' : r=alphaBits[5]; break;
//case '6' : r=alphaBits[6]; break;
//case '7' : r=alphaBits[7]; break;
//case '8' : r=alphaBits[8]; break;
//case '9' : r=alphaBits[9]; break;
//case 'u' : r=alphaBits[10]; break;
//case 'k' : r=alphaBits[11]; break;
//case 'm' : r=alphaBits[12]; break;
//case '?' : r=alphaBits[13]; break;
//case 'n' : r=alphaBits[14]; break;
//case 'y' : r=alphaBits[15]; break;
//}
//return r;