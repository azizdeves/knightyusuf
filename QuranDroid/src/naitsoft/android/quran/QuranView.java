package naitsoft.android.quran;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
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
	
	public QuranView(Context context) {
		super(context);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(50);
        mPaint.setTypeface(Typeface.SERIF);
        mPaint.setColor(Color.WHITE);
        text ="\u0631\u064e\u0628\u0651\u0650\u064a \u0641\u0650\u064a \u0643\u0650\u062a\u064e\u0627\u0628\u064d " +
        		"\u06d6 \u0644\u0651\u064e\u0627 \u064a\u064e\u0636\u0650\u0644\u0651\u0650 \u064f";

        text = DariGlyphUtils.reshapeText(text);

        Rect rec = new  Rect();
        mPaint.getTextBounds("\u0644", 0, 1, rec);
        currentLine = stepLine = (int) (rec.height()*1.75);
        
//        setOnClickListener(new OnClickListener() {
			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				v.get
//				
//			}
//		});
	}
	
    @Override 
    protected void onDraw(Canvas canvas) {

    	canvas.translate(canvas.getWidth(), 0);
    	canvas.save();

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
        	//canvas.drawText(text.charAt(i)+"", curseur, 50, mPaint);
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
    			
    			cnvs.drawText(text.charAt(i)+"", xpos[i], w.line, mPaint);
    		}
    	}
    }
    
    private int getLine(int yPos){
    	
    	return (int) ((yPos-stepLine*0.3) / stepLine );
    }
    
    private Word getWord(MotionEvent evt){
    	return null;
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	if(event.getAction()!= 0)
    		return false;
    	this.invalidate();
		float i = event.getX()-getWidth();
		log = event.getX()+" : "+event.getY()+" : "+getLine((int)event.getY());
		return true;
	}

	private int newLine(){
    	curseur = 0;
    	return currentLine+=stepLine;
    	
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
		return line +=70;
	}
	
	
}
