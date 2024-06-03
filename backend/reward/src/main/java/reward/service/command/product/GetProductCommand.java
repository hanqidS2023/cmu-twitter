package reward.service.command.product;

import reward.exception.ErrorHandling.RewardException;
import reward.model.Product;
import reward.service.command.GetCommand;

public class GetProductCommand implements GetCommand<Product> {
    private ProductReceiver receiver;
    private Product product;

    public GetProductCommand(ProductReceiver receiver) {
        this.receiver = receiver;
    }

    public void execute() throws RewardException {
        this.product = receiver.getProduct();
    }

    public Product getValue() {
        return this.product;
    }
}
