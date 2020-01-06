package uk.co.craigcodes.devbadge.service;

import lombok.AllArgsConstructor;
import org.kohsuke.github.*;
import org.springframework.stereotype.Service;
import uk.co.craigcodes.devbadge.exception.DevbadgeException;
import uk.co.craigcodes.devbadge.model.github.GitHubRepository;
import uk.co.craigcodes.devbadge.model.github.GithubUser;
import uk.co.craigcodes.devbadge.model.github.kohsuke.KohsukeRepository;
import uk.co.craigcodes.devbadge.model.github.kohsuke.KohsukeUser;
import uk.co.craigcodes.devbadge.model.user.User;
import uk.co.craigcodes.devbadge.repository.UserRepository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@AllArgsConstructor
@Service
public class KohsukeGithubService implements GithubService {

    private UserRepository userRepository;

    @Override
    public GithubUser getUser(String accessToken) throws IOException {
        return KohsukeUser.build(connectToGitHub(accessToken).getMyself(), accessToken);
    }

    @Override
    public GitHubRepository getRepository(String username, String repoName) throws IOException {
        final GHRepository repo = connectToGitHubForUser(username).getRepository(repoName);

        return KohsukeRepository.build(repo);
    }

    @Override
    public Set<String> getCommittedRepositoryNames(String username) throws IOException {
        final Set<String> committedRepositoryNames = new HashSet<>();

        final GitHub github = connectToGitHubForUser(username);

        final Iterator iterator = github
                .getMyself()
                .listEvents()
                .withPageSize(30)
                .iterator();

        int count = 0;
        while (iterator.hasNext()) {
            log("Done has next");

            final GHEventInfo event = (GHEventInfo)iterator.next();

            log("Got event");

            log("Getting repo");
            if (event.getType() == GHEvent.PUSH) {
                log("Repo not null");
                committedRepositoryNames.add(getRepoNameWithoutRemoteCall(event));
                log("Added repo");
            }

            System.out.println(count++);
        }

        return committedRepositoryNames;
    }

    private String getRepoNameWithoutRemoteCall(GHEventInfo event) {
        try {
            final Field repoField = GHEventInfo.class.getDeclaredField("repo");

            repoField.setAccessible(true);
            GHEventInfo.GHEventRepository repo = (GHEventInfo.GHEventRepository) repoField.get(event);

            final Field nameField = GHEventInfo.GHEventRepository.class.getDeclaredField("name");
            nameField.setAccessible(true);

            return nameField.get(repo).toString();

        } catch (Exception e) {
            throw new DevbadgeException("Cannot get repo", e);
        }
    }

    private void log(String toLog) {
        System.out.println(System.currentTimeMillis() + ": " + toLog);
    }

    private GitHub connectToGitHub(String accessToken) throws IOException {
        return GitHub.connectUsingOAuth(accessToken);
    }

    private GitHub connectToGitHubForUser(String username) throws IOException {
        final User user = userRepository.findById(username)
                .orElseThrow(() -> new DevbadgeException("Invalid user"));

        return GitHub.connectUsingOAuth(user.getAccessToken());
    }

    public void getRepositories() throws IOException {
        System.out.println("LOgging in");
        final GitHub github = GitHub.connectUsingOAuth("740501057184d445914df4ab61c366a8e5ff2103");

        System.out.println("Getting repos");

        PagedIterable<GHRepository> repos = github.getMyself()
                .listRepositories(30, GHMyself.RepositoryListFilter.OWNER);

        repos.asList().forEach(repo -> System.out.println(repo.getName()));

        PagedIterable<GHCommit> commits = github.searchCommits().author(github.getMyself().getLogin()).list();

        while(commits.iterator().hasNext()) {
            commits
                    .iterator()
                    .nextPage()
                    .forEach(commit -> {
                        try {
                            System.out.println(commit.getCommitShortInfo().getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
        //repos.keySet().forEach(name -> System.out.println(name));
    }
}
