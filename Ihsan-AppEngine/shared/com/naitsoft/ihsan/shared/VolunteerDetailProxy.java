package com.naitsoft.ihsan.shared;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName(value = "com.naitsoft.ihsan.entity.VolunteerDetail", locator = "com.naitsoft.ihsan.entity.VolunteerDetailLocator")
public interface VolunteerDetailProxy extends ValueProxy {

	Long getId();

	Date getDateNaissance();

	void setDateNaissance(Date dateNaissance);

	String getAdress();

	void setAdress(String adress);

	String getTelephone();

	void setTelephone(String telephone);

	int getRadius();

	void setRadius(int radius);

	String getSkills();

	void setSkills(String skills);

	String getBio();

	void setBio(String bio);

	int getRating();

	void setRating(int rating);

	Boolean getSex();

	void setSex(Boolean sex);

	String getVille();

	void setVille(String ville);

	long getLongitude();

	void setLongitude(long longitude);

	long getLatitude();

	void setLatitude(long latitude);

}
