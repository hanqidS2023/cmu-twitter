package reward.service.command.credit;

import java.util.List;

import org.springframework.stereotype.Service;

import reward.exception.ErrorHandling.ExceptionType;
import reward.exception.ErrorHandling.RewardException;
import reward.model.Credit;
import reward.model.CreditHistory;
import reward.service.command.Command;
import reward.service.command.GetCommand;

@Service
public class CreditInvoker {
    private Command command;

    private Object value;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() throws RewardException {
        this.command.execute();

        if (command instanceof GetCommand) {
            value = ((GetCommand<?>) command).getValue();
        }
    }

    public Credit getCreditInfo() throws RewardException {
        if (value instanceof Credit) {
            return (Credit) value;
        } else {
            throw new RewardException(ExceptionType.WRONGCOMMANDTYPE);
        }
    }

    @SuppressWarnings("unchecked")
    public List<CreditHistory> getHistory() throws RewardException {
        if (value instanceof List<?>) {
            List<?> list = (List<?>) value;
            if (list.stream().allMatch(CreditHistory.class::isInstance)) {
                return (List<CreditHistory>) list;
            } else {
                throw new RewardException(ExceptionType.WRONGCOMMANDTYPE);
            }
        } else {
            throw new RewardException(ExceptionType.WRONGCOMMANDTYPE);
        }
    }
}
