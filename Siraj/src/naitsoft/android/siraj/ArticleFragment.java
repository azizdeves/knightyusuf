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

public class ArticleFragment extends Fragment {


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
	public static ArticleFragment articleFrag;

	private LayoutInflater mInflater;
	public ArabicListAdapter arabicAdapter;
	public static MyListView articleTextView;
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
		

		arabicAdapter = new ArabicListAdapter((SirajActivity) getActivity());
		View v = inflater.inflate(R.layout.article_frag,container);
		articleTextView = (MyListView) v.findViewById(R.id.listMarkView);
		articleTextView.setOnScrollListener(arabicAdapter);
//		listTextLineView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		articleTextView.setDividerHeight(0);
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
			articleTextView.startSelection();
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

//	@Override
//	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//		for(Focusable focus : focusables){
//			focus.draw(canvas);
//		};
//	}
	
//	@Override
//	public void draw(Canvas canvas) {
//		// TODO Auto-generated method stub
//		super.draw(canvas);
////		for(Focusable focus : focusables){
////			if(focus.isVisible() == true)
////				focus.draw(canvas);
////		}
//	}

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
	public void onShowPress(MotionEvent e) {
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
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
