package io.protopanda.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "employee")
@ToString
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "city_name", nullable = false)
    private String cityName;

}
