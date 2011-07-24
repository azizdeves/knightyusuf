package naitsoft.android.quran;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

public class QuranDroidActivity extends Activity implements OnInitListener {
	private static final int TTS_CHECK_CODE = 0;
	public static final int MARK_CODE = 1;

	public TextToSpeech mTts;
	private QuranView qv;
	private DataBaseHelper myDbHelper;
	private String ayaTxt;
	private Button playBtn;
	private Button nextBtn;
	private Button prevBtn;
	private MediaPlayer player;
	private ScrollView scrollAya;
	private Button bigBtn;
	private Button smallBtn;
	private Button markBtn;
	static int aya ;
	static int sura;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		scrollAya = (ScrollView) findViewById(R.id.scroll);
		playBtn = (Button) findViewById(R.id.playBtn);
		nextBtn = (Button) findViewById(R.id.next);
		prevBtn = (Button) findViewById(R.id.prev);
		bigBtn = (Button) findViewById(R.id.big);
		smallBtn = (Button) findViewById(R.id.small);
		markBtn = (Button) findViewById(R.id.markBtn);
		
		qv = (QuranView) findViewById(R.id.quranTxt);
		initDB(); 
		
		prevBtn.setText(">>>");
		nextBtn.setText("<<<");
		
		initFromBundle(savedInstanceState);
		ayaTxt = myDbHelper.getAya(sura, aya);
		if(mTts == null){
			Intent checkIntent = new Intent();
			checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
			startActivityForResult(checkIntent, TTS_CHECK_CODE);
		}
		qv.setText(ayaTxt);
		
		qv.setEventListener(new QuranEventListener() {
			@Override
			public void onTouch(QuranEvent event) {
//				if(event.getDirct() == QuranEvent.SLIDE_RIGHT)
//					getNextAya();
//				else
//					getPrevAya();
//				
//				ayaTxt = myDbHelper.getAya(sura, aya);
//				qv.setText(ayaTxt);
			}
			@Override
			public void onClick(QuranEvent event) {
				mTts.speak(qv.getRoot(event.getWord()),TextToSpeech.QUEUE_FLUSH, null);
			}
		});
		bigBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				qv.setTxtSize(qv.getTxtSize()+2);
				qv.init(false);
				
			}
		});
		smallBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				qv.setTxtSize(qv.getTxtSize()-2);
				qv.init(false);
			}
		});
		playBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				playAya();
				
			}
		});
		nextBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getNextAya();
				ayaTxt = myDbHelper.getAya(sura, aya);
				qv.setText(ayaTxt);
				player = null;
				loadAudioAya(sura, aya);
			}
		});
		prevBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getPrevAya();
				ayaTxt = myDbHelper.getAya(sura, aya);
				qv.setText(ayaTxt);
				player = null;
				loadAudioAya(sura, aya);
			}
		});
		markBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				callMarkActivity();
			}
		});
		loadAudioAya(sura, aya);
	}

	public void callMarkActivity()
	{
//		myDbHelper.addMark("*", 3, 19);
//		Intent markIntent = new Intent(this,ListMarksActivity.class);
//		startActivityForResult(markIntent,1);
	}


	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		initFromBundle(savedInstanceState);
	}


	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("aya", aya);
		outState.putInt("sura", sura);
		outState.putFloat("size", qv.getTxtSize());
	}



	protected void onActivityResult(
			int requestCode, int resultCode, Intent data) {
		if (requestCode == TTS_CHECK_CODE) {
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
		if(requestCode == MARK_CODE){
			aya = data.getExtras().getInt("aya");
			sura = data.getExtras().getInt("sura");
		}
	}
	
	private void playAya(){
		if(player!=null){
			scrollAya.scrollTo(0, 0);
			player.start();
		}
	}
	
	private void loadAudioAya(final int sura, final int aya){
		Thread audioThread = new Thread(){
			public void run() {
				Context ctx = getApplicationContext();
			//	playBtn.setEnabled(false);
				player = MediaPlayer.create(ctx, Uri.parse("http://tanzil.net/res/audio/abdulbasit-mjwd/" 
						+addZero(sura)+addZero(aya)+	".mp3"));
			
			//	playBtn.setEnabled(true);
			}
		};
		audioThread.start();
	
	}

	private void initFromBundle(Bundle bundle){
		if(bundle==null){
			aya =5;
			sura =2;
			return;
		}
		aya = bundle.getInt("aya");
		sura = bundle.getInt("sura");
		qv.setTxtSize(bundle.getFloat("size"));
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
//		qv.setTts(mTts);
		//		String myText1 = "\u0631\u064e\u0628\u0651\u0650\u064a \u0641\u0650\u064a \u0643\u0650\u062a\u064e\u0627\u0628\u064d " +
		//		"\u06d6 \u0644\u0651\u064e\u0627 \u064a\u064e\u0636\u0650\u0644\u0651\u0650 \u064f";
		//		String myText2 = "I hope so, because it's time to wake up.";
		//		mTts.speak(myText1, TextToSpeech.QUEUE_FLUSH, null);
		//		mTts.speak(myText2, TextToSpeech.QUEUE_ADD, null);		
	}
	
	private String addZero(int nb)
	{
		String s = "00"+nb;
		return s.substring(s.length()-3);
		
	}
	public int getNextAya(){
		scrollAya.scrollTo(0, 0);
			return ++aya;
	}
	public int getPrevAya(){
		scrollAya.scrollTo(0, 0);
		return --aya;
	}
	
}

class Surah{
	int sura;
	int aya;
	String ayaTxt;
	int nbrAya;
	
	public int getNextAya(){
		if(aya<nbrAya)
			return ++aya;
		//getNextSura();
		return 0;
	}
	public int getPrevAya(){
		return --aya;
	}
	
	public int getSura() {
		return sura;
	}
	public void setSura(int sura) {
		this.sura = sura;
	}
	public int getAya() {
		return aya;
	}
	public void setAya(int aya) {
		this.aya = aya;
	}
	public String getAyaTxt() {
		return ayaTxt;
	}
	public void setAyaTxt(String ayaTxt) {
		this.ayaTxt = ayaTxt;
	}
	public int getNbrAya() {
		return nbrAya;
	}
	public void setNbrAya(int nbrAya) {
		this.nbrAya = nbrAya;
	}
	
}