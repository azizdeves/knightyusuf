package naitsoft.android.siraj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class MarksListFragment extends Fragment {

	private MarkListAdapter markListAdapter;
	private ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		markListAdapter = new MarkListAdapter(this);
		Bundle bund = getActivity().getIntent().getExtras();
		if(bund != null){ 
			markListAdapter.setIdBook( bund.getInt("idBook")==0?-1:bund.getInt("idBook"));
			markListAdapter.setIdChap( bund.getInt("idChap")==0?-1:bund.getInt("idChap"));
		}
		markListAdapter.init();

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v =  inflater.inflate(R.layout.chapters, container);
		 lv = (ListView)(v.findViewById(R.id.listChapters));
		lv.setAdapter(markListAdapter); 
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
				if(markListAdapter.loadMark(pos))
					markListAdapter.notifyDataSetChanged();
				else
				{
					callActivity(pos);
				}
			}
		});
		
		setHasOptionsMenu(true);
		ActionBar actionBar = ((FragmentActivity) getActivity()).getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
		return v;
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.layout.menu, menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){

		case android.R.id.home:
			Intent intent = new Intent(getActivity(), BooksListActivity.class);
			startActivity(intent);
			break;
		}
	   
		return true;
	}
	
	
	private void callActivity(int pos)
	{
		Intent intent = new Intent(this.getActivity(),SirajActivity.class);
		intent.putExtra("idBook", markListAdapter.markItems.get(pos).mark.idBook);
		intent.putExtra("idChap",markListAdapter.markItems.get(pos).mark.idChap);
		startActivity(intent);
	}
	

}
