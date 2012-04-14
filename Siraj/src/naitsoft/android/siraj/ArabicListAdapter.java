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

	private String text;
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


	public ArabicListAdapter(SirajActivity activ){
		mInflater = activ.getLayoutInflater(); 
		activity = activ;
		paint=SirajActivity.paint;
		initDB();
		//		loadChapter(activ.livre,activ.chapitre);

	}
	private void constructLine(){
		text = "تيب خسيهت بسي بنت سيخهت يبمنت يسبهت خسيب منس تيب هيسب";
		lines.clear();
		short numLine = 0;
		short startCur = 0;
		short lastSpace = -1;
		boolean isNewLine=false;
		boolean isBuildingMarkUi = false;
		Mark currentMark = null;
		MarkUI currentMarkUi = null;
		short lineWidth=0;
		float[] w = new float[1];
		marksUi = new ArrayList<MarkUI>();
		marks = new ArrayList<Mark>();
		currentMarks = new ArrayList<Mark>();
		tmpMarks = new ArrayList<Mark>();
		HashMap<Mark, MarkUI> markMap = new HashMap<Mark, MarkUI>();
		width = -width;
		marks.add(new Mark(0, 2, 10, 0, 0, ""));

		//		Iterator<Mark> iterMark = marks.iterator();
		int indexCurrentMark=0;
		//		if()
		//			currentMark = iterMark.next();

		marksUi.add(null);
		for(short i = 0; i<text.length(); i++){
			
			if(text.charAt(i)==' ')
				lastSpace = i;
			if(isNewLine){
				startCur = i;
				lineWidth = 0;
				lastSpace = -1;
				isNewLine = false;
				marksUi.add(null);
			}
			
			while(indexCurrentMark<marks.size() && marks.get(indexCurrentMark).startChar <= i)
				if(marks.get(indexCurrentMark).startChar == i){ 
					currentMarks.add(marks.get(indexCurrentMark));
					currentMarkUi = new MarkUI(lineWidth,lineWidth, numLine, numLine, marks.get(indexCurrentMark).markId);
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

			
			if(text.charAt(i)=='\n'){
				if(startCur!=i)
				{
					lines.add(new TextLine(text.substring(startCur, i),numLine++));
					isNewLine = true;
				}
				continue;
			}
			if((lineWidth -= ArabicTextView.getCharWidth(ArabicTextView.mPaint, text, i,w))< width){

				lines.add(new TextLine(text.substring(startCur, lastSpace),numLine++));
				i = lastSpace;
				isNewLine = true;
			}


//			if(!markMap.isEmpty()){
//				for(MarkUI mrkUi : markMap.values()){
//					mrkUi.endX = lineWidth;
//					mrkUi.endLine = numLine;
//				}
//
//			}
//			else
//				marksUi.add(null);
		}
		//		isLineInit = true;
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

	private void constructSimppeLine(){
		lines.clear();
		short numLine = 0;
		short startCur = 0;
		short lastSpace = -1;
		boolean isNewLine=false;
		boolean isBuildingMarkUi = false;
		Mark currentMark = null;
		MarkUI currentMarkUi = null;
		int lineWidth=0;
		float[] w = new float[1];
		marksUi = new ArrayList<MarkUI>();
		marks = new ArrayList<Mark>();

		Iterator<Mark> iterMark = marks.iterator();
		if(iterMark.hasNext())
			currentMark = iterMark.next();

		for(short i = 0; i<text.length(); i++){

			if(currentMark !=null){
				if(isBuildingMarkUi){

					if(text.charAt(i) == currentMark.endChar){				
						isBuildingMarkUi = false;
						currentMarkUi.endX = lineWidth;
						currentMarkUi.endLine = numLine;
						if(iterMark.hasNext())
							currentMark = iterMark.next();
						else
							currentMark = null;

					}

					if(isNewLine)
						marksUi.add(currentMarkUi);
				}else{
					if(text.charAt(i) == currentMark.startChar){				
						isBuildingMarkUi = true;
						currentMarkUi = new MarkUI(lineWidth,lineWidth,numLine,numLine, currentMark.markId);

						marksUi.add(currentMarkUi); 
					}

				}
			}else{
				if(isNewLine)
					marksUi.add(currentMarkUi);

			}
			if(text.charAt(i)==' ')
				lastSpace = i;
			if(isNewLine){
				startCur = i;
				lineWidth = 0;
				lastSpace = -1;
				isNewLine = false;
				//				if(isBuildingMarkUi)

			}
			if(text.charAt(i)=='\n'){
				if(startCur!=i)
				{
					lines.add(new TextLine(text.substring(startCur, i),numLine++));
					isNewLine = true;
				}
				continue;

			}
			//			if((lineWidth += ArabicTextView.getCharWidth(ArabicTextView.mPaint, text, i,w))> width){
			if((lineWidth = ArabicTextView.mPaint.getTextWidths( text, startCur,i+1,w))> width){
				lines.add(new TextLine(text.substring(startCur, lastSpace),numLine++));
				i = lastSpace;
				isNewLine = true;
			}

		}
		if(isBuildingMarkUi){
			currentMarkUi.endX = lineWidth;
			currentMarkUi.endLine = numLine;
		}
		else
			marksUi.add(null);

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
			view = mInflater.inflate(R.layout.rich_text_line, parent,false);
			holder = new ViewHolder();
			holder.arabText = (RichArabicTextView) view.findViewById(R.id.textView);
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
		text = myDbHelper.getChapter(idBook, idChap);//.substring(0,300);//wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
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
		return m;
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
	public void saveMark(Mark mrk) {
		marks.add(mrk);

	}
	public void updateMarkUi(MarkUI m) {
		for(int i = m.startLine;i<=m.endLine;i++){
			marksUi.add(i, m);
		}
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
