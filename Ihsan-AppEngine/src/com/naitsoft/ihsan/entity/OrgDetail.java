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
public class OrgDetail implements Serializable{
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	String email;
	Date dateNaissance;
	String adress;
	String telephone;
	String fax;
	String codePostal;
	String webSite;
	
	GeoPt position;
	int radius;
	
	Text bio;
	
	String ville;


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
