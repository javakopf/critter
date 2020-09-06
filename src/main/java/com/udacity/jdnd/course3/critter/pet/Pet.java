package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Pet {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private PetType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer owner;

    private String name;
    private LocalDate birthDate;
    private String notes;


    public Pet() {
    }

    public Pet(Long id, PetType type, Customer owner, String name, LocalDate birthDate, String notes) {
        this.id = id;
        this.type = type;
        this.owner = owner;
        this.name = name;
        this.birthDate = birthDate;
        this.notes = notes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public Customer getOwner() {
        return owner;
    }

    public void setOwner(Customer owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


}
