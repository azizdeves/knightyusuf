package naitsoft.web.marocpress.server.entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * Stores an RSS feed
 */
@Entity
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    
    private String link;
	private String name;
	private String rssLink ;
	private String xslt;
	private String mediaXPath;
	private Date lastArticle;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRssLink() {
		return rssLink;
	}
	public void setRssLink(String rssLink) {
		this.rssLink = rssLink;
	}
	public String getXslt() {
		return xslt;
	}
	public void setXslt(String xslt) {
		this.xslt = xslt;
	}
	public String getMediaXPath() {
		return mediaXPath;
	}
	public void setMediaXPath(String mediaXPath) {
		this.mediaXPath = mediaXPath;
	}
	public Date getLastArticle() {
		return lastArticle;
	}
	public void setLastArticle(Date lastArticle) {
		this.lastArticle = lastArticle;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	
}
