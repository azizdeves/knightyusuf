package com.naitsoft.ihsan.server;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.datanucleus.store.appengine.jpa.DatastoreEntityManager;
import org.datanucleus.store.appengine.jpa.DatastoreEntityManagerFactory;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.prospectivesearch.FieldType;
import com.google.appengine.api.prospectivesearch.ProspectiveSearchService;
import com.google.appengine.api.prospectivesearch.ProspectiveSearchServiceFactory;
import com.naitsoft.ihsan.entity.OrgDetail;
import com.naitsoft.ihsan.entity.Organization;
import com.naitsoft.ihsan.entity.Project;
import com.naitsoft.ihsan.entity.ProjectDetail;
import com.naitsoft.ihsan.entity.StreamItem;
import com.naitsoft.ihsan.entity.Volunteer;
import com.naitsoft.ihsan.entity.VolunteerDetail;

public class IhsanDao {

//	static Cache cache;
	EntityManager em;
	private static final Logger log = Logger.getLogger(HelloWorldService.class.getName());
	public IhsanDao() {
		em=EMF.get().createEntityManager();
	}
	
	public VolunteerDetail createVolunteerDetail() {
		VolunteerDetail detail = new  VolunteerDetail();
		return em.merge(detail);
	}

	public VolunteerDetail readVolunteerDetail(Long id) {
		return em.find(VolunteerDetail.class, id);
		
	}
	
	public VolunteerDetail updateVolunteerDetail(VolunteerDetail volunteerdetail) {
		 
		volunteerdetail = em.merge(volunteerdetail);
		return volunteerdetail;
	}
	
	public void deleteVolunteerDetail(VolunteerDetail volunteerdetail) {
		
		em.remove(volunteerdetail);
	}
	
	public List<VolunteerDetail> queryVolunteerDetails() {
//		Query query = em.createQuery("select from "+Task.class.getName()+" t where t.mominId = :momin AND  group =:group AND  t.date >= :start  AND  t.date  < :end order by date asc");	   
//		query.setParameter("group",Integer.parseInt(group));
		return null;
	}
	
	public StreamItem createStreamItem() {
		
		return null;
	}
	
	public StreamItem readStreamItem(Long id) {
		
		return null;
	}
	
	public void saveStreamItem(StreamItem streamitem) {
		em.persist(streamitem);
	}
	
	public void deleteStreamItem(StreamItem streamitem) {
		em.remove(streamitem);
		em.flush();
	}
	
	public List<StreamItem> queryStreamItems(long userId) {
		Query query = em.createQuery("select from "+StreamItem.class.getName()+" s where s.userId = :user order by date asc");	   
		query.setParameter("user",userId);
		return query.getResultList();
	}
	
	public Volunteer createVolunteer() {
		
		return null;
	}
	
	public Volunteer readVolunteer(Long id) {
		
		return null;
	}

	public Volunteer updateVolunteer(Volunteer volunteer) {
		
		return null;
	}

	public void deleteVolunteer(Volunteer volunteer) {
		

	}
	
	public List<Volunteer> queryVolunteers() {
		
		return null;
	}
	
	public Project createProject() {
		
		return null;
	}
	
	public Project readProject(Long id) {
		
		return null;
	}
	
	public Project updateProject(Project project1) {
		log.info("wwwwwwwwwwwwwwwww1111");
		ProspectiveSearchService searchService = ProspectiveSearchServiceFactory.getProspectiveSearchService();
		HashMap<String, FieldType> field = new HashMap<String, FieldType>();
		field.put("orgId", FieldType.INT32);
		field.put("skills", FieldType.STRING);
		field.put("projectId", FieldType.INT32);
		long d = (long) Math.pow(2, 63);
		searchService.subscribe("stream", "test1", 0, "_a_f_", field);
		searchService.subscribe("stream", "test2", 0, "a:s  orgId="+d, field);
		Project project = new Project();
		project.setOrgId(5);
		project.setSkills("_b_a_f_");
		em.getTransaction().begin();
		em.persist(project);
		em.getTransaction().commit();
		Entity entity = new Entity("project","proj");
		entity.setProperty("orgId", d);
		entity.setProperty("skills", "_a_f_");
		entity.setProperty("projectId", 3);
		try{
			
//			searchService.match(entity, "stream","resultkey",searchService.DEFAULT_RESULT_RELATIVE_URL, "default", searchService.DEFAULT_RESULT_BATCH_SIZE, true);
			searchService.match(entity, "stream");
		}catch(Exception e){
			e.getMessage();
		}
		
		log.info("wwwwwwwwwwwwwwwww");
		
		return project;
	}
	
	public void deleteProject(Project project) {
		

	}

	public List<Project> queryProjects() {
		
		return null;
	}

	
	public OrgDetail createOrgDetail() {
		
		return null;
	}

	
	public OrgDetail readOrgDetail(Long id) {
		
		return null;
	}

	
	public OrgDetail updateOrgDetail(OrgDetail orgdetail) {
		
		return null;
	}

	
	public void deleteOrgDetail(OrgDetail orgdetail) {
		

	}

	
	public List<OrgDetail> queryOrgDetails() {
		
		return null;
	}

	
	public ProjectDetail createProjectDetail() {
		
		return null;
	}

	
	public ProjectDetail readProjectDetail(Long id) {
		
		return null;
	}

	
	public ProjectDetail updateProjectDetail(ProjectDetail projectdetail) {
		
		return null;
	}

	
	public void deleteProjectDetail(ProjectDetail projectdetail) {
		

	}

	
	public List<ProjectDetail> queryProjectDetails() {
		
		return null;
	}

	
	public Organization createOrganization() {
		
		return null;
	}

	
	public Organization readOrganization(Long id) {
		
		return null;
	}

	
	public Organization updateOrganization(Organization organization) {
		
		return null;
	}

	
	public void deleteOrganization(Organization organization) {
		

	}

	
	public List<Organization> queryOrganizations() {
		
		return null;
	}
	/*@SuppressWarnings("unchecked")
	public List<Task> getWeekTasks(String momin, String group, Date startWeek)
	{
		Query query;
		List<Task> list;
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
//		String keyWeek = momin + startWeek.toGMTString().substring(0, 11);
		if(group == null){
			query = em.createQuery("select from "+Task.class.getName()+" t where t.mominId = :momin AND t.date >= :start  AND  t.date  < :end order by date asc");	   
//			list = (List<Task>) getCache().get(keyWeek);
//			if(list!=null)
//				return list;		
		}else{
			query = em.createQuery("select from "+Task.class.getName()+" t where t.mominId = :momin AND  group =:group AND  t.date >= :start  AND  t.date  < :end order by date asc");	   
			query.setParameter("group",Integer.parseInt(group));			
		}		
	    Date end = CalendarUtil.copyDate(startWeek);
	    CalendarUtil.addDaysToDate(end,7);
	    query.setParameter("momin",momin);
	    query.setParameter("start",startWeek);
//	    end.setTime(end.getTime()-2000);
	    query.setParameter("end",end);	    
	    list =new  ArrayList((List<Task>)query.getResultList());
	    
//	    if(group == null)
//	    	getCache().put(keyWeek, list);

	    return list;
	}
	
	public Task getTaskById(Long  id)
	{
		Key key=KeyFactory.createKey(Task.class.getSimpleName(), id);
		Task kh=em.find(Task.class,key);
		return kh;
	}

	@SuppressWarnings("deprecation")
	public void saveTask(Task task)
	{
//		Date startWeek = Task.moveToDay(CalendarUtil.copyDate(task.getDate()), -1);		
//		String keyWeek = task.getMominId() + startWeek.toGMTString().substring(0, 11);
		boolean update=true;
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		em.getTransaction().begin();
		if(task.getId()!=null){
			em.merge(task);
		}
		else{
			em.persist(task);
			update = false;
		}
		em.flush();
		em.getTransaction().commit();
		if(!update)
			return;
		
//		Queue que= QueueFactory.getQueue("userfeedupdates");
//		que.add(url("/task/tskseed?s=&m="+task.getMominId()+"&w="+task.getDate().getTime()+"&t="+new Date().getTime()+"&n="+ task.getName().replaceAll(" ","+")).method(Method.GET));
//		que.add(TaskOptions.Builder.url("/task/tskseed?s=&m="+task.getMominId()+"&w="+task.getDate().getTime()+"&t="+new Date().getTime()).param("n", task.getName()).method(Method.GET));
		/*que.add(url("/tskseed")
				.param("s","2")
				.param("n",task.getName())
				.param("m",task.getMominId()).
				param("w",task.getDate().getTime()+"")
				.param("t",new Date().getTime()+"")
				);*/
//		getCache().put(keyWeek, null);
		//TaskSeedServlet.stat(task.getName(), task.getMominId(), task.getDate(), new Date().getTime());
//		Key k = KeyFactory.createKey(Task.class.getSimpleName(), task.getId());
//		List<Key> keys = new ArrayList<Key>();keys.add(k);
//		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();	
//		Map<Key, Entity> map = ds.get(keys);
/*	}
	
	public void deleteTask(Task c)
	{
		
		em.getTransaction().begin();
		em.remove(c);
		em.getTransaction().commit();
	}

	public void delete(Object c)
	{
		em.getTransaction().begin();
		c=em.merge(c);
		em.remove(c);
		em.getTransaction().commit();
	}
	public void merge(Object o){
		em.getTransaction().begin();
		em.merge(o);
		em.getTransaction().commit();
		
	}
*/
//	public List<Statistic> fromEntity(Collection<Entity> col){
//		List<Statistic> list = new ArrayList<Statistic>();
//		Statistic st ;
//		for(Entity e : col){
//			st= new Statistic();
//			st.setKey((String) e.getProperty("key"));
//			st.setSerializedData((String) e.getProperty("serializedData"));
//			list.add(st);			st.getSerializedData().length();
//		}
//		return list;
//	}
		
}
