package com.cloudvisual.cvroot.entities;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PredixService {
    private String label; //name of service in market place
    private String name; //instance name
    private String plan; //plan
    //  private String fullJson;
}
