package naitsoft.web.marocpress.server;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import naitsoft.web.marocpress.server.entity.Article;
import naitsoft.web.marocpress.server.entity.AudioWord;
import naitsoft.web.marocpress.server.entity.Feed;
import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;


import com.google.appengine.api.datastore.Blob;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

public class TTSservice {
	static final String TITLE = "title";
	static final String DESCRIPTION = "description";
	static final String CHANNEL = "channel";
	static final String LANGUAGE = "language";
	static final String COPYRIGHT = "copyright";
	static final String LINK = "link";
	static final String AUTHOR = "author";
	static final String ITEM = "item";
	static final String PUB_DATE = "pubDate";
	static final String GUID = "guid";
	static final String MEDIA = "media:thumbnail";
	static long seek = -1;

	private static final Logger log = Logger.getLogger(TTSservice.class.getName());
	private static Cache cache;
	@SuppressWarnings("null")
	public static Feed readFeed() {
		Dao dao = new Dao();
		try {

			FileInputStream fis = new FileInputStream("quran.txt");
			BufferedReader bis = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
			String aya=""; 
			if(seek == -1){
				Long cachedSeek = (Long) getCache().get("s");
				if(cachedSeek == null){
					seek = dao.getLastSeek();
//					seek = 0;//TODO load the last seek from the database
				}else
				{
					seek = cachedSeek.longValue();
				}
			}
			URL url = null;
			bis.skip(seek);
			while((aya = bis.readLine()).isEmpty())
			{
//				bis.skip(1);
				seek++;
			}
			AudioWord aw;
			String[] words = aya.split(" ");
			String word="";
			for(int i =0; i<words.length;i++){
				word = words[i];
				aw = dao.getAudioWord(word.hashCode());
				if(aw!=null){
					if(word.equals(aw.getWord()))
						continue;
				}
				url=  new URL("http://translate.google.com/translate_tts?ie=UTF-8&tl=ar&q="+word);

				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.77 Safari/535.7");
				connection.setRequestProperty("Accept-Charset", "ISO-8859-1,UTF-8;q=0.7,*;q=0.3");
				connection.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6,fr;q=0.4");

				connection.connect();
				if(connection.getResponseCode()== 503)
					throw new Exception("bazaaaaaaaaaaaaaaaaaaaaaaaaaaaaf");
				int size = Integer.parseInt(connection.getHeaderField("Content-Length"));
				byte[] b =new byte[size];
				BufferedInputStream buf = new BufferedInputStream(connection.getInputStream());
				if(buf.read(b)!=-1)
				{
					aw = new  AudioWord();
					aw.setData(b);
					aw.setWord(word);
					aw.setSeek(seek);
					dao.save(aw);
					log.info("size= "+b.length);
					System.out.print("size= "+b.length);


				}
				//				Thread.currentThread().
				connection.disconnect();
			}
			seek += aya.length()+1;
			getCache().put("", seek);
			//			}

		}catch(Exception e){
			System.out.print(e);
			dao.sendMail(e);
			
		}
		return null;

	}
	public static Cache getCache()
	{
		if(cache !=null)
			return cache;
		try {
			CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
			cache = cacheFactory.createCache(Collections.emptyMap());
		} catch (CacheException e) {
			// ...
		}
		return cache;
	}






}
