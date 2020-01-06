package uk.co.craigcodes.devbadge.model.github;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public interface GitHubRepository {

    String getName();

    String getFullName();

    @JsonIgnore
    List<GithubCommit> getCommitsByAuthor(String author);
}
