package reward.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import reward.model.Product;
import reward.model.ProductRepository;
import reward.exception.ErrorHandling.ExceptionType;
import reward.exception.ErrorHandling.RewardException;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private Optional<Product> productInfo;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void setProductInfo(long id) {
        this.productInfo = productRepository.findById(id);
    }

    public Product createProduct(String name, int price, String imageUrl, boolean isPurchasable) throws RewardException {
        Optional<Product> existProduct = this.productRepository.findByName(name);
        if (!existProduct.isPresent()) {
            Product product = new Product(name, price, imageUrl, isPurchasable);
            this.productInfo = Optional.of(product);
            return this.productRepository.save(product);
        } else {
            throw new RewardException(ExceptionType.PRODUCTEXISTS);
        }
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct() throws RewardException {
        // Check if product exists
        if (!productInfo.isPresent()) {
            throw new RewardException(ExceptionType.PRODUCTNOTFOUND);
        }
        return productInfo.get();
    }

    public Product updateProduct(String name, Integer amount, String imageUrl, Boolean isPurchasable) throws RewardException {
        // Check if product exists
        if (!productInfo.isPresent()) {
            throw new RewardException(ExceptionType.PRODUCTNOTFOUND);
        }

        // Update product
        Product product = productInfo.get();
        product.setName(name);
        product.setPrice(amount);
        product.setImageUrl(imageUrl);
        product.setPurchasable(isPurchasable);
        productInfo = Optional.of(product);
        return this.productRepository.save(product);
    }

    public void deleteProduct() throws RewardException {
        // Check if product exists
        if (!productInfo.isPresent()) {
            throw new RewardException(ExceptionType.PRODUCTNOTFOUND);
        }

        // Delete product
        Product product = productInfo.get();
        productInfo = Optional.empty();
        this.productRepository.delete(product);
    }
}
