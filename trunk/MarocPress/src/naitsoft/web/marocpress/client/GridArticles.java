package naitsoft.web.marocpress.client;


import java.util.List;

import naitsoft.web.marocpress.server.entity.Article;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GridArticles extends Composite{

//	Article article;
	
	private Grid grid;
	private final GreetingServiceAsync service = GWT
											.create(GreetingService.class);
	
	public GridArticles() {
		grid = new Grid(10, 1);
		initWidget(grid);
		grid.setSize("100%", "43px");
		
	}

	public void initContent() {
		service.getArticles(new AsyncCallback<List<Article>>() {
			
			@Override
			public void onSuccess(List<Article> result) {
				initWithArticles(result);
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initWithArticles(List<Article> articles) {
		ArticleThumb thumb;
		int row,column;row=column=0;
		for(Article art : articles){
			thumb = new ArticleThumb(art);
			grid.setWidget(row, column++, thumb);
			if(column>0)
			{
				row++;
				column = 0;
			}
		}
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
