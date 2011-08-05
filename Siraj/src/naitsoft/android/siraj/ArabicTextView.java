package naitsoft.android.siraj;

import java.util.ArrayList;
import java.util.HashMap;


import android.R.style;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Config;
import android.view.MotionEvent;
import android.view.View;

public class ArabicTextView extends View {

	private ArabicTextEventListener  eventListener;
	private Paint mPaint;
	String text;
	TextLine line ;
	int stepLine ;
	private int width;
	boolean dirty = true;
	private Bitmap map;
	static int frame;
	private Rect clsRect = new Rect();
    float[] w = new float[1];
	public ArabicTextView(Context context,AttributeSet attr) {
		super(context,attr);
		//Typeface mFace = Typeface.createFromAsset(getContext().getAssets(),"fonts/Scheherazade.ttf");
        
        mPaint = SirajActivity.paint;
	}
	
    public int init(boolean initText)
    {
    	
        dirty = true;
        mPaint.getTextBounds("\u0644", 0, 1, clsRect);
        stepLine = (int) (clsRect.height()*2);

        invalidate();
        return 0;
    }
    
    @Override  
    protected void onDraw(Canvas canvas) {
//    	SirajActivity.text.setText(frame++);
    	width = canvas.getWidth();
    	canvas.translate(canvas.getWidth(), 0);
    	mPaint.setColor(Color.GRAY);
    	clsRect.set(-width, 0, 0, canvas.getHeight());
        canvas.drawRect(clsRect, mPaint);
        if(text == null)
        	return;
        mPaint.setColor(Color.WHITE); 
//        int prevCharWitdh=0;
        int cur=0;
        	w = new float[text.length()];
        	mPaint.getTextWidths(text, w);
//        	canvas.translate(canvas.getWidth(), 0);
        
        	float y = stepLine/1.5f;
        	
        	if(ArabicListAdapter.mBusy){
        		canvas.drawText(text, 0, y, mPaint);
        		return;
        	}
        	
        	for(int i =0, length=text.length(); i<length;i++){
        		canvas.drawText(text, i, i+1, cur,  y, mPaint);
        		if(!DariGlyphUtils.isHaraka(text.charAt(i)))
        			cur-=w[i];
//        		prevCharWitdh = (int) ArabicTextView.getCharWidth(mPaint, text, i,w);
        	}
//        	********************
//        	map = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_4444);
//    		Canvas cnvs = new Canvas(map);
//    		cnvs.translate(canvas.getWidth(), 0);
//        	for(int i =0; i<text.length();i++){
//        		
//        		cnvs.drawText(text,i,i+1, cur-=prevCharWitdh,  stepLine/1.5f, mPaint);
//        		prevCharWitdh = (int) ArabicTextView.getCharWidth(mPaint, text, i,w);
//        	}**************************
        
//        canvas.drawBitmap(map, -canvas.getWidth(), 0, mPaint);**************
    }
    
    private void constructWords(boolean toRequestLayout)
    {
//    	try{
//    	wrds.clear();
//    	currentLine = (int) (stepLine*0.75);
//         curseur=0;
//         int left,right;
//         Word wrd = new Word();
//         boolean isNewWrd = true;
//         for(int i = 0; i<text.length(); i++){
//        	 float charWidth = getCharWidth(i);
//         	if(isNewWrd){
//         		wrd.rect.right = curseur;
//         		wrd.idxRtxt = i;
//         	}
//         	if((curseur -= charWidth)< -width){
//         		newLine();
//         		i= wrd.idxRtxt;
//         		if(currentLine > this.getHeight()){
//         			
//         			break;
//         		}
//         		charWidth = getCharWidth(i);
//         		wrd.rect.right = curseur;
//         		wrd.rect.left = curseur -= charWidth;
//         	}
//         	wrd.line = currentLine;
//         	wrd.rect.left = curseur;
//         	wrd.idxLtxt = i;
//         	xpos[i]=curseur;
//         	
//         	if(text.charAt(i)==' '){
//         		isNewWrd = true;
//         		wrd.txt = text.substring(wrd.idxRtxt, wrd.idxLtxt);
//         		wrds.add(wrd);
//         		wrd = new Word();
//         	}
//         	else{
//         		isNewWrd = false;
//         	}
//         }
//         if(toRequestLayout)
//        	 requestLayout();
//    	}catch(Exception e){
//    		System.out.print(e.getMessage());
//    	}
    }
    

 
    public TextLine getLine() {
		return line;
	}

	public void setLine(TextLine line) {
		this.line = line;
		setText(line.getText());
		
	}
	
	public void setText(String text){
		this.text = text;
		init(true);
	}

	private void drawWords(Canvas cnvs)
    {
//    	if(dirty){
//    		map = Bitmap.createBitmap(cnvs.getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
//    		Canvas canvas = new Canvas(map);
//    		canvas.translate(canvas.getWidth(), 0);
//    		mPaint.setColor(Color.BLACK);
//    		canvas.drawRect(new Rect(-1000, 0, 0, 1000), mPaint);
//    		mPaint.setColor(Color.WHITE);
//    		int i;
//    		boolean stepUp = false;
//    		for(Word w : wrds){
//    			for(i=w.idxRtxt;i<=w.idxLtxt;i++){
//    				if(DariGlyphUtils.isHaraka(text.charAt(i))){
//    					canvas.drawText(text.charAt(i)+"", xpos[i], stepUp?w.line-15:w.line, harakaPaint);
//    					stepUp = true;
//    				}
//    				else{
//    					stepUp = false;
//    					canvas.drawText(text.charAt(i)+"", xpos[i], w.line, mPaint);
//    				}
//    			}
//    		}
//    	}
//    	cnvs.drawBitmap(map, -cnvs.getWidth(), 0, mPaint);
    }
    

    
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//    	if(event.getAction()== MotionEvent.ACTION_MOVE){
//    		isDraging = true;
//    	}
//    	if(event.getAction()== MotionEvent.ACTION_DOWN){
//    		xStartDrag = (int) event.getX();
//    	}
//    	if( event.getAction() == MotionEvent.ACTION_UP){
//    		if(isDraging ){
//    			if(Math.abs(xStartDrag - event.getX()) < width/3){
//    				isDraging = false;
//    				return false;
//    			}
//    			int direct = xStartDrag < event.getX() ? ArabicTextEvent.SLIDE_RIGHT : ArabicTextEvent.SLIDE_LEFT;
//    			eventListener.onTouch(new ArabicTextEvent(event, direct));
//
//    		}else{
//    			Word w = getWord((int)event.getX()-width, (int)event.getY());
//    			if(w != null){
//    				eventListener.onClick(new ArabicTextEvent(event, w));
//    			}
//    			//this.invalidate();
//    		}
//    		isDraging = false;
//    	}
//    	return true;
//	}
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	int width = MeasureSpec.getSize(widthMeasureSpec);
    	setMeasuredDimension(width, stepLine);
    }
    
    static public float getCharWidth(Paint paint, String text, int indxTxt,float[] w ){
    	if(DariGlyphUtils.isHaraka(text.charAt(indxTxt)))
    		return 0f;
		
		paint.getTextWidths(text, indxTxt, indxTxt+1, w);
		return w[0]; 
    }

	public String getText() {
		return text;
	}

	public int setText(String text,int cursorBegin) {
		this.text = text.substring(cursorBegin);
		return init(true);
	}

	public ArabicTextEventListener getEventListener() {
		return eventListener;
	}

	public void setEventListener(ArabicTextEventListener eventListener) {
		this.eventListener = eventListener;
	}

	public float getTxtSize() {
		return mPaint.getTextSize();
	}

	public void setTxtSize(float txtSize) {
		mPaint.setTextSize(txtSize);
	}

}

//class Word{
//	public Rect rect;
//	public String root;
//	public int line;
//	public int idxRtxt,idxLtxt;
//	public String txt;
//	
//	public Word(){
//		rect = new Rect();
//		line = 50;
//	}
//	
//	public Rect getRect(){
//		return rect;
//	}
//	public int newLIne()
//	{
//		return line +=70;
//	}
//}

interface ArabicTextEventListener{
	public void onClick(ArabicTextEvent event);
	public void onTouch(ArabicTextEvent event);
}

class ArabicTextEvent{

	final static public int SLIDE_RIGHT = 1;
	final static public int SLIDE_LEFT = 0;
	
	private MotionEvent event;
	private Word word; 
	private int dirct;

	public ArabicTextEvent(MotionEvent event){
		this.event = event;
	}
	public ArabicTextEvent(MotionEvent event, Word word){
		this.event = event;
		this.word = word;
	}
	public ArabicTextEvent(MotionEvent event,  int slideDirect){
		this.event = event;
		dirct = slideDirect;
	}

	public MotionEvent getEvent() {
		return event;
	}

	public void setEvent(MotionEvent event) {
		this.event = event;
	}

	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		this.word = word;
	}

	public int getDirct() {
		return dirct;
	}

	public void setDirct(int dirct) {
		this.dirct = dirct;
	}
	
}
class ViewHolder{
	public ArabicTextView arabText;
	
}
