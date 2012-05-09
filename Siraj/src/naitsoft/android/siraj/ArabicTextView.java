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
import android.text.style.LineHeightSpan.WithDensity;
import android.util.AttributeSet;
import android.util.Config;
import android.view.MotionEvent;
import android.view.View;

public class ArabicTextView extends View {

	//	private ArabicTextEventListener  eventListener;
	static public Paint mPaint = new Paint();
	static int a,b;
//	static final int backColor = Color.rgb(238, 255, 237);
	static final int backColor = Color.WHITE;
	static final int fontColor = Color.BLACK;
	String text;
	TextLine line ;
	static int stepLine ;
	static int margin;
	protected int width;
	boolean dirty = true;
	private Bitmap map;
	protected Rect clsRect = new Rect();
	float[] w = new float[200];
	//	private float startSelct = -1;
	//	private float endSelct;
	public ArabicTextView(Context context,AttributeSet attr) {
		super(context,attr);
		//Typeface mFace = Typeface.createFromAsset(getContext().getAssets(),"fonts/Scheherazade.ttf");

		//        mPaint = BooksListActivity.paint;
	}

	public void init()
	{
		mPaint.getTextBounds("\u0644", 0, 1, clsRect);
		stepLine = (int) (clsRect.height()*2.5);
		invalidate();

	}

	@Override  
	protected void onDraw(Canvas canvas) {
		//    	SirajActivity.text.setText(frame++);
		//    	width = canvas.getWidth();
		canvas.translate(canvas.getWidth(), 0);
		mPaint.setColor(backColor);
		clsRect.set(-width, 0, 0, canvas.getHeight());
		canvas.drawRect(clsRect, mPaint);
		if(text == null)
			return;
		mPaint.setColor(fontColor); 
		//        int prevCharWitdh=0;
		int cur=0;
//		w = new float[text.length()];
		mPaint.getTextWidths(text, w);
		//        	canvas.translate(canvas.getWidth(), 0);

		float y = stepLine/1.5f;

		//Light draw
//		if(ArabicListAdapter.mBusy){
		if(true){
			canvas.drawText(text, 0, y, mPaint);
			return;
		}

		//Heavy draw lettre by lettre
		for(int i =0, length=text.length(); i<length;i++){
			if(text.charAt(i)=='\n')continue;
			canvas.drawText(text, i, i+1, cur,  y, mPaint);
			if(!DariGlyphUtils.isHaraka(text.charAt(i))){
				cur-=w[i];
				if(ArticleFragment.status == ArticleFragment.SELECTING || ArticleFragment.status == ArticleFragment.SELECTED)
					w[i]=cur;
			}
			//        		prevCharWitdh = (int) ArabicTextView.getCharWidth(mPaint, text, i,w);
		}

	}
	
//	private void drawMark()

	public TextLine getLine() {
		return line;
	}

	public void setLine(TextLine line) {
		this.line = line;
		setText(line.getText());

	}

	public void setText(String text){
		this.text = text;
		init();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		//    	if(!SirajActivity.selecting)
		//    		return false;
		//    	if(event.getAction()== MotionEvent.ACTION_DOWN){
		//    		startSelct = event.getX();
		//    	}
		//    	if(event.getAction()== MotionEvent.ACTION_MOVE){
		//    		
		//    		endSelct = event.getX();
		//    		invalidate();
		//    	}
		//    	if(event.getAction()== MotionEvent.ACTION_UP){
		//    		SirajActivity.selecting = false;
		//    	}
		//    	return true;
		return false;

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		width = MeasureSpec.getSize(widthMeasureSpec);
		MyListView.stepLine = stepLine;
		setMeasuredDimension(width, stepLine);
	}

	static public float getCharWidth(Paint paint, String text, int indxTxt, float[] w ){
//		if(DariGlyphUtils.isHaraka(text.charAt(indxTxt)))
//			return 0f;

		paint.getTextWidths(text, indxTxt, indxTxt+1, w);
		return w[0]; 
	}

	public String getText() {
		return text;
	}

	//	public void setText(String text,int cursorBegin) {
	//		this.text = text.substring(cursorBegin);
	//		init();
	//	}


	public float getTxtSize() {
		return mPaint.getTextSize();
	}

	public void setTxtSize(float txtSize) {
		mPaint.setTextSize(txtSize);
	}

}

//interface ArabicTextEventListener{
//	public void onClick(ArabicTextEvent event);
//	public void onTouch(ArabicTextEvent event);
//}

//class ArabicTextEvent{
//
//	final static public int SLIDE_RIGHT = 1;
//	final static public int SLIDE_LEFT = 0;
//	
//	private MotionEvent event;
//	private Word word; 
//	private int dirct;
//
//	public ArabicTextEvent(MotionEvent event){
//		this.event = event;
//	}
//	public ArabicTextEvent(MotionEvent event, Word word){
//		this.event = event;
//		this.word = word;
//	}
//	public ArabicTextEvent(MotionEvent event,  int slideDirect){
//		this.event = event;
//		dirct = slideDirect;
//	}
//
//	public MotionEvent getEvent() {
//		return event;
//	}
//
//	public void setEvent(MotionEvent event) {
//		this.event = event;
//	}
//
//	public Word getWord() {
//		return word;
//	}
//
//	public void setWord(Word word) {
//		this.word = word;
//	}
//
//	public int getDirct() {
//		return dirct;
//	}
//
//	public void setDirct(int dirct) {
//		this.dirct = dirct;
//	}
//	
//}
class ViewHolder{
	public View arabText;

}
