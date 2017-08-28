package com.cloudvisual.cvroot.filters;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class PredixRawEnvFilterTest {

    PredixRawEnvFilter predixRawFilter = new PredixRawEnvFilter();

    @Test
    public void getServicesListFromJson() throws IOException {
        String vcap = FileUtils.readFileToString(new File("vcap.json"), "utf-8");
        ArrayList<PredixService> json = predixRawFilter.getVcapServicesListFromJson(vcap);
      //  predixRawFilter.getVcapApplicationFromJson(vcap);
       // log.info(lst.get(0).toString());
    }

    @Test
    public void getVcapApplicationFromJson() throws IOException {
        String vcap = FileUtils.readFileToString(new File("vcapNoservices.json"), "utf-8");
        PredixApp json = predixRawFilter.getVcapApplicationFromJson(vcap);
        //  predixRawFilter.getVcapApplicationFromJson(vcap);
        // log.info(lst.get(0).toString());
    }

}
