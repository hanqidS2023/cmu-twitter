package reward.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;
import reward.service.S3Service;
import reward.service.command.credit.CreditInvoker;
import reward.service.command.credit.CreditReceiver;
import reward.service.command.credit.DeductCoinsCommand;
import reward.service.command.credit.GetCreditHistoryCommand;
import reward.service.command.credit.GetCreditInfoCommand;
import reward.service.command.Command;
import reward.service.command.product.CreateProductCommand;
import reward.service.command.product.GetAllProductCommand;
import reward.service.command.product.GetProductCommand;
import reward.service.command.product.ProductInvoker;
import reward.service.command.product.ProductReceiver;
import reward.service.command.product.UpdateProductCommand;
import reward.dto.CreateProductRequest;
import reward.dto.PurchaseProductRequest;
import reward.dto.UpdateProductRequest;
import reward.dto.PurchaseProductMessage;
import reward.exception.ErrorHandling.RewardException;
import reward.model.Credit;
import reward.model.CreditHistory;
import reward.model.Product;

@RestController
@AllArgsConstructor
@RequestMapping("/shop")
public class RewardController {

    private final ProductInvoker productInvoker;

    private final ProductReceiver productReceiver;

    private final CreditInvoker creditInvoker;

    private final CreditReceiver creditReceiver;

    private final S3Service s3Service;

    private final MQProducer messageProducer;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(RewardController.class);

    private static final String LOGFORMAT = "\n{}\n";

    private void executeProductCommand(Command productCommand) throws RewardException {
        productInvoker.setCommand(productCommand);
        productInvoker.executeCommand();
    }

    private void executeCreditCommand(Command creditCommand) throws RewardException {
        creditInvoker.setCommand(creditCommand);
        creditInvoker.executeCommand();
    }

    private List<Product> getProducts() throws RewardException {
        executeProductCommand(new GetAllProductCommand(productReceiver));
        return productInvoker.getAllProducts();
    }

    private List<CreditHistory> getHistory(Long userId) throws RewardException {
        this.creditReceiver.setUserId(userId);
        executeCreditCommand(new GetCreditHistoryCommand(creditReceiver));
        return creditReceiver.getCreditHistory();
    }

    @PostMapping(value = "/createProduct", consumes = "multipart/form-data")
    public ResponseEntity<?> createProduct(@RequestPart CreateProductRequest createProductRequest,
            @RequestPart("image") MultipartFile image) throws IOException {
        String name = createProductRequest.getName();
        int price = createProductRequest.getPrice();
        // Check if product exists
        try {
            List<Product> products = getProducts();
            if (products.stream().anyMatch(p -> p.getName().equals(name))) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error: Product already exists");
            }
        } catch (RewardException e) {
            LOGGER.info(LOGFORMAT, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        if (image.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Bad request: Need to provide an icon image");
        }

        // Upload image
        String imageUrl = "";
        try {
            imageUrl = s3Service.uploadFile(image);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        // Setup product
        productReceiver.setNewName(name);
        productReceiver.setNewPrice(price);
        productReceiver.setNewImageUrl(imageUrl);

        try {
            // Create product command
            executeProductCommand(new CreateProductCommand(productReceiver));
            Product newProduct = productInvoker.getProduct();
            return ResponseEntity.ok(newProduct);
        } catch (RewardException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/allProducts")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = getProducts();
            return ResponseEntity.ok(products);
        } catch (RewardException e) {
            LOGGER.info(LOGFORMAT, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping(value = "/update/{productId}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateProduct(@PathVariable long productId,
            @RequestPart UpdateProductRequest updateProductRequest,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        // Check if product exists
        try {
            // Get the product command
            executeProductCommand(new GetProductCommand(productReceiver));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        String name = updateProductRequest.getName();
        Integer price = updateProductRequest.getPrice();
        Boolean isPurchasable = updateProductRequest.getIsPurchasable();
        String imageUrl = "";
        if (!image.isEmpty()) {
            // Upload image
            try {
                imageUrl = s3Service.uploadFile(image);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
        // Set the target product
        productReceiver.setProductId(productId);
        productReceiver.setNewName(name);
        productReceiver.setNewPrice(price);
        productReceiver.setIsPurchasable(isPurchasable);
        if (!imageUrl.isEmpty()) {
            productReceiver.setNewImageUrl(imageUrl);
        }

        try {
            executeProductCommand(new UpdateProductCommand(productReceiver));
            Product product = productInvoker.getProduct();
            return ResponseEntity.ok(product);
        } catch (RewardException e) {
            LOGGER.info(LOGFORMAT, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable long productId) {
        try {
            // Set the target product id
            productReceiver.setProductId(productId);

            // Get the product command
            executeProductCommand(new GetProductCommand(productReceiver));
            Product product = productInvoker.getProduct();
            return ResponseEntity.ok(product);
        } catch (RewardException e) {
            LOGGER.info(LOGFORMAT, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/userCredit/{userId}")
    public ResponseEntity<?> getCreditInfo(@PathVariable long userId) {
        try {
            // Set the target user id
            creditReceiver.setUserId(userId);

            // Get the user credit info command
            executeCreditCommand(new GetCreditInfoCommand(creditReceiver));
            Credit creditInfo = creditInvoker.getCreditInfo();
            return ResponseEntity.ok(creditInfo);
        } catch (RewardException e) {
            LOGGER.info(LOGFORMAT, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/purchaseProduct")
    public ResponseEntity<?> purchaseProduct(@RequestBody PurchaseProductRequest purchaseProductRequest) {
        Long userId = purchaseProductRequest.getUserId();
        Long productId = purchaseProductRequest.getProductId();
        // Target user id
        creditReceiver.setUserId(userId);
        // Target product id
        productReceiver.setProductId(productId);

        try {
            // Get user coins
            executeCreditCommand(new GetCreditInfoCommand(creditReceiver));
            Credit creditInfo = creditInvoker.getCreditInfo();
            int coins = creditInfo.getCoins();

            // Get the product
            executeProductCommand(new GetProductCommand(productReceiver));
            Product product = productInvoker.getProduct();
            int price = product.getPrice();

            if (coins < price) {
                return ResponseEntity.badRequest().body("Cannot buy item because of not enough coins");
            }

            // Set deduct amount
            creditReceiver.setChangeCoinsAmount(price);
            // Deduct user coins command
            executeCreditCommand(new DeductCoinsCommand(creditReceiver));

            // Return the image url
            String imageUrl = product.getImageUrl();

            // Message that user successfully buy the product
            PurchaseProductMessage purchaseProductMessage = new PurchaseProductMessage(userId, productId, imageUrl);
            String jsonString = mapper.writeValueAsString(purchaseProductMessage);
            messageProducer.sendImageToUser(jsonString);

            return ResponseEntity.ok(product);
        } catch (RewardException | JsonProcessingException e) {
            LOGGER.info(LOGFORMAT, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/creditHistory/{userId}")
    public ResponseEntity<?> getCreditHistory(@PathVariable long userId) {
        try {
            List<CreditHistory> creditHistories = getHistory(userId);
            List<CreditHistory> res = creditHistories.stream().filter(history -> history.getUserId() == userId)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(res);
        } catch (RewardException e) {
            LOGGER.info(LOGFORMAT, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
