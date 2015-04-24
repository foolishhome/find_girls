package com.appmodel.db;

public class DBAppConst {
    public static final int app_version = 1;

    // app ID for different businesses
    public static final long IM_CHAT_APP_ID = 1;
    public static final long GROUP_CHAT_APP_ID = 2;
    public static final long IM_HISTORY_APP_ID = 3;
    public static final long USER_APP_ID = 4;
    public static final long IM_UNREAD_APP_ID = 5;
    public static final long IM_DB_APP_ID = 6;
    public static final long CONSULT_HISTORY_APP_ID = 7;
    public static final long CONSULT_DETAIL_APP_ID = 8;

    public static final long HOSPITAL_APP_ID = 8;
    public static final long OFFICE_APP_ID = 9;

    public static final long CONSULTATION_APP_ID = 10;

    public static final long CONSULTATION_CHANGED_APP_ID = 11;

    public static class DBImTaskType {
        public static final long INSERT = 1;
        public static final long QUERY = 2;
        public static final long UPDATE = 3;
        public static final long DELETE = 4;
        // others
    };

    public static class ConsultHistoryTaskType
    {
        public static final long INSERT = 1;
        public static final long PATCH_INSERT = 2;
        public static final long DELETE_ALL = 3;
        public static final long UPDATE = 4;
        public static final long UPDATE_BY_MESSAGE_ID_EQUAL_CLIENT_MESSAGE_ID = 5;
        public static final long QUERY_AFTER_BY_MESSAGE_ID_DESC = 6;
        public static final long QUERY_SPECIAL_DOCTOR_AND_PATIENT_CONSULT = 7;
        public static final long QUERY_SPECIAL_DOCTOR_AND_PATIENT_CONSULT_BY_PAGE = 8;
        public static final long DELETE_BY_MESSAGE_ID = 9;
    };

    public static class ConsultDetailTaskType
    {
        public static final long INSERT = 1;
        public static final long DELETE_ALL = 2;
        public static final long UPDATE = 3;
        public static final long QUERY = 4;
        public static final long QUERY_LIST = 5;
    };

    public static class ConsultationTaskType
    {
        public static final long INSERT = 1;
        public static final long PATCH_INSERT = 2;
        public static final long UPDATE = 3;
        public static final long QUERY_CONSULTATION_BY_CONSULTATION_ID = 4;
        public static final long PATCH_QUERY_CONSULTATION_BY_CONSULTATION_ID = 5;
    };

    public static class ConsultationChangedTaskType
    {
        public static final long INSERT = 1;
        public static final long BATCH_INSERT = 2;
        public static final long UPDATE = 3;
        public static final long QUERY_CONSULTATIONS_BY_TYPE = 4;
        public static final long DEL_CONSULTATIONS_BY_TYPE = 5;
    };

    public final static long DB_REQ_PRIORITY_LOW    = 1;
    public final static long DB_REQ_PRIORITY_NORMAL = 2;
    public final static long DB_REQ_PRIORITY_HIGH   = 3;

}