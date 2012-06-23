package naitsoft.android.quranmemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class QuranMemorizerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
    }
    
    public void downloadPage()
    {
//    	"http://c00022506.cdn1.cloudfiles.rackspacecloud.com/39_2.png"
    	File fileTmp = null;
    	String folderPath = "/mnt/sdcard/QuranPages/";
    	URL u;
    	try {
    		u = new URL("http://quranflash.com/quran%20modules/Warsh/size%20B/Quran_Page_011.fbk");
    		fileTmp = new File(	"a.tmp" );
    		File file = new File("a.jpg");
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
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}