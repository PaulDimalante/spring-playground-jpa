package com.springJPA.demoJPA;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;


    @Column(columnDefinition = "date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveredOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getDeliveredOn() {
        return deliveredOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson)) return false;
        Lesson lesson = (Lesson) o;
        return getId().equals(lesson.getId()) &&
                getTitle().equals(lesson.getTitle()) &&
                getDeliveredOn().equals(lesson.getDeliveredOn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDeliveredOn());
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public void setDeliveredOn(Date deliveredOn) {
        this.deliveredOn = dateOnly(deliveredOn);
    }

    private Date dateOnly(Date date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateFormat.format(date)); // Output : 01/20/2012
        } catch (Exception e) {
            return null;
        }
    }
}