package reward.service.command;

import reward.exception.ErrorHandling.RewardException;

public interface Command {
    void execute() throws RewardException;
}
