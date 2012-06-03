package naitsoft.android.quran;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListMarksActivity extends Activity {

	private LayoutInflater mInflater;
	private ListMarkAdapter markAdapter;
	private ListView listMarkView;
	static Context ctx;
	int currAya;
	int currSura;

	public ListMarksActivity(){
		ctx = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = (LayoutInflater) this.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		markAdapter = new ListMarkAdapter(this);
		setContentView(R.layout.layout_mark);
		listMarkView = (ListView) findViewById(R.id.listMarkView);
		listMarkView.setAdapter(markAdapter);
//		listMarkView.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
//				sendResult(pos);
//			}
//		});
//		Button addMarkBtn = (Button) findViewById(R.id.addMarkBtn);
//		ImageButton addMarkBtn = (ImageButton) findViewById(R.id.imgMarkBtn);
//		addMarkBtn.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				markAdapter.addMark('*', currSura, currAya);
//				listMarkView.setAdapter(markAdapter);
//			}
//		});
		Bundle bund = getIntent().getExtras();
		if(bund != null){
			currAya = bund.getInt("aya");
			currSura = bund.getInt("sura");
		}
	
	}
	
	protected void sendResult(int pos) {
		Intent intent = new Intent();
		Mark mrk =  markAdapter.getMark(pos);
		intent.putExtra("sura",mrk.sura);
		intent.putExtra("aya", mrk.aya);
		setResult(QuranDroidActivity.MARK_CODE, intent);
		finish();		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public LayoutInflater getmInflater() {
		return mInflater;
	}

	public void setmInflater(LayoutInflater mInflater) {
		this.mInflater = mInflater;
	}

	public void update() {
		listMarkView.setAdapter(markAdapter);
	}

//	public DataBaseHelper getMyDbHelper() {
//		return myDbHelper;
//	}
//
//	public void setMyDbHelper(DataBaseHelper myDbHelper) {
//		this.myDbHelper = myDbHelper;
//	}
	
}



class ListMarkAdapter extends BaseAdapter implements ListAdapter 
{
//	private LayoutInflater mInflater;
	private  DataBaseHelper myDbHelper;
	private ArrayList<Mark> marks ;
	private 
	int size;
	private ListMarksActivity listeMarkActivity;
	public ListMarkAdapter(ListMarksActivity activ)
	{		listeMarkActivity = activ; 
			//myDbHelper = activ.getMyDbHelper();
			initDB();
			loadMarks();
	}
	
	public Mark getMark(int pos) {
		
		return marks.get(pos);
	}

	private void loadMarks(){
		Cursor cur = myDbHelper.getMarks();
		marks = new  ArrayList<Mark>(cur.getCount());
		if(cur.moveToFirst()){			//TODO cur empty		
			Mark mrk ;
			do{
				marks.add(new Mark(cur));
			}while(cur.moveToNext());
//			size = cur.getCount();
		}
		
	}
	public void addMark(char type , int sura , int aya){
		myDbHelper.addMark(String.valueOf(type), sura, aya);
		loadMarks();
//		marks.add(new Mark("+", 10, 3, 20));
//		size = marks.size();
		//this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return marks.size();
	}

	@Override
	public Object getItem(int pos) {
		return ""+pos;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public int getItemViewType(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
 		if(view == null){
			view = listeMarkActivity.getLayoutInflater().inflate(R.layout.list_item, parent,false);
		}
 		Mark mrk = marks.get(position);
 		ImageView typeImage = (ImageView) view.findViewById(R.id.imageType);
 		if(!"*".equals(mrk.type)){
// 			typeImage.setImageResource(android.R.drawable.star_on);
 			typeImage.setVisibility(View.INVISIBLE);
 		}else{
 			typeImage.setVisibility(View.VISIBLE);
 		}
 		
		TextView txt = (TextView) view.findViewById(R.id.textView1);
//		TextView txt1 = (TextView) view.findViewById(R.id.textView2);
		ImageButton deleteMarkBtn = (ImageButton) view.findViewById(R.id.deleteMark);
		txt.setTextSize(30);
		txt.setText(mrk.sura+" : "+QuranDroidActivity.suratName[mrk.sura]+"   ("+mrk.aya+")");
		txt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listeMarkActivity.sendResult(position);
				
			}
		});
//		txt1.setTextSize(30);
//		txt1.setText(new Date(mrk.date).toString());
		
		deleteMarkBtn.setOnClickListener(new DeleteMarkClickListener(mrk.id));
		return view;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
		
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int arg0) {
		return true;
	}

	public void initDB()
	{
		if(myDbHelper!=null && myDbHelper.isOpen()){
			return;
		}
		myDbHelper = new DataBaseHelper(ListMarksActivity.ctx);
		try {
			myDbHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		try {
			myDbHelper.openDataBase();
		}catch(SQLException sqle){
			throw sqle;
		}
	}
	class DeleteMarkClickListener implements View.OnClickListener{
		int id;
		public DeleteMarkClickListener(int id){
			this.id = id;
		}
		@Override
		public void onClick(View v) {
			DataBaseHelper.deleteMark(id);
			loadMarks();
			listeMarkActivity.update();
			
		}
	}
	
}



class Mark{
	String type;
	int id;
	int date;
	int sura;
	int aya;
	public Mark(String type, int date, int sura, int aya,int id) {
		this.type = type;
		this.date = date;
		this.sura = sura;
		this.aya = aya;
		this.id = id;
	}
	public Mark(Cursor cur) {
		this(cur.getString(0),cur.getInt(1),cur.getInt(2),cur.getInt(3),cur.getInt(4));
	}
	
	
}
