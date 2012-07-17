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
	private int oldSpeed;
	private boolean isSliding;

	public QuranStream(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint.setTextSize(20);
	}
	


	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setColor(Color.WHITE);
		canvas.drawRect(0, 0, getWidth(), getHeight()/3, paint);
		paint.setColor(Color.BLUE);
		 
	
		if (map == null || height != getHeight()) {
			height = getHeight();
			sPage = QuranMemorizerActivity.addZero(page);
			map = Saf7a.loadBitmap(sPage);
			map = Bitmap.createScaledBitmap(map, getWidth(), getHeight(), true);
			stepLines = null;
		}
		if (stepLines == null) {
			stepLines = Saf7a.calculStepLines(map);
			
			
			if(speed>0)
				streamLine = 0;
			else
				if(speed<0)
					streamLine = stepLines.length-3;
			speed = 0;
			isSliding = false;
			
		}
		
		if(streamCur>getWidth() || streamCur < 0){
			if(speed>=0){
				streamLine++;
				streamCur=0;
			}
			else{
				streamCur=getWidth();
				streamLine--;
			}
			if((speed>0 && streamLine==stepLines.length-1 )|| (speed<0 && streamLine==-1)){
				map = null;
				page += streamLine == -1 ?-1: 1;
				
				invalidate();
				return; 
			}
			
		}
		canvas.drawText("page:"+sPage+" line:"+streamLine, 10, 20, paint);
			srcRect.set(0, stepLines[streamLine], getWidth(), stepLines[streamLine+1]);
			dstRect.set(streamCur, 100, getWidth()+streamCur, 100+stepLines[streamLine+1]-stepLines[streamLine]);
			canvas.drawBitmap(map, srcRect, dstRect, paint);

			if(streamLine+2<stepLines.length){
				srcRect.set(0, stepLines[streamLine+1], getWidth(), stepLines[streamLine+2]);
				dstRect.set(streamCur-getWidth(),100, streamCur, 100+stepLines[streamLine+2]-stepLines[streamLine+1]);
				canvas.drawBitmap(map, srcRect, dstRect, paint);
			}
			streamCur += speed;
		if(speed!=0)
			invalidate();

	}



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);


		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isSliding = true;
			xStick = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
//			oldSpeed = speed;
			if(!isSliding)break;
			speed = (int) (xStick - event.getX())/35;
//			if(oldSpeed * speed<0)
//				streamLine--;
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			speed = 0;
			isSliding = false;
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
		invalidate();
		
	}




	public void init() {
		// TODO Auto-generated method stub
		
	}

}
