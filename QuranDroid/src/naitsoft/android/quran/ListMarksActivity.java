package naitsoft.android.quran;

import java.io.IOException;
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
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListMarksActivity extends Activity {

	private LayoutInflater mInflater;
	private ListMarkAdapter markAdapter;
	static Context ctx;

	public ListMarksActivity(){
		ctx = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = (LayoutInflater) this.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		markAdapter = new ListMarkAdapter(this);
		setContentView(R.layout.layout_mark);
		ListView listMarkView = (ListView) findViewById(R.id.listMarkView);
		listMarkView.setAdapter(markAdapter);
		listMarkView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("sura", 1);
				intent.putExtra("aya", 2);
				setResult(QuranDroidActivity.MARK_CODE, intent);
				finish();
			}
		});
		Button addMarkBtn = (Button) findViewById(R.id.markBtn);
		addMarkBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				markAdapter.addMark('*', 19, 3);
			}
		});
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

//	public DataBaseHelper getMyDbHelper() {
//		return myDbHelper;
//	}
//
//	public void setMyDbHelper(DataBaseHelper myDbHelper) {
//		this.myDbHelper = myDbHelper;
//	}
	
}



class ListMarkAdapter implements ListAdapter
{
	private LayoutInflater mInflater;
	private  DataBaseHelper myDbHelper;
	private ArrayList<Mark> marks ;
	int size;
	public ListMarkAdapter(ListMarksActivity activ)
	{		mInflater = activ.getLayoutInflater(); 
			//myDbHelper = activ.getMyDbHelper();
			initDB();
			loadMarks();
	}
	
	private void loadMarks(){
		
		Cursor cur = myDbHelper.getMarks();
		marks = new  ArrayList<Mark>(cur.getCount());
		if(!cur.moveToFirst()){			//TODO cur empty		
		}
		Mark mrk ;
		size = cur.getCount();
		while(cur.moveToNext()){
			
			marks.add(new Mark(cur));
		}

	}
	public void addMark(char type , int aya , int sura){
		myDbHelper.addMark(type+"", sura, aya);
	}

	@Override
	public int getCount() {
		return size;
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
	public View getView(int position, View view, ViewGroup parent) {
		if(view == null){
			view = mInflater.inflate(R.layout.list_item, parent,false);
		}
		TextView txt = (TextView) view.findViewById(R.id.textView1);
		txt.setTextSize(marks.get(position).sura);
		TextView txt1 = (TextView) view.findViewById(R.id.textView2);
		txt1.setText(marks.get(position).aya);
		return view;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return false;
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
		if(myDbHelper.isOpen()){
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
}
class Mark{
	String type;
	int date;
	int sura;
	int aya;
	public Mark(String type, int date, int sura, int aya) {
		this.type = type;
		this.date = date;
		this.sura = sura;
		this.aya = aya;
	}
	public Mark(Cursor cur) {
		this(cur.getString(0),cur.getInt(1),cur.getInt(2),cur.getInt(2));
	}
	
	
}
