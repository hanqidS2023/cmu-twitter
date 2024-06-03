package reward.service.command.credit;

import java.util.ArrayList;
import java.util.List;

import reward.exception.ErrorHandling.RewardException;
import reward.model.CreditHistory;
import reward.service.command.GetCommand;

public class GetCreditHistoryCommand implements GetCommand<List<CreditHistory>> {
    private CreditReceiver receiver;
    private List<CreditHistory> creditHistories;

    public GetCreditHistoryCommand(CreditReceiver receiver) {
        this.receiver = receiver;
        this.creditHistories = new ArrayList<>();
    }

    public void execute() throws RewardException {
        this.creditHistories.clear();
        List<CreditHistory> history = receiver.getCreditHistory();
        this.creditHistories.addAll(history);
    }

    public List<CreditHistory> getValue() {
        return this.creditHistories;
    }
}
