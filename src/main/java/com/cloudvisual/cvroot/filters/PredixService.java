package com.cloudvisual.cvroot.filters;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PredixService{
    private String label;
    private String name;
    private String plan;
  //  private String fullJson;
}
