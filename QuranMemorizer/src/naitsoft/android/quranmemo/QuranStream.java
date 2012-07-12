package naitsoft.android.quranmemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.SlidingDrawer;

public class QuranStream extends View implements OnGestureListener {

	int stepLines[];
	private Bitmap map;
	int height;
	int page = 4;
	String sPage;
	private GestureDetector gestDetect = new GestureDetector(this);
	Paint paint = new Paint();
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();
	int streamCur ;
	int streamLine;
	int speed;
	private Bitmap streamMap;
	private float xStick;

	public QuranStream(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	


	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setColor(Color.WHITE);
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
		if (map == null || height != getHeight()) {
			height = getHeight();
			sPage = QuranMemorizerActivity.addZero(page);
			map = Saf7a.loadBitmap(sPage);
			map = Bitmap.createScaledBitmap(map, getWidth(), getHeight(), true);
			stepLines = null;
		}
		if (stepLines == null) {
			stepLines = Saf7a.calculStepLines(map);
			
		}
		
		if(streamCur>getWidth()){
			streamLine++;
			if(streamLine==stepLines.length-2){
				map = null;
				page ++;
				streamLine = 0;
				invalidate();
				return;
			}
			streamCur=0;
		}
		if(speed >=0 ){
		srcRect.set(0, stepLines[streamLine], getWidth(), stepLines[streamLine+1]);
		dstRect.set(streamCur, 50, getWidth()+streamCur, 50+stepLines[streamLine+1]-stepLines[streamLine]);
		canvas.drawBitmap(map, srcRect, dstRect, paint);
	
		srcRect.set(0, stepLines[streamLine+1], getWidth(), stepLines[streamLine+2]);
		dstRect.set(streamCur-getWidth(),50, streamCur, 50+stepLines[streamLine+2]-stepLines[streamLine+1]);
		canvas.drawBitmap(map, srcRect, dstRect, paint);
		streamCur += speed;
		}
		
//		try {
//			Thread.currentThread().sleep(500);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if(speed!=0)
			invalidate();

	}
/*	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setColor(Color.WHITE);
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
		if (map == null || height != getHeight()) {
			height = getHeight();
			sPage = QuranMemorizerActivity.addZero(page);
			map = Saf7a.loadBitmap(sPage);
			map = Bitmap.createScaledBitmap(map, getWidth(), getHeight(), true);
			stepLines = null;
		}
		if (stepLines == null) {
			stepLines = Saf7a.calculStepLines(map);
			streamMap = Bitmap.createBitmap(stepLines.length * getWidth(), stepLines[1],  Config.RGB_565);
			Canvas c = new Canvas(streamMap);
			for(int i = 0; i<stepLines.length-1; ){
				srcRect.set(0, stepLines[i], getWidth(), stepLines[i+1]);
				dstRect.set(i*getWidth(), 0, ++i*getWidth(), stepLines[streamLine+1]);
				c.drawBitmap(map, srcRect, dstRect, paint);
			}
		}
		
		canvas.drawBitmap(streamMap,streamCur, 50 , paint);
//		
//		if(streamCur>getWidth()){
//			streamLine++;
//			if(streamLine==stepLines.length-2)
//				streamLine = 0;
//			streamCur=0;
//		}
//		srcRect.set(0, stepLines[streamLine], getWidth(), stepLines[streamLine+1]);
//		dstRect.set(streamCur, 50, getWidth()+streamCur, 50+stepLines[streamLine+1]-stepLines[streamLine]);
//		canvas.drawBitmap(map, srcRect, dstRect, paint);
//	
//		srcRect.set(0, stepLines[streamLine+1], getWidth(), stepLines[streamLine+2]);
//		dstRect.set(streamCur-getWidth(),50, streamCur, 50+stepLines[streamLine+2]-stepLines[streamLine+1]);
//		canvas.drawBitmap(map, srcRect, dstRect, paint);
		streamCur += 3;
//		try {
//			Thread.currentThread().sleep(500);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		invalidate();

	}
*/



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

//			gestDetect.onTouchEvent(event);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xStick = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			speed = (int) (xStick - event.getX())/25;
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			speed = 0;
			break;
		}
 
		return true;
	}



	@Override
	public void onLongPress(MotionEvent event) {
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		
		return true; 
	}
		

	@Override
	public boolean onDown(MotionEvent e) {

		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(Math.abs(velocityX)>700 && Math.abs(velocityY)<500){
		}else
		if(Math.abs(velocityY)>700 && Math.abs(velocityX)<500){
				
		}
				return false;

	}

	public void setPage(int i) {
		page = i;
		sPage = QuranMemorizerActivity.addZero(page);
		
	}

}
