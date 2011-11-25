package com.naitsoft.ihsan.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Text;

@Entity
public class VolunteerDetail implements Serializable{
	
	@Id	
	String id;
	
	Date dateNaissance;
	String adress;
	String telephone;
	GeoPt position;
	int radius;
	String skills;
	Text bio;
	int rating;
	Boolean sex;
	String ville;
	
//	@Basic(fetch = FetchType.EAGER)
//	List<String> friendsCalendar;
	
//	@Lob
//	HashMap<String, String> properties;
	
//	@Extension(vendorName = "datanucleus",            key = "gae.unindexed",            value = "true")

	public VolunteerDetail(){
	}
	public VolunteerDetail(String id2, Integer fACEBOOK) {
		id = id2;
	}
	public String getId() {
		return id;
	}

//	public Momin clon()  {
//		Momin m = new Momin();
//		m.setEmail(email);
//		if(this.getFriendsCalendar()!=null)
//			m.setFriendsCalendar(new ArrayList<String>(friendsCalendar));
//		
//		m.setId(id);
//		m.setName(name);
//	
//		return m;
//	}

	public void setId(String id) {
		this.id = id;
	}


//
//	public HashMap<String, String> getProperties() {
//		return properties;
//	}
//
//	public void setProperties(HashMap<String, String> properties) {
//		this.properties = properties;
//	}
	
	
	
	

}
