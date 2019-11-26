package uk.co.craigcodes.devbadge.model.github;

import java.util.List;

public interface GitHubRepository {

    String getName();

    String getFullName();

    List<GithubCommit> getCommitsByAuthor(String author);
}
