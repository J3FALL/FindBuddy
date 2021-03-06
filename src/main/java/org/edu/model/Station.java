package org.edu.model;

import org.hibernate.Hibernate;


import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by Ленизка on 13.11.2016.
 */
@Entity
@Table(name = "stations")
public class Station implements Serializable {
    private long id;
    private String name;
    private String color;
    private Set<Meeting> meetings = new HashSet<>();

    public Station() {

    }

    public Station(String name, String color) {
        this.name = name;
        this.color = color;
    }

    @Id
    @SequenceGenerator(name = "pk_stations_sequence", sequenceName = "stations_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_stations_sequence")
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "station_name", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "station_color", nullable = false)
    public String getColor() {
        return color;
    }


    public void setColor(String color) {
        this.color = color;
    }


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Meeting> getMeetings() {
        Hibernate.initialize(meetings);
        return meetings;
        }

    public void setMeetings(Set<Meeting> meetings) {this.meetings = meetings; }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

}
