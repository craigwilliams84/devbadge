package uk.co.craigcodes.devbadge.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.craigcodes.devbadge.model.badge.Badge;
import uk.co.craigcodes.devbadge.model.github.GitHubRepository;
import uk.co.craigcodes.devbadge.model.user.User;
import uk.co.craigcodes.devbadge.service.DevbadgeService;
import uk.co.craigcodes.devbadge.service.GithubOauthService;
import uk.co.craigcodes.devbadge.service.GithubService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class RestEndpoint {

    private DevbadgeService service;

    private GithubService gitHubService;

    private GithubOauthService oauthService;

    @RequestMapping(value="/mint", method=RequestMethod.POST)
    public List<Badge> mint(@RequestParam String user, @RequestParam String repoName) throws IOException {
        System.out.println(repoName);
        return service.mintBadgesForRepository(user, repoName);
    }

    @RequestMapping(value="/login")
    public User login(String code) {
        final String accessToken = oauthService.login(code);

        return service.loginUser(accessToken, code);
    }

    @RequestMapping(value="/user")
    public User getUserWithCode(@RequestParam String code) throws IOException {
        return service.getUserWithCode(code);
    }

    @RequestMapping(value="/badges")
    public List<Badge> getBadges(@RequestParam String user) throws IOException {
        return service.getBadges(user);
    }

    @RequestMapping(value="/repositories")
    public Set<String> getRepositories(@RequestParam String user) throws IOException {
        return gitHubService.getCommittedRepositoryNames(user);
    }
 }
