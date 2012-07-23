package naitsoft.android.quranmemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class SubhaActivity extends Activity {

	private static DataBaseHelper myDbHelper;
	private QuranStream stream;
	private Dialog dialog;
	private EditText pageText;
	private Spinner dikrSpinner;
	private ImageButton backBtn;
	private ImageButton pauseBtn;
	private SubhaView subhaView;
	static SubhaActivity activity;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		activity = this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.subha);
		subhaView = (SubhaView) findViewById(R.id.subha);
		dikrSpinner = (Spinner) findViewById(R.id.dikrSpinner);
		initDB();
		if(bundle != null){
		}
		backBtn = (ImageButton) findViewById(R.id.backBtn); 
		pauseBtn = (ImageButton) findViewById(R.id.pauseBtn); 
		
		backBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent streamIntent = new Intent(activity,QuranStreamActivity.class);
				startActivity(streamIntent);
				finish();
			}
		});
		pauseBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(	subhaView.status == SubhaView.PAUSED )
					subhaView.setStatus( SubhaView.COUNTING);
				else
					subhaView.setStatus(SubhaView.PAUSED);
			}
		});
		ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		spinAdapter.add("لا إله إلا الله");
		spinAdapter.add("الصلاة على رسول الله ص");
		spinAdapter.add("الإستغفار");
		spinAdapter.add("التسبيح");
		
		dikrSpinner.setAdapter(spinAdapter );
		dikrSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});

	}

	public void initDB()
	{
		myDbHelper = DataBaseHelper.getInstance(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
	}
	@Override
	protected void onRestoreInstanceState(Bundle bundle) {
		super.onRestoreInstanceState(bundle);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences.Editor pref= PreferenceManager.getDefaultSharedPreferences(this).edit();
		pref.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		menu.add(0,1,1,"Memo");
		menu.add(0,2,2,"Stream");
		menu.add(0,3,3,"Seek");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem 
			item) {
		switch(item.getItemId()){
		case 1:
			Intent memoIntent = new Intent(this,QuranMemorizerActivity.class);
			startActivity(memoIntent);
			finish();
			break;
		case 2:
			Intent streamIntent = new Intent(this,SubhaActivity.class);
			startActivity(streamIntent);
			finish();
			break;
		case 3:
			showDialog(0);
			pageText.setText(String.valueOf(stream.page));
			break;
		}

		return true;
	}
	
	@Override
	public void onBackPressed() {
	}
}
