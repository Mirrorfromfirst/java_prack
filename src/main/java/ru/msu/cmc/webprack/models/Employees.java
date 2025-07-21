package ru.msu.cmc.webprack.models;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Employees")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Employees implements CommonEntity<Long>{
    /*@Id
    private Long id;

    @MapsId
    @OneToOne(optional = true)
    @JoinColumn(name = "employee_id", referencedColumnName = "user_id", nullable = false)
    private Users user;*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = true)
    private Users user;

    @Column(nullable = true, name = "name")
    private String name;

    @Column(nullable = true, name = "position")
    private String position;

    @Column(nullable = true, name = "email")
    private String email;

    @Column(nullable = true, name = "phone")
    private String phone;

    @Column(nullable = true, name = "address")
    private String address;

    /*@Override
    public Long getId() {
        return user != null ? user.getId() : null;
    }

    @Override
    public void setId(Long id) {
        if (user == null) {
            user = new Users();
        }
        user.setId(id);
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employees other = (Employees) o;
        return Objects.equals(id, other.id)
                && name.equals(other.name)
                && position.equals(other.position)
                && Objects.equals(email, other.email)
                && Objects.equals(phone, other.phone)
                && Objects.equals(address, other.address);
    }
}

