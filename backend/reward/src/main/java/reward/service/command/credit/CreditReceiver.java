package reward.service.command.credit;

import org.springframework.stereotype.Service;

import lombok.*;
import reward.exception.ErrorHandling.RewardException;
import reward.model.Credit;
import reward.model.CreditHistory;
import reward.service.CreditService;

import java.util.List;

@Service
@Getter
@Setter
public class CreditReceiver {

    private Long userId;
    private String username;
    private Integer changePointsAmount;
    private Integer changeCoinsAmount;
    private final CreditService creditService;

    public CreditReceiver(CreditService creditService) {
        this.creditService = creditService;
    }

    public void setUserId(long userId) {
        this.userId = userId;
        creditService.setUserCreditInfo(userId);
    }

    // Method to create new user's credit info
    public Credit createUserCreditInfo() throws RewardException {
        return creditService.createUserCredit(userId, username);
    }

    // Method to get current credit info for the user
    public Credit getCreditInfo() throws RewardException {
        creditService.setUserCreditInfo(userId);
        return creditService.getCredit();
    }

    // Method to add points for the user
    public Credit addPoints() throws RewardException {
        creditService.setUserCreditInfo(userId);
        return creditService.addPoints(changePointsAmount);
    }

    // Method to add coins for the user
    public Credit addCoins() throws RewardException {
        creditService.setUserCreditInfo(userId);
        return creditService.addCoins(changeCoinsAmount);
    }

    // Method to deduct coins for the user
    public Credit deductCoins() throws RewardException {
        creditService.setUserCreditInfo(userId);
        return creditService.deductCoins(changeCoinsAmount);
    }

    // Method to find credit history
    public List<CreditHistory> getCreditHistory() throws RewardException {
        creditService.setUserCreditInfo(userId);
        return creditService.findHistoryByUserId(userId);
    }
}
