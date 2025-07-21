package ru.msu.cmc.webprack.models;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Services")
public class Services implements CommonEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = true, name = "service_id")
    private Long id;

    @Column(nullable = true, name = "name")
    private String name;

    @Column(nullable = true, name = "description")
    private String description;

    @Column(nullable = true, name = "price")
    private int price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Services other = (Services) o;
        return Objects.equals(id, other.id)
                && name.equals(other.name)
                && description.equals(other.description)
                && Objects.equals(price, other.price);
    }
}

