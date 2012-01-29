package naitsoft.android.quran;

import java.io.IOException;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

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
	private String ayaPath;
	private TextView suraAyaTxt;
	private ImageButton markBtn;
	private Thread audioThread;
	static int aya = 0;
	static int sura = 0;
	static int[] nbrAyatBySura = {1,7, 286, 200, 176, 120, 165, 206, 75, 129, 109, 123, 111, 43, 52, 99, 128, 111, 110, 98, 135, 112, 78, 118, 64, 77, 227, 93, 88, 69, 60, 34, 30, 73, 54, 45, 83, 182, 88, 75, 85, 54, 53, 89, 59, 37, 35, 38, 29, 18, 45, 60, 49, 62, 55, 78, 96, 29, 22, 24, 13, 14, 11, 11, 18, 12, 12, 30, 52, 52, 44, 28, 28, 20, 56, 40, 31, 50, 40, 46, 42, 29, 19, 36, 25, 22, 17, 19, 26, 30, 20, 15, 21, 11, 8, 8, 19, 5, 8, 8, 11, 11, 8, 3, 9, 5, 4, 7, 3, 6, 3, 5, 4, 5, 6};
//	static int[] sie = {0, 7, 293, 493, 669, 789, 954, 1160, 1235, 1364, 1473, 1596, 1707, 1750, 1802, 1901, 2029, 2140, 2250, 2348, 2483, 2595, 2673, 2791, 2855, 2932, 3159, 3252, 3340, 3409, 3469, 3503, 3533, 3606, 3660, 3705, 3788, 3970, 4058, 4133, 4218, 4272, 4325, 4414, 4473, 4510, 4545, 4583, 4612, 4630, 4675, 4735, 4784, 4846, 4901, 4979, 5075, 5104, 5126, 5150, 5163, 5177, 5188, 5199, 5217, 5229, 5241, 5271, 5323, 5375, 5419, 5447, 5475, 5495, 5551, 5591, 5622, 5672, 5712, 5758, 5800, 5829, 5848, 5884, 5909, 5931, 5948, 5967, 5993, 6023, 6043, 6058, 6079, 6090, 6098, 6106, 6125, 6130, 6138, 6146, 6157, 6168, 6176, 6179, 6188, 6193, 6197, 6204, 6207, 6213, 6216, 6221, 6225, 6230, 6236};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Remove notification bar
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.main);
		scrollAya = (ScrollView) findViewById(R.id.scroll);
		playBtn = (ImageButton) findViewById(R.id.playBtn); 
		nextBtn = (ImageButton) findViewById(R.id.next);
		prevBtn = (ImageButton) findViewById(R.id.prev);
		bigBtn = (ImageButton) findViewById(R.id.big);
		smallBtn = (ImageButton) findViewById(R.id.small);
		suraAyaTxt = (TextView) findViewById(R.id.suraAya);
		markBtn = (ImageButton) findViewById(R.id.markBtn);
		//<ImageButton android:text="X" android:id="@+id/markBtn"  android:src="@android:drawable/btn_star" android:layout_width="wrap_content" android:layout_height="wrap_content" android:layout_gravity="fill" android:layout_weight="1"></ImageButton>
		
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
				myDbHelper.addMark("*", sura, aya);
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
//		Surah s = myDbHelper.getLastMark();
//		if(s==null){
//			return;
//		}
//		sura = s.getSura();
//		aya = s.getAya();

		if(sura == 0){

			SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
			sura = pref.getInt("sura", 18);
			aya = pref.getInt("aya", 1);
			qv.setTxtSize(pref.getFloat("size", 80));
		}
		
	}

	@Override
	protected void onStop() {
		super.onStop();
//		myDbHelper.addMark("", sura, aya);

		SharedPreferences.Editor pref= PreferenceManager.getDefaultSharedPreferences(this).edit();
		pref.putInt("sura", sura);
		pref.putInt("aya", aya);
		pref.putFloat("size",qv.getTxtSize());
		pref.commit();
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("aya", aya);
		outState.putInt("sura", sura);
		outState.putFloat("size", qv.getTxtSize());
//		myDbHelper.addMark("", sura, aya);
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
		super.onActivityResult(requestCode, resultCode, data);
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
//			loadShowAya();
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
				try {
					Context ctx = getApplicationContext();
				//	playBtn.setEnabled(false);
					ayaPath = "/mnt/sdcard/QuranAya/" 
							+addZero(sura)+addZero(aya)+	".mp3" ;
					if(player == null){
						player = MediaPlayer.create(ctx, Uri.parse(ayaPath));
					}else
						player.setDataSource(ctx,Uri.parse(ayaPath));
	//				player = MediaPlayer.create(ctx, Uri.parse("http://tanzil.net/res/audio/abdulbasit-mjwd/" 
	//						+addZero(sura)+addZero(aya)+	".mp3"));
					player.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
//		this.getWindow().getAttributes()Title("sura: " + sura +"    aya: "+ aya);
		suraAyaTxt.setText(sura+":"+aya);
		
	}
	
	private String getAya(){
		String txt =  myDbHelper.getAya(sura, aya); 
//		if(!"".equals(txt))
//			return txt;
//		if(aya < 1){ 
//			sura--;		aya = 1;
//			if(sura<1)
//				sura = 114;
//
//		}else{
//			sura++;aya = 1;
//			if(sura>114){
//				sura = 1;
//			}
//
//		}
//		return		getAya();
		return txt;
	}

	@Override
	public void onInit(int status) { 	
	}
	
	private String addZero(int nb)
	{
		String s = "00"+nb;
		return s.substring(s.length()-3);
		
	}
	public void getNextAya(){
		scrollAya.scrollTo(0, 0);
		if(++aya>nbrAyatBySura[sura]){
			aya = 0;
			sura++;
		}
	}
	public void getPrevAya(){
		scrollAya.scrollTo(0, 0);
		if(--aya<1){
			sura--;
			aya = nbrAyatBySura[sura];
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		// Create the menu item and keep a reference to it.
//		MenuItem menuItem = menu.add(groupId, menuItemId,
//				menuItemOrder, menuItemText);

//		menu.add(0,2,0,"Preference");
		menu.add(0,1,1,"Marks");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem 
			item) {
		callMarkActivity();
	
		return true;
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