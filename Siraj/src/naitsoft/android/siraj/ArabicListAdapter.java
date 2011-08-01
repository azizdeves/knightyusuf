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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ArabicListAdapter implements ListAdapter {

	private String text;
	private Paint paint;
	int width;
	SirajActivity activity;
	ArrayList<TextLine> lines = new ArrayList<TextLine>();
	private LayoutInflater mInflater;
	private DataBaseHelper myDbHelper;
	boolean isLineInit=false;
	
	
	public ArabicListAdapter(SirajActivity activ){
		mInflater = activ.getLayoutInflater(); 
		activity = activ;
		paint=activ.paint;
		initDB();
		loadChapter();
		
	}
	private void constructLine(){
//		width = 800;
		int cursor;
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
			if((lineWidth += ArabicTextView.getCharWidth(paint, text, i,w))> width){
				
				lines.add(new TextLine(text.substring(startCur, lastSpace)));
				i = lastSpace;
				isNewLine = true;
			}

		}
		isLineInit = true;
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
	private void loadChapter(){
		text = myDbHelper.getChapter(1, 5);
		text = DariGlyphUtils.reshapeText(text);
	}
	public void initDB()
	{
		myDbHelper = new DataBaseHelper(activity);
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

		return 0;
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
		constructLine();
	}

}

class TextLine{
	String txt;

	public TextLine(String text) {
		txt = text;
	}

	public String getText(){
		return txt;
	}
	public void setText(String txt){
		this.txt = txt;
	}
}
