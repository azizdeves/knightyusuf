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

import naitsoft.web.marocpress.server.entity.AudioWord;
import naitsoft.web.marocpress.server.entity.Feed;

public class XsltTransformServlet extends HttpServlet {

	final static String hesRss = "http://hespress.com/feed/index.atom";
	final static String lakomeRss = "http://lakome.com/index.php?format=feed&type=rss&title=";
	final static String demainRss = "http://www.demainonline.com/feed/";
	final static String hibaRss = "http://hibapress.com/rss.php";
    private static final Logger log = Logger.getLogger(XsltTransformServlet.class.getName());
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
//    	TTSservice tts = new TTSservice();
//		tts.constructFiles();
    	
    	
    }
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//    throws ServletException, IOException {
//    	String hash = req.getParameter("h");
//    	if(hash!=null){
//    		resp.setContentType("audio/mpeg");
//    		Dao dao = new Dao();
//    		AudioWord aw = dao.getAudioWord(Integer.parseInt(hash));
//    		resp.getOutputStream().write(aw.getData().getBytes());
//    		resp.getOutputStream().close();
//    		
//    		
//    	}else{
//    		
//    		TTSservice tts = new TTSservice();
//    		tts.readFeed();
//    	}
//    	
//    	
//    }
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//    throws ServletException, IOException {
//    	
////    	Feed feed = new  Feed();
////    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
////    	try {
////    		feed.setLastArticle(format.parse("2010-11-01"));
////		} catch (ParseException e) {
////			e.printStackTrace();
////		}
////    	feed.setRssLink(hesRss);
////    	feed.setXslt("templatear.xsl");
//    	Dao dao = new Dao();
////    	Dao dao1 = new Dao();
////    	Dao.save(feed);
//    	
//    	List<Feed> feeds = dao.getFeeds();
//    	dao.close();
//    	for(Feed f : feeds){
//    		log.info("traitement feed= "+f.getName());
////    		dao1.update(f); 
//    		RSSFeedParser.readFeed(f);
//    	}
//    }
    


}
