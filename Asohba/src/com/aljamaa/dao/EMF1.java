package com.aljamaa.dao;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public  class EMF1 {
    private static  EntityManagerFactory emfInstance ;


    public EMF1(){

    	if(emfInstance==null)
    		emfInstance=Persistence.createEntityManagerFactory("transactions-optional");
    }
    public static EntityManagerFactory get() {
    	
    	if(emfInstance==null)
    		emfInstance=Persistence.createEntityManagerFactory("transactions-optional");
        return emfInstance;
    }
}