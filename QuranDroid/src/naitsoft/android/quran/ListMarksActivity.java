package naitsoft.android.quran;

import android.app.ListActivity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ListMarksActivity extends ListActivity {

	private LayoutInflater mInflater;

	public ListMarksActivity(){
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = (LayoutInflater) this.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		ListMarkAdapter markAdapter = new ListMarkAdapter(mInflater);
		setListAdapter(markAdapter);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}


class ListMarkAdapter implements ListAdapter
{
	private LayoutInflater mInflater;
	public ListMarkAdapter(LayoutInflater inflater)
	{
		mInflater = inflater;
	}
	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public Object getItem(int pos) {
		return ""+pos;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public int getItemViewType(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if(view == null){
			view = mInflater.inflate(R.layout.list_item, parent,false);
			
		}
		
		TextView txt = (TextView) view.findViewById(R.id.textView1);
		TextView txt1 = (TextView) view.findViewById(R.id.textView2);
		txt.setText(""+position);
		txt1.setText("kjkj"+position);
		return view;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
		
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int arg0) {
		return true;
	}
}
