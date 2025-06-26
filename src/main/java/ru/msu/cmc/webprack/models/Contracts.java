package ru.msu.cmc.webprack.models;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "Contracts")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Contracts implements CommonEntity<Long>{

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    Clients client;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    Services  service;

    @Id
    @Column(nullable = true, name = "contract_id")
    private Long contract_id;

    @Column(nullable = true, name = "start_date")
    private Date start_date;

    @Column(nullable = true, name = "end_date")
    private Date end_date;

    @Column(nullable = true, name = "terms")
    private String term;

    @Override
    public Long getId() {
        return client != null ? client.getId() : null;
    }

    @Override
    public void setId(Long ID) {
        if (ID == null) {
            client = new Clients();
        }
        client.setId(ID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; //aka link of two objects are the same
        if (o == null || getClass() != o.getClass()) return false; //aka links are not the same

        Contracts other = (Contracts) o;
        return Objects.equals(client, other.client)
                && contract_id.equals(other.contract_id)
                && service.equals(other.service)
                && Objects.equals(start_date, other.start_date)
                && Objects.equals(end_date, other.end_date)
                && Objects.equals(term, other.term);
    }
}

