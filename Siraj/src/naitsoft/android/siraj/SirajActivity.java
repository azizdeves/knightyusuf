package naitsoft.android.siraj;

import java.io.IOException;

import android.app.Activity;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;

public class SirajActivity extends Activity {
    

	private DataBaseHelper myDbHelper;
	private int livre;
	private int chapitre;
	private String contentChapter;

	private LayoutInflater mInflater;
	private ArabicListAdapter arabicAdapter;
	private ListView listMarkView;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        livre = 1;
        chapitre = 5;
//        initDB();
//        loadShowChapter();
        
//		mInflater = (LayoutInflater) this.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		arabicAdapter = new ArabicListAdapter(this);
		setContentView(R.layout.main);
		listMarkView = (ListView) findViewById(R.id.listMarkView);
		listMarkView.setAdapter(arabicAdapter);
		
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

