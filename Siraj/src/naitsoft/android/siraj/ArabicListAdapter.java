package naitsoft.android.siraj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;



import android.database.DataSetObserver;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class ArabicListAdapter implements ListAdapter , ListView.OnScrollListener{

	private Chapter chapter;
	ArrayList<Mark> marks ;
	ArrayList<Mark> currentMarks ;
	static ArrayList<MarkUI> marksUi ;
	private Paint paint;
	int width;
	SirajActivity activity;
	static ArrayList<TextLine> lines = new ArrayList<TextLine>();
	private LayoutInflater mInflater;
	private DataBaseHelper myDbHelper;
	private ArrayList<Mark> tmpMarks;
	//	boolean isLineInit=false;
	static public  boolean mBusy;
	private ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>();
	private int lastCharDrawn;


	public ArabicListAdapter(SirajActivity activ){
		mInflater = activ.getLayoutInflater(); 
		activity = activ;
		paint=SirajActivity.paint;
		initDB();

	}
	private void constructLine(){
		lines.clear();
		short numLine = 0;
		short startCur = 0;
		short lastSpace = -1;
		boolean isNewLine=false;
		MarkUI currentMarkUi = null;
		short lineWidth=0;
		float[] w = new float[1];
		marksUi = new ArrayList<MarkUI>();
//		marks = new ArrayList<Mark>();
		currentMarks = new ArrayList<Mark>();
		tmpMarks = new ArrayList<Mark>();
		HashMap<Mark, MarkUI> markMap = new HashMap<Mark, MarkUI>();
		width = -width;
		String text = chapter.getContent();
		int indexCurrentMark=0;
		marksUi.add(null);
		for(short i = 0; i<text.length(); i++){

			if(isNewLine){
				marksUi.add(null);
			}
				
			while(indexCurrentMark<marks.size() && marks.get(indexCurrentMark).startChar <= i)
				if(marks.get(indexCurrentMark).startChar == i){ 
					currentMarks.add(marks.get(indexCurrentMark)); 
					currentMarkUi = new MarkUI(lineWidth,lineWidth, numLine, numLine, marks.get(indexCurrentMark));
					addMarkUi(numLine,currentMarkUi);
					markMap.put(marks.get(indexCurrentMark++), currentMarkUi);
				}

			if(!currentMarks.isEmpty()){
				if(isNewLine){				
					addMarkUi(numLine, marksUi.get(numLine-1));
				}

				initCurrentMarksByEndChar(i);
				if(!tmpMarks.isEmpty()){	
					for(Mark mrk:tmpMarks){
						currentMarkUi = markMap.get(mrk);
						currentMarkUi.endX = lineWidth;
						currentMarkUi.endLine = numLine;
						currentMarks.remove(mrk);
						markMap.remove(mrk);
					}
				}
			}

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
					lines.add(new TextLine(text.substring(startCur, i).concat(" "),numLine++,startCur));
					isNewLine = true;
				}
				continue;
			}
			if((lineWidth -= ArabicTextView.getCharWidth(ArabicTextView.mPaint, text, i,w))< width){
				lines.add(new TextLine(text.substring(startCur, lastSpace+1),numLine++,startCur));
				i = lastSpace;
				isNewLine = true;
			}

		}
	}
	private void addMarkUi(int numLine, MarkUI currentMarkUi) {
		if(marksUi.get(numLine)==null)
			marksUi.set(numLine, currentMarkUi); 
		else 
			marksUi.get(numLine).next = currentMarkUi;

	}
	/**
	 * return la liste des Mark qui ont le startChar passé en parametre
	 * @return
	 */
	private void initMarkByStartChar( int startChar, int indexList){
		//		tmpMarks.clear();
		while(indexList<marks.size() && marks.get(indexList).startChar <= startChar)
			if(marks.get(indexList).startChar == startChar)
				currentMarks.add(marks.get(indexList++));

		//		return currentMarks;
	}
	private void initCurrentMarksByEndChar(int endChar){
		tmpMarks.clear();
		for(Mark m : currentMarks){
			if(m.endChar == endChar)
				tmpMarks.add(m);
		}
		//		return tmpMarks;

	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder holder;
		if(view == null){
			view = mInflater.inflate(R.layout.rich_text_line, parent,false);
			holder = new ViewHolder();
			holder.arabText = (RichArabicTextView) view.findViewById(R.id.textView);
			view.setTag(holder);
		}
		else{
			holder = (ViewHolder) view.getTag();
		}

		((ArabicTextView) holder.arabText).setLine(lines.get(position));
		lastCharDrawn = lines.get(position).startChar;
		return view;
	}
	public void loadChapter(int idBook, int idChap){ 
		chapter = myDbHelper.getChapter(idBook, idChap);//.substring(0,300);//wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
		chapter.setContent( DariGlyphUtils.reshapeText(chapter.getContent()));
		marks = myDbHelper.getMarks(idBook, idChap);
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
		try{

			if(select.startCursor.numLine == select.endCursor.numLine)
				return lines.get(select.startCursor.numLine).getText().substring(select.startCursor.idxChar, select.endCursor.idxChar);
			StringBuffer buf = new StringBuffer();
			buf.append(lines.get(select.startCursor.numLine).getText().substring(select.startCursor.idxChar));
			for(int i = select.startCursor.numLine+1;i<select.endCursor.numLine;i++){
				buf.append(lines.get(i).getText());
				buf.append(" ");
			}
			buf.append(lines.get(select.endCursor.numLine).getText().substring(0, select.endCursor.idxChar));
			return buf.toString();
		}catch(Exception e){
			return "";
		}
	}

	public Mark getAbsoluteTextSelectPosition(TextSelection select){
		if(select == null)
			return null;
		short s=0,e=0,i=0;
		for(;i<select.startCursor.numLine;i++){

			s+=lines.get(i).txt.length();
		}
		e=s;
		s+=select.startCursor.idxChar;
		for(;i<select.endCursor.numLine;i++){

			e+=lines.get(i).txt.length();
		}
		e+=select.endCursor.idxChar;
		Mark m = new Mark();
		m.startChar = s;
		m.endChar = e; 
		m.idBook = ArticleFragment.articleFrag.livre;
		m.idChap = ArticleFragment.articleFrag.chapitre;
		m.type = select.markUi.mark.type;
		return m;
	}
	@Override
	public int getCount() {

		return lines.size();
	}

	public int getLastCharDrawn() {
		return lastCharDrawn;
	}
	public void setLastCharDrawn(int lastCharDrawn) {
		this.lastCharDrawn = lastCharDrawn;
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


	public void registerDataSetObserver(DataSetObserver observer) {
	    observers.add(observer);
	}
	public void notifyDataSetChanged(){
	    for (DataSetObserver observer: observers) {
	        observer.onChanged();
	    }
	}
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		observers.remove(observer);

	}

	@Override
	public boolean areAllItemsEnabled() {

		return false;
	}

	@Override
	public boolean isEnabled(int arg0) {

		return true;
	}
//	public String getText() {
//		return text;
//	}
//	public void setText(String text) {
//		this.text = text;
//	}
	public Paint getPaint() {
		return paint;
	}
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	public int getWidth() {
		return width;
	}
	
	public Chapter getChapter() {
		return chapter;
	}
	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}
	public void setWidth(int width) {
		this.width = width;
		if(chapter == null)return;
		constructLine();
	}
	public void saveMark(Mark mrk, boolean isPersist) {
		if(isPersist)
			myDbHelper.updateMark(mrk);
		else{
			myDbHelper.addMark(mrk);
			marks.add(mrk);
		}

	}
	public void deleteMark(int id){
		myDbHelper.deleteMark(id);
	}
	public void updateMarkUi(MarkUI m) {
		for(int i = m.startLine;i<=m.endLine;i++){
			addMarkUi(i, m);
		}
	}


}

class TextLine{
	String txt;
	int numLine;
	int startChar;

	public TextLine(String text, int numLine, int startChar) {
		this.startChar = startChar;
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
