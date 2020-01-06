package uk.co.craigcodes.devbadge.model.github;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class BasicGithubRepository implements GitHubRepository {

    private String name;

    private String fullName;

    @Override
    public List<GithubCommit> getCommitsByAuthor(String author) {
        return null;
    }
}
