package com.aljamaa.server.quran;

import java.util.List;

import com.aljamaa.client.quran.QuranService;
import com.aljamaa.entity.quran.Mask;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class QuranServiceImpl extends RemoteServiceServlet implements
		QuranService {

	@Override
	public List<Mask> getPageMasks(int page) {
		QuranDao dao = new QuranDao();
		return dao.getPageMasks("momin", page);
	}

	@Override
	public void saveMask(Mask msk) {
		
	}



	
	
}
