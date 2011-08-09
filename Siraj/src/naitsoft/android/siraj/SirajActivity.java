package naitsoft.android.siraj;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SirajActivity extends Activity {
    

	public static final int CHAPTERS_CODE = 0;
	private DataBaseHelper myDbHelper;
	public int livre;
	public int chapitre;
	private String contentChapter;
	int textSize;

	private LayoutInflater mInflater;
	private ArabicListAdapter arabicAdapter;
	private ListView listMarkView;
	private LinearLayout linearLayout;
	public static TextView text;
	static public Paint paint;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
		listMarkView = (ListView) findViewById(R.id.listMarkView);
		linearLayout = (LinearLayout)findViewById(R.id.linearLayout); 
		ImageButton chapBtn = (ImageButton) findViewById(R.id.chaptersBtn);
		listMarkView.setOnScrollListener(arabicAdapter);
//		mInflater = (LayoutInflater) this.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		chapBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				callChaptersActivity();
				
			}
		});
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
//		SharedPreferences pref= getSharedPreferences("state", MODE_PRIVATE);
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
		arabicAdapter.setWidth(listMarkView.getWidth());
		listMarkView.setAdapter(arabicAdapter);
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
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
//		return super.onMenuItemSelected(featureId, item);
		callChaptersActivity();
		return true;
	}
	
	
}

