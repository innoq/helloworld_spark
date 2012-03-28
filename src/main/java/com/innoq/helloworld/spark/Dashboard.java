package com.innoq.helloworld.spark;

import com.innoq.helloworld.models.Profile;
import com.innoq.helloworld.models.Status;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class Dashboard {
    private EntityManager em;
    private Profile profile;
    private List<Status> statuses;
    private List<Profile> unacceptedContacts;
    private List<Profile> contactsOfContacts;

    public Dashboard(EntityManager entityManager) {
        this.em = entityManager;
        
    }
    
    public void load() {
        try {
            TypedQuery<Profile> query = em.createQuery("SELECT p FROM Profile p", Profile.class);
            query.setMaxResults(1);
            profile = query.getResultList().get(0);
            statuses = profile.getStatuses();


            

        } finally {
            // Close the PersistenceManager:
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            em.close();
        }
    }

    public Profile getProfile() {
        return profile;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public List<Profile> getUnacceptedContacts() {
        return unacceptedContacts;
    }

    public List<Profile> getContactsOfContacts() {
        return contactsOfContacts;
    }
}
