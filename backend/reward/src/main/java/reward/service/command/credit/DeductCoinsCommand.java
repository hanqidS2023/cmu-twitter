package reward.service.command.credit;

import reward.exception.ErrorHandling.RewardException;
import reward.model.Credit;
import reward.service.command.GetCommand;

public class DeductCoinsCommand implements GetCommand<Credit> {
    private CreditReceiver receiver;
    private Credit credit;

    public DeductCoinsCommand(CreditReceiver receiver) {
        this.receiver = receiver;
    }

    public void execute() throws RewardException {
        this.credit = receiver.deductCoins();
    }

    public Credit getValue() {
        return this.credit;
    }
}