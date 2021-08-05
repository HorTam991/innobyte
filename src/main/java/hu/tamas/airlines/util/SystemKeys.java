package hu.tamas.airlines.util;

public class SystemKeys {

    public static class Spring {
        public static final String DEFAULT_CHARSET = "UTF-8";
        public static final String PRODUCES_JSON_UTF8 = "application/json; charset=UTF-8";
        public static final String MYME_TYPE_PDF = "application/pdf";
        public static final String MULTIPART_CONTENT_TYPE = "content-type=multipart/form-data";
    }

    public static class ResponseTexts {
        public static final String ERROR_COMPILING_RESPONSE = "Error compiling response!";
        public static final String SUCCESS_DELETE = "Delete successful!";

        public static final String SUCCESS_UPLOAD = "Upload successful!";
        public static final String UPLOAD_FAILED = "Upload failed!";
        public static final String UPLOAD_FAILED_MISSING_FILE = "Upload failed, missing file!";

        public static final String SAME_CITIES_ERROR = "Starting and destination cities cannot be the same!";
    }

    public static final String FILE_SEPARATOR = ";";

}
