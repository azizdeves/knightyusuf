package naitsoft.android.quranmemo;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.MeasureSpec;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

public class Saf7a extends View implements OnGestureListener {

	final static int  EDITING_START = 1;
	final static int  EDITING_END = 2;
	final static int  EDITING_READY = 0;
	final static int  NO_EDITING = -1;
	
	int stepLines[];
	private int isEditing = NO_EDITING;
	ArrayList<Mask> masks = new ArrayList<Mask>();
	Mask editingMask;
	boolean isEditedMaskDirty ;
	private Bitmap map;
	int height;
	int page = 17;
	String sPage;
	private GestureDetector gestDetect = new GestureDetector(this);
	private ProgressDialog dialog;

	public Saf7a(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void loadMasks(){
		masks = QuranMemorizerActivity.getMasks(page);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		int seuil = 5;
		int cumul = 0;
		View popView = inflate(getContext(), R.layout.popup, null);
		PopupWindow pop = new PopupWindow(popView);
		pop.setHeight(50);
		pop.setWidth(getWidth());
		ImageButton btn = (ImageButton) popView.findViewById(R.id.delete_mrk);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String s = "kj";
				
			}
		});
//		pop.setContentView(popView);
		pop.showAtLocation(this, Gravity.TOP, 0, 0);
		if(true)
		return;
		if (map == null || height != getHeight()) {
			height = getHeight();
			sPage = QuranMemorizerActivity.addZero(page);
			map = BitmapFactory.decodeFile("/mnt/sdcard/QuranPages/"+sPage+".jpg");
			if(map == null){
				dialog = ProgressDialog.show(QuranMemorizerActivity.activity, "",     "Loading. Please wait...", true);
				dialog.show();
				Toast.makeText(QuranMemorizerActivity.activity, "loading", Toast.LENGTH_SHORT);
				QuranMemorizerActivity.activity.downloadPage(sPage);
				dialog.dismiss();
				map = BitmapFactory.decodeFile("/mnt/sdcard/QuranPages/"+sPage+".jpg");
			}
			map = Bitmap.createScaledBitmap(map, getWidth(),	getHeight(), true);
			stepLines = null;
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
			if(m.isHidden){
				paint.setColor(Color.GREEN);
//				paint.setStyle(Paint.Style.STROKE);
				paint.setAlpha(65);
			}else{
				if(editingMask != null && m == editingMask){
					paint.setColor(Color.GREEN);
					paint.setAlpha(65);
				}
				else
					paint.setColor(Color.WHITE);
//				paint.setStyle(Paint.Style.FILL_AND_STROKE);
			}
				
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
			if(isEditing > 0){
				saveMask();
				isEditing = 0;
			}
			//masks.add(editingMask);
			//editingMask = null;
			break;
		}

		return true;
	}

	private void saveMask() {
		QuranMemorizerActivity.saveMask(editingMask);
		
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
		isEditedMaskDirty = true;
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
		for (; i < stepLines.length && stepLines[i] < y; i++);
		return i;
	}

	@Override
	public void onLongPress(MotionEvent event) {
		Mask m = getClickedMask(event);
		if(m==null){
			isEditing = 0;
			editingMask = new Mask();
			editingMask.page = page;
			editingMask.startX = (int) event.getX() + 100;
			if (editingMask.startX > getWidth())
				editingMask.startX = getWidth();
			editingMask.startLine = getNumLine((int) event.getY());
			editingMask.endX = (int) event.getX() - 100;
			if (editingMask.endX < 0)
				editingMask.endX = 0;
			editingMask.endLine = getNumLine((int) event.getY());
			masks.add(editingMask);
		}else{
			editingMask = m;
			m.isHidden = false;
			isEditing = 0;
		}
//		QuranMemorizerActivity.activity.markBar.setVisibility(View.VISIBLE);
		invalidate(); 
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Mask m = getClickedMask(e);
		if(m==null){
//			if(isEditing == EDITING_READY){
			isEditing = -1;
			editingMask = null;
//			QuranMemorizerActivity.activity.markBar.setVisibility(View.GONE);
			
		}else{
			m.isHidden = !m.isHidden;
		
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
		if(Math.abs(velocityX)>1000 && Math.abs(velocityY)<500)
			if(velocityX>0)
				load(1);
			else
				load(-1);
				
				return false;

	}
	
	private void load(int i) {
		page += i;
		map = null;
		init();
		
	}



	public void removeEditingMask(){
		masks.remove(editingMask);
		isEditing = -1;
		editingMask = null;
		invalidate();
	}

	public void setPage(int i) {
		page = i;
		
	}

	public void init() {
		
		loadMasks();
		invalidate();
	}
    
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//    	int width = MeasureSpec.getSize(widthMeasureSpec);
//    	int height = MeasureSpec.getSize(heightMeasureSpec);
//    	
//    	setMeasuredDimension(width, height);
//    }

}

class Mask {
	public Mask(){
		id = -1;
		page = 1;
	}
	int id;
	boolean isHidden;
	int startLine;
	int endLine;
	int startX;
	int endX;
	int page;
}