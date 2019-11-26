package uk.co.craigcodes.devbadge.configuration;

import io.ipfs.api.IPFS;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IPFSConfiguration {

    @Bean
    public IPFS ipfs() {
        return new IPFS("ipfs.infura.io", 5001, "/api/v0/", true);
    }
}
