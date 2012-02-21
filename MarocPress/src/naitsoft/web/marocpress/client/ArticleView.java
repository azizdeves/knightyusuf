package naitsoft.web.marocpress.client;


import naitsoft.web.marocpress.server.entity.Article;
import naitsoft.web.marocpress.server.entity.ArticleContentDto;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ArticleView extends Composite{

	ArticleContentDto article;
	public static ArticleView view;
	
	private HTMLPanel panel;
	private Element div;
//http://ugsmag.com  http://ugsmag.com	http://ugsmag.com
//	public ArticleThumb(Article art) {
//		this();
//		article = art;
//		initContent();
//	}
	public ArticleView() {
		panel = new HTMLPanel("<div id='content' class='article_body'></div>");
		div = panel.getElementById("content");
		initWidget(panel);
		panel.setSize("100%", "100%");
		setSize("100%", "100%");
		view = this;
	}
	
	public ArticleView(ArticleContentDto art) {
		setContent(art);
		div = panel.getElementById("content");
//		initContent();
		initWidget(panel);
		panel.setSize("310px", "250px");
		view = this;
	}

	public void setContent(ArticleContentDto art)
	{
		article = art;
		div.setInnerHTML(art.getContent());
//		panel = new HTMLPanel(art.getContent());

	}
	

}

//"<div class='subfeature'>+" +
//"<img id='img' src='http://ugsmag.com/wp-content/uploads/ericsteuer-310x250.jpg' alt='eric-steuer' class='post-image' width='310' height='250'>				<div class='overlay'><a href='http://ugsmag.com/2011/07/eric-steuer/' title='Eric Steuer'><img src='http://ugsmag.com/wp-content/themes/ugsmag2010/images/overlay-h250.png' width='310' height='250' alt='Eric Steuer' class='bigpng'></a></div>" +
//"<div class='comcat'>" +
//"<div class='category'><a href='http://ugsmag.com/category/features/interviews/' title='View all posts in Interview' rel='category tag'>Interview</a></div>" +
//"<div class='commentsnum'><a href='http://ugsmag.com/2011/07/eric-steuer/#respond' title='Comment on Eric Steuer'>Comment</a></div>" +
//"<div class='date'></div>" +
//"</div>" +
//"<div class='subfeature-txt'><h2><a id='h2href' href='http://ugsmag.com/2011/07/eric-steuer/' rel='bookmark' title='Eric Steuer'>Eric Steuer</a></h2>" +
//"<p id='desc'>Eric Steuer (Meanest Man Contest) fills us in on his new project Not the 1s (with Mawnster) and speaks on the re-release of a split EP from his old group mic.edu, in commemoration of the 10 year ann. of the death of member Lafura “A-Twice” Jackson.</p>" +
//"</div>" +
//"</div>";
