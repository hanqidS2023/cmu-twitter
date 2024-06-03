package reward.service.command.product;

import reward.exception.ErrorHandling.RewardException;
import reward.model.Product;
import reward.service.command.GetCommand;

public class CreateProductCommand implements GetCommand<Product> {
    private ProductReceiver receiver;
    private Product product;

    public CreateProductCommand(ProductReceiver receiver) {
        this.receiver = receiver;
    }

    public void execute() throws RewardException {
        this.product = receiver.createNewProduct();
    }

    public Product getValue() {
        return this.product;
    }
}
