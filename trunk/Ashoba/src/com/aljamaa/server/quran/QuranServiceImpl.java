package com.aljamaa.server.quran;

import java.util.List;

import com.aljamaa.client.quran.QuranService;
import com.aljamaa.dao.TaskDao;
import com.aljamaa.entity.quran.Mask;
import com.aljamaa.shared.TaskException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class QuranServiceImpl extends RemoteServiceServlet implements
		QuranService {

	@Override
	public List<Mask> getPageMasks(int page) throws TaskException {
		QuranDao dao = new QuranDao();
		return dao.getPageMasks(TaskDao.getCurrentMomin().getId(), page);
	}

	@Override
	public String saveMask(Mask msk)  throws TaskException{
		QuranDao dao = new QuranDao();
		msk.encode();
		dao.save(msk);
		return "";
	}



	
	
}
