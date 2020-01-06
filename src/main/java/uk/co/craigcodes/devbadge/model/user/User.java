package uk.co.craigcodes.devbadge.model.user;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class User {

    @Id
    private String username;

    private String name;

    private String email;

    private String accessToken;

    private String reconcileCode;
}
