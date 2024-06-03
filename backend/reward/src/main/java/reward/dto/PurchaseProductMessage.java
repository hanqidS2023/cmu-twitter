package reward.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseProductMessage {
    private Long userId;
    private Long productId;
    private String imageUrl;
}
