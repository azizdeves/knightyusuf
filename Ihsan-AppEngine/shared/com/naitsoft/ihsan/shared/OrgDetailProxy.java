package com.naitsoft.ihsan.shared;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName(value = "com.naitsoft.ihsan.entity.OrgDetail", locator = "com.naitsoft.ihsan.entity.OrgDetailLocator")
public interface OrgDetailProxy extends ValueProxy {

	String getEmail();

	void setEmail(String email);

	Long getId();

	Date getDateNaissance();

	void setDateNaissance(Date dateNaissance);

	String getAdress();

	void setAdress(String adress);

	String getTelephone();

	void setTelephone(String telephone);

	String getFax();

	void setFax(String fax);

	String getCodePostal();

	void setCodePostal(String codePostal);

	String getWebSite();

	void setWebSite(String webSite);

	int getRadius();

	void setRadius(int radius);

	String getBio();

	void setBio(String bio);

	String getVille();

	void setVille(String ville);

	long getLongitude();

	void setLongitude(long longitude);

	long getLatitude();

	void setLatitude(long latitude);

}
