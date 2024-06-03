package reward.dto;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class UpdateUserRewardMessage implements Serializable {
    @NonNull
    private Long userId;
    @NonNull
    private String username;
    private Integer coinChangeAmount;
    private Integer pointChangeAmount;
}