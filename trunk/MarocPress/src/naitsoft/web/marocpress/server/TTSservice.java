package naitsoft.web.marocpress.server;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Text;

import naitsoft.web.marocpress.server.entity.AudioFile;
import naitsoft.web.marocpress.server.entity.AudioWord;
import naitsoft.web.marocpress.server.entity.Feed;
import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;


public class TTSservice {
	static long seek = -1;
	static int seuilQuota = 0;
	static int match=0;
	static int fromCache=0;

	private static final Logger log = Logger.getLogger(" ");
	private List<AudioWord> list;

	public void constructFiles(){
		log.setLevel(Level.ALL);
		log.info("Salam ");
		int limitQuery = 1;
		int stepLimit = 175;
//		getCache().get("limit");
		
		Dao dao = new Dao();
		try {
			long index=0;
			int j ;
			for(;limitQuery<17574;limitQuery+=j,index++){
				ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
				StringBuffer sBuf = new StringBuffer();
				log.info("reading limitQuery "+limitQuery);
				list = dao.getAudioWords(limitQuery, stepLimit);
				log.info("red result "+list.size());
				for( j=0;true;j++){
					if(byteArray.size()+list.get(j).getData().getBytes().length>1023000)
						break;
					byteArray.write(list.get(j).getData().getBytes());
					sBuf.append(list.get(j).getHash()+":"+list.get(j).getData().getBytes().length+" ");
					log.info(list.get(j).getHash()+":"+list.get(j).getData().getBytes().length+"__"+j);
//					log.info(list.get(j).getHash()+":"+list.get(j).getData().getBytes().length);
//					Thread.currentThread().sleep(500);
					
					
				}
				AudioFile f = new AudioFile();
				f.text = new Text(sBuf.toString());
				f.setData(byteArray.toByteArray());
				f.setSeek(index);
				log.warning("saving file="+index+" size="+f.getData().getBytes().length);
				dao.save(f);
				log.warning("saved");
				Thread.currentThread().sleep(3000);
			}
			
		}catch(Exception e)
		{
			log.severe(e.getMessage());
//			dao.sendMail(e.getMessage());
		}
	}
	private static Cache cache;
	@SuppressWarnings("null")
	public static Feed readFeed() {
		log.setLevel(Level.ALL);
		log.info("Salam ");
		if(seuilQuota>10){
			if(seuilQuota>50){
				seuilQuota=0;
			}
			seuilQuota++;
			return null;
		}
		Dao dao = new Dao();
		try {
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
			for(int j=0;j<5;j++){
				FileInputStream fis = new FileInputStream("quran.txt");
				BufferedReader bis = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
				String aya=""; 
				URL url = null;
				bis.skip(seek);
				while((aya = bis.readLine()).isEmpty())
				{
					//				bis.skip(1);
					seek++;
				}
				AudioWord aw= null;
				String[] words = aya.split(" ");
				String word="";
				for(int i =0; i<words.length;i++){
					word = words[i];
					if(getCache().get(word)==null){
						aw = dao.getAudioWord(word.hashCode());
						if(aw!=null){
							if(word.equals(aw.getWord())){
								log.info("BD "+word+"  m="+match+"  Cch="+fromCache);
								match++;
								getCache().put(word, new Integer(0));
								continue;
							}
						}
					}
					else{
						fromCache++;
						match++;
						log.info("CACHE  "+word+"  m="+match+"  Cch"+fromCache);
						continue;
					}
					log.info("NEW  "+word+"  m="+match+"  Cch="+fromCache);
					dao.sendMail("NEW  "+word+"  m="+match+"  Cch="+fromCache);

					url=  new URL("http://translate.google.com/translate_tts?ie=UTF-8&tl=ar&q="+word);

					HttpURLConnection connection = (HttpURLConnection) url.openConnection();

					connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
					connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.77 Safari/535.7");
					connection.setRequestProperty("Accept-Charset", "ISO-8859-1,UTF-8;q=0.7,*;q=0.3");
					connection.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6,fr;q=0.4");

					connection.connect();
					if(connection.getResponseCode()== 503)
						dao.sendMail("Status 503");
					int size = Integer.parseInt(connection.getHeaderField("Content-Length"));
					byte[] b =new byte[size];
					if(size<1000)
						dao.sendMail("size of "+word+" = "+size);
					BufferedInputStream buf = new BufferedInputStream(connection.getInputStream());
					if(buf.read(b)!=-1)
					{
						aw = new  AudioWord();
						aw.setData(b);
						aw.setWord(word);
						aw.setSeek(seek);
						dao.save(aw);
						getCache().put(aw.getWord(), new Integer(0));
						//					log.info("size= "+b.length);
						//					System.out.println("size= "+b.length);


					}
					//				Thread.currentThread().
					connection.disconnect();
				}
				seek += aya.length()+1;
				getCache().put("s", seek);
				if(j==0){
					Thread.currentThread().sleep(7000);
					log.info("Sleeping");
					
				}
			}
			//			try{
			//			if(match%5 ==0)
			//				dao.sendMail("match="+match+"  fromCache="+fromCache);
			//			log.info("match="+match+"  fromCache="+fromCache);
			//			}catch(Exception e) {			}
			//			}

		}catch(Exception e){
			seuilQuota++;
			log.severe(e.getMessage());
			if(seuilQuota>10)
				dao.sendMail(e.getMessage());

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
