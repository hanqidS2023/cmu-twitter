package reward.service.command.credit;

import reward.exception.ErrorHandling.RewardException;
import reward.model.Credit;
import reward.service.command.GetCommand;

public class AddCoinsCommand implements GetCommand<Credit> {
    private CreditReceiver receiver;
    private Credit credit;

    public AddCoinsCommand(CreditReceiver receiver) {
        this.receiver = receiver;
    }

    public void execute() throws RewardException {
        this.credit = receiver.addCoins();
    }

    public Credit getValue() {
        return this.credit;
    }
}