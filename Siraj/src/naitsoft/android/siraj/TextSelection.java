package naitsoft.android.siraj;


public class TextSelection {
	static private TextSelection currentSelection;
	TextCursor startCursor;
	TextCursor endCursor;
	TextCursor editingCursor;
	
	public TextSelection(){
		startCursor = new TextCursor();
		endCursor = new TextCursor();
		editingCursor = endCursor;
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
