package naitsoft.web.marocpress.client;

import java.util.List;

import naitsoft.web.marocpress.server.entity.Article;
import naitsoft.web.marocpress.server.entity.ArticleContentDto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	List<Article> getArticles();
	ArticleContentDto getArticleContent(long contentId);
}
