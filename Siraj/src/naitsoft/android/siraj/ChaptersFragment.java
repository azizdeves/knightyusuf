package naitsoft.android.siraj;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;


public class ChaptersFragment extends Fragment {

	private ChaptersAdapter chapterAdapter;
//	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(25);
        paint.setStyle(Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextAlign(Align.RIGHT);
        ArabicTextView.mPaint = paint;
        
		chapterAdapter = new ChaptersAdapter(this);
		Bundle bund = getActivity().getIntent().getExtras();
		if(bund != null){ 
			chapterAdapter.setIdBook( bund.getInt("idBook"));
//			getListView().setSelection(bund.getInt("idBook"))
		}
		else 
			chapterAdapter.setIdBook( 1);
		
		setHasOptionsMenu(true);
		ActionBar actionBar = ((FragmentActivity) getActivity()).getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v =  inflater.inflate(R.layout.chapters, container);
		ListView lv = (ListView)(v.findViewById(R.id.listChapters));
		lv.setAdapter(chapterAdapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				callActivity(pos);
			}
		});
		
		
	    
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.layout.menu, menu);
		inflater.inflate(R.layout.menu, menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){

		case android.R.id.home:
			Intent intent = new Intent(getActivity(), BooksListActivity.class);
			startActivity(intent);
			break;
			
		case R.id.menu_info: 
//			Intent intent = new Intent(this, PreferencesActivity.class);
//			startActivity(intent);
			Intent chapIntent = new Intent(getActivity(),MarksListActivity.class);	
			chapIntent.putExtra("idBook", chapterAdapter.idBook);
			startActivity(chapIntent);
			break;
		}
	   
		return true;
	}
	
	
	private void callActivity(int pos)
	{
		Intent intent = new Intent(this.getActivity(),SirajActivity.class);
		intent.putExtra("idBook", chapterAdapter.idBook);
		intent.putExtra("idChap",chapterAdapter.getChapter(pos).idChap);
		startActivity(intent);
	}
	

}

class ChaptersAdapter implements ListAdapter
{

	ArrayList<ChapterItem> chapters;
	private LayoutInflater mInflater;
	private ChaptersFragment activity;
	private int size;
	int idBook=1;
	private HashMap<Integer, ChapterItem> map;


	public ChaptersAdapter(ChaptersFragment activ){
		mInflater = activ.getActivity().getLayoutInflater(); 

		activity = activ;
		

	}

	public ChapterItem getChapter(int pos) {

		return chapters.get(pos);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if(view == null){
			view = mInflater.inflate(R.layout.chapter_line, parent,false);
			holder = new ViewHolder();
			holder.arabText = (ArabicTextView) view.findViewById(R.id.textView);
//			holder.arabText.setCatchTouchEvent(false);
			view.setTag(holder);
		}
		else{
			holder = (ViewHolder) view.getTag();
		}
		((ArabicTextView) holder.arabText).setText(chapters.get(position).title);
		return view;
	}

	public void loadChapters(int idBook){
		DataBaseHelper dbHelper = DataBaseHelper.getInstance(activity.getActivity());
		Cursor cur = dbHelper.getChaptersOfBook(idBook);
		ChapterItem c;
		chapters = new  ArrayList<ChapterItem>(cur.getCount());
		map = new HashMap<Integer, ChapterItem>(cur.getCount());
		if(cur.moveToFirst()){			//TODO cur empty		
			do{
				c=new ChapterItem(cur); //TODO optimiz
				map.put(c.idChap,c);
				chapters.add(c);
			}while(cur.moveToNext());
			size = cur.getCount();
			initLevels();
		}
		cur.close();
	}

	private void initLevels(){
		//		Chapter tmpChap = new Chapter();
		Integer idParent = new Integer(0);
		ChapterItem parentChap ;
		int i ;
		ChapterItem rootChap = new ChapterItem();
		for(ChapterItem c : chapters){
			if(c.idChap==c.idParent)
			{
				rootChap.addSubChapter(c);
				c.level = 1;
				continue;
			}
			idParent=c.idParent;
			parentChap = map.get(idParent);
			if(parentChap == null)
				idParent=c.idParent;
				
			c.level = (char) (parentChap.level+1);
			parentChap.addSubChapter(c);
			
			for(i = 1; i< c.level ; i++){
				c.title=" | " +c.title;
			}
		}
		map = null;
		chapters.clear();
		constructList(rootChap);


	}
	private void constructList(ChapterItem c){
		chapters.add(c);
		if(c.subChapters == null)
			return ;
		for(ChapterItem sc : c.subChapters)
			constructList(sc);
	}
	public void setIdBook(int id){
		idBook = id;
		loadChapters(idBook);
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
class ChapterItem{
	public int idParent;
	public String title;
	public char level;
	public Integer idChap;
	public ArrayList<ChapterItem> subChapters;

	public ChapterItem(Cursor cur) {
		init(cur);
//		subChapters = new ArrayList<Chapter>();
		//		this._id = cur.getInt(3);
	}
	public void init(Cursor cur){
		title = DariGlyphUtils.reshapeText(cur.getString(2));
		idChap = cur.getInt(0);
		idParent = cur.getInt(1);
//		cur.close();
	}
	public void addSubChapter(ChapterItem c){
		if(subChapters == null)
			subChapters = new ArrayList<ChapterItem>();
		subChapters.add(c);
	}
	public ChapterItem() {
		idChap = new Integer(0);
	}
	public boolean equals(Object o){
		return ((ChapterItem)o).idChap == idChap;

	}
}
