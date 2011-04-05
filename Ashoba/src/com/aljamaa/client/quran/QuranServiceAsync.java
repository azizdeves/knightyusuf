package com.aljamaa.client.quran;

import java.util.List;

import com.aljamaa.entity.quran.Mask;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface QuranServiceAsync {


	void getPageMasks(int page,
			AsyncCallback<List<Mask>> callback);

	void saveMask(Mask msk, AsyncCallback<String> callback);
}
