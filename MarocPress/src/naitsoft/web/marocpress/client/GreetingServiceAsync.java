package naitsoft.web.marocpress.client;

import java.util.List;

import naitsoft.web.marocpress.server.entity.Article;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void getArticles(AsyncCallback<List<Article>> callback);
}
