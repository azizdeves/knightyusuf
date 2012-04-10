package naitsoft.android.siraj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

public class RichArabicTextView extends ArabicTextView {


	public RichArabicTextView(Context context,AttributeSet attr) {
		super(context,attr);
		//Typeface mFace = Typeface.createFromAsset(getContext().getAssets(),"fonts/Scheherazade.ttf");

		//        mPaint = BooksListActivity.paint;
	}


	@Override  
	protected void onDraw(Canvas canvas) {
		//    	SirajActivity.text.setText(frame++);
		//    	width = canvas.getWidth();
		canvas.translate(canvas.getWidth(), 0);
		mPaint.setColor(backColor);
		clsRect.set(-width, 0, 0, canvas.getHeight());
		canvas.drawRect(clsRect, mPaint);
		if(text == null)
			return;
		mPaint.setColor(fontColor); 
		//        int prevCharWitdh=0;
		int cur=0;
//		w = new float[text.length()];
		mPaint.getTextWidths(text, w);
		//        	canvas.translate(canvas.getWidth(), 0);

		float y = stepLine/1.5f;

//		draw Mark
		MarkUI mrkUi= ArabicListAdapter.marksUi.get(line.numLine);
		while(mrkUi!=null) {
			
			
			if(mrkUi.startLine == line.numLine){
				a=mrkUi.startX;
			}else
				a = width;
			if(mrkUi.endLine == line.numLine){
				b=mrkUi.endX;
			}else
				b =0 ;
			mPaint.setColor(Color.BLUE);
			mPaint.setAlpha(65);
			canvas.drawRect(b-width, 0, a-width, stepLine, mPaint);
			mPaint.setColor(fontColor); 
			mrkUi = mrkUi.next;
			}
		//Light draw
//		if(ArabicListAdapter.mBusy){
		if(true){
			canvas.drawText(text, 0, y, mPaint);
//			if(TextSelection.getCurrentSelection()!=null)
//			{
//				TextSelection select = TextSelection.getCurrentSelection();
//				canvas.translate(-canvas.getWidth(), 0);
//				if(line.numLine == select.focusA.line)
//					select.focusA.draw(canvas);
//				if(line.numLine == select.focusB.line)
//					select.focusB.draw(canvas);
//			}
//			return;
		}

		//Heavy draw lettre by lettre
		for(int i =0, length=text.length(); i<length;i++){
			if(text.charAt(i)=='\n')continue;
//			canvas.drawText(text, i, i+1, cur,  y, mPaint);
//			if(!DariGlyphUtils.isHaraka(text.charAt(i))){
				cur-=w[i];
				if(ArticleFragment.status == ArticleFragment.SELECTING || ArticleFragment.status == ArticleFragment.SELECTED)
					w[i]=cur;
//			}
			//        		prevCharWitdh = (int) ArabicTextView.getCharWidth(mPaint, text, i,w);
		}
		if(ArticleFragment.status != ArticleFragment.SELECTING && ArticleFragment.status != ArticleFragment.SELECTED)
			return;
		if(TextSelection.getCurrentSelection()==null)
			return;
		TextSelection select = TextSelection.getCurrentSelection();
		canvas.translate(-canvas.getWidth(), 0);
		if(line.numLine == select.focusA.line)
			select.focusA.draw(canvas);
		if(line.numLine == select.focusB.line)
			select.focusB.draw(canvas);
		if(!TextSelection.isLineInSelction(line.numLine))
			return;
		select.initOrder(); 
		mPaint.setColor(Color.BLUE);
		mPaint.setAlpha(65);
		int start,end;
		int i = 0 ;
		if(line.numLine == select.getStartNumLine()){
			start = select.getStartX()-width;
			for(; i< w.length && w[i]>start; i++);	
			start = i == 0? width:(int) w[i-1];
			start+=width;
//			select.startCursor.x = start;
			select.focusA.x = start;
			select.startCursor.idxChar=i;
			select.focusA.line = line.numLine+1;
		}
		else
			start = width;

		if(line.numLine == select.getEndNumLine()) {

			end = select.getEndX()-width;
			for(i=0; i< w.length && w[i]>end; i++) 		;	
			if(i>= w.length)
				end = (int) w[--i];
			else
				end = (int) w[i];
			end+=width;
//			select.endCursor.x = end;
			select.focusB.x = end;
			select.endCursor.idxChar=i+1;
			select.focusB.line = line.numLine+1;
		}
		else
			end = 0;

		canvas.drawRect(end, 0, start, stepLine, mPaint);
		//        	Bitmap.createBitmap(getDrawingCache(), 50, 30, 100, 30);

		//        	********************
		//        	map = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_4444);
		//    		Canvas cnvs = new Canvas(map);
		//    		cnvs.translate(canvas.getWidth(), 0);
		//        	for(int i =0; i<text.length();i++){
		//        		
		//        		cnvs.drawText(text,i,i+1, cur-=prevCharWitdh,  stepLine/1.5f, mPaint);
		//        		prevCharWitdh = (int) ArabicTextView.getCharWidth(mPaint, text, i,w);
		//        	}**************************

		//        canvas.drawBitmap(map, -canvas.getWidth(), 0, mPaint);**************
	}
}