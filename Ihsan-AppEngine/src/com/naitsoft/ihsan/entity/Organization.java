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
public class Organization implements Serializable{
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	String name;
	String resume;
	String interest;
	int rating;
	@Basic(fetch = FetchType.LAZY)
	OrgDetail detail;

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

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public OrgDetail getDetail() {
		return detail;
	}

	public void setDetail(OrgDetail detail) {
		this.detail = detail;
	}

	
	

}
