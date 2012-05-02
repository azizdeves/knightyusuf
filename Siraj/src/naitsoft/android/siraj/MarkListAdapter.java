package naitsoft.android.siraj;

import java.sql.Array;
import java.util.ArrayList;

import android.app.Activity;
import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ListAdapter;

public class MarkListAdapter implements ListAdapter {

	int idBook = -1,idChap = -1;
	ArrayList<MarkItem> markItems = new ArrayList<MarkItem>();
	private DataBaseHelper myDbHelper;
	private Activity activity;
	private ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>();
	
	public MarkListAdapter(Fragment marksListFragment){
		this.activity = marksListFragment.getActivity();
		
		myDbHelper = DataBaseHelper.getInstance(activity);
	}
	
	public void init(){
		for(MarkItem m : myDbHelper.getMarksItem(idBook, idChap))
			markItems.add(m); 
		
	}

	public void registerDataSetObserver(DataSetObserver observer) {
	    observers.add(observer);
	}
	
	public void notifyDataSetChanged(){
	    for (DataSetObserver observer: observers) {
	        observer.onChanged();
	    }
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {

	}

	@Override
	public int getCount() {
		return markItems.size();
	}

	@Override
	public Object getItem(int position) {
		return markItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return markItems.get(position).mark.markId;
	}

	@Override
	public boolean hasStableIds() {
		
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = activity.getLayoutInflater().inflate(R.layout.mark_list_item, parent,false);
			holder = new ViewHolder();
			holder.arabText = (TextView) convertView.findViewById(R.id.markText);
			convertView.setTag(holder);
//			((TextView) holder.arabText).setKeyListener(null);
//			((TextView) holder.arabText).setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					
//				}
//			});
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}

		((TextView) holder.arabText).setText(markItems.get(position).toString());
		return convertView;
	}

	public boolean loadMark(int pos) {
		if(markItems.get(pos).content==null){
			markItems.get(pos).content =DariGlyphUtils.reshapeText(myDbHelper.getChapter(markItems.get(pos).mark.idBook, markItems.get(pos).mark.idChap).getContent())
			.substring(markItems.get(pos).mark.startChar, markItems.get(pos).mark.endChar);
			return true;
		}
		return false;
		
	}
	
	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return markItems.isEmpty();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	public int getIdChap() {
		return idChap;
	}

	public void setIdChap(int idChap) {
		this.idChap = idChap;
	}

	public int getIdBook() {
		return idBook;
	}

	public void setIdBook(int idBook) {
		this.idBook = idBook;
	}


}
class MarkItem{
	Mark mark;
	String content;
	String titleChapter;
	public MarkItem(Mark m) {
		mark = m;
	}
	public MarkItem() {
		mark = new Mark();
	}
	@Override
	public String toString() {
		String s =  titleChapter;
		if(content != null)
			s+=" \n "+content;
		
		return s;
	}
}