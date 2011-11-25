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

@Entity
public class Project implements Serializable{
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	String name;
	String org;
	String resume;
	String skills;
	int rating;
	@Basic(fetch = FetchType.LAZY)
	ProjectDetail detail;


	public Project(){
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrg() {
		return org;
	}



	public void setOrg(String org) {
		this.org = org;
	}



	public String getResume() {
		return resume;
	}



	public void setResume(String resume) {
		this.resume = resume;
	}



	public String getSkills() {
		return skills;
	}



	public void setSkills(String skills) {
		this.skills = skills;
	}



	public int getRating() {
		return rating;
	}



	public void setRating(int rating) {
		this.rating = rating;
	}



	public ProjectDetail getDetail() {
		return detail;
	}



	public void setDetail(ProjectDetail detail) {
		this.detail = detail;
	}


	
	
	

}
