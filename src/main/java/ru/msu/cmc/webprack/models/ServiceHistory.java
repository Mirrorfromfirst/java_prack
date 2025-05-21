package ru.msu.cmc.webprack.models;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ServiceHistory")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceHistory implements CommonEntity<Long> {

    @Id
    @Column(nullable = true, name = "history_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id", nullable = true)
    private Clients client;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id", nullable = true)
    private Employees employee;

    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "service_id", nullable = true)
    private Services service_id;

    @Column(name = "service_date")
    private Date service_date;

    @Column(nullable = true, name = "additional_perks")
    private String additional_perks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; //aka link of two objects are the same
        if (o == null || getClass() != o.getClass()) return false; //aka links are not the same

        ServiceHistory other = (ServiceHistory) o;
        return Objects.equals(id, other.id)
                && Objects.equals(client, other.client)
                && service_id.equals(other.service_id)
                && Objects.equals(employee, other.employee)
                && Objects.equals(service_date, other.service_date)
                && Objects.equals(additional_perks, other.additional_perks);
    }
}


