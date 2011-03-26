package com.aljamaa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

@Entity
public class Momin implements Serializable{
	
	@Id	
	String id;
	
	String name;
	
	String email;
	
	@Basic(fetch = FetchType.EAGER)
	List<String> friendsCalendar;
	
//	@Lob
//	HashMap<String, String> properties;
	
//	@Extension(vendorName = "datanucleus",            key = "gae.unindexed",            value = "true")

	public Momin(){
		friendsCalendar = new ArrayList<String>();
	}
	public Momin(String id2, Integer fACEBOOK) {
		id = id2;
	}
	public String getId() {
		return id;
	}

	public Momin clon()  {
		Momin m = new Momin();
		m.setEmail(email);
		if(this.getFriendsCalendar()!=null)
			m.setFriendsCalendar(new ArrayList<String>(friendsCalendar));
		
		m.setId(id);
		m.setName(name);
	
		return m;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getFriendsCalendar() {
		return friendsCalendar;
	}

	public void setFriendsCalendar(List<String> friendsCalendar) {
		this.friendsCalendar = friendsCalendar;
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
