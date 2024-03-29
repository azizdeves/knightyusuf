package com.aljamaa.quran.client;

import java.util.List;

import com.aljamaa.entity.quran.Mask;
import com.aljamaa.shared.TaskException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("quran")
public interface QuranService extends RemoteService {
	public List<Mask> getPageMasks(int page) throws TaskException;
	public String saveMask(Mask msk) throws TaskException;
	
}
