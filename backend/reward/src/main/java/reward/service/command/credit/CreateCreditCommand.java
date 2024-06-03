package reward.service.command.credit;

import reward.exception.ErrorHandling.RewardException;
import reward.model.Credit;
import reward.service.command.GetCommand;

public class CreateCreditCommand implements GetCommand<Credit> {
    private CreditReceiver receiver;
    private Credit credit;

    public CreateCreditCommand(CreditReceiver receiver) {
        this.receiver = receiver;
    }

    public void execute() throws RewardException {
        this.credit = receiver.createUserCreditInfo();
    }


    public Credit getValue() {
        return this.credit;
    }
}
