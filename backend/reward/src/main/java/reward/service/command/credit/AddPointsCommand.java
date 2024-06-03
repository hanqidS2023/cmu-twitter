package reward.service.command.credit;

import reward.exception.ErrorHandling.RewardException;
import reward.model.Credit;
import reward.service.command.GetCommand;

public class AddPointsCommand implements GetCommand<Credit> {
    private CreditReceiver receiver;
    private Credit credit;

    public AddPointsCommand(CreditReceiver receiver) {
        this.receiver = receiver;
    }

    public void execute() throws RewardException {
       this.credit = receiver.addPoints();
    }

    public Credit getValue() {
        return this.credit;
    }
}