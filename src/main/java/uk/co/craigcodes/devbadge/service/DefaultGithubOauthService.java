package uk.co.craigcodes.devbadge.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.co.craigcodes.devbadge.model.user.User;
import uk.co.craigcodes.devbadge.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DefaultGithubOauthService implements GithubOauthService {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    private UserRepository userRepository;

    @Override
    public String login(String code) {
        final Optional<User> existing = userRepository.findByReconcileCode(code);

        if (existing.isPresent()) {
            return existing.get().getAccessToken();
        }

        final String tokenUrl = buildAccessTokenUrl(code);
        System.out.println(tokenUrl);

        final ResponseEntity<String> response = REST_TEMPLATE.postForEntity(tokenUrl, null, String.class);

        MultiValueMap<String, String> queryParams =
                UriComponentsBuilder
                        .fromUriString("http://blah.com?" + response.getBody())
                        .build()
                        .getQueryParams();

        queryParams.forEach((key, value) -> System.out.println(key + "=" + value));

        return queryParams.get("access_token").get(0);
    }

    private String buildAccessTokenUrl(String code) {
       return new StringBuilder("https://github.com/login/oauth/access_token?")
                .append("client_id=4805e40f22af124502d7&")
                .append("redirect_url=http://localhost:3000&")
                .append("client_secret=2756d5962dd172468c83a3db5282012fcb173269&")
                .append("code=")
                .append(code)
                .toString();
    }
}
