package naitsoft.android.quran;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
	final static String ayaFolder = "/mnt/sdcard/QuranAya/";
	static int[] nbrAyatBySura = {1,7, 286, 200, 176, 120, 165, 206, 75, 129, 109, 123, 111, 43, 52, 99, 128, 111, 110, 98, 135, 112, 78, 118, 64, 77, 227, 93, 88, 69, 60, 34, 30, 73, 54, 45, 83, 182, 88, 75, 85, 54, 53, 89, 59, 37, 35, 38, 29, 18, 45, 60, 49, 62, 55, 78, 96, 29, 22, 24, 13, 14, 11, 11, 18, 12, 12, 30, 52, 52, 44, 28, 28, 20, 56, 40, 31, 50, 40, 46, 42, 29, 19, 36, 25, 22, 17, 19, 26, 30, 20, 15, 21, 11, 8, 8, 19, 5, 8, 8, 11, 11, 8, 3, 9, 5, 4, 7, 3, 6, 3, 5, 4, 5, 6};
	//	static int[] sie = {0, 7, 293, 493, 669, 789, 954, 1160, 1235, 1364, 1473, 1596, 1707, 1750, 1802, 1901, 2029, 2140, 2250, 2348, 2483, 2595, 2673, 2791, 2855, 2932, 3159, 3252, 3340, 3409, 3469, 3503, 3533, 3606, 3660, 3705, 3788, 3970, 4058, 4133, 4218, 4272, 4325, 4414, 4473, 4510, 4545, 4583, 4612, 4630, 4675, 4735, 4784, 4846, 4901, 4979, 5075, 5104, 5126, 5150, 5163, 5177, 5188, 5199, 5217, 5229, 5241, 5271, 5323, 5375, 5419, 5447, 5475, 5495, 5551, 5591, 5622, 5672, 5712, 5758, 5800, 5829, 5848, 5884, 5909, 5931, 5948, 5967, 5993, 6023, 6043, 6058, 6079, 6090, 6098, 6106, 6125, 6130, 6138, 6146, 6157, 6168, 6176, 6179, 6188, 6193, 6197, 6204, 6207, 6213, 6216, 6221, 6225, 6230, 6236};
	//	Al-Fatiha", "Al-Baqara", "Al-i-Imran", "An-Nisa", "Al-Ma'ida", "Al-An'am", "Al-A'raf", "Al-Anfal", "At-Tawba", "Yunus", "Hud", "Yusuf", "Ar-Ra'd", "Ibrahim", "Al-Hijr", "An-Nahl", "Al-Isra", "Al-Kahf", "Maryam", "Ta-Ha", "Al-Anbiya", "Al-Hajj", "Al-Mu'minun", "An-Nur", "Al-Furqan", "Ash-Shu'ara", "An-Naml", "Al-Qasas", "Al-Ankabut", "Ar-Rum", "Luqman", "As-Sajda", "Al-Ahzab", "Saba", "Fatir", "Ya-Sin", "As-Saffat", "Sad", "Az-Zumar", "Ghafir", "Fussilat", "Ash-Shura", "Az-Zukhruf", "Ad-Dukhan", "Al-Jathiya", "Al-Ahqaf", "Muhammad", "Al-Fath", "Al-Hujurat", "Qaf", "Adh-Dhariyat", "At-Tur", "An-Najm", "Al-Qamar", "Ar-Rahman", "Al-Waqi'a", "Al-Hadid", "Al-Mujadila", "Al-Hashr", "Al-Mumtahina", "As-Saff", "Al-Jumu'a", "Al-Munafiqun", "At-Taghabun", "At-Talaq", "At-Tahrim", "Al-Mulk", "Al-Qalam", "Al-Haqqa", "Al-Ma'arij", "Nuh", "Al-Jinn", "Al-Muzzammil", "Al-Muddathir", "Al-Qiyama", "Al-Insan", "Al-Mursalat", "An-Naba'", "An-Nazi'at", "Abasa", "At-Takwir", "Al-Infitar", "Al-Mutaffifin", "Al-Inshiqaq", "Al-Buruj", "At-Tariq", "Al-A'la", "Al-Ghashiya", "Al-Fajr", "Al-Balad", "Ash-Shams", "Al-Lail", "Ad-Dhuha", "Ash-Sharh", "At-Tin", "Al-Alaq", "Al-Qadr", "Al-Bayyina", "Az-Zalzala", "Al-Adiyat", "Al-Qari'a", "At-Takathur", "Al-Asr", "Al-Humaza", "Al-Fil", "Quraysh", "Al-Ma'un", "Al-Kawthar", "Al-Kafirun", "An-Nasr", "Al-Masad", "Al-Ikhlas", "Al-Falaq", "An-Nas"
	//	The Opening", "The Heifer", "The Family of Imran", "The Women", "The Table", "The Cattle", "The Heights", "The Spoils of War", "The Repentance", "Jonah", "Hud", "Joseph", "The Thunder", "Abraham", "The Stoneland", "The Honey Bees", "The Night Journey", "The Cave", "Mary", "Ta-Ha", "The Prophets", "The Pilgrimage", "The Believers", "The Light", "The Criterion", "The Poets", "The Ant", "The Stories", "The Spider", "The Romans", "Luqman", "The Prostration", "The Clans", "Sheba", "The Originator", "Yaseen", "Drawn up in Ranks", "The Letter Sad", "The Troops", "The Forgiver", "Explained in Detail", "The Consultation", "The Ornaments of Gold", "The Smoke", "Crouching", "The Dunes", "Muhammad", "The Victory", "The Inner Apartments", "The Letter Qaf", "The Winnowing Winds", "The Mount", "The Star", "The Moon", "The Beneficent", "The Inevitable", "The Iron", "The Pleading", "The Exile", "Examining Her", "The Ranks", "Friday", "The Hypocrites", "Mutual Disillusion", "Divorce", "The Prohibition", "The Sovereignty", "The Pen", "The Reality", "The Ascending Stairways", "Noah", "The Jinn", "The Enshrouded One", "The Cloaked One", "The Resurrection", "Human", "The Emissaries", "The Announcement", "Those Who Drag Forth", "He Frowned", "The Folding Up", "The Cleaving", "Defrauding", "The Splitting Open", "The Constellations", "The Morning Star", "The Most High", "The Overwhelming", "The Dawn", "The City", "The Sun", "The Night", "The Morning Hours", "The Consolation", "The Fig", "The Clot", "The Power, Fate", "The Evidence", "The Earthquake", "The Chargers", "The Calamity", "Competition", "The Time", "The Traducer", "The Elephant", "Quraysh", "Almsgiving", "Abundance", "The Disbelievers", "Divine Support", "The Palm Fibre", "Purity of Faith", "The Dawn", "Mankind"
	static String[] suratName = {"","الفاتحة", "البقرة", "آل عمران", "النساء", "المائدة", "الأنعام", "الأعراف", "الأنفال", "التوبة", "يونس", "هود", "يوسف", "الرعد", "ابراهيم", "الحجر", "النحل", "الإسراء", "الكهف", "مريم", "طه", "الأنبياء", "الحج", "المؤمنون", "النور", "الفرقان", "الشعراء", "النمل", "القصص", "العنكبوت", "الروم", "لقمان", "السجدة", "الأحزاب", "سبإ", "فاطر", "يس", "الصافات", "ص", "الزمر", "غافر", "فصلت", "الشورى", "الزخرف", "الدخان", "الجاثية", "الأحقاف", "محمد", "الفتح", "الحجرات", "ق", "الذاريات", "الطور", "النجم", "القمر", "الرحمن", "الواقعة", "الحديد", "المجادلة", "الحشر", "الممتحنة", "الصف", "الجمعة", "المنافقون", "التغابن", "الطلاق", "التحريم", "الملك", "القلم", "الحاقة", "المعارج", "نوح", "الجن", "المزمل", "المدثر", "القيامة", "الانسان", "المرسلات", "النبإ", "النازعات", "عبس", "التكوير", "الإنفطار", "المطففين", "الإنشقاق", "البروج", "الطارق", "الأعلى", "الغاشية", "الفجر", "البلد", "الشمس", "الليل", "الضحى", "الشرح", "التين", "العلق", "القدر", "البينة", "الزلزلة", "العاديات", "القارعة", "التكاثر", "العصر", "الهمزة", "الفيل", "قريش", "الماعون", "الكوثر", "الكافرون", "النصر", "المسد", "الإخلاص", "الفلق", "الناس"};

	private static String TAG = "ServerSocketTest";


	static private byte[] audio;
	private MediaPlayer med;

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

		initFromBundle(savedInstanceState);


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
				try {
					String path = "mnt/sdcard/audioWord.mp3";
					File file = new File(path);
					FileOutputStream f = new FileOutputStream(file);

					byte[] buffer = myDbHelper.getAudioWord(qv.getRoot(event.getWord()).hashCode());
					f.write(buffer,0,buffer.length);

					f.close();
					med = new MediaPlayer();
					med.setDataSource(path);
					med.prepare();
					med.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
		suraAyaTxt.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {

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
	public void callSelectorActivity()
	{
		Intent markIntent = new Intent(this,AyaSelectorActivity.class);
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
		if(player != null)
			player.stop();
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

		SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
		qv.setTxtSize(pref.getFloat("size", 80));
		if(sura == 0){

			sura = pref.getInt("sura", 18);
			aya = pref.getInt("aya", 1);
		}

	}

	@Override
	protected void onStop() {
		super.onStop();

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
				File fileTmp = null;
				try {
					Context ctx = getApplicationContext();
				//	playBtn.setEnabled(false);
					
					ayaPath = ayaFolder
							+addZero(sura)+addZero(aya)+	".mp3" ;
					File file = new File(ayaPath);
					if(!file.exists()){
						//						String path = Environment.getExternalStorageDirectory().getPath()+"/siraj.txt";
						URL u = new URL("http://tanzil.net/res/audio/abdulbasit-mjwd/" 
								+addZero(sura)+addZero(aya)+	".mp3");
						fileTmp = new File(ayaFolder+addZero(sura)+addZero(aya)+	"tmp.mp3" );
						HttpURLConnection c = (HttpURLConnection) u.openConnection();
						c.setRequestMethod("GET");
						c.setDoOutput(true);
						c.connect();
//						c.getHeaderFieldInt("Content-Length", 0);
						File folder = new File(ayaFolder);
						folder.mkdir();
						FileOutputStream f = new FileOutputStream(fileTmp);

						InputStream in = c.getInputStream();

						byte[] buffer = new byte[1024];
						int length;
						while ( ( length = in.read(buffer)) > 0 ) {
							f.write(buffer,0,length);
						}
						
						f.close();
						fileTmp.renameTo(file);
						fileTmp = null;
						
						//						player = MediaPlayer.create(ctx, Uri.parse("http://tanzil.net/res/audio/abdulbasit-mjwd/" 
						//								+addZero(sura)+addZero(aya)+	".mp3"));
					}
					if(player == null){
						player = MediaPlayer.create(ctx, Uri.parse(ayaPath));
					}else
						player.setDataSource(ctx,Uri.parse(ayaPath));
					player.prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (Exception e) {
					if(fileTmp!=null)
						fileTmp.delete();
					e.printStackTrace();
				}
				finally{
					
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
		if(player != null)
			player.stop();
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
		menu.add(0,1,1,"Marks");
		menu.add(0,2,2,"Selector");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem 
			item) {
		switch(item.getItemId()){
		case 1:
			callMarkActivity();
			break;
		case 2:
			callSelectorActivity();
			break;
		}

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

//var d={Sura:[[],[0,7,5,1,'الفاتحة',"Al-Fatiha",'The Opening','Meccan'],[7,286,87,40,'البقرة',"Al-Baqara",'The Heifer','Medinan'],[293,200,89,20,'آل عمران',"Al-i-Imran",'The Family of Imran','Medinan'],[493,176,92,24,'النساء',"An-Nisa",'The Women','Medinan'],[669,120,112,16,'المائدة',"Al-Ma'ida",'The Table','Medinan'],[789,165,55,20,'الأنعام',"Al-An'am",'The Cattle','Meccan'],[954,206,39,24,'الأعراف',"Al-A'raf",'The Heights','Meccan'],[1160,75,88,10,'الأنفال',"Al-Anfal",'The Spoils of War','Medinan'],[1235,129,113,16,'التوبة',"At-Tawba",'The Repentance','Medinan'],[1364,109,51,11,'يونس',"Yunus",'Jonah','Meccan'],[1473,123,52,10,'هود',"Hud",'Hud','Meccan'],[1596,111,53,12,'يوسف',"Yusuf",'Joseph','Meccan'],[1707,43,96,6,'الرعد',"Ar-Ra'd",'The Thunder','Medinan'],[1750,52,72,7,'ابراهيم',"Ibrahim",'Abraham','Meccan'],[1802,99,54,6,'الحجر',"Al-Hijr",'The Stoneland','Meccan'],[1901,128,70,16,'النحل',"An-Nahl",'The Honey Bees','Meccan'],[2029,111,50,12,'الإسراء',"Al-Isra",'The Night Journey','Meccan'],[2140,110,69,12,'الكهف',"Al-Kahf",'The Cave','Meccan'],[2250,98,44,6,'مريم',"Maryam",'Mary','Meccan'],[2348,135,45,8,'طه',"Ta-Ha",'Ta-Ha','Meccan'],[2483,112,73,7,'الأنبياء',"Al-Anbiya",'The Prophets','Meccan'],[2595,78,103,10,'الحج',"Al-Hajj",'The Pilgrimage','Medinan'],[2673,118,74,6,'المؤمنون',"Al-Mu'minun",'The Believers','Meccan'],[2791,64,102,9,'النور',"An-Nur",'The Light','Medinan'],[2855,77,42,6,'الفرقان',"Al-Furqan",'The Criterion','Meccan'],[2932,227,47,11,'الشعراء',"Ash-Shu'ara",'The Poets','Meccan'],[3159,93,48,7,'النمل',"An-Naml",'The Ant','Meccan'],[3252,88,49,9,'القصص',"Al-Qasas",'The Stories','Meccan'],[3340,69,85,7,'العنكبوت',"Al-Ankabut",'The Spider','Meccan'],[3409,60,84,6,'الروم',"Ar-Rum",'The Romans','Meccan'],[3469,34,57,4,'لقمان',"Luqman",'Luqman','Meccan'],[3503,30,75,3,'السجدة',"As-Sajda",'The Prostration','Meccan'],[3533,73,90,9,'الأحزاب',"Al-Ahzab",'The Clans','Medinan'],[3606,54,58,6,'سبإ',"Saba",'Sheba','Meccan'],[3660,45,43,5,'فاطر',"Fatir",'The Originator','Meccan'],[3705,83,41,5,'يس',"Ya-Sin",'Yaseen','Meccan'],[3788,182,56,5,'الصافات',"As-Saffat",'Drawn up in Ranks','Meccan'],[3970,88,38,5,'ص',"Sad",'The Letter Sad','Meccan'],[4058,75,59,8,'الزمر',"Az-Zumar",'The Troops','Meccan'],[4133,85,60,9,'غافر',"Ghafir",'The Forgiver','Meccan'],[4218,54,61,6,'فصلت',"Fussilat",'Explained in Detail','Meccan'],[4272,53,62,5,'الشورى',"Ash-Shura",'The Consultation','Meccan'],[4325,89,63,7,'الزخرف',"Az-Zukhruf",'The Ornaments of Gold','Meccan'],[4414,59,64,3,'الدخان',"Ad-Dukhan",'The Smoke','Meccan'],[4473,37,65,4,'الجاثية',"Al-Jathiya",'Crouching','Meccan'],[4510,35,66,4,'الأحقاف',"Al-Ahqaf",'The Dunes','Meccan'],[4545,38,95,4,'محمد',"Muhammad",'Muhammad','Medinan'],[4583,29,111,4,'الفتح',"Al-Fath",'The Victory','Medinan'],[4612,18,106,2,'الحجرات',"Al-Hujurat",'The Inner Apartments','Medinan'],[4630,45,34,3,'ق',"Qaf",'The Letter Qaf','Meccan'],[4675,60,67,3,'الذاريات',"Adh-Dhariyat",'The Winnowing Winds','Meccan'],[4735,49,76,2,'الطور',"At-Tur",'The Mount','Meccan'],[4784,62,23,3,'النجم',"An-Najm",'The Star','Meccan'],[4846,55,37,3,'القمر',"Al-Qamar",'The Moon','Meccan'],[4901,78,97,3,'الرحمن',"Ar-Rahman",'The Beneficent','Medinan'],[4979,96,46,3,'الواقعة',"Al-Waqi'a",'The Inevitable','Meccan'],[5075,29,94,4,'الحديد',"Al-Hadid",'The Iron','Medinan'],[5104,22,105,3,'المجادلة',"Al-Mujadila",'The Pleading','Medinan'],[5126,24,101,3,'الحشر',"Al-Hashr",'The Exile','Medinan'],[5150,13,91,2,'الممتحنة',"Al-Mumtahina",'Examining Her','Medinan'],[5163,14,109,2,'الصف',"As-Saff",'The Ranks','Medinan'],[5177,11,110,2,'الجمعة',"Al-Jumu'a",'Friday','Medinan'],[5188,11,104,2,'المنافقون',"Al-Munafiqun",'The Hypocrites','Medinan'],[5199,18,108,2,'التغابن',"At-Taghabun",'Mutual Disillusion','Medinan'],[5217,12,99,2,'الطلاق',"At-Talaq",'Divorce','Medinan'],[5229,12,107,2,'التحريم',"At-Tahrim",'The Prohibition','Medinan'],[5241,30,77,2,'الملك',"Al-Mulk",'The Sovereignty','Meccan'],[5271,52,2,2,'القلم',"Al-Qalam",'The Pen','Meccan'],[5323,52,78,2,'الحاقة',"Al-Haqqa",'The Reality','Meccan'],[5375,44,79,2,'المعارج',"Al-Ma'arij",'The Ascending Stairways','Meccan'],[5419,28,71,2,'نوح',"Nuh",'Noah','Meccan'],[5447,28,40,2,'الجن',"Al-Jinn",'The Jinn','Meccan'],[5475,20,3,2,'المزمل',"Al-Muzzammil",'The Enshrouded One','Meccan'],[5495,56,4,2,'المدثر',"Al-Muddathir",'The Cloaked One','Meccan'],[5551,40,31,2,'القيامة',"Al-Qiyama",'The Resurrection','Meccan'],[5591,31,98,2,'الانسان',"Al-Insan",'Human','Medinan'],[5622,50,33,2,'المرسلات',"Al-Mursalat",'The Emissaries','Meccan'],[5672,40,80,2,'النبإ',"An-Naba'",'The Announcement','Meccan'],[5712,46,81,2,'النازعات',"An-Nazi'at",'Those Who Drag Forth','Meccan'],[5758,42,24,1,'عبس',"Abasa",'He Frowned','Meccan'],[5800,29,7,1,'التكوير',"At-Takwir",'The Folding Up','Meccan'],[5829,19,82,1,'الإنفطار',"Al-Infitar",'The Cleaving','Meccan'],[5848,36,86,1,'المطففين',"Al-Mutaffifin",'Defrauding','Meccan'],[5884,25,83,1,'الإنشقاق',"Al-Inshiqaq",'The Splitting Open','Meccan'],[5909,22,27,1,'البروج',"Al-Buruj",'The Constellations','Meccan'],[5931,17,36,1,'الطارق',"At-Tariq",'The Morning Star','Meccan'],[5948,19,8,1,'الأعلى',"Al-A'la",'The Most High','Meccan'],[5967,26,68,1,'الغاشية',"Al-Ghashiya",'The Overwhelming','Meccan'],[5993,30,10,1,'الفجر',"Al-Fajr",'The Dawn','Meccan'],[6023,20,35,1,'البلد',"Al-Balad",'The City','Meccan'],[6043,15,26,1,'الشمس',"Ash-Shams",'The Sun','Meccan'],[6058,21,9,1,'الليل',"Al-Lail",'The Night','Meccan'],[6079,11,11,1,'الضحى',"Ad-Dhuha",'The Morning Hours','Meccan'],[6090,8,12,1,'الشرح',"Ash-Sharh",'The Consolation','Meccan'],[6098,8,28,1,'التين',"At-Tin",'The Fig','Meccan'],[6106,19,1,1,'العلق',"Al-Alaq",'The Clot','Meccan'],[6125,5,25,1,'القدر',"Al-Qadr",'The Power, Fate','Meccan'],[6130,8,100,1,'البينة',"Al-Bayyina",'The Evidence','Medinan'],[6138,8,93,1,'الزلزلة',"Az-Zalzala",'The Earthquake','Medinan'],[6146,11,14,1,'العاديات',"Al-Adiyat",'The Chargers','Meccan'],[6157,11,30,1,'القارعة',"Al-Qari'a",'The Calamity','Meccan'],[6168,8,16,1,'التكاثر',"At-Takathur",'Competition','Meccan'],[6176,3,13,1,'العصر',"Al-Asr",'The Time','Meccan'],[6179,9,32,1,'الهمزة',"Al-Humaza",'The Traducer','Meccan'],[6188,5,19,1,'الفيل',"Al-Fil",'The Elephant','Meccan'],[6193,4,29,1,'قريش',"Quraysh",'Quraysh','Meccan'],[6197,7,17,1,'الماعون',"Al-Ma'un",'Almsgiving','Meccan'],[6204,3,15,1,'الكوثر',"Al-Kawthar",'Abundance','Meccan'],[6207,6,18,1,'الكافرون',"Al-Kafirun",'The Disbelievers','Meccan'],[6213,3,114,1,'النصر',"An-Nasr",'Divine Support','Medinan'],[6216,5,6,1,'المسد',"Al-Masad",'The Palm Fibre','Meccan'],[6221,4,22,1,'الإخلاص',"Al-Ikhlas",'Purity of Faith','Meccan'],[6225,5,20,1,'الفلق',"Al-Falaq",'The Dawn','Meccan'],[6230,6,21,1,'الناس',"An-Nas",'Mankind','Meccan'],[6236,1]],Juz:[[],[1,1],[2,142],[2,253],[3,93],[4,24],[4,148],[5,82],[6,111],[7,88],[8,41],[9,93],[11,6],[12,53],[15,1],[17,1],[18,75],[21,1],[23,1],[25,21],[27,56],[29,46],[33,31],[36,28],[39,32],[41,47],[46,1],[51,31],[58,1],[67,1],[78,1],[115,1]],Page:[[],[1,1],[2,1],[2,6],[2,17],[2,25],[2,30],[2,38],[2,49],[2,58],[2,62],[2,70],[2,77],[2,84],[2,89],[2,94],[2,102],[2,106],[2,113],[2,120],[2,127],[2,135],[2,142],[2,146],[2,154],[2,164],[2,170],[2,177],[2,182],[2,187],[2,191],[2,197],[2,203],[2,211],[2,216],[2,220],[2,225],[2,231],[2,234],[2,238],[2,246],[2,249],[2,253],[2,257],[2,260],[2,265],[2,270],[2,275],[2,282],[2,283],[3,1],[3,10],[3,16],[3,23],[3,30],[3,38],[3,46],[3,53],[3,62],[3,71],[3,78],[3,84],[3,92],[3,101],[3,109],[3,116],[3,122],[3,133],[3,141],[3,149],[3,154],[3,158],[3,166],[3,174],[3,181],[3,187],[3,195],[4,1],[4,7],[4,12],[4,15],[4,20],[4,24],[4,27],[4,34],[4,38],[4,45],[4,52],[4,60],[4,66],[4,75],[4,80],[4,87],[4,92],[4,95],[4,102],[4,106],[4,114],[4,122],[4,128],[4,135],[4,141],[4,148],[4,155],[4,163],[4,171],[4,176],[5,3],[5,6],[5,10],[5,14],[5,18],[5,24],[5,32],[5,37],[5,42],[5,46],[5,51],[5,58],[5,65],[5,71],[5,77],[5,83],[5,90],[5,96],[5,104],[5,109],[5,114],[6,1],[6,9],[6,19],[6,28],[6,36],[6,45],[6,53],[6,60],[6,69],[6,74],[6,82],[6,91],[6,95],[6,102],[6,111],[6,119],[6,125],[6,132],[6,138],[6,143],[6,147],[6,152],[6,158],[7,1],[7,12],[7,23],[7,31],[7,38],[7,44],[7,52],[7,58],[7,68],[7,74],[7,82],[7,88],[7,96],[7,105],[7,121],[7,131],[7,138],[7,144],[7,150],[7,156],[7,160],[7,164],[7,171],[7,179],[7,188],[7,196],[8,1],[8,9],[8,17],[8,26],[8,34],[8,41],[8,46],[8,53],[8,62],[8,70],[9,1],[9,7],[9,14],[9,21],[9,27],[9,32],[9,37],[9,41],[9,48],[9,55],[9,62],[9,69],[9,73],[9,80],[9,87],[9,94],[9,100],[9,107],[9,112],[9,118],[9,123],[10,1],[10,7],[10,15],[10,21],[10,26],[10,34],[10,43],[10,54],[10,62],[10,71],[10,79],[10,89],[10,98],[10,107],[11,6],[11,13],[11,20],[11,29],[11,38],[11,46],[11,54],[11,63],[11,72],[11,82],[11,89],[11,98],[11,109],[11,118],[12,5],[12,15],[12,23],[12,31],[12,38],[12,44],[12,53],[12,64],[12,70],[12,79],[12,87],[12,96],[12,104],[13,1],[13,6],[13,14],[13,19],[13,29],[13,35],[13,43],[14,6],[14,11],[14,19],[14,25],[14,34],[14,43],[15,1],[15,16],[15,32],[15,52],[15,71],[15,91],[16,7],[16,15],[16,27],[16,35],[16,43],[16,55],[16,65],[16,73],[16,80],[16,88],[16,94],[16,103],[16,111],[16,119],[17,1],[17,8],[17,18],[17,28],[17,39],[17,50],[17,59],[17,67],[17,76],[17,87],[17,97],[17,105],[18,5],[18,16],[18,21],[18,28],[18,35],[18,46],[18,54],[18,62],[18,75],[18,84],[18,98],[19,1],[19,12],[19,26],[19,39],[19,52],[19,65],[19,77],[19,96],[20,13],[20,38],[20,52],[20,65],[20,77],[20,88],[20,99],[20,114],[20,126],[21,1],[21,11],[21,25],[21,36],[21,45],[21,58],[21,73],[21,82],[21,91],[21,102],[22,1],[22,6],[22,16],[22,24],[22,31],[22,39],[22,47],[22,56],[22,65],[22,73],[23,1],[23,18],[23,28],[23,43],[23,60],[23,75],[23,90],[23,105],[24,1],[24,11],[24,21],[24,28],[24,32],[24,37],[24,44],[24,54],[24,59],[24,62],[25,3],[25,12],[25,21],[25,33],[25,44],[25,56],[25,68],[26,1],[26,20],[26,40],[26,61],[26,84],[26,112],[26,137],[26,160],[26,184],[26,207],[27,1],[27,14],[27,23],[27,36],[27,45],[27,56],[27,64],[27,77],[27,89],[28,6],[28,14],[28,22],[28,29],[28,36],[28,44],[28,51],[28,60],[28,71],[28,78],[28,85],[29,7],[29,15],[29,24],[29,31],[29,39],[29,46],[29,53],[29,64],[30,6],[30,16],[30,25],[30,33],[30,42],[30,51],[31,1],[31,12],[31,20],[31,29],[32,1],[32,12],[32,21],[33,1],[33,7],[33,16],[33,23],[33,31],[33,36],[33,44],[33,51],[33,55],[33,63],[34,1],[34,8],[34,15],[34,23],[34,32],[34,40],[34,49],[35,4],[35,12],[35,19],[35,31],[35,39],[35,45],[36,13],[36,28],[36,41],[36,55],[36,71],[37,1],[37,25],[37,52],[37,77],[37,103],[37,127],[37,154],[38,1],[38,17],[38,27],[38,43],[38,62],[38,84],[39,6],[39,11],[39,22],[39,32],[39,41],[39,48],[39,57],[39,68],[39,75],[40,8],[40,17],[40,26],[40,34],[40,41],[40,50],[40,59],[40,67],[40,78],[41,1],[41,12],[41,21],[41,30],[41,39],[41,47],[42,1],[42,11],[42,16],[42,23],[42,32],[42,45],[42,52],[43,11],[43,23],[43,34],[43,48],[43,61],[43,74],[44,1],[44,19],[44,40],[45,1],[45,14],[45,23],[45,33],[46,6],[46,15],[46,21],[46,29],[47,1],[47,12],[47,20],[47,30],[48,1],[48,10],[48,16],[48,24],[48,29],[49,5],[49,12],[50,1],[50,16],[50,36],[51,7],[51,31],[51,52],[52,15],[52,32],[53,1],[53,27],[53,45],[54,7],[54,28],[54,50],[55,17],[55,41],[55,68],[56,17],[56,51],[56,77],[57,4],[57,12],[57,19],[57,25],[58,1],[58,7],[58,12],[58,22],[59,4],[59,10],[59,17],[60,1],[60,6],[60,12],[61,6],[62,1],[62,9],[63,5],[64,1],[64,10],[65,1],[65,6],[66,1],[66,8],[67,1],[67,13],[67,27],[68,16],[68,43],[69,9],[69,35],[70,11],[70,40],[71,11],[72,1],[72,14],[73,1],[73,20],[74,18],[74,48],[75,20],[76,6],[76,26],[77,20],[78,1],[78,31],[79,16],[80,1],[81,1],[82,1],[83,7],[83,35],[85,1],[86,1],[87,16],[89,1],[89,24],[91,1],[92,15],[95,1],[97,1],[98,8],[100,10],[103,1],[106,1],[109,1],[112,1],[115,1]]};
//function lol(){
//var t ;
//var di = document.getElementById("article_body");

//for(i=1;i<d.Sura.length;i++)
//	t+=d.Sura[i][6]+", ";
//di.innerText=t;
//	
//}
//lol();