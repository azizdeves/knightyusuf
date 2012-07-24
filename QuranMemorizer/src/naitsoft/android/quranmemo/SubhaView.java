package naitsoft.android.quranmemo;

import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;

public class SubhaView extends View implements OnGestureListener  {

	static final int READY = 1;
	static final int COUNTING = 2;
	static final int PAUSED = 0;
	
	
	private GestureDetector gestDetect = new GestureDetector(this);
	Paint paint = new Paint();
	Paint secondPaint = new Paint();
	private WakeLock wakeLock;
	private int width;
	private int height;
	String sCount;
	private Rect bounds;
	SubhaView subha;
	Jalsa jalsa;
	int status;
	private Thread timer;
	Vibrator v;
	public Jalsa jalsaTotal;
	 
	

	

	public SubhaView(Context context, AttributeSet attrs) {
		super(context, attrs);
		v = (Vibrator) SubhaActivity.activity.getSystemService(Context.VIBRATOR_SERVICE);
		paint.setTextSize(50);
		paint.setAntiAlias(true);
		secondPaint.setTextSize(30);
		secondPaint.setAntiAlias(true);
		secondPaint.setColor(Color.GRAY);
		bounds = new Rect();
		sCount = "0";
		subha = this;
		status = READY;
		jalsa = new Jalsa();
		PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		//		wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,		"MyWakeLock");
		
		timer = new Thread(){
			public void run() {
				try {
					while(true){
						if(status == COUNTING){
							jalsa.duration++;
							jalsaTotal.duration++;
							subha.postInvalidate();
						}
						sleep(1000);
					}
				} catch (InterruptedException e) {
				}
			};
		};
		timer.start();
		
	}




	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		try{
			paint.setColor(Color.BLACK);
			
			canvas.drawRect(0, 0, width, height, paint);
			paint.setColor(Color.WHITE);
//			paint.getTextBounds(sCount, 0, sCount.length(), bounds);
//			if(jalsa.count != 0)
			paint.setTextSize(40);
			canvas.drawText(jalsa.getDuration()+(status==PAUSED?"(Paused)":""), 0, 50, paint);
			paint.setTextSize(60);
			paint.getTextBounds(sCount, 0, sCount.length(), bounds);
			canvas.drawText(sCount, width/2-bounds.right/2, height/3, paint);
			canvas.drawText("الحصيلة اليوم", 0, height*0.8f, secondPaint);
			canvas.drawText(jalsaTotal.getDuration(), 0, height*0.85f, secondPaint);
			canvas.drawText(String.valueOf(jalsaTotal.count), 0, height*0.9f, secondPaint);
			
			
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
		gestDetect.onTouchEvent(event);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//			wakeLock.acquire();
			if(status == READY){

				status = COUNTING;
				jalsa.start= (new Date()).getTime()/1000;
			}
			if(status!=PAUSED)
			{
				jalsa.count ++;
				jalsaTotal.count++;
				sCount = String.valueOf(jalsa.count);
//				sCountTotal = String.valueOf(jalsa.count);
				if((jalsa.count%100)==0)
					v.vibrate(100);
				invalidate();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
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
		return false;
	}


	public void init() {
		// TODO Auto-generated method stub

	}




	public int getStatus() {
		return status;
	}




	public void setStatus(int status) {
		this.status = status;
//		if(status == COUNTING)
//			timer.resume();
//		else
//			timer.suspend();
	}

//	public void update() {
//		requestLayout();
//	}


}
class Jalsa{
	long start;
	int dikr;
	int count;
	int duration;
	int h , m,s;
	
	public String getDuration(){
		h=duration/3600;
		m=(duration-h*3600)/60;
		s=(duration-h*3600-m*60);
		return (h)+"h"+m+"m"+s+"s";
	}
}
