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
import android.widget.Button;
import android.widget.EditText;

public class QuranStreamActivity extends Activity {

	private static DataBaseHelper myDbHelper;
	private QuranStream stream;
	private Dialog dialog;
	private EditText pageText;
	static QuranStreamActivity activity;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		activity = this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.quran_stream);
		stream = (QuranStream) findViewById(R.id.stream);
		initDB();
		if(bundle != null){
			stream.streamLine= bundle.getInt("streamLine",0);
			stream.setScale(bundle.getFloat("scale",1));
			stream.setPage(bundle.getInt("pageStream", 4));
		}

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
		stream.setScale(pref.getFloat("scale",1));
		stream.setPage(pref.getInt("pageStream", 4));
	}
	@Override
	protected void onRestoreInstanceState(Bundle bundle) {
		super.onRestoreInstanceState(bundle);
		stream.streamCur= bundle.getInt("streamCur",0);
		stream.streamLine= bundle.getInt("streamLine",0);
		stream.setScale(bundle.getFloat("scale",1));
		stream.setPage(bundle.getInt("pageStream", 4));
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("pageStream", stream.page);
		outState.putInt("streamLine", stream.streamLine);
		outState.putInt("streamCur", stream.streamCur);
		outState.putFloat("scale", stream.scale);
	}
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences.Editor pref= PreferenceManager.getDefaultSharedPreferences(this).edit();
		pref.putInt("pageStream", stream.page);
		pref.putInt("streamLine", stream.streamLine);
		pref.putInt("streamCur", stream.streamCur);
		pref.putFloat("scale", stream.scale);
		pref.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		// Create the menu item and keep a reference to it.
		//		MenuItem menuItem = menu.add(groupId, menuItemId,
		//				menuItemOrder, menuItemText);

		//		menu.add(0,2,0,"Preference");
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
			//			markIntent.putExtra("aya", aya);
			//			markIntent.putExtra("sura", sura);
			startActivity(memoIntent);
			break;
		case 2:
			Intent streamIntent = new Intent(this,QuranStreamActivity.class);
			//			markIntent.putExtra("aya", aya);
			//			markIntent.putExtra("sura", sura);
			startActivity(streamIntent);
			break;
		case 3:
			showDialog(0);
			pageText.setText(String.valueOf(stream.page));
			break;
		}

		return true;
	}
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		//		return super.onCreateDialog(id, args);
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.page_select);
		pageText = (EditText) dialog.findViewById(R.id.pageTextSelect);
		Button goBtn = (Button) dialog.findViewById(R.id.go_btn);
		goBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				stream.setPage(Integer.parseInt(pageText.getText().toString()));
				dialog.dismiss();
				//				stream.init();
			}
		});
		return dialog;
	}
}
