package org.edu.model;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "meetings")
public class Meeting implements Serializable {

    private long id;
    private String title;
    private String description;
    private LocalDateTime createDate;
    private LocalDateTime startDate;
    private long longitude;
    private long latitude;
    private User author;
    private Set<User> subscribedUsers = new HashSet<>();
    private Station station;

    //private List<Comment> comments = new ArrayList<>();
    //private List<Category> categories = new ArrayList<>();


    public Meeting() {

    }

    public Meeting(String title, String description, Station station) {
        this.title = title;
        this.description = description;
        this.createDate = LocalDateTime.now();
        this.station = station;
    }

    @Id
    @SequenceGenerator(name = "pk_meetings_sequence", sequenceName = "meeting_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_meetings_sequence")
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "create_date", nullable = false)
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Column(name = "start_date", nullable = false)
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    @Column(name = "longitude")
    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    @Column(name = "latitude")
    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }


    /*public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }*/

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "station")
    public Station getStation() {
        return station;
    }


    public void setStation(Station station) {
        this.station = station;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "subscribedMeetings")
    public Set<User> getSubscribedUsers() {
        return subscribedUsers;
    }

    public void setSubscribedUsers(Set<User> subscribedUsers) {
        this.subscribedUsers = subscribedUsers;
    }



    /*@ManyToMany
    @JoinTable(name = "category_meetings",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {this.categories = categories; }*/

    @Override
    public String toString() {
        return "Meeting {" +
                " id = " + id +
                ", title = " + title +
                ", description = " + description +
                ", create_date = " + createDate +
                ", start_date = " + startDate +
                ", longitude = " + longitude +
                ", latitude = " + latitude +
//                ", author = " + author +
                "}";


    }
}
