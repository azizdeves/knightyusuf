package naitsoft.android.quran;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;

public class QuranDroidActivity extends Activity implements OnInitListener {
	private static final int MY_DATA_CHECK_CODE = 0;
	/** Called when the activity is first created. */

	public TextToSpeech mTts;
	private QuranView qv;
	private DataBaseHelper myDbHelper;
	private String ayaTxt;
	static int aya ;
	static int sura;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initDB();
		sura = 3;
		aya = 16;
		ayaTxt = myDbHelper.getAya(sura, aya);
		//Intent checkIntent = new Intent();
		//checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		//startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

		qv = new QuranView(this,ayaTxt);
		
		qv.setEventListener(new QuranEventListener() {
			@Override
			public void onTouch(QuranEvent event) {
				if(event.getDirct() == QuranEvent.SLIDE_RIGHT)
					getNextAya();
				else
					getPrevAya();
				
				ayaTxt = myDbHelper.getAya(sura, aya);
				qv.setText(ayaTxt);
			}
			@Override
			public void onClick(QuranEvent event) {
				//mTts.speak(qv.getRoot(event.getWord()),TextToSpeech.QUEUE_FLUSH, null);
			}
		});

		setContentView(qv);
	}

	protected void onActivityResult(
			int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				mTts = new TextToSpeech(this, this);
			} else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent.setAction(
						TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
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

	@Override
	public void onInit(int status) {
		qv.setTts(mTts);
		//		String myText1 = "\u0631\u064e\u0628\u0651\u0650\u064a \u0641\u0650\u064a \u0643\u0650\u062a\u064e\u0627\u0628\u064d " +
		//		"\u06d6 \u0644\u0651\u064e\u0627 \u064a\u064e\u0636\u0650\u0644\u0651\u0650 \u064f";
		//		String myText2 = "I hope so, because it's time to wake up.";
		//		mTts.speak(myText1, TextToSpeech.QUEUE_FLUSH, null);
		//		mTts.speak(myText2, TextToSpeech.QUEUE_ADD, null);		
	}
	public int getNextAya(){
		return aya;
	}
	public int getPrevAya(){
		return --aya;
	}
}