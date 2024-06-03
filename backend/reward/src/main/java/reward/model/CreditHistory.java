package reward.model;

import java.time.LocalDateTime;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "credit_history")
public class CreditHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    private Long userId;
    private LocalDateTime timestamp;
    private Integer coins;
    private Integer points;

    protected CreditHistory() {
    }

    public CreditHistory(Long userId) {
        this.userId = userId;
    }
}
