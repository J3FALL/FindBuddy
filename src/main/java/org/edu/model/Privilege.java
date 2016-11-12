package org.edu.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "privileges")
public class Privilege implements Serializable {

    private long id;
    private String name;
    private List<Role> roles = new ArrayList<>();

    public Privilege() {
    }

    public Privilege(String name) {
        this.name = name;
    }

    @Id
    @SequenceGenerator(name = "pk_privileges_sequence", sequenceName = "privileges_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_privileges_sequence")
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "privileges", fetch = FetchType.LAZY)
    public List<Role> getRoles() {
        Hibernate.initialize(roles);
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}