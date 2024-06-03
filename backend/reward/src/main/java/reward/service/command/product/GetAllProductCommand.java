package reward.service.command.product;

import java.util.ArrayList;
import java.util.List;

import reward.exception.ErrorHandling.RewardException;
import reward.model.Product;
import reward.service.command.GetCommand;

public class GetAllProductCommand implements GetCommand<List<Product>> {
    private ProductReceiver receiver;
    private List<Product> allProducts;

    public GetAllProductCommand(ProductReceiver receiver) {
        this.receiver = receiver;
        allProducts = new ArrayList<>();
    }

    public void execute() throws RewardException {
        this.allProducts.clear();
        List<Product> products = receiver.getAllProducts();
        this.allProducts.addAll(products);
    }

    public List<Product> getValue() {
        return this.allProducts;
    }
}
