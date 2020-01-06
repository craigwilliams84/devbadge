package uk.co.craigcodes.devbadge.service;

import uk.co.craigcodes.devbadge.model.github.GitHubRepository;
import uk.co.craigcodes.devbadge.model.github.GithubUser;

import java.io.IOException;
import java.util.Set;

public interface GithubService {

    GithubUser getUser(String accessToken) throws IOException;

    GitHubRepository getRepository(String username, String repoName) throws IOException;

    Set<String> getCommittedRepositoryNames(String username) throws IOException;
}
