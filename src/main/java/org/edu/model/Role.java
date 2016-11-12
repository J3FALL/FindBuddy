package org.edu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role implements Serializable{

    private long id;
    private String name;
    private List<User> users = new ArrayList<>();
    private List<Privilege> privileges = new ArrayList<>();

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    @Id
    @SequenceGenerator(name = "pk_roles_sequence", sequenceName = "roles_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_roles_sequence")
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

//    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    public List<Privilege> getPrivileges() {
        Hibernate.initialize(privileges);
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    public List<User> getUsers() {
        Hibernate.initialize(users);
        return users;
    }


    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
