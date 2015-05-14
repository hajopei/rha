/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rha.boundary;

import com.rha.control.LocalDateConverter;
import com.rha.entity.BookedResource;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alacambra
 */
@Stateless
public class BookedResourceFacade extends AbstractFacade<BookedResource> {

    @PersistenceContext(unitName = "rha")
    EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<BookedResource> getBookedResourcesFor(int projectId, int divisionId) {

        List<BookedResource> bookedResources
                = em.createNamedQuery(BookedResource.byProjectAndDivision, BookedResource.class)
                .setParameter("pid", projectId)
                .setParameter("did", divisionId)
                .getResultList();

        return bookedResources;
    }

    public List<BookedResource> getBookedResourcesForDivision(int divisionId) {

        List<BookedResource> bookedResources
                = em.createNamedQuery(BookedResource.byDivision)
                .setParameter("did", divisionId)
                .getResultList();

        return bookedResources;
    }

    public List<BookedResource> getBookedResourcesForDivision(
            int divisionId, LocalDate startDate, LocalDate endDate) {

        List<BookedResource> bookedResources
                = em.createNamedQuery(BookedResource.byDivisionForPeriod)
                .setParameter("did", divisionId)
                .setParameter("startDate", LocalDateConverter.toDate(startDate))
                .setParameter("endDate", LocalDateConverter.toDate(endDate))
                .getResultList();

        return bookedResources;
    }

    public List<Integer> getTotalBookedResourcesPerProjectForDivision(int divisionId) {

        List<Integer> bookedResources
                = em.createNamedQuery(BookedResource.totalByDivision)
                .setParameter("did", divisionId)
                .getResultList();

        return bookedResources;
    }

    public List<Integer> getTotalBookedResourcesPerProjectForDivision(
            int divisionId, LocalDate startDate, LocalDate endDate) {

        List<Integer> bookedResources
                = em.createNamedQuery(BookedResource.totalByDivision)
                .setParameter("did", divisionId)
                .getResultList();

        return bookedResources;
    }

    public BookedResourceFacade() {
        super(BookedResource.class);
    }

//    public List<StepPeriod> getPeriods() {
//        return new CalendarGenerator(
//                LocalDate.now(),
//                LocalDate.now().plusYears(1), Step.MONTH)
//                .getEntries();
//    }
    public void updateOrCreateBookings(Collection<BookedResource> resources) {
        resources.stream().filter(r -> r.getId() != null || r.getBooked() != 0)
                .forEach(r -> {
                    em.merge(r);
                });
    }
}
