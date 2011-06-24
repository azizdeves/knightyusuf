package naitsoft.android.quran;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;

public class QuranView extends View {

	Paint paint;
	private Paint mPaint;
	String text;
	ArrayList<Word> wrds = new ArrayList<Word>();
	private int currentLine=50;
	private int curseur;
	private float[] widths;
	
	public QuranView(Context context) {
		super(context);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(30);
        mPaint.setTypeface(Typeface.SERIF);
        mPaint.setColor(Color.WHITE);
        text ="\u0631\u064e\u0628\u0651\u0650\u064a \u0641\u0650\u064a \u0643\u0650\u062a\u064e\u0627\u0628\u064d \u06d6 \u0644\u0651\u064e\u0627 \u064a\u064e\u0636\u0650\u0644\u0651\u064f";
        float[] widths = new float[text.length()];
        mPaint.getTextWidths(text, widths);
        
//        ArabicReshaper resh = new ArabicReshaper(text);
//        text = resh.getReshapedWord();

        text = DariGlyphUtils.reshapeText(text);
	}
	
    @Override 
    protected void onDraw(Canvas canvas) {

    	canvas.translate(canvas.getWidth(), 0);

        widths = new float[text.length()];
        mPaint.getTextWidths(text, widths);
        
        curseur=0;
        int left,right;
        Word wrd = new Word();
       
        boolean isNewWrd = true;
        for(int i = 0; i<text.length(); i++){
        	if(isNewWrd){
        		wrd.rect.right = curseur;
        		wrd.idxRtxt = i;
        	}
        	if((curseur -= widths[i])< -canvas.getWidth()){
        		newLine();
        		i= wrd.idxRtxt+1;
        		wrd.rect.right = curseur;
        		wrd.rect.left = curseur -= widths[i];
        	}
        	//canvas.drawText(text.charAt(i)+"", curseur, 50, mPaint);
        	wrd.line = currentLine;
        	wrd.rect.left = curseur;
        	wrd.idxLtxt = i;
        	widths[i]=curseur;
        	
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
    	
    }
    
    public ArrayList<Word> getWords(String txt){
        float[] widths = new float[txt.length()];
        mPaint.getTextWidths(txt, widths);
        wrds = new ArrayList<Word>();
        String[] token = txt.split(" ");
        return wrds;
    }
    
    private void drawWords(Canvas cnvs)
    {
    	int i;
    	for(Word w : wrds){
    		for(i=w.idxRtxt;i<=w.idxLtxt;i++){
    			
    			cnvs.drawText(text.charAt(i)+"", widths[i], w.line, mPaint);
    		}
    	}
    }
    
    private int newLine(){
    	curseur = 0;
    	return currentLine+=50;
    	
    }
    
    
    
//    protected String reverse(String s){
//    	
//    	return s.get;
//    }

}

class Word{
	public Rect rect;
	public String txt;
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
		return line +=30;
	}
	
	
}
