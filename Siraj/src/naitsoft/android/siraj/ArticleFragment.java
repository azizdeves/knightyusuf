package naitsoft.android.siraj;

import java.io.IOException;
import java.util.ArrayList;

import android.support.v4.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ArticleFragment extends Fragment {


	public static final int CHAPTERS_CODE = 0;
	public static final char SELECTING = 's';
	public static final char SELECTED = 'S';
	public static final char NORMAL = 'n';
	
	public static char status = NORMAL;
	private DataBaseHelper myDbHelper;
	public int livre;
	public int chapitre;
	private int lastCharPos;
	int textSize;
	public static Context context;
	public static ArticleFragment articleFrag;

	private LayoutInflater mInflater;
	public ArabicListAdapter arabicAdapter;
	public static MyListView articleTextView;
	private LinearLayout linearLayout;
	static public  Menu menu;
	static public MarkBar markBar;
	public static TextView text;
//	static public Paint paint; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		articleFrag = this;
		context = getActivity().getApplicationContext();
		livre = 1; 
		chapitre = 5;
		lastCharPos = -1;
		Bundle bund = getActivity().getIntent().getExtras();
		if(bund != null){ 
			livre= bund.getInt("idBook");
			chapitre = bund.getInt("idChap");
			
		}

		setHasOptionsMenu(true);
		ActionBar actionBar = ((FragmentActivity) getActivity()).getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        ArabicTextView.mPaint.setTextSize(Integer.parseInt(pref.getString("txtSize", "20") ));
        lastCharPos = pref.getInt("lastCharPos",-1);
        status = NORMAL;
        TextSelection.clear();

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		

		arabicAdapter = new ArabicListAdapter((SirajActivity) getActivity());
		View v = inflater.inflate(R.layout.article_frag,container);
		articleTextView = (MyListView) v.findViewById(R.id.listMarkView);
		articleTextView.setOnScrollListener(arabicAdapter);
//		listTextLineView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		articleTextView.setDividerHeight(0);
		
		markBar = new MarkBar(this,v);
		
//		if(lastCharPos != -1)
//			scrollToChar(lastCharPos);
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
			if(status != NORMAL)
				break;
			articleTextView.startSelection();
			markBar.setVisibility(View.VISIBLE);
//			menu.getItem(3).setVisible(true);
//			menu.getItem(1).setVisible(false);
//			menu.getItem(4).setVisible(true);
			
			break;
		case R.id.menu_info: 
//			Intent intent = new Intent(this.getActivity(), PreferencesActivity.class);
//			startActivity(intent);
			break;
		case R.id.menu_settings: 
			Intent intent = new Intent(this.getActivity(), PreferencesActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_search: 
			Bundle bundle = new Bundle();
			bundle.putInt("idBook", livre);
			getActivity().startSearch(null,false, bundle , false);
//			Intent chapIntent = new Intent(getActivity(),MarksListActivity.class);	
//			chapIntent.putExtra("idBook", livre);
//			chapIntent.putExtra("idChap", chapitre);
//			startActivity(chapIntent);
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
		articleTextView.scrollUp();
	}
	
	public void scrollDown()
	{
		articleTextView.scrollDown();
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
	public void scrollToChar(int indexChar){
		while(arabicAdapter.getLastCharDrawn() < indexChar){
			articleTextView.smoothScrollBy(50, 300);
			
		}
	}

	public void loadShowChapter(){
		arabicAdapter.loadChapter(livre, chapitre);
		arabicAdapter.setWidth(articleTextView.getWidth());
		articleTextView.setAdapter(arabicAdapter);
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

	@Override
	public void onStart() {
		super.onStart();
		
	}
	@Override
	public void onStop() {
		super.onStop();

		SharedPreferences.Editor pref= PreferenceManager.getDefaultSharedPreferences(this.getActivity()).edit();
		pref.putInt("lastCharPos", arabicAdapter.getLastCharDrawn());
		pref.commit();

	}

	
}

class MyListView extends ListView implements OnGestureListener
{

	public static int stepLine;
	private ArrayList<Focusable> focusables= new ArrayList<Focusable>();
	private Focusable curFocus;
	private GestureDetector gestDetect = new GestureDetector(this);

	public MyListView(Context context) {
		super(context);
	}
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void scrollUp(){
		smoothScrollBy(-50, 3000);
	}
	
	public void scrollDown(){
		smoothScrollBy(50, 3000);
	}



	public Focusable getTouchedFocus(MotionEvent ev){ 
//		for(Focusable f : focusables){
//			if( f.getRect().contains((int)ev.getX(), (int)ev.getY()+getListScrollY()))//TODO need improvement 
//				return f;
//		}
		return null;
	}
	public  int getIndexLine(float y){
		return  ((int)(y+getListScrollY()) / stepLine);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		if(ArticleFragment.status == ArticleFragment.NORMAL  ) {
			
			gestDetect.onTouchEvent(ev);
			return super.onTouchEvent(ev); 
		}
		
		int line = getIndexLine(ev.getY());
		if(ArticleFragment.status == ArticleFragment.SELECTING  ) {
			
			if(ev.getAction()== MotionEvent.ACTION_MOVE){
				if(ev.getY()<50)
					smoothScrollBy(-50, 3000);
				if(ev.getY()>getHeight()-50)
					smoothScrollBy(50, 3000);
				if(curFocus!=null){
					curFocus.onTouchEvent(ev,line);
				}
				
			}
			if(ev.getAction()== MotionEvent.ACTION_UP){
				
				if(curFocus!=null){
					curFocus.onTouchEvent(ev,line);
					curFocus = null;
				}
				
			}
			return true;
		}
		if(  ArticleFragment.status == ArticleFragment.SELECTED)				
			if(ev.getAction()== MotionEvent.ACTION_DOWN){
				Focusable focus = TextSelection.getTouchedCurseur((int) ev.getX(), line);
				if(focus!=null){
					curFocus = focus;
					focus.onTouchEvent(ev,line);
					return true;
				}
			}

		gestDetect.onTouchEvent(ev);
		return super.onTouchEvent(ev); 

	}
	@Override
	public void onLongPress(MotionEvent e) {
//		if(SirajActivity.status == SirajActivity.SELECTED)
//			return;
		
	}
	
	public void startSelection()
	{
		ArticleFragment.status = ArticleFragment.SELECTED;
		TextSelection sel = TextSelection.getInstance();	
		sel.startCursor.x = (int) (getWidth()*0.6);
		sel.endCursor.x = (int) (getWidth()*0.4);
		int line = getIndexLine(getHeight()/2);
		sel.startCursor.numLine = line ;
		sel.endCursor.numLine = line ;
		addFocus(sel.focusA);
		addFocus(sel.focusB);
		//dispatchTouchEvent(e);
		invalidate();
	}
	
	public int getListScrollY() {
		LinearLayout ll = (LinearLayout) getChildAt(0);
		ArabicTextView atv = (ArabicTextView) (ll).getChildAt(0);
		return -ll.getTop()+atv.line.numLine*atv.stepLine;
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}	
	@Override
	public void onShowPress(MotionEvent e) {	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if(ArticleFragment.status == ArticleFragment.NORMAL){
			int line = getIndexLine(e.getY());
			MarkUI m = ArabicListAdapter.marksUi.get(line);
			m = MarkUI.getMarkUiByEvent(m, line, (int) e.getX()-getWidth());
			if(m== null)
				return true;
			m.isActive = false;
			TextSelection sel = TextSelection.getInstance();	
			TextSelection.markUi = m;
			
			sel.startCursor.x = m.startX+getWidth();
			sel.endCursor.x = m.endX+getWidth();
			sel.startCursor.numLine = m.startLine ;
			sel.endCursor.numLine = m.endLine ;
			addFocus(sel.focusA);
			addFocus(sel.focusB);
			ArticleFragment.status = ArticleFragment.SELECTED;
//			invalidate();
			ArticleFragment.markBar.setVisibility(View.VISIBLE);
			ArticleFragment.menu.getItem(4).setVisible(true);
		}
		return true;
	}

	public void addFocus(Focusable f){
		focusables.add(f);
	}
}
abstract class  Focusable{
	BitmapDrawable pic;
	int width;
	int height;
	int x;
	int y;
	Rect rect;
	int idDrawable;
	boolean visible = true;
	int line;
	TextCursor textCursor;
	
	public Focusable(int idDraw){
		pic = (BitmapDrawable) ArticleFragment.context.getResources().getDrawable(idDraw);
		idDrawable = idDraw;
		rect = new Rect();
		line=-1;
	}
	
	public void setVisible(boolean v){
		visible =v;
	}
	public  boolean isVisible(){
		return visible;
	}
	public Rect getRect() {
		return rect;
	}

	public void setRect(Rect rect) {
		this.rect = rect;
	}

	public int getIdDrawable() {
		return idDrawable;
	}

	public void draw(Canvas canvas) {
		if(!visible)return;
		rect.set(x-25, y, x+25, y+50);
		pic.setBounds(rect);
		pic.draw(canvas);
	}

	public void setIdDrawable(int idDrawable) {
		this.idDrawable = idDrawable;
	}

	public BitmapDrawable getPic() {
		return pic;
	}

	public void setPic(BitmapDrawable pic) {
		this.pic = pic;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public abstract void onTouchEvent(MotionEvent ev, int indexLine)  ;
	
}
class MarkBar{

	private LinearLayout parentView;
	private ArticleFragment articleFragment;

	public MarkBar(final ArticleFragment articleFragment, View v) {
		this.articleFragment = articleFragment;
		parentView = (LinearLayout) v.findViewById(R.id.mark_bar);
		Button yellowBtn = (Button) v.findViewById(R.id.yellow_mrk);
		Button blueBtn = (Button) v.findViewById(R.id.blue_mrk);
		Button greenBtn = (Button) v.findViewById(R.id.green_mrk);
		Button redBtn = (Button) v.findViewById(R.id.red_mrk);
		ImageButton saveBtn = (ImageButton) v.findViewById(R.id.save_mrk);
		ImageButton deleteBtn = (ImageButton) v.findViewById(R.id.delete_mrk);
		
		yellowBtn.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				TextSelection.markUi.mark.type = Color.YELLOW;
				articleFragment.articleTextView.requestLayout();
			}
		});
		redBtn.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				TextSelection.markUi.mark.type = Color.RED;
				articleFragment.articleTextView.requestLayout();
				
			}
		});
		blueBtn.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				TextSelection.markUi.mark.type = Color.BLUE;
				articleFragment.articleTextView.requestLayout();
				
			}
		});
		greenBtn.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				TextSelection.markUi.mark.type = Color.GREEN;
				articleFragment.articleTextView.requestLayout();
			}
		});
		saveBtn.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Mark mrk = articleFragment.arabicAdapter.getAbsoluteTextSelectPosition(TextSelection.getCurrentSelection());
				if(TextSelection.markUi.mark.markId != -1)
				{
					mrk.markId = TextSelection.markUi.mark.markId;
					mrk.type = TextSelection.markUi.mark.type;
					articleFragment.arabicAdapter.saveMark(mrk,true);
				}
				else
					articleFragment.arabicAdapter.saveMark(mrk, false);
				
				MarkUI mrkUi = TextSelection.getCurrentSelection().getMarkUi(articleFragment.arabicAdapter.width);
				mrkUi.mark = mrk;
				articleFragment.arabicAdapter.updateMarkUi(mrkUi);
				ArticleFragment.status = ArticleFragment.NORMAL;
				TextSelection.clear();
				setVisibility(View.GONE);
//				menu.getItem(3).setVisible(false);
//				menu.getItem(1).setVisible(true);
//				menu.getItem(4).setVisible(false);
//				listTextLineView.invalidate();
			}
		});
		deleteBtn.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				if(ArticleFragment.status == ArticleFragment.SELECTED && TextSelection.markUi != null){
					articleFragment.arabicAdapter.deleteMark(TextSelection.markUi.mark.markId);
					articleFragment.status = articleFragment.NORMAL;
					TextSelection.clear();
					setVisibility(View.GONE);
				}
			}
		});
		
		
	}

	public void setVisibility(int visibility) {
		parentView.setVisibility(visibility );
		
	}
	
}