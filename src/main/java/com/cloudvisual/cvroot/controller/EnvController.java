package com.cloudvisual.cvroot.controller;

import com.cloudvisual.cvroot.entities.PredixApp;
import com.cloudvisual.cvroot.entities.VCAP;
import com.cloudvisual.cvroot.parsers.PredixVcapParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ariko on 8/30/2017.
 */
@Slf4j
@RestController
@RequestMapping("/api/vcap")
public class EnvController {

    @Autowired
    PredixVcapParserService vcapService;

    @RequestMapping(value = "/parse", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<PredixApp> getAppEntity(@RequestBody VCAP vcap) {
        PredixApp app = vcapService.getApplicationFromVCAP(vcap);
        if (app == null) {
            return new ResponseEntity("error processing vcap", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(app, HttpStatus.OK);
    }

    @RequestMapping(value = "/raw-parse", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<PredixApp> getAppEntity(@RequestBody String raw) {
        PredixApp app = vcapService.getVcapApplicationFromRawVCAP(raw);
        if (app == null) {
            return new ResponseEntity("error processing vcap", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(app, HttpStatus.OK);
    }

}
