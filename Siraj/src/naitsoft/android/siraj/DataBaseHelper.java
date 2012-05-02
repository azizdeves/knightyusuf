package naitsoft.android.siraj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{
	 
	
    public static SQLiteDatabase getMyDataBase() {
		return myDataBase;
	}

	public static void setMyDataBase(SQLiteDatabase myDataBase) {
		DataBaseHelper.myDataBase = myDataBase;
	}

	//The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/naitsoft.android.siraj/databases/";
 
    private static String DB_NAME = "siraj.db";
    private static String CONTENT_BOOK_TAB = "detail_livres";
    private static String MARK_TAB = "marks";
    
    static public DataBaseHelper myDbHelper;
    static private SQLiteDatabase myDataBase; 
 
    private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 

    public static DataBaseHelper getInstance(Context ctx){
    	if(myDbHelper == null){
    		myDbHelper = new DataBaseHelper(ctx);
    		
    		try {
    			myDbHelper.createDataBase();
    		} catch (IOException ioe) {
    			throw new Error("Unable to create database");
    		}
    		try {
    			myDbHelper.openDataBase();
    		}catch(SQLException sqle){
    			throw sqle;
    		}
    	}
    	return myDbHelper;
    }

   

    private void copyDatabaseSplited() throws IOException
    {
       // File Path = myContext.getDir("Data", 0);
        //File dBFile = new File(DB_PATH,DB_NAME);
        AssetManager am = myContext.getAssets();
    	String outFileName = DB_PATH + DB_NAME;
    	 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
        //dBFile.createNewFile();
        byte []b = new byte[1024];
        int i, r;
//        String []Files = am.list("");
//        Arrays.sort(Files);
        for(i=1;i<14;i++)
        {
//               String fn = String.format("%d.sqlite", i);
//            if(Arrays.binarySearch(Files, fn) < 0)
//                   break;
            InputStream is = am.open("siraj"+i+".sqlite"); 
            while((r = is.read(b)) != -1)
            	myOutput.write(b, 0, r);
            is.close();
        }
        myOutput.flush();
        myOutput.close();
    }
    
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
        		copyDatabaseSplited();
        		myDataBase.execSQL("create table marks(_id Integer primary key, type Integer, start Integer, end Integer, book Integer, chapter Integer, note text)");
        		
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	if(myDataBase!=null && myDataBase.isOpen())
    		return;
    		
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
	public static boolean isOpen(){
		return myDataBase.isOpen();
	}
 
	public static Chapter getChapter(int livre , int chapitre)
	{
		Chapter chap = new Chapter();
		Cursor cur = myDataBase.query(CONTENT_BOOK_TAB, new String[]{"titre_chapitre","contenu_chapitre"}, "id_chapitre=? and id_livre=?", new String[]{String.valueOf(chapitre),String.valueOf(livre)},null, null, null);
		if(cur.moveToFirst()){
			chap.setTitle(cur.getString(0));
			chap.setContent(cur.getString(1));
		}
		cur.close();
		return chap;
	}
	
	public  Cursor getChaptersOfBook(int livre)
	{
		Cursor cur = myDataBase.query(CONTENT_BOOK_TAB, new String[]{"id_chapitre","id_chapitrep","titre_chapitre"},"id_livre=?", new String[]{String.valueOf(livre)}, null, null, null);
		return cur;
	}
	
	public static ArrayList<Mark> getMarks(int book, int chapter){
//		String constraint="" ;
//		String[] valConstraint = null;
//		if(book != -1){
//			constraint = "book=?";
//			valConstraint =  new String[]{String.valueOf(book)};
//		}
//		if(chapter != -1){
//			constraint += " and chapter=?";
//			valConstraint =  new String[]{String.valueOf(book), String.valueOf(chapter)};
//		}
		Cursor cur = myDataBase.query(MARK_TAB, new String[]{"_id","type","book","chapter","start","end","note"},"book=? and chapter=?",new String[]{String.valueOf(book), String.valueOf(chapter)} , null, null, "start asc");
		ArrayList<Mark> marks = new ArrayList<Mark>();;
		if(cur.moveToFirst()){			//TODO cur empty
			Mark m;
			do{
				m = new Mark(); //TODO optimiz
				m.markId = cur.getInt(0);
				m.type = cur.getInt(1);
				m.idBook = cur.getInt(2);
				m.idChap = cur.getInt(3);
				m.startChar = cur.getInt(4);
				m.endChar = cur.getInt(5);
				m.note = cur.getString(6);
				marks.add(m);
			}while(cur.moveToNext());
		}
		cur.close();
		return marks;
	}
	public static ArrayList<MarkItem> getMarksItem(int book, int chapter){
		String constraint="" ;
		String[] valConstraint = null;
		if(book != -1){
			constraint = "m.book=?";
			valConstraint =  new String[]{String.valueOf(book)};
		}
		if(chapter != -1){
			constraint += " and m.chapter=?";
			valConstraint =  new String[]{String.valueOf(book), String.valueOf(chapter)};
		}
		
		String query = "select m._id,m.type,m.book,m.chapter,m.start,m.end,m.note,l.titre_chapitre " +
				"from marks m inner join detail_livres l on m.book=l.id_livre and m.chapter=l.id_chapitre where "
			+constraint+"order by m.book asc, m.chapter asc";
		Cursor cur = myDataBase.rawQuery(query, valConstraint);
		
		ArrayList<MarkItem> marks = new ArrayList<MarkItem>();;
		if(cur.moveToFirst()){			//TODO cur empty
			MarkItem m;
			do{
				m = new MarkItem(); //TODO optimiz
				m.mark.markId = cur.getInt(0);
				m.mark.type = cur.getInt(1);
				m.mark.idBook = cur.getInt(2);
				m.mark.idChap = cur.getInt(3);
				m.mark.startChar = cur.getInt(4);
				m.mark.endChar = cur.getInt(5);
				m.mark.note = cur.getString(6);
				m.titleChapter = cur.getString(7);
				marks.add(m);
			}while(cur.moveToNext());
		}
		cur.close();
		return marks;
	}
	
	public void addMark(Mark m){
		ContentValues val = new ContentValues(4);
		val.put("type", m.type);
		val.put("chapter", m.idChap);
		val.put("book", m.idBook);
		val.put("note", m.note);
		val.put("start", m.startChar);
		val.put("end", m.endChar);
		m.markId = (int) myDataBase.insert(MARK_TAB, null, val);
	}
	
	public void updateMark(Mark m){
		ContentValues val = new ContentValues(4);
		val.put("type", m.type);
		val.put("chapter", m.idChap);
		val.put("book", m.idBook);
		val.put("note", m.note);
		val.put("start", m.startChar);
		val.put("end", m.endChar);
		myDataBase.update(MARK_TAB, val, "_id = ?",  new String[]{String.valueOf(m.markId) });
	}
	
	public void deleteMark(int id)
	{
		myDataBase.delete(MARK_TAB, "_id = ?",  new String[]{String.valueOf(id) });
	}
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.

//	public Mark getMarks(int markId) {
//		Cursor cur = myDataBase.query(MARK_TAB, new String[]{"_id","type","book","chapter","start","end","note"},"_id=? ", new String[]{String.valueOf(markId)}, null, null, null);
//		Mark m=null;
//		if(cur.moveToFirst()){			//TODO cur empty
//				m = new Mark(); //TODO optimiz
//				m.markId = cur.getInt(0);
//				m.type = cur.getInt(1);
//				m.idBook = cur.getInt(2);
//				m.idChap = cur.getInt(3);
//				m.startChar = cur.getInt(4);
//				m.endChar = cur.getInt(5);
//				m.note = cur.getString(6);
//		}
//		cur.close();
//		return m;		
//	}
 
}