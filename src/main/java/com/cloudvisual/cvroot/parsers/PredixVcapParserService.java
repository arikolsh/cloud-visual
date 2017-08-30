package com.cloudvisual.cvroot.parsers;

import com.cloudvisual.cvroot.entities.PredixApp;
import com.cloudvisual.cvroot.entities.PredixService;
import com.cloudvisual.cvroot.entities.VCAP;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.cloudvisual.cvroot.parsers.ParserConstants.*;

@Slf4j
@Service
public class PredixVcapParserService {

    //////////////////// handling VCAP object ////////////////////

    private ArrayList<PredixService> getServicesListFromVCAP(VCAP vcap) {
        ArrayList<PredixService> allServiceLst = new ArrayList<>();
        if (vcap == null) {
            return allServiceLst;
        }
        try {
            JsonNode vcapServiceNode = vcap.getVCAP_SERVICES();
            if (vcapServiceNode == null) {
                return allServiceLst;
            }
            Iterator<String> services = vcapServiceNode.fieldNames();
            while (services.hasNext()) {
                String service = services.next();
                JsonNode currServiceNode = vcapServiceNode.get(service);
                if (currServiceNode.isArray()) {
                    for (final JsonNode objNode : currServiceNode) {
                        //add new predix service to list
                        allServiceLst.add(PredixService.builder()
                                .label(service) //service name
                                .name(objNode.get(NAME).toString()) //instance name
                                .plan(objNode.get(PLAN).toString()) //plan
                                // .fullJson(currServiceNode.toString()) //full json
                                .build());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allServiceLst;
    }

    public PredixApp getApplicationFromVCAP(VCAP vcap) {
        if (vcap == null || vcap.getVCAP_APPLICATION() == null) {
            return null;
        }
        try {
            List<PredixService> services = getServicesListFromVCAP(vcap);
            //group services instances by their name in the market(label)
            Map<String, List<PredixService>> boundServicesByLabel =
                    services.stream().collect(Collectors.groupingBy(PredixService::getLabel));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode vcapAppNode = vcap.getVCAP_APPLICATION();
            //build application vcap
            PredixApp predixApp = PredixApp.builder()
                    .id(getStringValueFromJson(vcapAppNode, APP_ID))
                    //.api(getStringValueFromJson(vcapAppNode, CF_API))
                    .name(getStringValueFromJson(vcapAppNode, APP_NAME))
                    //.spaceId(getStringValueFromJson(vcapAppNode, SPACE_ID))
                    //.spaceName(getStringValueFromJson(vcapAppNode, SPACE_NAME))
                    //.version(getStringValueFromJson(vcapAppNode, APP_VERSION))
                    .uris(mapper.convertValue(vcapAppNode.get(APP_URIS), List.class))
                    //.limits(mapper.convertValue(vcapAppNode.get(APP_LIMITS), Map.class))
                    .boundServices(boundServicesByLabel)
                    .build();
            log.info(predixApp.toString());
            return predixApp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    ////////// handling raw env text from cf env <app name> //////////

    private String getVcapServicesJsonStrFromRawEnv(String raw) {
        if (raw == null) {
            return null;
        }
        String clean = raw.replaceAll(LINE_SEPERATOR_REGEX, "");
        int start = clean.indexOf('{');
        int finish = clean.contains("}{") ? (clean.indexOf("}{") + 1) : clean.length();
        return clean.substring(start, finish);
    }

    private String getVcapApplicationJsonStrFromRawEnv(String raw) {
        if (raw == null) {
            return null;
        }
        String clean = raw.replaceAll(LINE_SEPERATOR_REGEX, "");
        int start = clean.contains("}{") ? (clean.indexOf("}{") + 1) : 0;
        int finish = clean.lastIndexOf('}') + 1;
        return clean.substring(start, finish);
    }

    private ArrayList<PredixService> getServicesListFromRawVCAP(String raw) {
        ArrayList<PredixService> allServiceLst = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        final String vcapServicesJson = getVcapServicesJsonStrFromRawEnv(raw);
        if (vcapServicesJson == null) {
            return allServiceLst;
        }
        try {
            JsonNode root = mapper.readTree(vcapServicesJson);
            JsonNode vcapServiceNode = root.get(VCAP_SERVICES);
            allServiceLst = getServicesListFromVCAP(VCAP.builder().VCAP_SERVICES(vcapServiceNode).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allServiceLst;
    }

    public PredixApp getVcapApplicationFromRawVCAP(String raw) {
        try {
            final String vcapServicesJson = getVcapServicesJsonStrFromRawEnv(raw);
            if (vcapServicesJson == null) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode vcapServiceNode = mapper.readTree(vcapServicesJson).get(VCAP_SERVICES);
            final String vcapAppJson = getVcapApplicationJsonStrFromRawEnv(raw);
            if (vcapAppJson == null) {
                return null;
            }
            JsonNode vcapAppNode = mapper.readTree(vcapAppJson).get(VCAP_APPLICATION);
            return getApplicationFromVCAP(VCAP.builder().VCAP_APPLICATION(vcapAppNode).VCAP_SERVICES(vcapServiceNode).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getStringValueFromJson(JsonNode node, String field) {
        JsonNode keyNode = node.get(field);
        String value = keyNode != null ? keyNode.toString() : null;
        return value;
    }

/*    private List<String> getStringListFromJson(ObjectMapper mapper, JsonNode node, String field) throws IOException {
        JsonNode keyNode = node.get(field);
        return mapper.readValue(keyNode.toString(), new TypeReference<List<String>>() {
        });
    }*/

/*    private void printPredixServicesList(ArrayList<PredixService> allServiceLst) {
        allServiceLst.stream().map((service) ->
        {
            return Arrays.asList(service.getLabel(), service.getName(), service.getPlan()).toArray();
        }).forEach(tuple -> log.info(Arrays.toString(tuple)));
    }*/

}
