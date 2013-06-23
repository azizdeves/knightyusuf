package naitsoft.android.quran;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;


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
    private static String DB_PATH = "/data/data/naitsoft.android.quran/databases/";
 
    private static String DB_NAME = "quran.sql3";
    private static String AUDIO_DB_NAME = "QuranAudioWord.sqlite";
    private static String QURAN_TAB = "quran_text";
    private static String MARK_TAB = "marks";
    
 
    static public DataBaseHelper myDbHelper;
    static private SQLiteDatabase myDataBase; 
    static private SQLiteDatabase audioDataBase; 
 
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
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
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
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    	audioDataBase = SQLiteDatabase.openDatabase("/mnt/sdcard/"+AUDIO_DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
 
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
 
	public byte[] getAudioWord(int hash){
		try {
			
			Cursor cur = audioDataBase.rawQuery("select data from AudioWords where hash="+hash,null);
			byte[] i = null;
			//			rs.beforeFirst();
			if(cur.moveToFirst())
				i= cur.getBlob(0);
			cur.close();
			return i;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static String getAya(int sura , int aya)
	{
		Cursor cur = myDataBase.query(QURAN_TAB, new String[]{"text"}, "sura=? and aya=?", new String[]{String.valueOf(sura),String.valueOf(aya)}, null, null, null);
		String i = "";
		if(cur.moveToFirst()){
			i = cur.getString(0);
		}
		cur.close();
		return i;
	}
//	public static int getAyaCount(int sura){
//		Cursor cur = myDataBase.rawQuery("select count(*) from ? where sura=?", new String[]{QURAN_TAB,String.valueOf(sura)});
//		if(cur.moveToFirst())
//			return cur.getInt(0);
//		return 0;
//		
//	}
	public static Cursor getMarks(){
		Cursor cur = myDataBase.query(MARK_TAB, new String[]{"type","date","sura","aya","_id"},"type=?", new String[]{String.valueOf("*")}, null, null, "date desc");
		return cur;
	}
	
	public static boolean deleteMark(int id ){
		return 1==myDataBase.delete(MARK_TAB, "_id=?", new String[]{String.valueOf(id)});
	}
	
	public Surah getLastMark(){
		Cursor cur = myDataBase.query(MARK_TAB, new String[]{"type","sura","aya"},null, null, null, null, "date desc","1");
		Surah sura = null;
		if(cur.moveToFirst())
			 sura = new Surah(cur);
		cur.close();
		return sura;
	}
	
	public void addMark(String type,int sura, int aya){
		ContentValues val = new ContentValues(4);
		val.put("type", type);
		val.put("date", (int)(new Date().getTime()));
		val.put("aya", aya);
		val.put("sura", sura);
//		myDataBase.beginTransaction();
		myDataBase.insert(MARK_TAB, null, val);
//		myDataBase.endTransaction();
		
	}
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
 
}