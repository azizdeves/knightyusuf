package naitsoft.android.siraj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;

public class MarksListActivity extends FragmentActivity {


//	private ChaptersAdapter chapterAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.marks_frag);

//		ActionBar actionBar = this.getSupportActionBar();
//	    actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;

	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case 1: 
			break;
		case 2: 
//			Intent intent = new Intent(this, PreferencesActivity.class);
//			startActivity(intent);
			break;
		case 3:

			break;
		case android.R.id.home:
			Intent intent = new Intent(this, BooksListActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}
	private void callActivity(int pos)
	{
		Intent intent = new Intent(this,SirajActivity.class);
//		intent.putExtra("idBook", chapterAdapter.idBook);
//		intent.putExtra("idChap",chapterAdapter.getChapter(pos).idChap);
		startActivity(intent);
	}
}
