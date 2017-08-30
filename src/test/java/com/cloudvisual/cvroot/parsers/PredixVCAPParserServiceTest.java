package com.cloudvisual.cvroot.parsers;

import com.cloudvisual.cvroot.entities.PredixApp;
import com.cloudvisual.cvroot.entities.PredixService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class PredixVCAPParserServiceTest {

    PredixVcapParserService predixRawFilter = new PredixVcapParserService();

    @Test
    public void getServicesListFromJson() throws IOException {
        String vcap = FileUtils.readFileToString(new File("vcap.json"), "utf-8");
        //ArrayList<PredixService> json = predixRawFilter.getServicesListFromRawVCAP(vcap);
      //  predixRawFilter.getVcapApplicationFromJson(vcap);
       // log.info(lst.get(0).toString());
    }

    @Test
    public void getVcapApplicationFromJson() throws IOException {
        String vcap = FileUtils.readFileToString(new File("vcapNoservices.json"), "utf-8");
        PredixApp json = predixRawFilter.getVcapApplicationFromRawVCAP(vcap);
        //  predixRawFilter.getVcapApplicationFromJson(vcap);
        // log.info(lst.get(0).toString());
    }

}
