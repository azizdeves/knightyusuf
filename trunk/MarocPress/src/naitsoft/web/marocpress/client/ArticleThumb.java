package naitsoft.web.marocpress.client;


import naitsoft.web.marocpress.server.entity.Article;
import naitsoft.web.marocpress.server.entity.ArticleContentDto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ArticleThumb extends Composite{

	Article article;
	private final GreetingServiceAsync service = GWT
								.create(GreetingService.class);
	private HTMLPanel panel;
//http://ugsmag.com  http://ugsmag.com	http://ugsmag.com
//	public ArticleThumb(Article art) {
//		this();
//		article = art;
//		initContent();
//	}
	public ArticleThumb(Article art) {
		article = art;
		String htmlContent = "<div class='subfeature'>+" +
				"<img id='img"+article.getId()+"' src='' alt='' class='post-image' width='310' height='250'>				<div class='overlay'><a id='overlay"+article.getId()+"' href='#' title=''><img src='http://ugsmag.com/wp-content/themes/ugsmag2010/images/overlay-h250.png' width='310' height='250' alt='' class='bigpng'></a></div>" +
				"<div class='comcat'>" +
				"<div class='category'><a href='' id='feed"+article.getId()+"' title='' rel='feed'></a></div>" +
//				"<div class='commentsnum'><a href='http://ugsmag.com/2011/07/eric-steuer/#respond' title='Comment on Eric Steuer'>Comment</a></div>" +
				"<div class='date'></div>" +
				"</div>" +
				"<div class='subfeature-txt'><h2 id='title"+article.getId()+"'><a id='h2href"+article.getId()+"' href='#' rel='bookmark' ></a></h2>" +
				"<p id='desc"+article.getId()+"'></p>" +
				"</div>" +
				"</div>";
		panel = new HTMLPanel(htmlContent);
		initContent();
		initWidget(panel);
		panel.setSize("310px", "250px");
		panel.sinkEvents(Event.ONCLICK);
		
	}
	@Override
	public void onBrowserEvent(Event event) {
		// TODO Auto-generated method stub
		super.onBrowserEvent(event);
		
		loadArticleContent();
	}
	private void initContent() {
//		article = new  Article();
//		article.setTitle("Tiiiiitle");
//		article.setDescripion("description sdlfk sldfj lskdfj ");
//		article.setMedia("http://s1.hespress.com/cache/thumbnail/article_medium/amdhnewcons_865104348.jpg");
		Element img = panel.getElementById("img"+article.getId());
		Element desc = panel.getElementById("desc"+article.getId());
		Element title = panel.getElementById("h2href"+article.getId());
		Element overlay = panel.getElementById("overlay"+article.getId());
		Element feed = panel.getElementById("feed"+article.getId());
		feed.setAttribute("href", article.getFeedId().toString());
		feed.setInnerText("hespress");
		overlay.setAttribute("title", article.getTitle());
//		overlay.setAttribute("href", article.getLink());
		title.setInnerText(article.getTitle());
//		title.setAttribute("href", article.getLink());
//		desc.setInnerText(article.getDescripion());
		img.setAttribute("src", article.getMedia());
				
	}
	
	private void loadArticleContent()
	{
		service.getArticleContent(article.getContentId(), new AsyncCallback<ArticleContentDto>() {
			
			@Override
			public void onSuccess(ArticleContentDto result) {
				// TODO Auto-generated method stub
				ArticleView.view.setContent(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
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
