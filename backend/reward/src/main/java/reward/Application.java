package reward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.time.format.DateTimeFormatter;
import reward.exception.ErrorHandling.RewardException;
import reward.service.command.product.CreateProductCommand;
import reward.service.command.Command;
import reward.service.command.product.ProductInvoker;
import reward.service.command.product.ProductReceiver;

@SpringBootApplication
public class Application {

	private final ProductInvoker invoker;

	private final ProductReceiver receiver;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public Application(ProductInvoker invoker, ProductReceiver receiver) {
		this.invoker = invoker;
		this.receiver = receiver;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	private void createProduct(String name, int price, String imagrUrl, boolean isPurchasable) throws RewardException {
		receiver.setNewName(name);
		receiver.setNewPrice(price);
		receiver.setNewImageUrl(imagrUrl);
		receiver.setIsPurchasable(isPurchasable);
		Command createProductCommand = new CreateProductCommand(receiver);
		invoker.setCommand(createProductCommand);
		invoker.executeCommand();
	}

	@EventListener(ApplicationReadyEvent.class)
	public void insertDefaultIcons() throws RewardException {
		// Create several default products
		createProduct("icon-advanced", 1, "https://cmux-reward.s3.us-east-2.amazonaws.com/advanced.png", true);

		createProduct("icon-tartan", 6, "https://cmux-reward.s3.us-east-2.amazonaws.com/tartan.png", false);

		createProduct("icon-mickey", 5, "https://cmux-reward.s3.us-east-2.amazonaws.com/mickey.png", true);

	}
}
