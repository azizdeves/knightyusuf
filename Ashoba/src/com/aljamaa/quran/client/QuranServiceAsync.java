package com.aljamaa.quran.client;

import java.util.List;

import com.aljamaa.entity.quran.Mask;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QuranServiceAsync {

	void getPageMasks(int page, AsyncCallback<List<Mask>> callback);

	void saveMask(Mask msk, AsyncCallback<String> callback);

}
