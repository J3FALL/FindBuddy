package org.edu.model;

import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by Ленизка on 13.11.2016.
 */
@Entity
@Table(name = "categories")
public class Category implements Serializable {

    private long id;
    private String name;
    private String color;
    private List<User> users = new ArrayList<>();

    public Category() {

    }

    public Category(String name, String color) {

        this.name = name;
        this.color = color;
    }

    @Id
    @SequenceGenerator(name = "pk_categories_sequence", sequenceName = "categories_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_categories_sequence")
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "category_name", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "category_color", unique = true, nullable = false)
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @ManyToMany
    @JoinTable(name = "category_users",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) { this.users = users; }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
