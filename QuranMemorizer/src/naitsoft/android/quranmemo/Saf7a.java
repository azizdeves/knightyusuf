package naitsoft.android.quranmemo;

import java.util.ArrayList;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
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

public class Saf7a extends View implements OnGestureListener {

	int stepLines[];
	private int isEditing;
	ArrayList<Mask> masks = new ArrayList<Mask>();
	Mask editingMask;
	private Bitmap map;
	private GestureDetector gestDetect = new GestureDetector(this);
	
	public Saf7a(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		int seuil =5;
		int cumul = 0;
		if(map==null){
			map = BitmapFactory.decodeFile("/mnt/sdcard/QuranPages/a.jpg");
			map = Bitmap.createScaledBitmap(map, canvas.getWidth(), canvas.getHeight(), true);
		}
		if(stepLines == null){
			int numLines[] = new int[100];
			int cur = 0;
			int pixel = 0;
			for(int y = 0; y < map.getHeight() ; y++){
				cumul = 0;
				for(int x = 0; x<map.getWidth(); x++){
					pixel = map.getPixel(x, y);
					if(Color.red(pixel)+Color.blue(pixel)+Color.green(pixel)< 500){
						cumul++;
					}
				}
				if(cumul < seuil)
					numLines[cur++] = y;
			}
			
			paint.setColor(Color.WHITE);
			cur = 0;
			stepLines = new int[30];
			int startSpace,endSpace;
			startSpace = 0;
			for(int i = 1; i< numLines.length ; i++){
				if( numLines[i]-numLines[i-1] > 5){
					endSpace = numLines[i-1];
					stepLines[cur++] = startSpace + (endSpace - startSpace)/2;
					startSpace = numLines[i];
//					canvas.drawLine(0, stepLines[cur-1], canvas.getWidth(), stepLines[cur-1], paint);
				}
			}
		}
		canvas.drawBitmap(map,0,0,  paint);	
		for(Mask m : masks)
			canvas.drawRect(m.endX,getY(m.startLine-1),m.startX, getY(m.startLine), paint);
		if(editingMask != null)
			canvas.drawRect(editingMask.endX,getY(editingMask.startLine-1),editingMask.startX, getY(editingMask.startLine), paint);

		if(isEditing  == -1)
			return;
		
	

	}
	private int getY(int numLine) {
		return stepLines[numLine];
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		gestDetect.onTouchEvent(event);
		
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			if(isEditing == 0){
				isEditing = getNearstCursor(event);
				
			}
			else{
				getClickedMask(event);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			updateMask(event);

			break;
		case MotionEvent.ACTION_UP:
			isEditing = 0;
			masks.add(editingMask);
			editingMask = null;
			break;
		}

		return true;
	}
	
	private Mask getClickedMask(MotionEvent event) {
		int line = getNumLine((int) event.getY());
		for(Mask m : masks){
			if(m.startLine == line || m.endLine == line
					|| (m.startLine < line && m.endLine > line))
				return m;
		}
		return null;
		
	}

	/**
	 * return 1 if near endcursor
	 * 0 near start
	 * -1 none of them
	 * @param event
	 * @return 
	 */
	private int getNearstCursor(MotionEvent event) {
		int d1 = (int) Math.abs(2*event.getX()-editingMask.startX + getY(editingMask.startLine));
		int d2 = (int) Math.abs(2*event.getX()-editingMask.endX + getY(editingMask.endLine));
		if(d1<d2)
			if(d1<100)
				return 0;
			else
				return -1;
		else
			if(d2<100)
				return 1;
			else
				return -1;
			
		
	}

	private void updateMask(MotionEvent event) {

		if(isEditing==1){
			editingMask.startX = (int) event.getX();
			editingMask.startLine = getNumLine((int) event.getY());
		}
		else
			if(isEditing==2){
				editingMask.endX = (int) event.getX();
				editingMask.endLine = getNumLine((int) event.getY());
			}
		invalidate();
	}

	private int getNumLine(int y){
		int i = 0;
		for(; i< stepLines.length && stepLines[i] < y; i++)	;
		return i;
	}

	@Override
	public void onLongPress(MotionEvent event) {
		isEditing = 0;
		editingMask = new Mask();
		editingMask.startX = (int) event.getX()+100;
		if(editingMask.startX > getWidth()) 	editingMask.startX = getWidth();
		editingMask.startLine = getNumLine((int) event.getY());
		editingMask.endX = (int) event.getX()-100;
		if(editingMask.endX < 0)		editingMask.endX = 0;
		editingMask.endLine = getNumLine((int) event.getY());
	}

	@Override
	public boolean  onDown(MotionEvent e) {
		
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		
		return false;
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

}
class Mask{
	int startLine;
	int endLine;
	int startX;
	int endX;
}