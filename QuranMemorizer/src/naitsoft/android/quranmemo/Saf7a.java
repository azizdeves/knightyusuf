package naitsoft.android.quranmemo;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Saf7a extends View {

	int stepLines[];
	private boolean isDrawing;
	Mask drawingMask;
	public Saf7a(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Bitmap map = BitmapFactory.decodeFile("/mnt/sdcard/QuranPages/a.jpg");
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		int seuil =5;
		int cumul = 0;
		//		Rect rSrc = new Rect(0,0,map.getWidth(),map.getHeight());
		//		Rect rDest = new Rect(0,0,canvas.getWidth(),canvas.getHeight());
		map = Bitmap.createScaledBitmap(map, canvas.getWidth(), canvas.getHeight(), true);
		if(stepLines == null){
			int numLines[] = new int[1000];
			boolean isLineClean = true;
			//		int[] pixels = new int[map.getWidth()*map.getHeight()];
			//		map.getPixels(pixels , 0, map.getWidth(), 0, 0, map.getWidth(),map.getHeight() );
			int cur = 0;
			int pixel = 0;
			for(int y = 0; y < map.getHeight() ; y++){
				isLineClean = true;
				cumul = 0;
				for(int x = 0; x<map.getWidth(); x++){
					pixel = map.getPixel(x, y);
					if(Color.red(pixel)+Color.blue(pixel)+Color.green(pixel)< 500){
						cumul++;
						//					isLineClean = false;
						//					break;
					}
				}
				if(cumul < seuil)
					//			if(isLineClean)
					numLines[cur++] = y;
			}
			//		canvas.scale(map.getWidth(), map.getHeight());
			//		map.setDensity(canvas.getDensity());
			paint.setColor(Color.RED);
			cur = 0;
			stepLines = new int[30];
			int startSpace,endSpace;
			startSpace = 0;
			for(int i = 1; i< numLines.length ; i++){
				if( numLines[i]-numLines[i-1] > 5){
					endSpace = numLines[i-1];
					stepLines[cur++] = startSpace + (endSpace - startSpace)/2;
					startSpace = numLines[i];
					canvas.drawLine(0, stepLines[cur-1], canvas.getWidth(), stepLines[cur-1], paint);
				}
			}
		}
		canvas.drawBitmap(map,0,0,  paint);	
		if(drawingMask != null)
			canvas.drawRect(drawingMask.endX,getY(drawingMask.startLine-1),drawingMask.startX, getY(drawingMask.startLine), paint);


	}
	private int getY(int numLine) {
		return stepLines[numLine];
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			isDrawing = true;
			break;
		case MotionEvent.ACTION_MOVE:
			updateMask(event);

			break;
		case MotionEvent.ACTION_UP:
			isDrawing = false;
			drawingMask = null;
			break;
		}

		return true;
	}
	private void updateMask(MotionEvent event) {
		if(drawingMask == null){
			drawingMask = new Mask();
			drawingMask.startX = (int) event.getX();
			drawingMask.startLine = getNumLine((int) event.getY());
		}
		drawingMask.endX = (int) event.getX();
		drawingMask.endLine = getNumLine((int) event.getY());
		invalidate();
//		requestLayout();
	}

	private int getNumLine(int y){
		int i = 0;
		for(; i< stepLines.length && stepLines[i] < y; i++)	;
		return i;
	}

}
class Mask{
	int startLine;
	int endLine;
	int startX;
	int endX;
}