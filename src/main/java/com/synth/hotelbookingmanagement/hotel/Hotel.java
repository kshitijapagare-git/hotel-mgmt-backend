package com.synth.hotelbookingmanagement.hotel;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.Objects;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "hotels")
@EntityListeners(AuditingEntityListener.class)
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = true)
    private Integer starRating;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(nullable = false)
    private String status;


    // ─── Tell-Don't-Ask state predicates ──────────────────────────────────────

    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    public boolean isInactive() {
        return "INACTIVE".equals(status);
    }

    public boolean isRenovation() {
        return "RENOVATION".equals(status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hotel other)) return false;
        return id != null && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
