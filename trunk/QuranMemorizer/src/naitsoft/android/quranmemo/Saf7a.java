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
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;

public class Saf7a extends View implements OnGestureListener {

	int stepLines[];
	private int isEditing = -1;
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
		int seuil = 5;
		int cumul = 0;
		if (map == null) {
			map = BitmapFactory.decodeFile("/mnt/sdcard/QuranPages/a.jpg");
			map = Bitmap.createScaledBitmap(map, canvas.getWidth(),
					canvas.getHeight(), true);
		}
		if (stepLines == null) {
			int numLines[] = new int[1000];
			int cur = 0;
			int pixel = 0;
			for (int y = 0; y < map.getHeight(); y++) {
				cumul = 0;
				for (int x = 0; x < map.getWidth(); x++) {
					pixel = map.getPixel(x, y);
					if (Color.red(pixel) + Color.blue(pixel)
							+ Color.green(pixel) < 500) {
						cumul++;
					}
				}
				if (cumul < seuil)
					numLines[cur++] = y;
			}

			paint.setColor(Color.WHITE);
			cur = 0;
			stepLines = new int[30];
			int startSpace, endSpace;
			startSpace = 0;
			for (int i = 1; i < numLines.length; i++) {
				if (numLines[i] - numLines[i - 1] > 5) {
					endSpace = numLines[i - 1];
					stepLines[cur++] = startSpace + (endSpace - startSpace) / 2;
					startSpace = numLines[i];
				}
			}
		}
		canvas.drawBitmap(map, 0, 0, paint);
		for (Mask m : masks){
			if(m.startLine == m.endLine)
				canvas.drawRect(m.endX, getY(m.startLine - 1), m.startX,getY(m.startLine), paint);
			else{
				canvas.drawRect(0, getY(m.startLine - 1),m.startX ,getY(m.startLine), paint);
				for(int i = m.startLine+1;i< m.endLine ; i++)
					canvas.drawRect(0, getY(i-1), getWidth(),getY(i), paint);
					
				canvas.drawRect(m.endX, getY(m.endLine - 1), getWidth(),getY(m.endLine), paint);
			}
		
		
		}

		if (isEditing == -1)
			return;

		BitmapDrawable curDraw = (BitmapDrawable) getContext().getResources()
				.getDrawable(android.R.drawable.ic_input_add);
		curDraw.setBounds(editingMask.startX - 50,
				getY(editingMask.startLine) , editingMask.startX + 50,
				getY(editingMask.startLine) + 100);
		curDraw.draw(canvas);

//		curDraw = (BitmapDrawable) getContext().getResources().getDrawable(android.R.drawable.ic_input_add);
		curDraw.setBounds(editingMask.endX - 50,
				getY(editingMask.endLine) , editingMask.endX + 50,
				getY(editingMask.endLine) + 100);
		curDraw.draw(canvas);

	}

	private int getY(int numLine) {
		return stepLines[numLine];
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

//		if(isEditing==-1){
			gestDetect.onTouchEvent(event);
//			return true;
//		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (isEditing == 0) {
				getNearstCursor(event);

			} else {
//				getClickedMask(event);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (isEditing > 0) {
				updateMask(event);
			}
			break;
		case MotionEvent.ACTION_UP:
			if(isEditing > 0)
				isEditing = 0;
			//masks.add(editingMask);
			//editingMask = null;
			break;
		}

		return true;
	}

	private Mask getClickedMask(MotionEvent event) {
		if(isEditing !=- 1)
			return null;
		int line = getNumLine((int) event.getY());
		for (Mask m : masks) {
			if (m.startLine == line || m.endLine == line
					|| (m.startLine < line && m.endLine > line))
				return m;
		}
		return null;

	}

	/**
	 * return 1 if near endcursor 0 near start -1 none of them
	 * 
	 * @param event
	 * @return
	 */
	private void getNearstCursor(MotionEvent event) { 
		
		int d1 = (int) ( Math.abs( event.getX() - editingMask.startX) +	Math.abs(event.getY() - getY(editingMask.startLine)));
		int d2 = (int) (Math.abs( event.getX() - editingMask.endX )+		Math.abs(event.getY() - getY(editingMask.endLine)));
		if (d1 < d2){
			if (d1 < 200)
				isEditing = 1;
		}
		else if (d2 < 200)
			isEditing = 2;
		

	}

	private void updateMask(MotionEvent event) {

		if (isEditing == 1) {
			editingMask.startX = (int) event.getX();
			editingMask.startLine = getNumLine((int) event.getY())-1;
		} else if (isEditing == 2) {
			editingMask.endX = (int) event.getX();
			editingMask.endLine = getNumLine((int) event.getY())-1;
		}
		switchCursor();
		invalidate();
	}

	private void switchCursor() {
		int x , line;
		if(editingMask.startLine > editingMask.endLine
				|| (editingMask.startLine == editingMask.endLine && editingMask.startX < editingMask.endX)){
			x = editingMask.startX;
			line = editingMask.startLine;
			editingMask.startX = editingMask.endX;
			editingMask.startLine = editingMask.endLine;
			editingMask.endLine = line;
			editingMask.endX = x;
			
			isEditing = isEditing == 2? 1 : 2;
			
			
		}
		
	}

	private int getNumLine(int y) {
		int i = 0;
		for (; i < stepLines.length && stepLines[i] < y; i++)
			;
		return i;
	}

	@Override
	public void onLongPress(MotionEvent event) {
		isEditing = 0;
		editingMask = new Mask();
		editingMask.startX = (int) event.getX() + 100;
		if (editingMask.startX > getWidth())
			editingMask.startX = getWidth();
		editingMask.startLine = getNumLine((int) event.getY());
		editingMask.endX = (int) event.getX() - 100;
		if (editingMask.endX < 0)
			editingMask.endX = 0;
		editingMask.endLine = getNumLine((int) event.getY());
		masks.add(editingMask);
		invalidate();
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Mask m = getClickedMask(e);
		if(m==null){
			isEditing = -1;
			editingMask = null;
			
		}else{
		
		editingMask = m;
		isEditing = 0;
		}
		invalidate();
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

}

class Mask {
	int startLine;
	int endLine;
	int startX;
	int endX;
}