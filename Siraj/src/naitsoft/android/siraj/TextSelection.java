package naitsoft.android.siraj;

import android.view.MotionEvent;


public class TextSelection {
	static private TextSelection currentSelection;
	TextCursor startCursor;
	TextCursor endCursor;
	TextCursor editingCursor;
	static Focusable focusA ;
	static Focusable focusB ;
	static MarkUI markUi;
	
	public TextSelection(){ 
		startCursor = new TextCursor();
		endCursor = new TextCursor();
		editingCursor = endCursor;
		initFocus();
		
	}
	
//	public MarkUI getMarkUi(){
//		
//	}
	
	private void initFocus(){
		focusA = new Focusable(android.R.drawable.ic_input_add) {			
			public void onTouchEvent(MotionEvent ev, int indexLine) {
				handleTouchEvent(this, ev,  indexLine);				
			}
		};
		focusB = new Focusable(android.R.drawable.ic_input_add) {			
			public void onTouchEvent(MotionEvent ev, int indexLine) {
				handleTouchEvent(this, ev,  indexLine);					
			}
			public boolean isVisible() {
				return ArticleFragment.status==ArticleFragment.SELECTING || ArticleFragment.status==ArticleFragment.SELECTED;
			}
		};
		focusA.textCursor = startCursor;
		focusB.textCursor = endCursor;
		
	}
	
	protected void handleTouchEvent(Focusable cur, MotionEvent ev, int indexLine) {
		TextSelection select;
//		int indexLine = ArticleFragment.listTextLineView.getIndexLine(ev.getY());
		if(ev.getAction()== MotionEvent.ACTION_DOWN){
			ArticleFragment.status = ArticleFragment.SELECTING;
//			select = TextSelection.getCurrentSelection();
//			select.setStartNumLine(indexLine);
//			select.setEndNumLine(indexLine);
//			select.setStartX( (int) ev.getX());
//			select.setEndX( (int) ev.getX());
		}
		if(ev.getAction()== MotionEvent.ACTION_MOVE){
			select = TextSelection.getCurrentSelection();
			cur.textCursor.numLine = indexLine-1;
			cur.textCursor.x = (int) ev.getX();
			ArticleFragment.articleTextView.invalidate();
		}
		if(ev.getAction()== MotionEvent.ACTION_UP){ 
			ArticleFragment.status = ArticleFragment.SELECTED;
			
		}
		
	}
	
	public static Focusable getTouchedCurseur(int x, int numLine) {
		if(focusA.line== numLine)
			if(focusB.line == numLine){
				if(Math.abs(x-focusA.x)<Math.abs(x-focusB.x)) 
					return focusA;
				else 
					return focusB;
			}
			else
				return focusA;
		if(focusB.line== numLine)
			return focusB;
		
		return null;
	}

	public static TextSelection getCurrentSelection(){
		
		return currentSelection;
	}
	public static TextSelection getInstance(){
		markUi = null;
		return currentSelection = new TextSelection();
	}
	public static boolean isLineInSelction(int numLine) {
		return currentSelection.startCursor.numLine>= numLine && currentSelection.endCursor.numLine <= numLine
				|| currentSelection.startCursor.numLine <= numLine && currentSelection.endCursor.numLine >= numLine;
	}
	
	public MarkUI getMarkUi(int width){
		return new MarkUI(currentSelection.focusA.x+width, currentSelection.focusB.x+width, currentSelection.startCursor.numLine, currentSelection.endCursor.numLine, 0);
	}
	
	public int getStartNumLine(){
		return currentSelection.startCursor.numLine;
	}
	public int getEndNumLine(){
		return currentSelection.endCursor.numLine;
	}
	public int getStartX(){
		return currentSelection.startCursor.x;
	}
	public int getEndX(){
		return currentSelection.endCursor.x;
	}
	public void initOrder(){
		if(getStartNumLine() == getEndNumLine()){
			if(getStartX()<getEndX()){
				swapCursor();
			}
		}
		else
			if(getStartNumLine() > getEndNumLine()){
				swapCursor();
				
			}
	}
	
	private void swapCursor(){
		TextCursor tmp;
		tmp = currentSelection.startCursor;
		startCursor = currentSelection.endCursor;
		currentSelection.endCursor = tmp;
		//editingCursor = editingCursor == startCursor? endCursor : startCursor;
	}

	public String getText(){
		
		return "";
	}
	public void setEndNumLine(int indexLine) {
		currentSelection.endCursor.numLine = indexLine;
	}

	public void setStartNumLine(int indexLine) {
		currentSelection.startCursor.numLine = indexLine;
	}

	public void setStartX(int x) {
		currentSelection.startCursor.x = x;
	}

	public void setEndX(int x) {
		currentSelection.endCursor.x = x;
	}

	public static void clear() {
		currentSelection = null;		
	}
	
	
}
class TextCursor{
	int numLine;
	int x;
	int idxChar;
}
class MarkUI{
	boolean isActive;
	int startX;
	int endX;
	int startLine;
	int endLine;
	int markId;
	MarkUI next;
	public MarkUI(int startX, int endX, int startLine, int endLine, int markId) {
		super();
		isActive = true;
		this.startX = startX;
		this.endX = endX;
		this.startLine = startLine;
		this.endLine = endLine;
		this.markId = markId;
	}
	
	static public MarkUI getMarkUiByEvent(MarkUI mrk , int line , int x){
		MarkUI m = null;
		while(mrk != null){
			if(mrk.isActive){
				if(mrk.startLine == line){
					if(mrk.startX >= x){
						if(mrk.endLine == line){
							if(mrk.endX <= x){
								m = mrk;
							}
						}
						else
							m=mrk;
					}
				}else
					if(mrk.endLine == line){
						if(mrk.startX <= x){
							m=mrk;
						}
					}
					else
						if(mrk.startLine < line && mrk.endLine > line){
							m = mrk;
						}
			}
			mrk = mrk.next;
		}
		return m;
	}
	
}
class Mark{
	int markId;
	int type;
	int startChar;
	int endChar;
	int idChap;
	int idBook;
	String note;
	public Mark(){
		
	}
	public Mark(int markId, int i, int j, int idChap,
			int idBook, String note) {
		super();
		this.markId = markId;
		this.startChar = i;
		this.endChar = j;
		this.idChap = idChap;
		this.idBook = idBook;
		this.note = note;
	}
	
}
