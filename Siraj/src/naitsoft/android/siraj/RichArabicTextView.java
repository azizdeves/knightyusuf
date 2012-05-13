package naitsoft.android.siraj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

public class RichArabicTextView extends ArabicTextView {

	static public int pading = 15;

	public RichArabicTextView(Context context,AttributeSet attr) {
		super(context,attr);
		//Typeface mFace = Typeface.createFromAsset(getContext().getAssets(),"fonts/Scheherazade.ttf");

		//        mPaint = BooksListActivity.paint;
	}


	@SuppressWarnings("static-access")
	@Override  
	protected void onDraw(Canvas canvas) {
		canvas.translate(canvas.getWidth(), 0);
		mPaint.setColor(backColor);
		clsRect.set(-width, 0, 0, canvas.getHeight());
		canvas.drawRect(clsRect, mPaint);
		if(text == null)
			return;
		mPaint.setColor(fontColor); 
		int cur=-pading;
		mPaint.getTextWidths(text, w);

		float y = stepLine/1.5f;

		//		draw Mark
		MarkUI mrkUi= ArabicListAdapter.marksUi.get(line.numLine);
		while(mrkUi!=null ) {
			if(mrkUi.isActive && mrkUi.startLine <= line.numLine && mrkUi.endLine >= line.numLine){

				if(mrkUi.startLine == line.numLine){
					a=mrkUi.startX;
				}else
					a = 0;
				if(mrkUi.endLine == line.numLine){
					b=mrkUi.endX;
				}else
					b =-width ;
				mPaint.setColor(mrkUi.mark.type);
				mPaint.setAlpha(65);
				canvas.drawRect(b, 2, a, stepLine, mPaint);
				mPaint.setColor(fontColor); 
			}
			mrkUi = mrkUi.next;
		}
		//Light draw

		canvas.drawText(text, -pading, y, mPaint);


		if(ArticleFragment.status != ArticleFragment.SELECTING && ArticleFragment.status != ArticleFragment.SELECTED)
			return;
		//Heavy draw lettre by lettre
		for(int i =0, length=text.length(); i<length;i++){
			if(text.charAt(i)=='\n')continue;
			cur-=w[i];
//			if(ArticleFragment.status == ArticleFragment.SELECTING || ArticleFragment.status == ArticleFragment.SELECTED)
				w[i]=cur;
			//			}
		}
		if(TextSelection.getCurrentSelection()==null)
			return;
		TextSelection select = TextSelection.getCurrentSelection();
		//		canvas.translate(-canvas.getWidth(), 0);
		if(line.numLine == select.focusA.line)
			select.focusA.draw(canvas);
		if(line.numLine == select.focusB.line)
			select.focusB.draw(canvas);
		if(!TextSelection.isLineInSelction(line.numLine))
			return;
		select.initOrder(); 
		mPaint.setColor(select.markUi.mark.type);
		mPaint.setAlpha(65);
		int start,end;
		int i = 0 ;
		if(line.numLine == select.getStartNumLine()){
			start = select.getStartX();
			for(; i< w.length && w[i]>=start; i++);	
			start = i == 0? 0:(int) w[i-1];

			//			select.startCursor.x = start;
			select.focusA.x = start;
			select.startCursor.x = start;
			select.startCursor.idxChar=i;
			select.focusA.line = line.numLine+1;
		}
		else
			start = 0;

		if(line.numLine == select.getEndNumLine()) {

			end = select.getEndX();
			for(i=5; i< w.length && w[i]>=end; i++) 		;	
			end = i == w.length? width:(int) w[i-1];
			select.endCursor.x = end;
			select.focusB.x = end;
			select.endCursor.idxChar=i+1;
			select.focusB.line = line.numLine+1;
		}
		else
			end = -width;

		canvas.drawRect(end, 2, start, stepLine, mPaint);
	}
}