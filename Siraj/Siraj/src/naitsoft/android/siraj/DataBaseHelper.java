package naitsoft.android.siraj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
 
	public static String getChapter(int livre , int chapitre)
	{
		Cursor cur = myDataBase.query(CONTENT_BOOK_TAB, new String[]{"contenu_chapitre"}, "id_chapitre=? and id_livre=?", new String[]{String.valueOf(chapitre),String.valueOf(livre)},null, null, null);
		if(cur.moveToFirst())
			return cur.getString(0);
		return "";
	}
	
	public  Cursor getChaptersOfBook(int livre)
	{
		Cursor cur = myDataBase.query(CONTENT_BOOK_TAB, new String[]{"id_chapitre","id_chapitrep","titre_chapitre"},"id_livre=?", new String[]{String.valueOf(livre)}, null, null, null);
		return cur;
	}
	public static Cursor getMarks(){
		Cursor cur = myDataBase.query(MARK_TAB, new String[]{"type","date","sura","aya"},null, null, null, null, null);
		return cur;
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