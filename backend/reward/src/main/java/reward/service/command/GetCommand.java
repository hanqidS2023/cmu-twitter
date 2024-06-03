package reward.service.command;

public interface GetCommand<T> extends Command {
    T getValue();
}
