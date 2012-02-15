package naitsoft.web.marocpress.server;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

public class RSSFeedParser {
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


	@SuppressWarnings("null")
	public static Feed readFeed(Feed feedSrc) {
		try {
			final URL url=  new URL(feedSrc.getRssLink());;
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.77 Safari/535.7");
			connection.setRequestProperty("Accept-Charset", "ISO-8859-1,UTF-8;q=0.7,*;q=0.3");
			connection.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6,fr;q=0.4");

			XmlReader reader = null;
//			byte[] b =new byte[100];
//			connection.getInputStream().read(b);
			reader = new XmlReader(connection.getInputStream(),true,"UTF-8");
			SyndFeed feed = new SyndFeedInput().build(reader); 
			Date lastPublish=feedSrc.getLastArticle();
			SyndEntry entry = null;
			ArrayList<Article> entriesToHandle = new ArrayList<Article>();
			for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
				entry = (SyndEntry) i.next();
				if(entry.getPublishedDate().compareTo(feedSrc.getLastArticle())<0)
					continue;
				
				entriesToHandle.add(new Article(entry,feedSrc));
				if(entry.getPublishedDate().compareTo(lastPublish)>0)
					lastPublish = entry.getPublishedDate();
			}
//			Dao dao = new Dao();
			feedSrc.setLastArticle(lastPublish);
			Dao.update(feedSrc);
			//save feed
			for(Article e : entriesToHandle)
			{
				Handler.transform(e);
				
//				Queue que= QueueFactory.getQueue("userfeedupdates");
//				que.add(url("/task/tskseed?s=&m="+task.getMominId()+"&w="+task.getDate().getTime()+"&t="+new Date().getTime()+"&n="+ task.getName().replaceAll(" ","+")).method(Method.GET));
//				que.add(TaskOptions.Builder.url("/task/tskseed?s=&m="+task.getMominId()+"&w="+task.getDate().getTime()+"&t="+new Date().getTime()).param("n", task.getName()).method(Method.GET));
				/*que.add(url("/tskseed")
						.param("s","2")
						.param("n",task.getName())
						.param("m",task.getMominId()).
						param("w",task.getDate().getTime()+"")
						.param("t",new Date().getTime()+"")
						);*/
			}
			
		}catch(Exception e){
			System.out.print(e);
		}
		return null;

	}

	 


}
