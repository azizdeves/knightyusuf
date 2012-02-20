package naitsoft.web.marocpress.server;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import naitsoft.web.marocpress.server.entity.ArticleContentDto;

import com.google.appengine.api.datastore.Text;
import com.google.gwt.user.client.rpc.GwtTransient;


@Entity
public class ArticleContent implements Serializable{
        
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;   
        
//        @Transient
//        private String sContent;
        
        @GwtTransient
        private Text content;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public ArticleContentDto getDtoContent() {
			ArticleContentDto dto = new ArticleContentDto();
			dto.setContent(content.getValue());
			dto.setId(id);
			return dto;
		}
//
//		public void setsContent(String sContent) {
//			this.sContent = sContent;
//		}

		public Text getContent() {
			return content;
		}

		public void setContent(Text content) {
			this.content = content;
		}

        
        
}
