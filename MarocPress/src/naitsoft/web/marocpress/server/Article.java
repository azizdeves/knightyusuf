package naitsoft.web.marocpress.server;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.google.gwt.user.client.rpc.GwtTransient;
import com.sun.syndication.feed.synd.SyndEntry;

@Entity
public class Article implements Serializable{
        
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;        
        
        private String link;
        
        private Date date;
        
        private String title;
        
        private String descripion;
        
        private String media;
        
        private Long feedId;
        
        @Transient
        @GwtTransient
        private Feed feed;
        

		public Article(SyndEntry entry,Feed feed) {
			link = entry.getLink();
			feedId = feed.getId();
			title = entry.getTitle();
			date = entry.getPublishedDate();
			this.feed = feed;
		}

		public Feed getFeed() {
			return feed;
		}

		public void setFeed(Feed feed) {
			this.feed = feed;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}

		public String getDescripion() {
			return descripion;
		}

		public void setDescripion(String descripion) {
			this.descripion = descripion;
		}

		public String getMedia() {
			return media;
		}

		public void setMedia(String media) {
			this.media = media;
		}

		public Long getFeedId() {
			return feedId;
		}

		public void setFeedId(Long feedId) {
			this.feedId = feedId;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}
        
		
        
        


        
        
}
