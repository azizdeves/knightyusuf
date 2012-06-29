package naitsoft.android.quranmemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

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
    private static String DB_PATH = "/data/data/naitsoft.android.quranmemo/databases/";
 
    private static String DB_NAME = "siraj.db";
    private static String CONTENT_BOOK_TAB = "detail_livres";
    private static String Mask_TAB = "Masks";
    
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
 
        		openDataBase();
        		myDataBase.execSQL("create table Masks(_id Integer primary key,  page Integer, startX Integer, endX Integer, startLine Integer, endLine Integer)");
        		
    		} catch (Exception e) {
 
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
 

	public  Cursor getChaptersOfBook(int livre)
	{
		Cursor cur = myDataBase.query(CONTENT_BOOK_TAB, new String[]{"id_chapitre","id_chapitrep","titre_chapitre"},"id_livre=?", new String[]{String.valueOf(livre)}, null, null, 
				" id_chapitrep asc, id_chapitre asc");
		return cur;
	}
	/*
	public ArrayList<Mask> getMasks(int book, int chapter){
		String constraint = null ;
		String[] valConstraint = null;
		if(book != -1){
			constraint = "book=?";
			valConstraint =  new String[]{String.valueOf(book)};
		}
		if(chapter != -1){
			constraint += " and chapter=?";
			valConstraint =  new String[]{String.valueOf(book), String.valueOf(chapter)};
		}
		Cursor cur = myDataBase.query(Mask_TAB, new String[]{"_id","type","book","chapter","start","end","note"},constraint,valConstraint , null, null, "start asc");
		ArrayList<Mask> Masks = new ArrayList<Mask>();;
		if(cur.moveToFirst()){			//TODO cur empty
			Mask m;
			do{
				m = new Mask(); //TODO optimiz
				m.MaskId = cur.getInt(0);
				m.type = cur.getInt(1);
				m.idBook = cur.getInt(2);
				m.idChap = cur.getInt(3);
				m.startChar = cur.getInt(4);
				m.endChar = cur.getInt(5);
				m.note = cur.getString(6);
				Masks.add(m);
			}while(cur.moveToNext());
		}
		cur.close();
		return Masks;
	}
	public  Collection<ArrayList<MaskItem>> getMasksItem(int book, int chapter){
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
				"from Masks m inner join detail_livres l on m.book=l.id_livre and m.chapter=l.id_chapitre where "
			+constraint+"order by m.book asc, m.chapter asc";
		Cursor cur = myDataBase.rawQuery(query, valConstraint);
		HashMap<Integer, ArrayList<MaskItem>> map = new HashMap<Integer, ArrayList<MaskItem>>();
		if(cur.moveToFirst()){			//TODO cur empty
			MaskItem m;
			do{
				m = new MaskItem(); //TODO optimiz
				m.Mask.MaskId = cur.getInt(0);
				m.Mask.type = cur.getInt(1);
				m.Mask.idBook = cur.getInt(2);
				m.Mask.idChap = cur.getInt(3);
				m.Mask.startChar = cur.getInt(4);
				m.Mask.endChar = cur.getInt(5);
				m.Mask.note = cur.getString(6);
				m.titleChapter = cur.getString(7);
				if(!map.containsKey(m.Mask.idChap ))
					map.put(m.Mask.idChap , new ArrayList<MaskItem>());
				map.get(m.Mask.idChap ).add(m);
//				Masks.add(m);
			}while(cur.moveToNext());
		}
		cur.close();
		
		return map.values();
	}
	
	public  ArrayList<MaskItem> getSearchResult(int book, String token){

		String constraint="contenu_chapitre like ?";
		String[] valConstraint = null;
		if(book != -1){
			constraint += " and id_livre=?";
			valConstraint =  new String[]{ "%"+token+"%",String.valueOf(book)};
		}
		else
			valConstraint =  new String[]{"%"+token+"%"};
			
		String query = "select  id_chapitre, titre_chapitre, contenu_chapitre from detail_livres where " +constraint;
		Cursor cur = myDataBase.rawQuery(query, valConstraint);
		ArrayList<MaskItem> list = new ArrayList<MaskItem>();
		if(cur.moveToFirst()){			//TODO cur empty
			MaskItem m;
			do{
				m = new MaskItem(); //TODO optimiz
				m.Mask.idBook = book;
				m.Mask.idChap = cur.getInt(0);
				m.titleChapter = cur.getString(1);
				m.content = cur.getString(2);
				list.add(m);
			}while(cur.moveToNext());
		}
		cur.close();
		
		return list;
	}
	public void addMask(Mask m){
		ContentValues val = new ContentValues(4);
		val.put("type", m.type);
		val.put("chapter", m.idChap);
		val.put("book", m.idBook);
		val.put("note", m.note);
		val.put("start", m.startChar);
		val.put("end", m.endChar);
		m.MaskId = (int) myDataBase.insert(Mask_TAB, null, val);
	}
	
	public void updateMask(Mask m){
		ContentValues val = new ContentValues(4);
		val.put("type", m.type);
		val.put("chapter", m.idChap);
		val.put("book", m.idBook);
		val.put("note", m.note);
		val.put("start", m.startChar);
		val.put("end", m.endChar);
		myDataBase.update(Mask_TAB, val, "_id = ?",  new String[]{String.valueOf(m.MaskId) });
	}
	*/
	public void deleteMask(int id)
	{
		myDataBase.delete(Mask_TAB, "_id = ?",  new String[]{String.valueOf(id) });
	}
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.

//	public Mask getMasks(int MaskId) {
//		Cursor cur = myDataBase.query(Mask_TAB, new String[]{"_id","type","book","chapter","start","end","note"},"_id=? ", new String[]{String.valueOf(MaskId)}, null, null, null);
//		Mask m=null;
//		if(cur.moveToFirst()){			//TODO cur empty
//				m = new Mask(); //TODO optimiz
//				m.MaskId = cur.getInt(0);
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