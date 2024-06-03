package reward.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CreditHistoryRepository extends JpaRepository<CreditHistory, Long> {
    List<CreditHistory> findAllByUserId(final Long userId);
}
