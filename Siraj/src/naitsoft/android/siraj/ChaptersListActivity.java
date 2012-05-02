package naitsoft.android.siraj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;

public class ChaptersListActivity extends FragmentActivity {


	private ChaptersAdapter chapterAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.chapters_frag);

//		ActionBar actionBar = this.getSupportActionBar();
//	    actionBar.setDisplayHomeAsUpEnabled(true);
		
		ActionBar actionBar = this.getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;

	}

	
	private void callActivity(int pos)
	{
		Intent intent = new Intent(this,SirajActivity.class);
		intent.putExtra("idBook", chapterAdapter.idBook);
		intent.putExtra("idChap",chapterAdapter.getChapter(pos).idChap);
		startActivity(intent);
	}
}
