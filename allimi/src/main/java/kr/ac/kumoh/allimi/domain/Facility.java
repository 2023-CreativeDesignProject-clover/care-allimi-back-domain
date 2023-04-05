package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
public class Facility {

    /**
     * facility_id
     * Name
     * Address
     * Tel
     */

    @Id
    @Column(name = "facility_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String tel;

}
