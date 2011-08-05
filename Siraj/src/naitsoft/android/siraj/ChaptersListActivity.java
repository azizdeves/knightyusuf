package naitsoft.android.siraj;

import java.util.ArrayList;
import java.util.HashMap;

import naitsoft.android.quran.Mark;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class ChaptersListActivity extends ListActivity {

	
}

class ChaptersAdapter implements ListAdapter
{

	HashMap<Integer,Chapter> chapters;
	private LayoutInflater mInflater;
	private SirajActivity activity;
	private DataBaseHelper dbHelper;
	private int size;

	
	public ChaptersAdapter(SirajActivity activ){
		mInflater = activ.getLayoutInflater(); 
		
		activity = activ;
		paint=activ.paint;
		initDB();
		loadChapter();
		
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
 		if(view == null){
			view = mInflater.inflate(R.layout.layout_mark, parent,false);
			holder = new ViewHolder();
			holder.arabText = (ArabicTextView) view.findViewById(R.id.textView);
			view.setTag(holder);
		}
 		else{
 			holder = (ViewHolder) view.getTag();
 		}
		holder.arabText.setText(chapters.get(position).title);
		return view;
	}

	public void loadChapters(int idBook){
		dbHelper = DataBaseHelper.getInstance(activity);
		Cursor cur = dbHelper.getChaptersOfBook(idBook);
		if(cur.moveToFirst()){			//TODO cur empty		
			do{
				chapters.add(new Chapter(cur));
			}while(cur.moveToNext());
			size = cur.getCount();
		}
	}
	
	private void initLevels(){
		char[] levels = new char[chapters.size()];
//		Chapter c;
		for(Chapter c : chapters){
			if(c.idChap==c.idParent)
			{
				c.level = 1;
			}
			c.level = chapters.get
		}
		
		
	}
	
	@Override
	public int getViewTypeCount() {
		return 0;
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
	public int getCount() {
		return chapters.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		return 1;
	}
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int arg0) {
		return true;
	}
	
}
class Chapter{
	public int idParent;
	public String title;
	public char level;
	public int idChap;
	int _id;
	public Chapter(Cursor cur) {
		title = cur.getString(2);
		idChap = cur.getInt(0);
		idParent = cur.getInt(1);
		this._id = cur.getInt(3);
	}
	public boolean equals(Object o){
		return ((Chapter)o).idChap == idChap;
		
	}
}