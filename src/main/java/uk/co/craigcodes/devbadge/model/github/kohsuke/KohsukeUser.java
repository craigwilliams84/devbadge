package uk.co.craigcodes.devbadge.model.github.kohsuke;

import lombok.Getter;
import org.kohsuke.github.GHUser;
import uk.co.craigcodes.devbadge.exception.DevbadgeException;
import uk.co.craigcodes.devbadge.model.github.GithubUser;

import java.io.IOException;

@Getter
public class KohsukeUser implements GithubUser {

    private GHUser wrapped;

    private String username;

    private String name;

    private String email;

    private String accessToken;

    private KohsukeUser(GHUser user, String accessToken) {
        this.wrapped = user;
        this.accessToken = accessToken;

        username = user.getLogin();

        try {
            name = user.getName();
            email = user.getEmail();
        } catch (IOException e) {
            throw new DevbadgeException("Unable to build user", e);
        }
    }

    public static KohsukeUser build(GHUser user, String accessToken) {
        return new KohsukeUser(user, accessToken);
    }
}
