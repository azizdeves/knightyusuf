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

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class Handler {

	public static void mains(String[] args) {
//		RSSFeedParser parser = new RSSFeedParser(
//				"http://lakome.com/index.php?format=feed&type=rss&title=");
//		Feed feed = parser.readFeed();
//		System.out.println(feed);
//		for (FeedMessage message : feed.getMessages()) {
//			System.out.println(message);
//
//		}


	        try {
	        	String message = URLEncoder.encode("my message", "UTF-8");
	            URL url = new URL("http://www.lakome.com");
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setDoOutput(true);
//	            connection.setRequestMethod("POST");
//	            connection.setRequestProperty("X-MyApp-Version", "2.7.3");
	            connection.getInputStream();
//	            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//	            writer.write("message=" + message);
//	            writer.close();
	    
	            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
	                // OK
	            } else {
	                // Server returned HTTP error code.
	            }
	        } catch (MalformedURLException e) {
	            // ...
	        } catch (IOException e) {
	            // ...
	        }
	        

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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// create an instance of HtmlCleaner
		HtmlCleaner cleaner = new HtmlCleaner();
        HttpClient httpclient = new DefaultHttpClient();
        
		CleanerProperties props = cleaner.getProperties();

		props.setOmitXmlDeclaration(true);

//		File file = new File("aar.html");
		try {
            HttpGet httpget = new HttpGet("http://www.hespress.com/");

            URL url = new URL("http://hespress.com/politique/47144.html");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.77 Safari/535.7");
            connection.setRequestProperty("Accept-Charset", "ISO-8859-1,UTF-8;q=0.7,*;q=0.3");
            connection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
            connection.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6,fr;q=0.4");
            connection.setRequestProperty("Connection", "keep-alive");
//            connection.setRequestProperty("Host", "hespress.com");
//            connection.setRequestProperty("Referer", "http://hespress.com/");
            
            
//          httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//          httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.77 Safari/535.7");
//          httpget.setHeader("Accept-Charset", "ISO-8859-1,UTF-8;q=0.7,*;q=0.3");
//          httpget.setHeader("Accept-Encoding", "gzip,deflate,sdch");
//          httpget.setHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6,fr;q=0.4");
//          httpget.setHeader("Connection", "keep-alive");
//          httpget.setHeader("Host", "hespress.com");
//          httpget.setHeader("Referer", "http://hespress.com/");
          // Create a response handler
//          ResponseHandler<String> responseHandler = new BasicResponseHandler();
//          String responseBody = httpclient.execute(httpget, responseHandler);
          
          
//			TagNode node = cleaner.clean(new URL("http://lakome.com/%D8%B1%D8%A3%D9%8A/49-%D9%83%D8%AA%D8%A7%D8%A8-%D8%A7%D9%84%D8%B1%D8%A3%D9%8A/12099-2012-02-03.html"),"UTF-8");
			TagNode node = cleaner.clean(connection.getInputStream(),"UTF-8");
//			TagNode[] myNodes = node.getElementsByAttValue("class", "article-content", true, true);
			String content = new PrettyXmlSerializer(props).getAsString( node,"UTF-8");
			
			TransformerFactory tFactory = TransformerFactory.newInstance();
			content = content.replaceAll("xmlns:xml=\"xml\"", "");
			Transformer transformer =
					tFactory.newTransformer
					(new StreamSource
							("templatear.xsl"));

			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			transformer.transform(new StreamSource(new ByteArrayInputStream(content.getBytes("UTF-8")))
			,new StreamResult(out));

			Article article = new Article();
			article.setContent(out.toString("UTF-8"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		catch (TransformerException e) {
			e.printStackTrace();
		}

	}

}
