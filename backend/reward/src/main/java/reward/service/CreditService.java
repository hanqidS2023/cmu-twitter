package reward.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import reward.model.Credit;
import reward.model.CreditHistory;
import reward.model.CreditHistoryRepository;
import reward.model.CreditRepository;
import reward.exception.ErrorHandling.ExceptionType;
import reward.exception.ErrorHandling.RewardException;

@Service
public class CreditService {

    private final CreditRepository creditRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private Optional<Credit> userCreditInfo;

    private static final Integer NEW_USER_POINT = 5;
    private static final Integer NEW_USER_COIN = 5;

    public CreditService(CreditRepository creditRepository, CreditHistoryRepository creditHistoryRepository) {
        this.creditRepository = creditRepository;
        this.creditHistoryRepository = creditHistoryRepository;
    }

    public void setUserCreditInfo(long userId) {
        this.userCreditInfo = creditRepository.findById(userId);
    }

    public Credit createUserCredit(long userId, String username) throws RewardException {
        // Check if user already exists
        this.setUserCreditInfo(userId);
        if (this.userCreditInfo.isPresent()) {
            throw new RewardException(ExceptionType.USEREXISTS);
        }

        // Insert new user credit into db
        Credit creditInfo = new Credit(userId, username, NEW_USER_POINT, NEW_USER_COIN);
        userCreditInfo = Optional.of(creditInfo);
        Credit credit = creditRepository.save(creditInfo);

        // Create history and save it in credit history table
        CreditHistory creditHistory = new CreditHistory(userId);
        creditHistory.setPoints(NEW_USER_POINT);
        creditHistory.setTimestamp(LocalDateTime.now());
        creditHistory.setCoins(NEW_USER_COIN);
        creditHistoryRepository.save(creditHistory);
        return credit;
    }

    public Credit getCredit() throws RewardException {
        // Check if user is valid
        if (this.userCreditInfo.isPresent()) {
            return userCreditInfo.get();
        } else {
            throw new RewardException(ExceptionType.USERNOTFOUND);
        }
    }

    public Credit addPoints(int amount) throws RewardException {
        // Check if user is valid
        if (this.userCreditInfo.isPresent()) {
            Credit creditInfo = userCreditInfo.get();

            // Get credit info
            long userId = creditInfo.getUserId();
            int curPoints = creditInfo.getPoints();
            int curCoins = creditInfo.getCoins();
            int newPoints = curPoints + amount;

            // Create history and save it in credit history table
            CreditHistory creditHistory = new CreditHistory(userId);
            creditHistory.setPoints(newPoints);
            creditHistory.setTimestamp(LocalDateTime.now());
            creditHistory.setCoins(curCoins);
            creditHistoryRepository.save(creditHistory);

            // Update credit table
            creditInfo.setPoints(newPoints);
            userCreditInfo = Optional.of(creditInfo);
            return creditRepository.save(creditInfo);
        } else {
            throw new RewardException(ExceptionType.USERNOTFOUND);
        }
    }

    public Credit addCoins(int amount) throws RewardException {
        // Check if user is valid
        if (this.userCreditInfo.isPresent()) {
            Credit creditInfo = userCreditInfo.get();

            // Get credit info
            long userId = creditInfo.getUserId();
            int curPoints = creditInfo.getPoints();
            int curCoins = creditInfo.getCoins();
            int newCoins = curCoins + amount;

            // Create history and save it in credit history table
            CreditHistory creditHistory = new CreditHistory(userId);
            creditHistory.setPoints(curPoints);
            creditHistory.setTimestamp(LocalDateTime.now());
            creditHistory.setCoins(newCoins);
            creditHistoryRepository.save(creditHistory);

            // Update credit table
            creditInfo.setCoins(newCoins);
            userCreditInfo = Optional.of(creditInfo);
            return creditRepository.save(creditInfo);
        } else {
            throw new RewardException(ExceptionType.USERNOTFOUND);
        }
    }

    public Credit deductCoins(int amount) throws RewardException {
        // Check if user is valid
        if (this.userCreditInfo.isPresent()) {
            Credit creditInfo = userCreditInfo.get();

            // Get credit info
            long userId = creditInfo.getUserId();
            int curPoints = creditInfo.getPoints();
            int curCoins = creditInfo.getCoins();
            int newCoins = curCoins - amount;

            // If not enough coins, throw exception
            if (newCoins < 0) {
                throw new RewardException(ExceptionType.NOTENOUGHCOINS);
            }

            // Create history and save it in credit history table
            CreditHistory creditHistory = new CreditHistory(userId);
            creditHistory.setPoints(curPoints);
            creditHistory.setTimestamp(LocalDateTime.now());
            creditHistory.setCoins(newCoins);
            creditHistoryRepository.save(creditHistory);

            // Update credit table
            creditInfo.setCoins(newCoins);
            userCreditInfo = Optional.of(creditInfo);
            return creditRepository.save(creditInfo);
        } else {
            throw new RewardException(ExceptionType.USERNOTFOUND);
        }
    }

    public List<CreditHistory> findHistoryByUserId(long userId) throws RewardException {
        // Check if user is valid
        if (!this.userCreditInfo.isPresent()) {
            throw new RewardException(ExceptionType.USERNOTFOUND);
        }
        return this.creditHistoryRepository.findAllByUserId(userId);
    }
}