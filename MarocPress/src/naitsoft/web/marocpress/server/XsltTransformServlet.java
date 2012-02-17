package naitsoft.web.marocpress.server;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XsltTransformServlet extends HttpServlet {

	final static String hesRss = "http://hespress.com/feed/index.atom";
	final static String lakomeRss = "http://lakome.com/index.php?format=feed&type=rss&title=";
	final static String demainRss = "http://www.demainonline.com/feed/";
	final static String hibaRss = "http://hibapress.com/rss.php";
    private static final Logger log = Logger.getLogger(XsltTransformServlet.class.getName());
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
    	
//    	Feed feed = new  Feed();
//    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//    	try {
//    		feed.setLastArticle(format.parse("2010-11-01"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//    	feed.setRssLink(lakomeRss);
//    	feed.setXslt("template_lakome.xsl");
////    	Dao dao = new Dao();
//    	Dao.save(feed);
    	List<Feed> feeds = Dao.getFeeds();
    	for(Feed f : feeds){
    		log.info("traitement feed= "+f.getName());
    		
    		RSSFeedParser.readFeed(f);
    	}
    }
    


}
