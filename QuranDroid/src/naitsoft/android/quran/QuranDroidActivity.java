package naitsoft.android.quran;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;

public class QuranDroidActivity extends Activity implements OnInitListener {
	private static final int TTS_CHECK_CODE = 0;
	public static final int MARK_CODE = 1;

	public TextToSpeech mTts;
	private QuranView qv;
	private DataBaseHelper myDbHelper;
	private String ayaTxt;
	private ImageButton playBtn;
	private ImageButton nextBtn;
	private ImageButton prevBtn;
	private MediaPlayer player;
	private ScrollView scrollAya;
	private ImageButton bigBtn;
	private ImageButton smallBtn;
	private ImageButton markBtn;
	private Thread audioThread;
	static int aya = 1;
	static int sura = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		scrollAya = (ScrollView) findViewById(R.id.scroll);
		playBtn = (ImageButton) findViewById(R.id.playBtn);
		nextBtn = (ImageButton) findViewById(R.id.next);
		prevBtn = (ImageButton) findViewById(R.id.prev);
		bigBtn = (ImageButton) findViewById(R.id.big);
		smallBtn = (ImageButton) findViewById(R.id.small);
		markBtn = (ImageButton) findViewById(R.id.markBtn);
		
		qv = (QuranView) findViewById(R.id.quranTxt);
		initDB(); 
		
//		prevBtn.setText(">>>");
//		nextBtn.setText("<<<");
		
		initFromBundle(savedInstanceState);
		if(mTts == null){
			Intent checkIntent = new Intent();
			checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
			startActivityForResult(checkIntent, TTS_CHECK_CODE);
		}
		
		
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
				loadShowAya();
				player = null;
			}
		});
		prevBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getPrevAya();
				loadShowAya();
				player = null;
			}
		});
		markBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				callMarkActivity();
			}
		});
	}

	public void callMarkActivity()
	{
		Intent markIntent = new Intent(this,ListMarksActivity.class);
		markIntent.putExtra("aya", aya);
		markIntent.putExtra("sura", sura);
		startActivityForResult(markIntent,MARK_CODE);
	}




	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		audioThread = new Thread();
		loadShowAya();
	}

	@Override
	protected void onStart() {
		super.onStart();
		
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("aya", aya);
		outState.putInt("sura", sura);
		outState.putFloat("size", qv.getTxtSize());
		myDbHelper.addMark("", sura, aya);
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(initFromBundle(savedInstanceState))
			return;
		Surah s = myDbHelper.getLastMark();
		if(s==null){
			return;
		}
		sura = s.getSura();
		aya = s.getAya();
		
		
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
			if(data==null)return;
			aya = data.getExtras().getInt("aya");
			sura = data.getExtras().getInt("sura");
			loadShowAya();
		}
	}
	
	private void playAya(){
		if(player!=null){
			scrollAya.scrollTo(0, 0);
			player.start();
		}
	}
	
	private void loadAudioAya(final int sura, final int aya){
		player = null;
		audioThread=null;
		audioThread = new Thread(){
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

	private boolean initFromBundle(Bundle bundle){
		if(bundle==null){
			return false;
		}
		aya = bundle.getInt("aya");
		sura = bundle.getInt("sura");
		qv.setTxtSize(bundle.getFloat("size"));
		return true;
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
	private void loadShowAya(){
		ayaTxt = getAya();
		qv.setText(ayaTxt);
		loadAudioAya(sura, aya);
	}
	
	private String getAya(){
		String txt =  myDbHelper.getAya(sura, aya);
		if(!"".equals(txt))
			return txt;
		if(aya < 1){ 
			sura--;		aya = 1;
			if(sura<1)
				sura = 114;

		}else{
			sura++;aya = 1;
			if(sura>114){
				sura = 1;
			}

		}

		return		getAya();
	}

	@Override
	public void onInit(int status) { 	
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
	
	public Surah (Cursor cur){
		sura = cur.getInt(1);
		aya = cur.getInt(2);
	}
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