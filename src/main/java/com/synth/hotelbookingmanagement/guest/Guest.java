package com.synth.hotelbookingmanagement.guest;

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
@Table(name = "guests")
@EntityListeners(AuditingEntityListener.class)
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, length = 80)
    private String firstName;

    @Column(nullable = false, length = 80)
    private String lastName;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = true, length = 100)
    private String nationality;

    @Column(nullable = true, length = 50, unique = true)
    private String passportNumber;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Guest other)) return false;
        return id != null && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
