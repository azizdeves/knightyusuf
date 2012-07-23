package naitsoft.android.quranmemo;

import java.io.IOException;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
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
 
    private static String DB_NAME = "quranMemo.db";
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
        		myDataBase.execSQL("create table StepLines(_id Integer primary key,  page Integer, step String)");
        		
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
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
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
 
	public ArrayList<Mask> getMasks(int page){

		Cursor cur = myDataBase.query(Mask_TAB, new String[]{"_id","page","startX","endX","startLine","endLine"},"page = ?",new String[]{String.valueOf(page)} , null, null, "startLine asc");
		ArrayList<Mask> Masks = new ArrayList<Mask>();;
		if(cur.moveToFirst()){			
			Mask m;
			do{
				m = new Mask(); 
				m.id = cur.getInt(0);
				m.page = cur.getInt(1);
				m.startX = cur.getInt(2);
				m.endX = cur.getInt(3);
				m.startLine = cur.getInt(4);
				m.endLine = cur.getInt(5);
				Masks.add(m);
			}while(cur.moveToNext());
		}
		cur.close();
		return Masks;
	}
	

	public void addMask(Mask m){
		ContentValues val = new ContentValues(5);
		val.put("page", m.page);
		val.put("startX", m.startX);
		val.put("endX", m.endX);
		val.put("startLine", m.startLine);
		val.put("endLine", m.endLine);
		m.id = (int) myDataBase.insert(Mask_TAB, null, val);
	}
	
	public void updateMask(Mask m){
		ContentValues val = new ContentValues(5);
		val.put("page", m.page);
		val.put("startX", m.startX);
		val.put("endX", m.endX);
		val.put("startLine", m.startLine);
		val.put("endLine", m.endLine);
		myDataBase.update(Mask_TAB, val, "_id = ?",  new String[]{String.valueOf(m.id) });
	}
	
	public void deleteMask(int id)
	{
		myDataBase.delete(Mask_TAB, "_id = ?",  new String[]{String.valueOf(id) });
	}

 
}