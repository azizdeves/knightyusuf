package naitsoft.android.quran;

import java.util.ArrayList;
import java.util.HashMap;

import naitsoft.android.quran.DariGlyphUtils.Glyph;

import android.R.style;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Config;
import android.view.MotionEvent;
import android.view.View;

public class QuranView extends View {

	private QuranEventListener  eventListener;
	private int xStartDrag;
	Paint paint;
	private Paint mPaint;
	String text;
	ArrayList<Word> wrds = new ArrayList<Word>();
	private int currentLine;
	private int curseur;
	private float[] widths;
	private int[] xpos;
	int stepLine ;
	private HashMap<Integer, String> roots;
	private int width;
	boolean isDraging;
	private Paint harakaPaint;
	boolean dirty = true;
	private Bitmap map;
	private Rect clsRect = new Rect(-1000, 0, 0, 1000);
	
	public QuranView(Context context,AttributeSet attr) {
		super(context,attr);
		//Typeface mFace = Typeface.createFromAsset(getContext().getAssets(),"fonts/Scheherazade.ttf");
        harakaPaint = new Paint();
        harakaPaint.setAntiAlias(true);
        harakaPaint.setTextSize(30);
        harakaPaint.setStyle(Style.FILL);
        harakaPaint.setColor(Color.GREEN);
        harakaPaint.setAlpha(160);
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(30);
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(Color.WHITE);
	}
	
    public void init(boolean initText)
    {
    	if(initText){
    		text = DariGlyphUtils.reshapeText(text);
    		roots = DariGlyphUtils.getRoots();
    	}
        dirty = true;
        Rect rec = new  Rect();
        mPaint.getTextBounds("\u0644", 0, 1, rec);
        stepLine = (int) (rec.height()*3);
        currentLine = (int) (stepLine*0.75);
    	widths = new float[text.length()];
        xpos = new int[text.length()];
        mPaint.getTextWidths(text, widths);
        requestLayout(); 
        invalidate();
    }
    
    @Override 
    protected void onDraw(Canvas canvas) {
//    	width = canvas.getWidth();
    	canvas.translate(width, 0);
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(clsRect , mPaint);
        if(text == null)
        	return;
        drawWords(canvas);
        dirty = false;
    }
    
    private void constructWords(boolean toRequestLayout)
    {
    	try{
    	wrds.clear();
    	currentLine = (int) (stepLine*0.75);
         curseur=0;
         int left,right;
         Word wrd = new Word();
         boolean isNewWrd = true;
         for(int i = 0; i<text.length(); i++){
        	 float charWidth = getCharWidth(i);
         	if(isNewWrd){
         		wrd.rect.right = curseur;
         		wrd.idxRtxt = i;
         	}
         	if((curseur -= charWidth)< -width){
         		newLine();
         		i= wrd.idxRtxt;
         		charWidth = getCharWidth(i);
         		wrd.rect.right = curseur;
         		wrd.rect.left = curseur -= charWidth;
         	}
         	wrd.line = currentLine;
         	wrd.rect.left = curseur;
         	wrd.idxLtxt = i;
         	xpos[i]=curseur;
         	
         	if(text.charAt(i)==' '){
         		isNewWrd = true;
         		wrd.txt = text.substring(wrd.idxRtxt, wrd.idxLtxt);
         		wrds.add(wrd);
         		wrd = new Word();
         	}
         	else{
         		isNewWrd = false;
         	}
         }
         if(toRequestLayout)
        	 requestLayout();
    	}catch(Exception e){
    		System.out.print(e.getMessage());
    	}
    }
 
    private void drawWords(Canvas cnvs)
    {
    	if(dirty){
    		map = Bitmap.createBitmap(cnvs.getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
    		Canvas canvas = new Canvas(map);
    		canvas.translate(width, 0);
    		mPaint.setColor(Color.BLACK);
    		canvas.drawRect(new Rect(-1000, 0, 0, 1000), mPaint);
    		mPaint.setColor(Color.WHITE);
    		int i;
    		boolean stepUp = false;
    		for(Word w : wrds){
    			for(i=w.idxRtxt;i<=w.idxLtxt;i++){
    				if(DariGlyphUtils.isHaraka(text.charAt(i))){
    					canvas.drawText(text,i,i+1, xpos[i], stepUp?w.line-15:w.line, harakaPaint);
    					stepUp = true;
    				}
    				else{
    					stepUp = false;
    					canvas.drawText(text,i,i+1, xpos[i], w.line, mPaint);
    				}
    			}
    		}
    	}
    	cnvs.drawBitmap(map, -width, 0, mPaint);
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
    	if(event.getAction()== MotionEvent.ACTION_MOVE){
    		isDraging = true;
    	}
    	if(event.getAction()== MotionEvent.ACTION_DOWN){
    		xStartDrag = (int) event.getX();
    	}
    	if( event.getAction() == MotionEvent.ACTION_UP){
    		if(isDraging ){
    			if(Math.abs(xStartDrag - event.getX()) < width/3){
    				isDraging = false;
    				return false;
    			}
    			int direct = xStartDrag < event.getX() ? QuranEvent.SLIDE_RIGHT : QuranEvent.SLIDE_LEFT;
    			eventListener.onTouch(new QuranEvent(event, direct));

    		}else{
    			Word w = getWord((int)event.getX()-width, (int)event.getY());
    			if(w != null){
    				eventListener.onClick(new QuranEvent(event, w));
    			}
    			//this.invalidate();
    		}
    		isDraging = false;
    	}
    	return true;
	}
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	int width = MeasureSpec.getSize(widthMeasureSpec);
    	if(dirty){
    		this.width = width;
    		constructWords(false); 
    	}
    	int height = MeasureSpec.getSize(heightMeasureSpec);
    	int viewHeight = currentLine + stepLine;
    	setMeasuredDimension(width, viewHeight);
    }
    
    private float getCharWidth(int indxTxt){
    	if(DariGlyphUtils.isHaraka(text.charAt(indxTxt)))
    		return 0f;
    	return widths[indxTxt];
    }
    public String getRoot(Word w){ 
    	return roots.get(text.substring(w.idxRtxt, w.idxLtxt).hashCode());
    }

	private int newLine(){
    	curseur = 0;
    	return currentLine+=stepLine;
    }

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		init(true);
	}

	public QuranEventListener getEventListener() {
		return eventListener;
	}

	public void setEventListener(QuranEventListener eventListener) {
		this.eventListener = eventListener;
	}

	public float getTxtSize() {
		return mPaint.getTextSize();
	}

	public void setTxtSize(float txtSize) {
		mPaint.setTextSize(txtSize);
		harakaPaint.setTextSize(txtSize);
	}

}

class Word{
	public Rect rect;
	public String root;
	public int line;
	public int idxRtxt,idxLtxt;
	public String txt;
	
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

interface QuranEventListener{
	public void onClick(QuranEvent event);
	public void onTouch(QuranEvent event);
}

class QuranEvent{

	final static public int SLIDE_RIGHT = 1;
	final static public int SLIDE_LEFT = 0;
	
	private MotionEvent event;
	private Word word;
	private int dirct;

	public QuranEvent(MotionEvent event){
		this.event = event;
	}
	public QuranEvent(MotionEvent event, Word word){
		this.event = event;
		this.word = word;
	}
	public QuranEvent(MotionEvent event,  int slideDirect){
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
