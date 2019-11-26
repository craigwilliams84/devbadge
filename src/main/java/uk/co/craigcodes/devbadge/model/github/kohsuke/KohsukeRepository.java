package uk.co.craigcodes.devbadge.model.github.kohsuke;

import org.kohsuke.github.GHRepository;
import uk.co.craigcodes.devbadge.model.github.GitHubRepository;
import uk.co.craigcodes.devbadge.model.github.GithubCommit;

import java.util.List;
import java.util.stream.Collectors;

public class KohsukeRepository implements GitHubRepository {

    private GHRepository wrapped;

    private KohsukeRepository(GHRepository repo) {
        this.wrapped = repo;
    }

    public static KohsukeRepository build(GHRepository repo) {
        return new KohsukeRepository(repo);
    }

    @Override
    public String getName() {
        return wrapped.getName();
    }

    @Override
    public String getFullName() {
        return wrapped.getFullName();
    }

    public List<GithubCommit> getCommitsByAuthor(String author) {
        return wrapped
                .queryCommits()
                .author(author)
                .list()
                .asList()
                .stream()
                .map(commit -> KohsukeCommit.build(commit))
                .filter(commit -> !(commit.getMessage().contains("Merge pull request #")
                        || commit.getMessage().contains("Merge remote-tracking branch")
                        || commit.getMessage().contains("Merge branch '")))
                .collect(Collectors.toList());
    }
}
