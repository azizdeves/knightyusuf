package naitsoft.web.marocpress.server;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.net.URL;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyHtmlSerializer;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.SimpleXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.google.appengine.api.datastore.Text;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class Handler {

	final static String hesRss = "http://hespress.com/feed/index.atom";
	final static String lakomeRss = "http://lakome.com/index.php?format=feed&type=rss&title=";
	final static String demainRss = "http://www.demainonline.com/feed/";
	final static String hibaRss = "http://hibapress.com/rss.php";

	/**
	 * @param args
	 */
	public static void transform(Article article) {
		// create an instance of HtmlCleaner
		HtmlCleaner cleaner = new HtmlCleaner();

		CleanerProperties props = cleaner.getProperties();

		props.setOmitXmlDeclaration(true);

		try {
			URL url = new URL(article.getLink());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.77 Safari/535.7");
			connection.setRequestProperty("Accept-Charset", "ISO-8859-1,UTF-8;q=0.7,*;q=0.3");
			connection.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6,fr;q=0.4");
			connection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
//			connection.setRequestProperty("Connection", "keep-alive");
			TagNode node = cleaner.clean(new GZIPInputStream(connection.getInputStream()),"UTF-8");
			if(article.getFeed().getMediaXPath()!=null){
				Object[] myNodes = node.evaluateXPath(article.getFeed().getMediaXPath());

				if(myNodes[0]!=null)
					article.setMedia(((TagNode)myNodes[0]).getAttributeByName("src"));
			}
			String content = new PrettyXmlSerializer(props).getAsString( node,"UTF-8");

			TransformerFactory tFactory = TransformerFactory.newInstance();
			content = content.replaceAll("xmlns:xml=\"xml\"", "");
			Transformer transformer = tFactory.newTransformer(new StreamSource(article.getFeed().getXslt()));

			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			transformer.transform(new StreamSource(new ByteArrayInputStream(content.getBytes("UTF-8")))
				,new StreamResult(out));

			ArticleContent articleContent = new ArticleContent();
			articleContent.setContent(new Text(out.toString("UTF-8")));
//			System.out.println(articleContent.getContent());
			
//			Dao dao = new Dao();
			Dao.save(article);
			Dao.save(articleContent);

		} catch (IOException e) {
			e.printStackTrace();
		}

		catch (TransformerException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (XPatherException e) {
			e.printStackTrace();
		} 

	}

	public static void main(String[] args) {


		XmlReader reader = null;
		try {
			URL url  = new URL(hesRss);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.77 Safari/535.7");
			connection.setRequestProperty("Accept-Charset", "ISO-8859-1,UTF-8;q=0.7,*;q=0.3");
			connection.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6,fr;q=0.4");

			reader = new XmlReader(connection.getInputStream(),true,"UTF-8");
			SyndFeed feed = new SyndFeedInput().build(reader); 
			System.out.println("Feed Title: "+ feed.getAuthor());

			for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
				SyndEntry entry = (SyndEntry) i.next();
				System.out.println(entry.getTitle());
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//	            if (reader != null)
			//	                reader.close();
		}

		//		RSSFeedParser parser = new RSSFeedParser(hibaRss);
		//		Feed feed = parser.readFeed();
		//		System.out.println(feed);
		//		for (FeedMessage message : feed.getMessages()) {
		//			System.out.println(message);
		//
		//		}




		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpGet httpget = new HttpGet("http://www.hespress.com/");

			//            System.out.println("executing request " + getURI());

			httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.77 Safari/535.7");
			httpget.setHeader("Accept-Charset", "ISO-8859-1,UTF-8;q=0.7,*;q=0.3");
			httpget.setHeader("Accept-Encoding", "gzip,deflate,sdch");
			httpget.setHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6,fr;q=0.4");
			httpget.setHeader("Connection", "keep-alive");
			httpget.setHeader("Host", "hespress.com");
			httpget.setHeader("Referer", "http://hespress.com/");
			// Create a response handler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpclient.execute(httpget, responseHandler);
			System.out.println("----------------------------------------");
			System.out.println(responseBody);
			System.out.println("----------------------------------------");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}	

	}

}
