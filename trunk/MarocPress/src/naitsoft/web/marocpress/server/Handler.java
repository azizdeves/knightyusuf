package naitsoft.web.marocpress.server;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.URL;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


import naitsoft.web.marocpress.server.entity.Article;
import naitsoft.web.marocpress.server.entity.ArticleContentDto;

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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static final Logger log = Logger.getLogger(RSSFeedParser.class.getName());
	/**
	 * @param args
	 */
	public static void transform(Article article) {
		// create an instance of HtmlCleaner
		HtmlCleaner cleaner = new HtmlCleaner();
//		http://t1.hespress.com/files/amdhnewcons_865104348.jpg
//		http://s1.hespress.com/cache/thumbnail/article_medium/amdhnewcons_865104348.jpg
		CleanerProperties props = cleaner.getProperties();

//		props.setOmitXmlDeclaration(true);

		try {
			URL url = new URL(article.getLink());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.77 Safari/535.7");
			connection.setRequestProperty("Accept-Charset", "ISO-8859-1,UTF-8;q=0.7,*;q=0.3");
			connection.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6,fr;q=0.4");
			connection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
//			connection.setRequestProperty("Connection", "keep-alive");
			log.info( "start cleaning ");
			TagNode node=null;
			try{
			 node = cleaner.clean(new GZIPInputStream(connection.getInputStream()),"UTF-8");
		} catch (IOException e) {
			node = cleaner.clean(connection.getInputStream(),"UTF-8");
		}
			if(article.getFeed().getMediaXPath()!=null){
				
				Object[] myNodes = node.evaluateXPath(article.getFeed().getMediaXPath());

				if(myNodes[0]!=null){
					String src =((TagNode)myNodes[0]).getAttributeByName("src");
					if(!src.contains(article.getFeed().getName()))
						src = article.getFeed().getName()+src;
					article.setMedia(src);
				}
			}
			Object[] myNodes = node.evaluateXPath(article.getFeed().getXslt());
			
			String content = new PrettyXmlSerializer(props).getAsString( (TagNode) myNodes[0],"UTF-8");
//
//			TransformerFactory tFactory = TransformerFactory.newInstance();
//			content = content.replaceAll("xmlns:xml=\"xml\"", "");
//			Transformer transformer = tFactory.newTransformer(new StreamSource(article.getFeed().getXslt()));
//
//			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			transformer.transform(new StreamSource(new ByteArrayInputStream(content.getBytes("UTF-8")))
//				,new StreamResult(out));

			
			ArticleContent articleContent = new ArticleContent();
//			content = out.toString("UTF-8");
			articleContent.setContent(new Text(content));
//			System.out.println(articleContent.getContent());
			
			Dao dao = new Dao();
			dao.saveArticleContent(articleContent);
			article.setDescripion(content.substring(0, content.length()<250?content.length():250));
			article.setContentId(articleContent.getId());
			dao.save(article);

		} catch (IOException e) {
			e.printStackTrace();
		}

//		catch (TransformerException e) {
//			e.printStackTrace();
//		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (XPatherException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {


		HtmlCleaner cleaner = new HtmlCleaner();
//		http://t1.hespress.com/files/amdhnewcons_865104348.jpg
//		http://s1.hespress.com/cache/thumbnail/article_medium/amdhnewcons_865104348.jpg
		CleanerProperties props = cleaner.getProperties();

//		props.setOmitXmlDeclaration(true);

		try {
			URL url = new URL("http://www.aljamaa.net/ar/document/52770.shtml");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.77 Safari/535.7");
			connection.setRequestProperty("Accept-Charset", "ISO-8859-1,UTF-8;q=0.7,*;q=0.3");
			connection.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6,fr;q=0.4");
			connection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
//			connection.setRequestProperty("Connection", "keep-alive");
			log.info( "start cleaning ");
			TagNode node = cleaner.clean(new GZIPInputStream(connection.getInputStream()),"UTF-8");
			String content = new PrettyXmlSerializer(props).getAsString( node,"UTF-8");

			TransformerFactory tFactory = TransformerFactory.newInstance();
			content = content.replaceAll("xmlns:xml=\"xml\"", "");
			Transformer transformer = tFactory.newTransformer(new StreamSource("d://templatear.xsl"));
	
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			transformer.transform(new StreamSource(new ByteArrayInputStream(content.getBytes("UTF-8")))
				,new StreamResult(out));

			ArticleContent articleContent = new ArticleContent();
			content = out.toString("UTF-8");
			articleContent.setContent(new Text(content));
			

		} catch (IOException e) {
			e.printStackTrace();
		}

		catch (TransformerException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}