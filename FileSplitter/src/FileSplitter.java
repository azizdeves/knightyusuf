import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class FileSplitter {

	public static void main(String[] args) throws Exception {  
	    String base = "c:\\siraj";  
	    String ext = ".sql";  
	    int split = 1024 * 1016;  
	    byte[] buf = new byte[1024];  
	    int chunkNo = 1;  
	    File inFile = new File(base + ext);  
	    FileInputStream fis = new FileInputStream(inFile);  
	    while (true) {  
	      FileOutputStream fos = new FileOutputStream(new File(base + chunkNo + ext));  
	      for (int i = 0; i < split / buf.length; i++) {  
	        int read = fis.read(buf);  
	        fos.write(buf, 0, read);  
	        if (read < buf.length) {  
	          fis.close();  
	          fos.close();  
	          return;  
	        }  
	      }  
	      fos.close();  
	      chunkNo++;  
	    }  
	  } 
}
