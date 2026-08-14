package com.synth.hotelbookingmanagement.room;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;
import java.math.BigDecimal;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.Objects;
import com.synth.hotelbookingmanagement.hotel.Hotel;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rooms")
@EntityListeners(AuditingEntityListener.class)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, length = 20)
    private String roomNumber;

    @Column(nullable = false)
    private String type;

    @Column(nullable = true)
    private Integer floor;

    @Column(nullable = false)
    private BigDecimal pricePerNight;

    @Column(nullable = true)
    private Integer capacity;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotelId;


    // ─── Tell-Don't-Ask state predicates ──────────────────────────────────────

    public boolean isSingle() {
        return "SINGLE".equals(type);
    }

    public boolean isDouble() {
        return "DOUBLE".equals(type);
    }

    public boolean isSuite() {
        return "SUITE".equals(type);
    }

    public boolean isDeluxe() {
        return "DELUXE".equals(type);
    }

    public boolean isPenthouse() {
        return "PENTHOUSE".equals(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room other)) return false;
        return id != null && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
