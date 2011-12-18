package naitsoft.android.siraj;

import java.io.IOException;
import java.util.ArrayList;

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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SirajActivity extends Activity {


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

	private LayoutInflater mInflater;
	private ArabicListAdapter arabicAdapter;
	public static MyListView listTextLineView;
	private LinearLayout linearLayout;
	public static TextView text;
	static public Paint paint;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		livre = 6; 
		chapitre = 1;
		//        initDB();
		//        loadShowChapter();
		Bundle bund = getIntent().getExtras();
		if(bund != null){ 
			livre= bund.getInt("idBook");
			chapitre = bund.getInt("idChap");
			//			getListView().setSelection(bund.getInt("idBook"))
		}

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(25);
		paint.setStyle(Style.FILL);
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setTextAlign(Align.RIGHT);
		ArabicTextView.mPaint = paint;

		arabicAdapter = new ArabicListAdapter(this);
		setContentView(R.layout.main);
		listTextLineView = (MyListView) findViewById(R.id.listMarkView);
		//		linearLayout = (LinearLayout)findViewById(R.id.linearLayout); 
		//ImageButton chapBtn = (ImageButton) findViewById(R.id.chaptersBtn);
		listTextLineView.setOnScrollListener(arabicAdapter);
		//		mInflater = (LayoutInflater) this.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		//		chapBtn.setOnClickListener(new View.OnClickListener() {
		//			public void onClick(View v) {
		//				callChaptersActivity();
		//				
		//			}
		//		});
	}

	protected void callChaptersActivity() {
		Intent chapIntent = new Intent(this,BooksListActivity.class);	
		chapIntent.putExtra("idBook", livre);
		chapIntent.putExtra("idChap", chapitre);
		startActivity(chapIntent);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(!hasFocus)
			return;
		loadShowChapter();

	}

	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
		//ArabicTextView.mPaint.setTextSize(pref.getInt("textsize", 30));
		//		chapitre = pref.getInt("chap", 1);
		//		livre = pref.getInt("livre", 1);
	}

	protected void getPrevPage() {

	}

	protected void getNextPage() {

	}

	public void initDB()
	{
		myDbHelper = new DataBaseHelper(this);
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

	private void loadShowChapter(){
		arabicAdapter.loadChapter(livre, chapitre);
		arabicAdapter.setWidth(listTextLineView.getWidth());
		listTextLineView.setAdapter(arabicAdapter);
		//		cursorEnd = lis.setText(contentChapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CHAPTERS_CODE){
			if(data==null)return;
			chapitre = data.getExtras().getInt("idChap");
			//			loadShowChapter();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		//		SharedPreferences.Editor editor = getSharedPreferences("state", MODE_PRIVATE).edit();
		//		editor.putInt("chap", chapitre);
		//		editor.putInt("livre", livre);
		//		editor.commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Group ID
		int groupId = 0;
		// Unique menu item identifier. Used for event handling.
		int menuItemId = 1;//MENU_ITEM;
		// The order position of the item
		int menuItemOrder = Menu.NONE;
		// Text to be displayed for this menu item.
		String menuItemText = "Index";
		// Create the menu item and keep a reference to it.
		MenuItem menuItem = menu.add(groupId, menuItemId,
				menuItemOrder, menuItemText);

		menu.add(0,2,0,"Preference");
		menu.add(0,3,2,"Select");
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		//		return super.onMenuItemSelected(featureId, item);
		switch(item.getItemId()){
		case 1: 
			callChaptersActivity();
			break;
		case 2: 
			Intent intent = new Intent(this, PreferencesActivity.class);
			startActivity(intent);
			break;
		case 3:
			status = SELECTING;

			break;
		}
		return true;
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
	

//	@Override
//	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//		for(Focusable focus : focusables){
//			focus.draw(canvas);
//		};
//	}
	
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
//		for(Focusable focus : focusables){
//			if(focus.isVisible() == true)
//				focus.draw(canvas);
//		}
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
		
		if(SirajActivity.status == SirajActivity.NORMAL  ) {
			
			gestDetect.onTouchEvent(ev);
			return super.onTouchEvent(ev); 
		}
		
		int line = getIndexLine(ev.getY());
		if(SirajActivity.status == SirajActivity.SELECTING  ) {
			
			if(ev.getAction()== MotionEvent.ACTION_MOVE){
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
		if(  SirajActivity.status == SirajActivity.SELECTED)				
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
		SirajActivity.status = SirajActivity.SELECTED;
		TextSelection sel = TextSelection.getInstance();	
		sel.startCursor.x = (int) (e.getX());
		sel.endCursor.x = (int) (e.getX());
		int line = getIndexLine(e.getY());
		sel.startCursor.numLine = line ;
		sel.endCursor.numLine = line ;
		addFocus(sel.focusA);
		addFocus(sel.focusB);
		//dispatchTouchEvent(e);
		invalidate();
	}
	
	public int getListScrollY() {
		ArabicTextView atv = (ArabicTextView) ((LinearLayout) getChildAt(0)).getChildAt(0);
		return -atv.getTop()+atv.line.numLine*atv.stepLine;
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
		pic = (BitmapDrawable) SirajActivity.context.getResources().getDrawable(idDraw);
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
		rect.set(x, y, x+50, y+50);
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
