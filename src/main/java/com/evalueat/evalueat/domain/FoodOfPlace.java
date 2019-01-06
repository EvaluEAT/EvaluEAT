package com.evalueat.evalueat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A FoodOfPlace.
 */
@Entity
@Table(name = "food_of_place")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FoodOfPlace implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Food food;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Place place;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public FoodOfPlace food(Food food) {
        this.food = food;
        return this;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Place getPlace() {
        return place;
    }

    public FoodOfPlace place(Place place) {
        this.place = place;
        return this;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FoodOfPlace foodOfPlace = (FoodOfPlace) o;
        if (foodOfPlace.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), foodOfPlace.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FoodOfPlace{" +
            "id=" + getId() +
            "}";
    }
}
