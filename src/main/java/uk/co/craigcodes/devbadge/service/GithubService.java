package uk.co.craigcodes.devbadge.service;

import uk.co.craigcodes.devbadge.model.github.GitHubRepository;

import java.io.IOException;

public interface GithubService {

    GitHubRepository getRepository(String repoName) throws IOException;
}
