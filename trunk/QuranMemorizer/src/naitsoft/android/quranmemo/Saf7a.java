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
import android.view.View;

public class Saf7a extends View {

	public Saf7a(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Bitmap map = BitmapFactory.decodeFile("/mnt/sdcard/QuranPages/a.jpg");
		Paint paint = new Paint();
		int seuil =100;
		int cumul = 0;
//		Rect rSrc = new Rect(0,0,map.getWidth(),map.getHeight());
//		Rect rDest = new Rect(0,0,canvas.getWidth(),canvas.getHeight());
		map = Bitmap.createScaledBitmap(map, canvas.getWidth(), canvas.getHeight(), true);
		int numLines[] = new int[100];
		boolean isLineClean = true;
		int[] pixels = new int[map.getWidth()*map.getHeight()];
		map.getPixels(pixels , 0, map.getWidth(), 0, 0, map.getWidth(),map.getHeight() );
		int cur = 0;
		int pixel = 0;
		for(int y = 0; y < canvas.getHeight()/2 ; y++){
			cumul = 0;
			for(int x = 0; x<canvas.getWidth(); x++){
				pixel = map.getPixel(x, y);
				if(Color.red(pixel)+Color.blue(pixel)+Color.green(pixel)> 700){
					cumul++;
//					isLineClean = false;
//					break;
				}
			}
			if(cumul < seuil)
				numLines[cur++] = y;
		}
//		canvas.scale(map.getWidth(), map.getHeight());
//		map.setDensity(canvas.getDensity());
		canvas.drawBitmap(map,0,0,  paint);	
		paint.setColor(Color.RED);
		for(int i = 0; i< numLines.length && numLines[i]<canvas.getHeight(); i++){
			
			canvas.drawLine(0, numLines[i], canvas.getWidth(), numLines[i], paint);
		}
		
		
		
	}
	
	
}
