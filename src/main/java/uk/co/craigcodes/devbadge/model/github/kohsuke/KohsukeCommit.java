package uk.co.craigcodes.devbadge.model.github.kohsuke;

import org.kohsuke.github.GHCommit;
import uk.co.craigcodes.devbadge.DevbadgeException;
import uk.co.craigcodes.devbadge.model.github.GithubCommit;

import java.io.IOException;

public class KohsukeCommit implements GithubCommit {

    private GHCommit wrapped;

    private GHCommit.ShortInfo cachedShortInfo;

    private KohsukeCommit(GHCommit commit) {
        this.wrapped = commit;
    }

    public static KohsukeCommit build(GHCommit commit) {
        return new KohsukeCommit(commit);
    }

    public String getMessage() {
        return getShortInfo().getMessage();
    }

    public String getSHA1() {
        return wrapped.getSHA1();
    }

    private GHCommit.ShortInfo getShortInfo() {
        if (cachedShortInfo == null) {
            try {
                cachedShortInfo = wrapped.getCommitShortInfo();
            } catch (IOException e) {
                throw new DevbadgeException("Error when obtaining commit short info", e);
            }
        }

        return cachedShortInfo;
    }
}
