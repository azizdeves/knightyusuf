package naitsoft.web.marocpress.server;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.google.gwt.user.client.rpc.GwtTransient;

@Entity
public class Article implements Serializable{
        
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;        
        
        private String link;
        
        private String content;

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

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
        
        
        


        
        
}
