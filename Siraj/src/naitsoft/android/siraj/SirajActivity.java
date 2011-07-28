package naitsoft.android.siraj;

import java.io.IOException;
import android.app.Activity;
import android.database.SQLException;
import android.os.Bundle;

public class SirajActivity extends Activity {
    

	private QuranView qv;
	private DataBaseHelper myDbHelper;
	private int livre;
	private int chapitre;
	private String contentChapter;
	private int cursorBegin;
	private int cursorEnd;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        qv = (QuranView) findViewById(R.id.quranTxt);
        livre = 1;
        chapitre = 5;
        initDB();
        loadShowChapter();
    }

	public void initDB()
	{
		myDbHelper = new DataBaseHelper(this);
		//        myDbHelper = new DataBaseHelper(this);
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
		contentChapter = myDbHelper.getChapter(livre, chapitre).substring(0, 100);
		cursorEnd = qv.setText(contentChapter,cursorBegin);
	}
}

