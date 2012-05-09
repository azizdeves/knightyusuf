package naitsoft.android.siraj;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import android.app.Activity;
import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.ListAdapter;

public class MarkListAdapter implements ExpandableListAdapter{

	int idBook = -1,idChap = -1;
	String token;
	ArrayList<ArrayList<MarkItem>> markItems ;
	private DataBaseHelper myDbHelper;
	private Activity activity;
	private ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>();
	String[] groupTitles;
	boolean isSearch ;
	private ArrayList<MarkItem> searchResult;
	
	public MarkListAdapter(Fragment marksListFragment){
		this.activity = marksListFragment.getActivity();
		
		myDbHelper = DataBaseHelper.getInstance(activity);
	}
	
	public void init(){
//		isSearch = true;
//		token = "قومة";
		isSearch = token!=null;
		if(isSearch){
			searchResult = myDbHelper.getSearchResult(idBook, token);
			groupTitles = new String[searchResult.size()];
			int i = 0;
			markItems = new ArrayList<ArrayList<MarkItem>>(searchResult.size());
			for(MarkItem mi : searchResult){
				markItems.add(i,null);
				groupTitles[i++] = mi.titleChapter ;
			}
		}
		else{
			Collection<ArrayList<MarkItem>> m = myDbHelper.getMarksItem(idBook, idChap);
			markItems = new ArrayList<ArrayList<MarkItem>>();
//			ArrayList<String> group = new ArrayList<String>();
			int i = 0;
			groupTitles = new String[m.size()];
			for(ArrayList<MarkItem> array : m){
//				group.add(array.get(0).titleChapter);
				groupTitles[i++]= array.get(0).titleChapter;
				markItems.add(array);
			}
//			group.toArray(groupTitles);
		}
			
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
		observers.remove(observer);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	public void loadMark(int pos) {
		
		if(isSearch ){
			if(markItems.get(pos)!=null)
				return;
			int i =0;
			String content = searchResult.get(pos).content;
			MarkItem mi ;
			markItems.add(pos, new ArrayList<MarkItem>());
			
			while((i=content.indexOf(token, i))!=-1){
				mi = new MarkItem();
				mi.mark.idBook = idBook;
				mi.mark.idChap = searchResult.get(pos).mark.idChap;
				mi.mark.startChar = i<80?0:i-80;
				i=mi.mark.endChar = i>content.length()-80?content.length()-1:i+80;
				mi.content = content.substring(mi.mark.startChar, mi.mark.endChar);
				markItems.get(pos).add(mi);
				
			}
		}else{
			if(markItems.get(pos).get(0).content==null){
				String text = DariGlyphUtils.reshapeText(myDbHelper.getChapter(markItems.get(pos).get(0).mark.idBook, markItems.get(pos).get(0).mark.idChap).getContent());
				for(MarkItem marki: markItems.get(pos))
					marki.content =text.substring(marki.mark.startChar, marki.mark.endChar);
			}
		}
		
	}

	@Override
	public boolean isEmpty() {
		return groupTitles.length==0;
	}

	@Override
	public boolean areAllItemsEnabled() {
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

	@Override
	public int getGroupCount() {
		return groupTitles.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(isSearch)
			loadMark(groupPosition);
		
		return markItems.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		
		return groupTitles[groupPosition];
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return markItems.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		
		return groupTitles[groupPosition].hashCode();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return markItems.get(groupPosition).get(childPosition).mark.markId;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = activity.getLayoutInflater().inflate(R.layout.mark_list_item, parent,false);
			holder = new ViewHolder();
			holder.arabText = (TextView) convertView.findViewById(R.id.markText);
			convertView.setTag(holder);
			((TextView) holder.arabText).setTextSize(20);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}

		((TextView) holder.arabText).setText(groupTitles[groupPosition]);
		if(isExpanded){
			loadMark(groupPosition);
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = activity.getLayoutInflater().inflate(R.layout.mark_list_item, parent,false);
			holder = new ViewHolder();
			holder.arabText = (TextView) convertView.findViewById(R.id.markText);
			convertView.setTag(holder);
			((TextView) holder.arabText).setTextSize(20);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}

		((TextView) holder.arabText).setText(markItems.get(groupPosition).get(childPosition).content);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		
		return true;
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		loadMark(groupPosition);
		
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		
		return groupId*1000+childId;
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		
		return groupId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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