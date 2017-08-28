package com.cloudvisual.cvroot.filters;

public class Constants {
    public static final String VCAP_SERVICES = "VCAP_SERVICES";
    public static final String VCAP_APPLICATION = "VCAP_APPLICATION";
    public static final String NAME = "name";
    public static final String PLAN = "plan";
    public static final String LINE_SEPERATOR_SPACE_REGEX = "\\r|\\n| "; //line breaks and spaces
    public static final String LINE_SEPERATOR_REGEX = "\\r|\\n"; //line breaks
    public static final String APP_ID = "application_id";
    public static final String APP_NAME = "application_name";
    public static final String SPACE_ID = "space_id";
    public static final String SPACE_NAME = "space_name";
    public static final String APP_VERSION = "version";
    public static final String APP_URIS = "application_uris";
    public static final String APP_LIMITS = "limits";
    public static final String CF_API = "cf_api";

    private Constants() {
    }

}
