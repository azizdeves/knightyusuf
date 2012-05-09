package naitsoft.android.siraj;

import java.io.IOException;
import java.util.ArrayList;

import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentManager;
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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.text.Html;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.WindowManager;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SirajActivity extends FragmentActivity {


	public static final int CHAPTERS_CODE = 0;
//	public static final char SELECTING = 's';
//	public static final char SELECTED = 'S';
//	public static final char NORMAL = 'n';
	
//	public static char status = NORMAL;
	private DataBaseHelper myDbHelper;
//	public int livre;
//	public int chapitre;
	int textSize;
	public static Context context;

//	private LayoutInflater mInflater;
	private ArabicListAdapter arabicAdapter;
//	public static MyListView listTextLineView;
//	private LinearLayout linearLayout;
	private Menu menu;

	private ProgressDialog dialog;
	public static TextView text;
	static public Paint paint; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		context = getApplicationContext(); 
//		dialog = ProgressDialog.show(this, "", 
//                "Loading. Please wait...", true);
//		livre = 1; 
//		chapitre = 5;
		        initDB();
		//        loadShowChapter();
//		Bundle bund = getIntent().getExtras();
//		if(bund != null){ 
//			livre= bund.getInt("idBook");
//			chapitre = bund.getInt("idChap");
//			//			getListView().setSelection(bund.getInt("idBook"))
//		}

//		Paint paint = new Paint();
//		paint.setAntiAlias(true);
//		paint.setTextSize(25);
//		paint.setStyle(Style.FILL);
//		paint.setColor(Color.WHITE);
//		paint.setAntiAlias(true);
//		paint.setTextAlign(Align.RIGHT);
//		ArabicTextView.mPaint = paint;

//		arabicAdapter = new ArabicListAdapter(this);
		setContentView(R.layout.article_activ);
		
//		ArticleFragment artFrag = (ArticleFragment)
//				getFragmentManager().findFragmentById(R.id.article_frag);
		ActionBar actionBar = this.getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

	}

//	protected void callChaptersActivity() {
//		Intent chapIntent = new Intent(this,ChaptersListActivity.class);	
//		chapIntent.putExtra("idBook", livre);
//		chapIntent.putExtra("idChap", chapitre);
//		startActivity(chapIntent);
//	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
//		ArticleFragment af = (ArticleFragment)(
		if(!hasFocus)
			return;
		ArticleFragment.articleFrag.loadShowChapter();
//		dialog.dismiss();
//		dialog = null;
		//		super.onWindowFocusChanged(hasFocus);
//		loadShowChapter();

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

//	private void loadShowChapter(){
//		arabicAdapter.loadChapter(livre, chapitre);
//		arabicAdapter.setWidth(listTextLineView.getWidth());
//		listTextLineView.setAdapter(arabicAdapter);
//		//		cursorEnd = lis.setText(contentChapter);
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CHAPTERS_CODE){
			if(data==null)return;
//			chapitre = data.getExtras().getInt("idChap");
			//			loadShowChapter();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
//		if(keyCode == 24){
//			
//		}
		
//		if(keyCode == 25){
//			
//		}
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

//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.layout.menu, menu);
//		this.menu = menu;
		return true;

//		super.onCreateOptionsMenu(menu);
//		// Group ID
//		int groupId = 0;
//		// Unique menu item identifier. Used for event handling.
//		int menuItemId = 1;//MENU_ITEM;
//		// The order position of the item
//		int menuItemOrder = Menu.NONE;
//		// Text to be displayed for this menu item.
//		String menuItemText = "Index";
//		// Create the menu item and keep a reference to it.
//		MenuItem menuItem = menu.add(groupId, menuItemId,
//				menuItemOrder, menuItemText);
//
//		menu.add(0,2,0,"Preference");
//		menu.add(0,3,2,"Select");
//		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem 
			item) {
		return ArticleFragment.articleFrag.onOptionsItemSelected(item);
//		switch(item.getItemId()){
//		case R.id.menu_edit: 
//			listTextLineView.startSelection();
//			break;
//		case 2: 
//			Intent intent = new Intent(this, PreferencesActivity.class);
//			startActivity(intent);
//			break;
//		case R.id.menu_share:
//			String selectionTxt = ArabicListAdapter.getTextFromSelection(TextSelection.getCurrentSelection());
//			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
////			sharingIntent.setType("text/html");
//			sharingIntent.setType("text/plain");
//			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, selectionTxt);
////			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>"+selectionTxt+"</p>"));
//			startActivity(Intent.createChooser(sharingIntent,"Share using"));
//			break;
//		case R.id.menu_save:
//			MenuItem item1 = menu.findItem(R.id.menu_share);
//			item1.setVisible(false);
//
//			break;
//		case android.R.id.home:
//			callChaptersActivity();
//			break;
//		}
//	   
//		return true;
	}


}
class Chapter{
	String content;
	int idChap;
	String title;
	
	public Chapter()
	{
		content = "";
		title = "";
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIdChap() {
		return idChap;
	}
	public void setIdChap(int idChap) {
		this.idChap = idChap;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}

