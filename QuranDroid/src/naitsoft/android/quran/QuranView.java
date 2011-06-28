package naitsoft.android.quran;

import java.util.ArrayList;
import java.util.HashMap;

import naitsoft.android.quran.DariGlyphUtils.Glyph;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;

public class QuranView extends View {

	Paint paint;
	private Paint mPaint;
	String text;
	ArrayList<Word> wrds = new ArrayList<Word>();
	private int currentLine;
	private int curseur;
	private float[] widths;
	private int[] xpos;
	int stepLine ;
	String log="";
	private TextToSpeech tts;
	private HashMap<Integer, String> roots;
	
	public QuranView(Context context) {
		super(context);
		//tts = ((QuranDroidActivity)context).mTts;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(35);
        mPaint.setTypeface(Typeface.SERIF);
        mPaint.setColor(Color.WHITE);
        text ="\u0631\u064e\u0628\u0651\u0650\u064a \u0641\u0650\u064a \u0643\u0650\u062a\u064e\u0627\u0628\u064d " +
        		"\u06d6 \u0644\u0651\u064e\u0627 \u064a\u064e\u0636\u0650\u0644\u0651\u0650 \u064f";

        text = DariGlyphUtils.reshapeText(text);
        roots = DariGlyphUtils.getRoots();
        Rect rec = new  Rect();
        mPaint.getTextBounds("\u0644", 0, 1, rec);
        currentLine = stepLine = (int) (rec.height()*1.75);
	}
	
    @Override 
    protected void onDraw(Canvas canvas) {

    	canvas.translate(canvas.getWidth(), 0);
        widths = new float[text.length()];
        xpos = new int[text.length()];
        mPaint.getTextWidths(text, widths);
        currentLine = stepLine;
        curseur=0;
        int left,right;
        Word wrd = new Word();
        canvas.drawLine(0, currentLine, -300, currentLine, mPaint);
        boolean isNewWrd = true;
        for(int i = 0; i<text.length(); i++){
        	if(isNewWrd){
        		wrd.rect.right = curseur;
        		wrd.idxRtxt = i;
        	}
        	if((curseur -= widths[i])< -canvas.getWidth()){
        		canvas.drawLine(0, currentLine, -300, currentLine, mPaint);
        		newLine();
        		i= wrd.idxRtxt;
        		wrd.rect.right = curseur;
        		wrd.rect.left = curseur -= widths[i];
        	}
        	wrd.line = currentLine;
        	wrd.rect.left = curseur;
        	wrd.idxLtxt = i;
        	xpos[i]=curseur;
        	
        	if(text.charAt(i)==' '){
        		isNewWrd = true;
        		wrds.add(wrd);
        		wrd = new Word();
        	}
        	else{
        		isNewWrd = false;
        	}
        }
        drawWords(canvas);
        Paint plog = new Paint();
        plog.setTextSize(15);
        plog.setColor(Color.WHITE);
        canvas.drawText(log, -this.getWidth(),10, plog);
    }
    

    private void drawWords(Canvas cnvs)
    {
    	int i;
    	for(Word w : wrds){
    		for(i=w.idxRtxt;i<=w.idxLtxt;i++){
    			
    			cnvs.drawText(text.charAt(i)+"", xpos[i], w.line, mPaint);
    		}
    	}
    }
    
    private int getLine(int yPos){
    	
    	return (int) ((yPos-stepLine*0.3) / stepLine );
    }
    
    private Word getWord(int x, int y){
    	int line = getLine(y);
    	for(Word w : wrds){
    		if(getLine(w.line) == line && w.rect.right > x)
    			if(w.rect.left < x)
    				return w;
    	}
    	return null;
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	if(event.getAction()!= 0)
    		return false;
    	Word w = getWord((int)event.getX()-getWidth(), (int)event.getY());
    	if(w != null){
    		log = roots.get(text.substring(w.idxRtxt, w.idxLtxt).hashCode());//DariGlyphUtils.getRootWord(text.substring(w.idxRtxt, w.idxLtxt));
    		tts.speak(log,TextToSpeech.QUEUE_FLUSH, null);
    	}
    	this.invalidate();
		return true;
	}

	private int newLine(){
    	curseur = 0;
    	return currentLine+=stepLine;
    	
    }

	public TextToSpeech getTts() {
		return tts;
	}

	public void setTts(TextToSpeech tts) {
		this.tts = tts;
	}

}

class Word{
	public Rect rect;
	public String root;
	public int line;
	public int idxRtxt,idxLtxt;
	
	public Word(){
		rect = new Rect();
		line = 50;
	}
	
	public Rect getRect(){
		return rect;
	}
	public int newLIne()
	{
		return line +=70;
	}
	
	
}
