package naitsoft.web.marocpress.server.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Text;
import com.google.gwt.thirdparty.guava.common.annotations.GwtIncompatible;
import com.google.gwt.user.client.rpc.GwtTransient;
import com.sun.syndication.feed.synd.SyndEntry;

@Entity
public class AudioFile implements Serializable{
        
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;        
                
        private String word;
        
        private int hash;
        
        private Long seek;	
        
        
        private Blob data;
        public  Text text;

		
		public AudioFile() {
			// TODO Auto-generated constructor stub
		}


		public Blob getData() {
			return data;
		}


		public void setData(Blob data) {
			this.data = data;
		}
		public void setData(byte[] data) {
			this.data = new Blob(data);
		}


		public Long getSeek() {
			return seek;
		}


		public void setSeek(Long contentId) {
			this.seek = contentId;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getWord() {
			return word;
		}

		public void setWord(String media) {
			this.word = media;
			hash = word.hashCode();
		}

		public int getHash() {
			return hash;
		}

		public void setHash(int feedId) {
			this.hash = feedId;
		}


        
		
        
        


        
        
}
