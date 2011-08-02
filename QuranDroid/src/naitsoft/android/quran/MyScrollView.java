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
import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Config;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView{

//	GestureDetector gestureDetector = new GestureDetector(get);
	public MyScrollView(Context context) {
		super(context);
	}
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return super.onTouchEvent(ev);
//		return false;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		super.dispatchTouchEvent(ev);
		return true;
	}
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//	    super.onTouchEvent(event);
//	    return gestureDetector.onTouchEvent(event);
//	}
//
//	@Override 
//	public boolean dispatchTouchEvent(MotionEvent ev){
//	    gestureDetector.onTouchEvent(ev);
//	    super.dispatchTouchEvent(ev); 
//	    return true;
//	} 
	
	
}
