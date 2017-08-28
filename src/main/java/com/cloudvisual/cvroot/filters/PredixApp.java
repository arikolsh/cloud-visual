package com.cloudvisual.cvroot.filters;

import lombok.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PredixApp {
    private String id;
    private String name;
    private List<String> uris;
    private String version;
    private String api;
    private String spaceId;
    private String spaceName;
    private Map<String, String> limits;
    private Map<String, List<PredixService>> boundServices; //key= service, value= list of service instances
    //private String fullJson;
}
