package reward.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseProductRequest {
    private long userId;
    private long productId;
}