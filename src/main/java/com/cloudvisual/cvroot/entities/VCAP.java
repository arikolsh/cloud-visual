package com.cloudvisual.cvroot.entities;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by ariko on 8/30/2017.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VCAP { //cf env <app>
    private JsonNode VCAP_SERVICES;
    private JsonNode VCAP_APPLICATION;
}
