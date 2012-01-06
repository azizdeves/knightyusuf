package naitsoft.android.siraj;

import java.io.IOException;
import java.util.ArrayList;



import android.database.DataSetObserver;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class ArabicListAdapter implements ListAdapter , ListView.OnScrollListener{

	private String text;
	private Paint paint;
	int width;
	SirajActivity activity;
	static ArrayList<TextLine> lines = new ArrayList<TextLine>();
	private LayoutInflater mInflater;
	private DataBaseHelper myDbHelper;
//	boolean isLineInit=false;
	static public  boolean mBusy;
	
	
	public ArabicListAdapter(SirajActivity activ){
		mInflater = activ.getLayoutInflater(); 
		activity = activ;
		paint=activ.paint;
		initDB();
//		loadChapter(activ.livre,activ.chapitre);
		
	}
	private void constructLine(){
//		width = 800;
		lines.clear();
		int numLine = 0;
		int startCur = 0;
		int endCur = 0;
		int lastSpace = -1;
		boolean isNewLine=false;
		int lineWidth=0;
		float[] w = new float[1];
		for(int i = 0; i<text.length(); i++){
			if(text.charAt(i)==' ')
				lastSpace = i;

			if(isNewLine){
				startCur = i;
				lineWidth = 0;
				lastSpace = -1;
				isNewLine = false;

			}
			if(text.charAt(i)=='\n'){
				if(startCur!=i)
				{
					lines.add(new TextLine(text.substring(startCur, i),numLine++));
					isNewLine = true;
				}
				continue;
				
			}
			if((lineWidth += ArabicTextView.getCharWidth(ArabicTextView.mPaint, text, i,w))> width){
				
				lines.add(new TextLine(text.substring(startCur, lastSpace),numLine++));
				i = lastSpace;
				isNewLine = true;
			}

		}
//		isLineInit = true;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
//		if(!isLineInit){
//			width = view.getWidth();
//			constructLine();
//		}
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
//		txt.setTxtSize(10f);
 		
		holder.arabText.setLine(lines.get(position));
		return view;
	}
	public void loadChapter(int idBook, int idChap){
		text = myDbHelper.getChapter(idBook, idChap);//wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
		text = DariGlyphUtils.reshapeText(text);
	}
	
	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	       switch (scrollState) {
	        case OnScrollListener.SCROLL_STATE_IDLE:
	            mBusy = false;
	            
	            int first = view.getFirstVisiblePosition();
	            int count = view.getChildCount();
	            for (int i=0; i<count&& !mBusy; i++) {
	            	ViewHolder holder = (ViewHolder)view.getChildAt(i).getTag();
	            	holder.arabText.invalidate();
//	                if (t.getTag() != null) {
//	                    t.invalidate();
//	                }
	            }
	            
	            break;
	        case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
	            mBusy = false;
	            break;
	        case OnScrollListener.SCROLL_STATE_FLING:
	            mBusy = true;
	            break;
	        }
	}
	
	public void initDB()
	{
		myDbHelper = DataBaseHelper.getInstance(activity);
	}
	
	public static String getTextFromSelection(TextSelection select){
		if(select.startCursor.numLine == select.endCursor.numLine)
			return lines.get(select.startCursor.numLine).getText().substring(select.startCursor.idxChar, select.endCursor.idxChar);
		StringBuffer buf = new StringBuffer();
		buf.append(lines.get(select.startCursor.numLine).getText().substring(select.startCursor.idxChar));
		for(int i = select.startCursor.numLine+1;i<select.endCursor.numLine;i++){
			buf.append(lines.get(i).getText());
		}
		buf.append(lines.get(select.endCursor.numLine).getText().substring(0, select.endCursor.idxChar));
		return buf.toString();
	}
	@Override
	public int getCount() {

		return lines.size();
	}

	@Override
	public Object getItem(int position) {

		return lines.get(position);
	}

	@Override
	public long getItemId(int position) {

		return lines.get(position).numLine;
	}

	@Override
	public int getItemViewType(int position) {

		return 0;
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
	public void registerDataSetObserver(DataSetObserver observer) {


	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {


	}

	@Override
	public boolean areAllItemsEnabled() {

		return false;
	}

	@Override
	public boolean isEnabled(int arg0) {

		return true;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Paint getPaint() {
		return paint;
	}
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
		if(text == null)return;
		constructLine();
	}


}

class TextLine{
	String txt;
	int numLine;

	public TextLine(String text, int numLine) {
		txt = text;
		this.numLine = numLine;
	}

	public String getText(){
		return txt;
	}
	public void setText(String txt){
		this.txt = txt;
	}
}
