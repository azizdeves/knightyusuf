package naitsoft.android.quranmemo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

public class QuranStreamActivity extends Activity {
    
	private static DataBaseHelper myDbHelper;
	private QuranStream stream;
	static QuranStreamActivity activity;
	
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        activity = this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.quran_stream);
        stream = (QuranStream) findViewById(R.id.stream);
        initDB();
        if(bundle != null)
        	stream.setPage(bundle.getInt("pageStream", 4));
        
    }

	public void initDB()
	{
		myDbHelper = DataBaseHelper.getInstance(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
		stream.streamCur= pref.getInt("streamCur",0);
		stream.streamLine= pref.getInt("streamLine",0);
		stream.setPage(pref.getInt("pageStream", 4));
	}
	@Override
	protected void onRestoreInstanceState(Bundle bundle) {
		super.onRestoreInstanceState(bundle);
		stream.streamCur= bundle.getInt("streamCur",0);
		stream.streamLine= bundle.getInt("streamLine",0);
		stream.setPage(bundle.getInt("pageStream", 4));
	}
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences.Editor pref= PreferenceManager.getDefaultSharedPreferences(this).edit();
		pref.putInt("pageStream", stream.page);
		pref.putInt("streamLine", stream.streamLine);
		pref.putInt("streamCur", stream.streamCur);
		pref.commit();
	}
}
