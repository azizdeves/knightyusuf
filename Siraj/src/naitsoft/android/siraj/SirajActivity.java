package naitsoft.android.siraj;

import java.io.IOException;

import android.app.Activity;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SirajActivity extends Activity {
    

	private DataBaseHelper myDbHelper;
	private int livre;
	private int chapitre;
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
        livre = 1;
        chapitre = 5;
//        initDB();
//        loadShowChapter();
        
		paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        paint.setStyle(Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextAlign(Align.RIGHT);
        
//		mInflater = (LayoutInflater) this.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		arabicAdapter = new ArabicListAdapter(this);
		setContentView(R.layout.main);
		listMarkView = (ListView) findViewById(R.id.listMarkView);
		linearLayout = (LinearLayout)findViewById(R.id.linearLayout); 
		listMarkView.setOnScrollListener(arabicAdapter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus)
        	return;
        arabicAdapter.setWidth(listMarkView.getWidth());
		listMarkView.setAdapter(arabicAdapter);

    }
    
	@Override
	protected void onStart() {
		super.onStart();
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
		contentChapter = myDbHelper.getChapter(livre, chapitre).substring(0, 1000);
//		cursorEnd = lis.setText(contentChapter);
	}
}

