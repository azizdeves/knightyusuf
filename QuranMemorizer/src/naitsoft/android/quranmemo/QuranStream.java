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
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.MeasureSpec;
import android.widget.SlidingDrawer;

public class QuranStream extends View implements OnGestureListener ,ScaleGestureDetector.OnScaleGestureListener {

	static final int SLIDING = 1;
	static final int SCALING = 2;
	static final int IDLE = 0;
	
	int stepLines[];
	private Bitmap map;
	int height;
	int width;
	int mapWidth;
	int filter;
	
	float scale = 1;
	float oldScale = 1;
	int tmp;
	
	int page ;
	String sPage;
	private GestureDetector gestDetect = new GestureDetector(this);
	private ScaleGestureDetector scaleDetect = new ScaleGestureDetector(getContext(),this);
	Paint paint = new Paint();
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();
	int streamCur ;
	int streamLine;
	int speed;
	private Bitmap streamMap;
	private float xStick;
	private int status;
	private WakeLock wakeLock;
	int[] filters = {0,Color.YELLOW,Color.GREEN, Color.BLUE, Color.RED};

	public QuranStream(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint.setTextSize(20);
		PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,		"MyWakeLock");
	}




	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		try{
			paint.setColor(Color.WHITE);
			canvas.drawRect(0, 0, width, height/2, paint);
			paint.setColor(Color.BLUE);
			if (map == null ) {
				sPage = QuranMemorizerActivity.addZero(page);
				map = Saf7a.loadBitmap(sPage);
				map = Bitmap.createScaledBitmap(map, (int)(map.getWidth()*scale), (int)(map.getHeight()*scale), true);
				mapWidth = map.getWidth();
//				stepLines = null;
				
				streamCur=0;
			}
			if (stepLines == null) {
				stepLines = Saf7a.calculStepLines(map);
				if(speed>0)
					streamLine = 0;
				else
					if(speed<0)
						streamLine = stepLines.length-3;
				speed = 0;
				status = IDLE;
			}

			if(status == SCALING){
				
				srcRect.set(0, stepLines[streamLine], mapWidth, stepLines[streamLine+1]);
				dstRect.set(width - (int) (mapWidth*scale), 100, width, (int) (100+(stepLines[streamLine+1]-stepLines[streamLine])*scale));
				canvas.drawBitmap(map, srcRect, dstRect, paint);
				return;
			}
			
			if(streamCur>mapWidth || streamCur < 0){
				if(speed>=0){
					streamLine++;
					streamCur=0;
				}
				else{
					streamCur=mapWidth;
					streamLine--;
				}
				if((speed>0 && streamLine==stepLines.length-1 )|| (speed<0 && streamLine==-1)){
					map = null;
					stepLines = null;
					page += streamLine == -1 ?-1: 1;
					streamLine = 0;
					invalidate();
					return; 
				}
			}
			canvas.drawText("page:"+sPage+" line:"+streamLine, 10, 20, paint);

			srcRect.set(0, stepLines[streamLine], mapWidth, stepLines[streamLine+1]);
			dstRect.set(tmp=width - mapWidth+streamCur, 100, width+streamCur, 100+stepLines[streamLine+1]-stepLines[streamLine]);
			canvas.drawBitmap(map, srcRect, dstRect, paint);

			if(streamLine+2<stepLines.length){
				srcRect.set(0, stepLines[streamLine+1], mapWidth, stepLines[streamLine+2]);
				dstRect.set(tmp - mapWidth,100, tmp, 100+stepLines[streamLine+2]-stepLines[streamLine+1]);
				canvas.drawBitmap(map, srcRect, dstRect, paint);
			}
			if((tmp - mapWidth)>0 && streamLine+3<stepLines.length){
				srcRect.set(0, stepLines[streamLine+2], mapWidth, stepLines[streamLine+3]);
				dstRect.set(tmp - 2*mapWidth,100, tmp - mapWidth, 100+stepLines[streamLine+3]-stepLines[streamLine+2]);
				canvas.drawBitmap(map, srcRect, dstRect, paint);
			}
			if(filter != 0){

				paint.setColor(filters[filter]);
				paint.setAlpha(75);
				canvas.drawRect(0, 0, width, height/2, paint);
			}
			streamCur += speed;
			if(speed!=0)
				invalidate();
		}finally{

		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		scaleDetect.onTouchEvent(event);
		gestDetect.onTouchEvent(event);
		
		if(status == SCALING)
			return true;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			wakeLock.acquire();
			status = SLIDING;
			xStick = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			if(status != SLIDING)break;
			speed = (int) (xStick - event.getX())/35;
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			wakeLock.release();
			speed = 0;
			status = IDLE;
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
		if(Math.abs(velocityY)>700 && Math.abs(velocityX)<500){
			if(velocityY>0){
				filter++;
				if(filter == filters.length)
					filter = 0;
			}
			else{
				filter--;
				if(filter == -1)
					filter = filters.length-1;
			}
			invalidate();
			return true;
		}
		return false;
	}

	public void setPage(int i) {
		page = i;
		sPage = QuranMemorizerActivity.addZero(page);
		map = null;
		invalidate();

	}


	public void init() {
		// TODO Auto-generated method stub

	}





	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		status = SCALING;
//		map = Bitmap.createScaledBitmap(map, (int)(map.getWidth()*scale), (int)(map.getHeight()*scale), true);
		map = Saf7a.loadBitmap(sPage);
		Saf7a.scaleStepLines(stepLines, 1/oldScale);
		mapWidth = map.getWidth();
//		scale = (int) detector.getScaleFactor();
		return true;
	}

	
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		scale *=  detector.getScaleFactor();
		invalidate();
		return true;
	}



	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		scale *=  detector.getScaleFactor();
		status = IDLE;
		map = null;
		stepLines = null;
//		streamCur *= scale;
//		Saf7a.scaleStepLines(stepLines, scale/oldScale);
		oldScale = scale;
		invalidate();
	}

	public void setScale(float s)
	{
		scale = oldScale = s;
	}
}
