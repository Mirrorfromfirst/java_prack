package ru.msu.cmc.webprack.models;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Clients")
@Inheritance(strategy = InheritanceType.JOINED)  // <-- вероятно по умолчанию
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Clients implements CommonEntity<Long> {
    /*@Id
    private Long id;

    @MapsId
    @OneToOne(optional = true)
    @JoinColumn(name = "client_id", referencedColumnName = "user_id", nullable = true)
    private Users user;*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    //@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    //@JoinColumn(name = "user_id", unique = true)
    //private Users user;

    //@OneToOne
    //@JoinColumn(name = "user_id", nullable = true, unique = true)  // Необязательная связь
    //private Users user;

    @Column(nullable = true, name = "username")
    private String name;

    @Column(nullable = true, name = "phone")
    private String phone;

    @Column(nullable = true, name = "email")
    private String email;

    @Column(nullable = true, name = "address")
    private String address;

    //@Override
    //public Long getId() {
      //  return user != null ? user.getId() : null;
   //}

    @Override
    public void setId(Long id) {
        this.id = id; // Устанавливайте ID самой сущности Clients, а не связанного User
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; //aka link of two objects are the same
        if (o == null || getClass() != o.getClass()) return false; //aka links are not the same

        Clients other = (Clients) o;
        return Objects.equals(id, other.id)
                && name.equals(other.name)
                && phone.equals(other.phone)
                && Objects.equals(email, other.email)
                && Objects.equals(address, other.address);
    }
}

