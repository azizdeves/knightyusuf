package naitsoft.android.siraj;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CopyOfSirajActivity extends Activity {


//        public static final int CHAPTERS_CODE = 0;
//        public static final char SELECTING = 's';
//        public static final char SELECTING_BEGIN = 'B';
//        public static final char SELECTING_END = 'E';
//        public static final char SELECTED = 'S';
//        public static final char NORMAL = 'I';
//        
//        public static char status ;
//        private DataBaseHelper myDbHelper;
//        public int livre;
//        public int chapitre;
//        int textSize;
//
//        private LayoutInflater mInflater;
//        private ArabicListAdapter arabicAdapter;
//        private MyListView listTextLineView;
//        private LinearLayout linearLayout;
//        public static TextView text;
//        static public Paint paint;
//
//        /** Called when the activity is first created. */
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//                super.onCreate(savedInstanceState);
//                livre = 6; 
//                chapitre = 1;
//                //        initDB();
//                //        loadShowChapter();
//                Bundle bund = getIntent().getExtras();
//                if(bund != null){ 
//                        livre= bund.getInt("idBook");
//                        chapitre = bund.getInt("idChap");
//                        //                      getListView().setSelection(bund.getInt("idBook"))
//                }
//
//                Paint paint = new Paint();
//                paint.setAntiAlias(true);
//                paint.setTextSize(25);
//                paint.setStyle(Style.FILL);
//                paint.setColor(Color.WHITE);
//                paint.setAntiAlias(true);
//                paint.setTextAlign(Align.RIGHT);
//                ArabicTextView.mPaint = paint;
//
//                arabicAdapter = new ArabicListAdapter(this);
//                setContentView(R.layout.main);
//                listTextLineView = (MyListView) findViewById(R.id.listMarkView);
//                //              linearLayout = (LinearLayout)findViewById(R.id.linearLayout); 
//                //ImageButton chapBtn = (ImageButton) findViewById(R.id.chaptersBtn);
//                listTextLineView.setOnScrollListener(arabicAdapter);
//                //              mInflater = (LayoutInflater) this.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//                //              chapBtn.setOnClickListener(new View.OnClickListener() {
//                //                      public void onClick(View v) {
//                //                              callChaptersActivity();
//                //                              
//                //                      }
//                //              });
//        }
//
//        protected void callChaptersActivity() {
//                Intent chapIntent = new Intent(this,BooksListActivity.class);   
//                chapIntent.putExtra("idBook", livre);
//                chapIntent.putExtra("idChap", chapitre);
//                startActivity(chapIntent);
//        }
//
//        @Override
//        public void onWindowFocusChanged(boolean hasFocus) {
//                super.onWindowFocusChanged(hasFocus);
//                if(!hasFocus)
//                        return;
//                loadShowChapter();
//
//        }
//
//        @Override
//        protected void onStart() {
//                super.onStart();
//                SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
//                //ArabicTextView.mPaint.setTextSize(pref.getInt("textsize", 30));
//                //              chapitre = pref.getInt("chap", 1);
//                //              livre = pref.getInt("livre", 1);
//        }
//
//        protected void getPrevPage() {
//
//        }
//
//        protected void getNextPage() {
//
//        }
//
//        public void initDB()
//        {
//                myDbHelper = new DataBaseHelper(this);
//                try {
//                        myDbHelper.createDataBase();
//                } catch (IOException ioe) {
//                        throw new Error("Unable to create database");
//                }
//                try {
//                        myDbHelper.openDataBase();
//                }catch(SQLException sqle){
//                        throw sqle;
//                }
//
//        }
//
//        private void loadShowChapter(){
//                arabicAdapter.loadChapter(livre, chapitre);
//                arabicAdapter.setWidth(listTextLineView.getWidth());
//                listTextLineView.setAdapter(arabicAdapter);
//                //              cursorEnd = lis.setText(contentChapter);
//        }
//
//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//                if(requestCode == CHAPTERS_CODE){
//                        if(data==null)return;
//                        chapitre = data.getExtras().getInt("idChap");
//                        //                      loadShowChapter();
//                }
//        }
//
//        @Override
//        protected void onStop() {
//                super.onStop();
//                //              SharedPreferences.Editor editor = getSharedPreferences("state", MODE_PRIVATE).edit();
//                //              editor.putInt("chap", chapitre);
//                //              editor.putInt("livre", livre);
//                //              editor.commit();
//
//        }
//
//        @Override
//        public boolean onCreateOptionsMenu(Menu menu) {
//                super.onCreateOptionsMenu(menu);
//                // Group ID
//                int groupId = 0;
//                // Unique menu item identifier. Used for event handling.
//                int menuItemId = 1;//MENU_ITEM;
//                // The order position of the item
//                int menuItemOrder = Menu.NONE;
//                // Text to be displayed for this menu item.
//                String menuItemText = "Index";
//                // Create the menu item and keep a reference to it.
//                MenuItem menuItem = menu.add(groupId, menuItemId,
//                                menuItemOrder, menuItemText);
//
//                menu.add(0,2,0,"Preference");
//                menu.add(0,3,2,"Select");
//                return true;
//        }
//        @Override
//        public boolean onMenuItemSelected(int featureId, MenuItem item) {
//                // TODO Auto-generated method stub
//                //              return super.onMenuItemSelected(featureId, item);
//                switch(item.getItemId()){
//                case 1: 
//                        callChaptersActivity();
//                        break;
//                case 2: 
//                        Intent intent = new Intent(this, PreferencesActivity.class);
//                        startActivity(intent);
//                        break;
//                case 3:
//                        status = SELECTING;
//
//                        break;
//                }
//                return true;
//        }
//
//
//}
//
//class MyListView extends ListView
//{
//
//        public static int stepLine;
//        public MyListView(Context context) {
//                super(context);
//        }
//        public MyListView(Context context, AttributeSet attrs) {
//                super(context, attrs);
//        }
//        public MyListView(Context context, AttributeSet attrs, int defStyle) {
//                super(context, attrs, defStyle);
//        }
//
//        @Override
//        public boolean dispatchTouchEvent(MotionEvent ev) {
//                return super.dispatchTouchEvent(ev);
//                //              return false;
//        }
//
//        @Override
//        public boolean onTouchEvent(MotionEvent ev) {
//                //              int aaa = ev.getPointerCount();
//                if(CopyOfSirajActivity.status == CopyOfSirajActivity.NORMAL) 
//                        return super.onTouchEvent(ev); 
//                int indexLine =  ((int)(ev.getY()+getScrollY()) / stepLine);
//                TextSelection select ;
//                if(ev.getAction()== MotionEvent.ACTION_DOWN){
//                	if(CopyOfSirajActivity.status == CopyOfSirajActivity.SELECTING) {
//                		CopyOfSirajActivity.status = CopyOfSirajActivity.SELECTING_BEGIN;
//                		select = TextSelection.getInstance();
//                		select.setStartNumLine( indexLine);
//                		select.setStartX( (int) ev.getX());
//                	}
//                	if(CopyOfSirajActivity.status == CopyOfSirajActivity.SELECTING_BEGIN) {
//                		select = TextSelection.getCurrentSelection();
//                		select.setStartNumLine( indexLine);
//                		select.setStartX( (int) ev.getX());
//                	}
//                	if(CopyOfSirajActivity.status == CopyOfSirajActivity.SELECTING_END) {
//                		select = TextSelection.getCurrentSelection();
//                		select.setEndNumLine(indexLine);
//                		select.setEndX( (int) ev.getX());
//                	}
//                	
//                }
//                if(ev.getAction()== MotionEvent.ACTION_MOVE){  
//
//                        select = TextSelection.getCurrentSelection();
//                        select.editingCursor.numLine = indexLine;
//                        select.editingCursor.x = (int) ev.getX();
//                        invalidate();
//                }
//                if(ev.getAction()== MotionEvent.ACTION_UP){
//                	CopyOfSirajActivity.status = CopyOfSirajActivity.SELECTING_BEGIN;
//                	if(CopyOfSirajActivity.status == CopyOfSirajActivity.SELECTING_BEGIN) {
//                		select = TextSelection.getCurrentSelection();
//                		select.setEndNumLine( indexLine);
//                		select.setEndX( (int) ev.getX());
//                	}
//                        select = TextSelection.getCurrentSelection();
////                      select.editingCursor.numLine = indexLine;
////                      select.setEndX((int) ev.getX());
//                        //SirajActivity.status = SirajActivity.SELECTED;
//                        String t = ((ArabicListAdapter)getAdapter()).getTextFromSelection(select);
//                        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//                        shareIntent.setType("text/plain");
//                        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Some text");
//                        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, t);
//                        getContext().startActivity(Intent.createChooser(shareIntent, "Title for chooser"));
//                }
//                return true;
//
//        }
//


}