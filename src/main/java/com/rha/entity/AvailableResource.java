/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rha.entity;

import com.rha.control.LocalDateConverter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "AVAILABLERESOURCE")
@NamedQueries({
    @NamedQuery(name = AvailableResource.availabiltyInPeriod, query
            = "SELECT ar FROM AvailableResource ar "
            + "WHERE ar.startDate>=:startDate AND ar.endDate<=:endDate order by ar.startDate"),
    @NamedQuery(name = AvailableResource.totalAvailabiltyInPeriod, query
            = "SELECT new com.rha.entity.PeriodTotal(ar.startDate, ar.endDate, sum(ar.available))"
            + "FROM AvailableResource ar "
            + "WHERE "
            + "ar.startDate>=:startDate AND ar.endDate<=:endDate "
            + "group by ar.startDate, ar.endDate order by ar.startDate"),})

public class AvailableResource implements Serializable, PeriodWithValue {

    private static final String prefix = "com.rha.entity.AvailableResource.";
    public static final String availabiltyInPeriod = prefix + "availabiltyInPeriod";
    public static final String totalAvailabiltyInPeriod = prefix + "totalAvailabiltyInPeriod";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @ManyToOne
    Service service;

    @Temporal(TemporalType.DATE)
    Date startDate;

    @Temporal(TemporalType.DATE)
    Date endDate;

    Long available = 0L;

    @Transient
    private boolean persisted = true;
    
    @Transient
    private Integer position;

    public boolean isPersisted() {
        return persisted;
    }

    public void setPersisted(boolean persisted) {
        this.persisted = persisted;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public LocalDate getStartDate() {
        if (startDate != null) {
            return LocalDateConverter.toLocalDate(startDate);
        } else {
            return null;
        }
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = LocalDateConverter.toDate(startDate);
    }

    public LocalDate getEndDate() {
        if (endDate != null) {
            return LocalDateConverter.toLocalDate(endDate);
        } else {
            return null;
        }
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = LocalDateConverter.toDate(endDate);
    }

    public Long getAvailable() {
        return available;
    }

    public void setAvailable(Long available) {
        this.available = available;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AvailableResource) {
            final AvailableResource other = (AvailableResource) obj;
            return new EqualsBuilder()
                    .append(id, other.getId())
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public Integer getPosition() {
        return position;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void setValue(Long o) {
        setAvailable(o);
    }

    @Override
    public void setPeriod(LocalDate[] period) {
        setStartDate(period[0]);
        setEndDate(period[1]);
    }

    @Override
    public Long getValue() {
        return available;
    }

}
