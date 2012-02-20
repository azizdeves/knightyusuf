package naitsoft.web.marocpress.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import naitsoft.web.marocpress.server.entity.Article;
import naitsoft.web.marocpress.server.entity.ArticleContent;
import naitsoft.web.marocpress.server.entity.Feed;
import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.sun.syndication.feed.synd.SyndEntry;

public class Dao {

	static Cache cache;
	EntityManager em;
	public Dao() {
		em=EMF.get().createEntityManager();
	}

	//    @SuppressWarnings("unchecked")
	//    public List<Task> getWeekTasks(String momin, String group, Date startWeek)
	//    {
	//            Query query;
	//            List<Task> list;
	//            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	////          String keyWeek = momin + startWeek.toGMTString().substring(0, 11);
	//            if(group == null){
	//                    query = em.createQuery("select from "+Task.class.getName()+" t where t.mominId = :momin AND t.date >= :start  AND  t.date  < :end order by date asc");     
	////                  list = (List<Task>) getCache().get(keyWeek);
	////                  if(list!=null)
	////                          return list;            
	//            }else{
	//                    query = em.createQuery("select from "+Task.class.getName()+" t where t.mominId = :momin AND  group =:group AND  t.date >= :start  AND  t.date  < :end order by date asc");         
	//                    query.setParameter("group",Integer.parseInt(group));                    
	//            }               
	//        Date end = CalendarUtil.copyDate(startWeek);
	//        CalendarUtil.addDaysToDate(end,7);
	//        query.setParameter("momin",momin);
	//        query.setParameter("start",startWeek);
	////      end.setTime(end.getTime()-2000);
	//        query.setParameter("end",end);          
	//        list =new  ArrayList((List<Task>)query.getResultList());
	//        
	////      if(group == null)
	////          getCache().put(keyWeek, list);
	//
	//        return list;
	//    }

	public List<Article> getArticles(){
		Query query;
		List<Article> list;
		query = em.createQuery("select from "+Article.class.getName()+" a  order by date desc limit 1,10 ");   
//		query.setMaxResults(10);
		return list =new  ArrayList((List<Article>)query.getResultList());
	}
	public  List<Feed> getFeeds(){
		
		try{
			Query query;
		List<Feed> list;
		query = em.createQuery("select from "+Feed.class.getName()+" a "); 
		return list =new  ArrayList((List<Feed>)query.getResultList());
		}finally{
//			em.close();
			
		}
	}

	public static Article getArticleFromEntry(SyndEntry entry,Feed feed) {
		Article art = new Article();
		art.setLink(entry.getLink());
		art.setFeedId(feed.getId());
		art.setTitle(entry.getTitle());
		art.setDate( entry.getPublishedDate());
		art.setFeed(feed) ;
		return art;
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

	public static void cache(String key, Object o){
		cache.put(key, o);
	}
	
	
	public ArticleContent getArticleContentById(Long  id)
	{
		Key key=KeyFactory.createKey(ArticleContent.class.getSimpleName(), id);
		ArticleContent kh=em.find(ArticleContent.class,key);
		return kh;
	}

	public  void update(Object o){
		em.getTransaction().begin();
//		em.joinTransaction();
		em.merge(o);
		em.flush();
		em.getTransaction().commit();
//		em.close();
	}
//	private static EntityManager em{
//		if(em == null)
//		{
//			em=EMF.get().createEntityManager();
//		}
//		return em;
//	}
	public  void save(Object o){
		em.getTransaction().begin();
		em.persist(o);
		em.flush();
		em.getTransaction().commit();
//		em.close();
	}
	
	public void saveArticleContent(ArticleContent artContent)
	{
		em.getTransaction().begin();
		if(artContent.getId()!=null){
			em.merge(artContent);
		}
		else{
			em.persist(artContent);
		}
		em.flush();
		em.getTransaction().commit();
//		em.close();

	}

	public void delete(Object c)
	{
		em.getTransaction().begin();
		em.remove(c);
		em.getTransaction().commit();
	}
	
	public void close()
	{
		if(em.isOpen())
			em.close();
	}
}