package com.naitsoft.ihsan.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Text;

@Entity
public class ProjectDetail implements Serializable{
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Basic(fetch=FetchType.LAZY)
	Organization org;
	String email;
	Date dateNaissance;
	String adress;
	String telephone;
	String fax;
	String codePostal;
	String webSite;	
	GeoPt position;
	int radius;	
	Text description;
	Date start;
	Date end;
	String ville;

	public ProjectDetail(){
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
	
	

}
