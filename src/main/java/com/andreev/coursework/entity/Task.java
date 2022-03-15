package com.andreev.coursework.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "date_ending")
    private Date dateFinish;

    @Column(name = "data")
    private byte[] pdf;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "creator_id")
    private Participant creator;

    @ManyToMany(cascade = CascadeType.ALL,
        fetch = FetchType.LAZY)
    @JoinTable(
        name = "student_task"
        , joinColumns = @JoinColumn(name = "task_id")
        , inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Participant> executors;

    public Task() {
    }

    public Task(String description, Date dateFinish, byte[] pdf, Participant creator) {
        this.description = description;
        this.dateFinish = dateFinish;
        this.pdf = pdf;
        this.creator = creator;
    }

    public void addExecutorToList(Participant participant) {
        if (executors == null) {
            executors = new HashSet<>();
        }
        executors.add(participant);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(Date dateFinish) {
        this.dateFinish = dateFinish;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public Participant getCreator() {
        return creator;
    }

    public void setCreator(Participant creator) {
        this.creator = creator;
    }

    public Set<Participant> getExecutors() {
        return executors;
    }

    public void setExecutors(Set<Participant> executors) {
        this.executors = executors;
    }
}
