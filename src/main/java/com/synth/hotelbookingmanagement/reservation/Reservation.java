package com.synth.hotelbookingmanagement.reservation;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;
import java.time.LocalDate;
import java.math.BigDecimal;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.Objects;
import com.synth.hotelbookingmanagement.room.Room;
import com.synth.hotelbookingmanagement.guest.Guest;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reservations")
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Column(nullable = true)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private String status;

    @Column(nullable = true, length = 500)
    private String specialRequests;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guestId;


    // ─── Tell-Don't-Ask state predicates ──────────────────────────────────────

    public boolean isConfirmed() {
        return "CONFIRMED".equals(status);
    }

    public boolean isCheckedIn() {
        return "CHECKED_IN".equals(status);
    }

    public boolean isCheckedOut() {
        return "CHECKED_OUT".equals(status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }

    public boolean isNoShow() {
        return "NO_SHOW".equals(status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation other)) return false;
        return id != null && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
