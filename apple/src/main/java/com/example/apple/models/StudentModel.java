package com.example.apple.models;
import jakarta.persistence.*;

@Entity
@Table(name = "students")

public class StudentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "surname")
    private String surname;

    @Column(name = "name")
    private String name;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    public long getId() {
        return this.id;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getName() {
        return this.name;
    }

    public String getFatherName() {
        return this.fatherName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setFatherName(final String fatherName) {
        this.fatherName = fatherName;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public StudentModel() {
    }

    public StudentModel(final long id, final String surname, final String name, final String fatherName, final String email, final String phone) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.fatherName = fatherName;
        this.email = email;
        this.phone = phone;
    }
}
