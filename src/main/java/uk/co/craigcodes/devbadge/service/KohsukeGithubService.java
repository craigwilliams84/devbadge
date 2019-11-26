package uk.co.craigcodes.devbadge.service;

import org.kohsuke.github.*;
import org.springframework.stereotype.Service;
import uk.co.craigcodes.devbadge.model.github.GitHubRepository;
import uk.co.craigcodes.devbadge.model.github.kohsuke.KohsukeRepository;

import java.io.IOException;
import java.util.Map;

@Service
public class KohsukeGithubService implements GithubService {

    public GitHubRepository getRepository(String repoName) throws IOException {
        final GHRepository repo = connectToGitHub().getRepository(repoName);

        return KohsukeRepository.build(repo);
    }

    private GitHub connectToGitHub() throws IOException {
        return GitHub.connectUsingOAuth("740501057184d445914df4ab61c366a8e5ff2103");
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
