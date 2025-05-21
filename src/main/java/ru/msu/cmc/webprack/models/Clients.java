package ru.msu.cmc.webprack.models;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Clients")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Clients implements CommonEntity<Long> {
    @Id
    private Long id;

    @MapsId
    @OneToOne(optional = true)
    @JoinColumn(name = "client_id", referencedColumnName = "user_id", nullable = true)
    private Users user;

    @Column(nullable = true, name = "username")
    private String name;

    @Column(nullable = true, name = "phone")
    private String phone;

    @Column(nullable = true, name = "email")
    private String email;

    @Column(nullable = true, name = "address")
    private String address;

    @Override
    public Long getId() {
        return user != null ? user.getId() : null;
    }

    @Override
    public void setId(Long id) {
        if (user == null) {
            user = new Users();
        }
        user.setId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; //aka link of two objects are the same
        if (o == null || getClass() != o.getClass()) return false; //aka links are not the same

        Clients other = (Clients) o;
        return Objects.equals(user, other.user)
                && name.equals(other.name)
                && phone.equals(other.phone)
                && Objects.equals(email, other.email)
                && Objects.equals(address, other.address);
    }
}

