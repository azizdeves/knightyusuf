package naitsoft.android.siraj;


public class TextSelection {
	static private TextSelection currentSelection;

	int numLineStart;
	int numLineEnd;
	int xStart;
	int xEnd;
	
	public static TextSelection getCurrentSelection(){
		return currentSelection;
	}
	public static TextSelection getInstance(){
		return currentSelection = new TextSelection();
	}
	public static boolean isLineInSelction(int numLine) {
		return currentSelection.numLineEnd >= numLine && currentSelection.numLineStart <= numLine
				|| currentSelection.numLineEnd <= numLine && currentSelection.numLineStart >= numLine;
	}
	
}
