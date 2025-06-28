package ru.msu.cmc.webprack.models;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class Users implements CommonEntity<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "user_id")
    private Long id;

    @Column(nullable = true, name = "username")
    @NonNull
    private String name;

    @Column(nullable = false, name = "password_hash")
    private String pass;

    @Column(nullable = true, name = "role")
    private String role;

    /*@OneToOne(mappedBy = "user")
    private Employees employee;

    @OneToOne(mappedBy = "user")
    private Clients client;*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; //aka link of two objects are the same
        if (o == null || getClass() != o.getClass()) return false; //aka links are not the same

        Users other = (Users) o;
        return Objects.equals(id, other.id)
                && name.equals(other.name)
                && pass.equals(other.pass)
                && Objects.equals(role, other.role);
    }
}

