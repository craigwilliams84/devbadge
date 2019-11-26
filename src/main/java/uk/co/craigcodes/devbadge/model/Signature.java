package uk.co.craigcodes.devbadge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An eliptic curve signature.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Signature {
    private Integer v;
    private String r;
    private String s;
}