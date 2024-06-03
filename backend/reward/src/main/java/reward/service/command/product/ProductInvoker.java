package reward.service.command.product;

import java.util.List;

import org.springframework.stereotype.Service;

import reward.exception.ErrorHandling.ExceptionType;
import reward.exception.ErrorHandling.RewardException;
import reward.model.Product;
import reward.service.command.Command;
import reward.service.command.GetCommand;

@Service
public class ProductInvoker {
    private Command command;

    private Object product;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() throws RewardException {
        this.command.execute();

        if (command instanceof GetCommand) {
            product = ((GetCommand<?>) command).getValue();
        }
    }

    public Product getProduct() throws RewardException {
        if (product instanceof Product) {
            return (Product) product;
        } else {
            throw new RewardException(ExceptionType.WRONGCOMMANDTYPE);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Product> getAllProducts() throws RewardException {
        if (product instanceof List<?>) {
            List<?> list = (List<?>) product;
            if(list.isEmpty()){
                return (List<Product>) list;
            }
            if (list.stream().allMatch(Product.class::isInstance)) {
                return (List<Product>) list;
            } else {
                throw new RewardException(ExceptionType.WRONGCOMMANDTYPE);
            }
        } else {
            throw new RewardException(ExceptionType.WRONGCOMMANDTYPE);
        }
    }
}
