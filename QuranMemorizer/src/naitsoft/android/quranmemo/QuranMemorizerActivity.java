package naitsoft.android.quranmemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class QuranMemorizerActivity extends Activity {
    
	private static DataBaseHelper myDbHelper;
	 MarkBar markBar;
	private Saf7a saf7a;
	static QuranMemorizerActivity activity;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        saf7a = (Saf7a) findViewById(R.id.saf7a);
        initDB();
        saf7a.setPage(17);
        saf7a.init();
        markBar = new MarkBar(this);
    }
    
    public static ArrayList<Mask> getMasks(int page){
    	return myDbHelper.getMasks(page);
    }
	public static void saveMask(Mask msk) {
		if(msk.id != -1)
			myDbHelper.updateMask(msk);
		else{
			myDbHelper.addMask(msk);
			
		}

	}
	public void deleteMask(){
		int id = saf7a.editingMask.id;
		if(id != -1){
			
			deleteMask(id);
			saf7a.removeEditingMask();
		}
	}
	public void deleteMask(int id){
		myDbHelper.deleteMask(id);
	}

    public void downloadPage(String sPage)
    {
//    	"http://c00022506.cdn1.cloudfiles.rackspacecloud.com/39_2.png"
    	File fileTmp = null;
    	String folderPath = "/mnt/sdcard/QuranPages/";
//    	String sPage = addZero(page);
    	URL u;
    	try {
    		u = new URL("http://www.myquran-contents.us/content/quran/arabic/"+sPage+".jpg");
    		fileTmp = new File(	folderPath+sPage+".tmp" );
    		File file = new File(folderPath+sPage+".jpg");
    		HttpURLConnection c = (HttpURLConnection) u.openConnection();
    		c.setRequestMethod("GET");
    		c.setDoOutput(true);
    		c.connect();
    		//		c.getHeaderFieldInt("Content-Length", 0);
    		File folder = new File(folderPath);
    		folder.mkdir();
    		FileOutputStream f = new FileOutputStream(fileTmp);

    		InputStream in = c.getInputStream();

    		byte[] buffer = new byte[1024];
    		int length;
    		while ( ( length = in.read(buffer)) > 0 ) {
    			f.write(buffer,0,length);
    		}

    		f.close();
    		fileTmp.renameTo(file);
    		fileTmp = null;
    	} catch (MalformedURLException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	public void initDB()
	{
		myDbHelper = DataBaseHelper.getInstance(this);
	}
	
	public static  String addZero(int nb)
	{
		String s = "000"+nb;
		return s.substring(s.length()-4);
		
	}
}
class MarkBar{

	private LinearLayout parentView;
	private QuranMemorizerActivity qActivity;

	public MarkBar(final QuranMemorizerActivity qActivity) {
		this.qActivity = qActivity;
		parentView = (LinearLayout) qActivity.findViewById(R.id.mark_bar);
//		Button greenBtn = (Button) qActivity.findViewById(R.id.green_mrk);
		ImageButton saveBtn = (ImageButton) qActivity.findViewById(R.id.save_mrk);
		ImageButton deleteBtn = (ImageButton) qActivity.findViewById(R.id.delete_mrk);
		
	
		saveBtn.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				
			}
		});
		deleteBtn.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				qActivity.deleteMask();
				setVisibility(View.GONE);
			}
		});
		
		
	}

	public void setVisibility(int visibility) {
		parentView.setVisibility(visibility );
		
	}

}