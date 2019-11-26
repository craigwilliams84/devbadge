package uk.co.craigcodes.devbadge.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.craigcodes.devbadge.service.DevbadgeService;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class RestEndpoint {

    private DevbadgeService service;

//    @RequestMapping(value="/test")
//    public String test() throws IOException {
//        System.out.println("TEST");
//        service.getRepositories();
//
//        return "blah";
//    }

    @RequestMapping(value="/mint")
    public String mint(@RequestParam String author, @RequestParam String repoName) throws IOException {
        System.out.println(repoName);
        service.mintBadgesForRepository(author, repoName);

        return "blah";
    }
}
