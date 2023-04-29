package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDate;

@Entity
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invit_id")
    private Long id;

    @NotNull
    @Column(name = "user_role")
    private UserRole userRole;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @CreationTimestamp
    private LocalDate dates;


}
