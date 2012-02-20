package naitsoft.web.marocpress.server.entity;

import java.io.Serializable;



public class ArticleContentDto implements Serializable{
        
        private Long id;   
        
//        @Transient
//        private String sContent;
        
//        @GwtTransient
        private String content;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
		

//		public String getsContent() {
//			if(sContent == null)
//				sContent = content.getValue();
//			return sContent;
//		}
//
//		public void setsContent(String sContent) {
//			this.sContent = sContent;
//		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

        
        
}
