package naitsoft.android.siraj;

import android.view.MotionEvent;


public class TextSelection {
	static private TextSelection currentSelection;
	TextCursor startCursor;
	TextCursor endCursor;
	TextCursor editingCursor;
	static Focusable focusA ;
	static Focusable focusB ;
	
	public TextSelection(){
		startCursor = new TextCursor();
		endCursor = new TextCursor();
		editingCursor = endCursor;
		initFocus();
		
	}
	
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
				return SirajActivity.status==SirajActivity.SELECTING || SirajActivity.status==SirajActivity.SELECTED;
			}
		};
		focusA.textCursor = startCursor;
		focusB.textCursor = endCursor;
		
	}
	
	protected void handleTouchEvent(Focusable cur, MotionEvent ev, int indexLine) {
		TextSelection select;
//		int indexLine = SirajActivity.listTextLineView.getIndexLine(ev.getY());
		if(ev.getAction()== MotionEvent.ACTION_DOWN){
			SirajActivity.status = SirajActivity.SELECTING;
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
			SirajActivity.listTextLineView.invalidate();
		}
		if(ev.getAction()== MotionEvent.ACTION_UP){ 
			SirajActivity.status = SirajActivity.SELECTED;
			
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
		return currentSelection = new TextSelection();
	}
	public static boolean isLineInSelction(int numLine) {
		return currentSelection.startCursor.numLine>= numLine && currentSelection.endCursor.numLine <= numLine
				|| currentSelection.startCursor.numLine <= numLine && currentSelection.endCursor.numLine >= numLine;
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
	
}
class TextCursor{
	int numLine;
	int x;
	int idxChar;
}
