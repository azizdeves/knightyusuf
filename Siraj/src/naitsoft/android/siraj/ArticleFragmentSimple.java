package naitsoft.android.siraj;

import java.io.IOException;
import java.util.ArrayList;

import android.support.v4.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PictureDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.text.Html;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MenuInflater;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ArticleFragmentSimple extends Fragment {


	public static final int CHAPTERS_CODE = 0;
	public static final char SELECTING = 's';
	public static final char SELECTED = 'S';
	public static final char NORMAL = 'n';
	
	public static char status = NORMAL;
	private DataBaseHelper myDbHelper;
	public int livre;
	public int chapitre;
	int textSize;
	public static Context context;
	public static ArticleFragmentSimple articleFrag;

	private LayoutInflater mInflater;
	public ArabicListAdapter arabicAdapter;
	public static MyListView listTextLineView;
	private LinearLayout linearLayout;
	private Menu menu;
	private LinearLayout markBar;
	public static TextView text;
	static public Paint paint; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		articleFrag = this;
		context = getActivity().getApplicationContext();
		livre = 1; 
		chapitre = 5;
		//        initDB();
		//        loadShowChapter();
		Bundle bund = getActivity().getIntent().getExtras();
		if(bund != null){ 
			livre= bund.getInt("idBook");
			chapitre = bund.getInt("idChap");
			//			getListView().setSelection(bund.getInt("idBook"))
		}

		setHasOptionsMenu(true);
		ActionBar actionBar = ((FragmentActivity) getActivity()).getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(25);
		paint.setStyle(Style.FILL);
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setTextAlign(Align.RIGHT);
		ArabicTextView.mPaint = paint;

		arabicAdapter = new ArabicListAdapter((SirajActivity) getActivity());
		View v = inflater.inflate(R.layout.article_frag,container);
		listTextLineView = (MyListView) v.findViewById(R.id.listMarkView);
		listTextLineView.setOnScrollListener(arabicAdapter);
//		listTextLineView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		listTextLineView.setDividerHeight(0);
		markBar = (LinearLayout) v.findViewById(R.id.mark_bar);

		return v; 
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.menu, menu);
		this.menu = menu;
//		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menu_edit:  
			listTextLineView.startSelection();
			markBar.setVisibility(View.VISIBLE);
			break;
		case 2: 
//			Intent intent = new Intent(this, PreferencesActivity.class);
//			startActivity(intent);
			break;
		case R.id.menu_share:
			String selectionTxt = ArabicListAdapter.getTextFromSelection(TextSelection.getCurrentSelection());
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//			sharingIntent.setType("text/html");
			sharingIntent.setType("text/plain");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, selectionTxt);
//			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>"+selectionTxt+"</p>"));
			startActivity(Intent.createChooser(sharingIntent,"Share using"));
			break;
		case R.id.menu_save:
//			MenuItem item1 = menu.findItem(R.id.menu_share);
			Mark mrk = arabicAdapter.getAbsoluteTextSelectPosition(TextSelection.getCurrentSelection());
			arabicAdapter.saveMark(mrk);
			arabicAdapter.updateMarkUi(TextSelection.getCurrentSelection().getMarkUi(arabicAdapter.width));
			status = NORMAL;
			TextSelection.clear();
			markBar.setVisibility(View.GONE);
//			listTextLineView.invalidate();
			

			break;
		case android.R.id.home:
			callChaptersActivity();
			break;
		}
	   
		return true;
	}
	

	protected void callChaptersActivity() {
		Intent chapIntent = new Intent(getActivity(),ChaptersListActivity.class);	
		chapIntent.putExtra("idBook", livre);
		chapIntent.putExtra("idChap", chapitre);
		startActivity(chapIntent);
	}

	public void scrollUp()
	{
		listTextLineView.scrollUp();
	}
	
	public void scrollDown()
	{
		listTextLineView.scrollDown();
	}
	
//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		super.onWindowFocusChanged(hasFocus);
//		if(!hasFocus)
//			return;
//		loadShowChapter();
//
//	}
//	

//	@Override
//	protected void onStart() {
//		super.onStart();
//		SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
//		//ArabicTextView.mPaint.setTextSize(pref.getInt("textsize", 30));
//		//		chapitre = pref.getInt("chap", 1);
//		//		livre = pref.getInt("livre", 1);
//	}

//	protected void getPrevPage() {
//
//	}
//
//	protected void getNextPage() {
//
//	}

	public void initDB()
	{
		myDbHelper = new DataBaseHelper(getActivity());
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

	public void loadShowChapter(){
		arabicAdapter.loadChapter(livre, chapitre);
		arabicAdapter.setWidth(listTextLineView.getWidth());
		listTextLineView.setAdapter(arabicAdapter);
		//		cursorEnd = lis.setText(contentChapter);
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if(requestCode == CHAPTERS_CODE){
//			if(data==null)return;
//			chapitre = data.getExtras().getInt("idChap");
//			//			loadShowChapter();
//		}
//	}

//	@Override
//	protected void onStop() {
//		super.onStop();
//		//		SharedPreferences.Editor editor = getSharedPreferences("state", MODE_PRIVATE).edit();
//		//		editor.putInt("chap", chapitre);
//		//		editor.putInt("livre", livre);
//		//		editor.commit();
//
//	}

	
}

