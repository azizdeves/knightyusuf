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
			public void onTouchEvent(MotionEvent ev) {
				handleTouchEvent(ev);				
			}
		};
		focusB = new Focusable(android.R.drawable.ic_input_add) {			
			public void onTouchEvent(MotionEvent ev) {
				handleTouchEvent(ev);				
			}
			public boolean isVisible() {
				return SirajActivity.status==SirajActivity.SELECTING || SirajActivity.status==SirajActivity.SELECTED;
			}
		};
		
	}
	
	protected void handleTouchEvent(MotionEvent ev) {
		TextSelection select;
		int indexLine = SirajActivity.listTextLineView.getIndexLine(ev.getY());
		if(ev.getAction()== MotionEvent.ACTION_DOWN){
			SirajActivity.status = SirajActivity.SELECTING;
			select = TextSelection.getCurrentSelection();
			select.setStartNumLine(indexLine);
			select.setEndNumLine(indexLine);
			select.setStartX( (int) ev.getX());
			select.setEndX( (int) ev.getX());
		}
		if(ev.getAction()== MotionEvent.ACTION_MOVE){
			select = TextSelection.getCurrentSelection();
			select.editingCursor.numLine = indexLine;
			select.editingCursor.x = (int) ev.getX();
			SirajActivity.listTextLineView.invalidate();
		}
		if(ev.getAction()== MotionEvent.ACTION_UP){
			SirajActivity.status = SirajActivity.SELECTED;
			
		}
		
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
