package naitsoft.web.marocpress.server;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Text;


@Entity
public class ArticleContent implements Serializable{
        
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;        
        
//        private String link;
        
        private Text content;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

//		public String getLink() {
//			return link;
//		}
//
//		public void setLink(String link) {
//			this.link = link;
//		}

		public Text getContent() {
			return content;
		}

		public void setContent(Text content) {
			this.content = content;
		}

        
        
}
