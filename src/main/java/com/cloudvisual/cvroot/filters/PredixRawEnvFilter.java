package com.cloudvisual.cvroot.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.cloudvisual.cvroot.filters.Constants.*;

@Slf4j
@Component
public class PredixRawEnvFilter {

    private String getVcapServicesJson(String raw) {
        String clean = raw.replaceAll(LINE_SEPERATOR_REGEX, "");
        int start = clean.indexOf('{');
        int finish = clean.contains("}{") ? (clean.indexOf("}{") + 1) : clean.length();
        return clean.substring(start, finish);
    }

    private String getVcapApplicationJson(String raw) {
        String clean = raw.replaceAll(LINE_SEPERATOR_REGEX, "");
        int start = clean.contains("}{") ? (clean.indexOf("}{") + 1) : 0;
        int finish = clean.lastIndexOf('}') + 1;
        return clean.substring(start, finish);
    }

    public ArrayList<PredixService> getVcapServicesListFromJson(String env) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            final String vcapServicesJson = getVcapServicesJson(env);
            JsonNode root = mapper.readTree(vcapServicesJson);
            JsonNode vcapServiceNode = root.get(VCAP_SERVICES);
            Iterator<String> services = vcapServiceNode.fieldNames();
            ArrayList<PredixService> allServiceLst = new ArrayList<>();
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
            return allServiceLst;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PredixApp getVcapApplicationFromJson(String env) {
        try {
            List<PredixService> services = getVcapServicesListFromJson(env);
            //group services instances by their name in the market(label)
            Map<String, List<PredixService>> boundServicesByLabel =
                    Optional.ofNullable(services).orElseGet(null).stream().collect(Collectors.groupingBy(PredixService::getLabel));

            final String vcapAppJson = getVcapApplicationJson(env);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(vcapAppJson);
            JsonNode vcapAppNode = root.get(VCAP_APPLICATION);
            //build application vcap
            PredixApp predixApp = PredixApp.builder()
                    .id(getStringValueFromJson(vcapAppNode, APP_ID))
                    .api(getStringValueFromJson(vcapAppNode, CF_API))
                    .name(getStringValueFromJson(vcapAppNode, APP_NAME))
                    .spaceId(getStringValueFromJson(vcapAppNode, SPACE_ID))
                    .spaceName(getStringValueFromJson(vcapAppNode, SPACE_NAME))
                    .version(getStringValueFromJson(vcapAppNode, APP_VERSION))
                    .uris(mapper.convertValue(vcapAppNode.get(APP_URIS), List.class))
                    .limits(mapper.convertValue(vcapAppNode.get(APP_LIMITS), Map.class))
                    .boundServices(boundServicesByLabel)
                    .build();
            log.info(predixApp.toString());
            return predixApp;
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

    private void printPredixServicesList(ArrayList<PredixService> allServiceLst) {
        allServiceLst.stream().map((service) ->
        {
            return Arrays.asList(service.getLabel(), service.getName(), service.getPlan()).toArray();
        }).forEach(tuple -> log.info(Arrays.toString(tuple)));
    }

}
