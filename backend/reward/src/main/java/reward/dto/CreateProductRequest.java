package reward.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateProductRequest {
    private String name;
    private int price;
}