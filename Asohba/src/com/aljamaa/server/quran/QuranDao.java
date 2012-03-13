package com.aljamaa.server.quran;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import com.aljamaa.dao.EMF1;
import com.aljamaa.entity.quran.Mask;


public class QuranDao {

	EntityManager em;
	public QuranDao() {
		em=EMF1.get().createEntityManager();
	}
	
	public List<Mask> getPageMasks(String momin, int page)
	{
		Query query;
		query = em.createQuery("select from "+Mask.class.getName()+" t where t.mominId = :momin AND  page =:page order by date asc");	   
		query.setParameter("page",page);			
		query.setParameter("momin",momin);
		List<Mask> list= new ArrayList<Mask>();
		for(Mask msk : (List<Mask>)query.getResultList()){
			msk.decode();
			list.add(msk);
		}
//	    list =new  ArrayList();
	    return list;
	}
	
	public void save(Mask msk){
		em.getTransaction().begin();
		if(msk.getId()==null)
			em.persist(msk);
		else
			em.merge(msk);
		em.getTransaction().commit();
	}
}
