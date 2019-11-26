package uk.co.craigcodes.devbadge.model.badge.nft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Erc721Metadata {

    private String name;

    private String description;

    @JsonProperty("external_url")
    private String externalUrl;

    @JsonProperty("image")
    private String imageUrl;

    private List<Map<String, Object>> attributes;
}
