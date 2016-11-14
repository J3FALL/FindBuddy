package org.edu.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.codehaus.jackson.annotate.JsonBackReference;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "comments")
public class Comment implements Serializable {

    private long id;
    private Calendar date;
    private String text;
    private User author;

    public Comment() {
    }

    public Comment(long id, Calendar date, String text) {
        this.id = id;
        this.date = date;
        this.text = text;
    }

    public Comment(Calendar date, String text) {
        this.date = date;
        this.text = text;
    }
    @Id
    @SequenceGenerator(name = "pk_comments_sequence", sequenceName = "comments_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_comments_sequence")
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:ss")
    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @Column
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", author=" + author +
                '}';
    }
}