package naitsoft.android.siraj;

import java.util.ArrayList;

import android.app.ListActivity;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class ChaptersListActivity extends ListActivity {

	
}

class ChaptersAdapter implements ListAdapter
{

	ArrayList<Chapter> chapters;
	private LayoutInflater mInflater;

	
	public ChaptersAdapter(SirajActivity activ){
		mInflater = activ.getLayoutInflater(); 
		
		activity = activ;
		paint=activ.paint;
		initDB();
		loadChapter();
		
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
 		if(view == null){
			view = mInflater.inflate(R.layout.layout_mark, parent,false);
			holder = new ViewHolder();
			holder.arabText = (ArabicTextView) view.findViewById(R.id.textView);
			view.setTag(holder);
		}
 		else{
 			holder = (ViewHolder) view.getTag();
 		}
		holder.arabText.setText(chapters.get(position).title);
		return view;
	}

	@Override
	public int getViewTypeCount() {
		return 0;
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
	public int getCount() {
		return chapters.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		return 1;
	}
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int arg0) {
		return true;
	}
	
}
class Chapter{
	public String title;
	public char level;
}