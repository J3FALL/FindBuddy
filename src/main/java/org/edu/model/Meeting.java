package org.edu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Pavel on 13.11.2016.
 */
@Entity
@Table(name = "meetings")
public class Meeting implements Serializable {

    private long id;
    private String title;
    private String description;
    private Calendar startDate;
    private Date createDate;
    private long longitude;
    private long latitude;
    private User author;
<<<<<<< HEAD
    private Station station;
    //private List<Comment> comments = new ArrayList<>();
=======
    private Station station_id;
    private List<Comment> comments = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
>>>>>>> origin/develop

    public Meeting() {

    }

    public Meeting(String title, String description, Calendar startDate, Station station) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        createDate = Calendar.getInstance().getTime();//save creation date
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

    @Column(name = "start_date", nullable = false)
    @JsonProperty("start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:ss")
    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    @Column(name = "create_date", nullable = false)
    @JsonProperty("create_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:ss")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    @ManyToOne(fetch = FetchType.EAGER)
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
    public Station getStation() {return station;}

<<<<<<< HEAD
    public void setStation(Station station) {this.station = station;}
=======
    public void setStation(Station station_id) {this.station_id = station_id;}

    @ManyToMany
    @JoinTable(name = "category_meetings",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {this.categories = categories; }

>>>>>>> origin/develop
}
