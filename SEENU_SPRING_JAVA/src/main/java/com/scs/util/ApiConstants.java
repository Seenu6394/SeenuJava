package com.scs.util;

public class ApiConstants {

	public static final String SERVICES_PATH = "D:/mobileServices";
	public static final String GET_USERS = "/admin/getUsers";
	public static final String GET_LOCALE = "/getLocale";
	public static final String GET_LANGUAGES = "/getLanguages";
	public static final String GET_KU_DETAILS = "/getKuDetails";
	public static final String GET_CONVERSATION_DETAILS = "/getConversationDetails";
	public static final String CUSTOMER = "/customer";
	public static final String KU = "/ku";
	public static final String KU_CAN = "/kuCan";
	public static final String KU_IMPORT = "/Createku";
	public static final String VALIDATE_IMPORT = "/validateImport";
	public static final String SETTINGS_IMPORT = "/settingsImport";
	public static final String SETTINGS_KEYWORDS = "/getSettings";
	
	public static final String CONVERSATION = "/conversation";
	public static final String INTENT = "/intent";
	public static final String PURIST_LOGIN = "/testLoginPurist";
	public static final String S4M_BOT_LOGIN = "/s4mBotLogin";
	public static final String IMPORT_KU = "/importNewKu";

	public static final String WORKFLOW = "/workFlow";
	public static final String WORKFLOW_SEQUENCE = "/workFlowSequence";
	public static final String UPDATE_TERMINAL_TYPE = "/updateTerminaltype";
	public static final String UPDATE_LINK = "/updateLink";
	public static final String DELETE_INTENT_MAPPING_BY_INT_ID = "/deleteIntentMappingByIntentId";
	public static final String DELETE_WORKFLOW_SEQUENCE_BY_INT_ID = "/deleteWorkFlowSequenceByIntentId";
	public static final String WORKFLOW_SEQUENCE_BY_INT_ID = "/workFlowSequencebyIntId";
	public static final String WORKFLOW_SEQUENCE_BY_WF_ID = "/workFlowSequencebyWorkflowId";
	
	
	public static final String WORKFLOW_BY_ENT_ID = "/workFlowbyEntID";
	public static final String WORKFLOW_BY_ACT_ID = "/workFlowbyActID";
	
	
	public static final String INTENT_RESPONSE = "/intentResponse";
	public static final String UPDATE_RESPONSE = "/updateResponse";
	public static final String MAPPING_REGEX = "/mappingRegEx";
	public static final String FORGOT_PASS = "/forgotPassword";
	public static final String RESET_PASS = "/resetPassword";
	public static final String CHANGE_PASS = "/changePassword";
	public static final String USER = "/admin/user";
	public static final String LANGUAGE = "/language";
	public static final String KEYWORD = "/keyword";
	public static final String FILLER_KEYWORD = "/fillerKeyword";
	public static final String PROJECT_KEYWORD = "/projectKeyword";
	public static final String DELETE_PROJECT_KEYWORD = "/deleteProjectKeyword";
	public static final String CANCEL_KEYWORD = "/cancelKeyword";
	public static final String TEST = "/test";
	public static final String ENTITY = "/entity";
	public static final String ENTITY_QUESTION = "/entityQuestion";
	public static final String REGULAR_EXPRESSION = "/regularExpression";
	public static final String SERVICE_ACTION = "/serviceAction";
	public static final String SERVICE_ACTION_INTENT = "/serviceActionIntent";
	public static final String REGEX_MAPPING = "/regexMapping";
	
	
	public static final String ACTION_ERROR_RESPONSE = "/deleteErrorResponse";
	public static final String SELECTION_LIST = "/selectionList";
	
	
	public static final String DELETE_RESPONSE_BY_ID = "/deleteResponse";
	public static final String ROLE = "/userRoles";

	public static final String GET_INTENT_DETAILS = "/getIntentDetails";
	public static final String GET_FILLER_KEYWORD = "/getFillerKeywords";
	public static final String GET_PROJECT_KEYWORD = "/getProjectKeywords";
	public static final String GET_CANCEL_KEYWORD = "/getCancelKeywords";
	public static final String GET_REGULAR_EXPRESSIONS = "/getRegularExpressions";
	public static final String GET_INTENT_REGULAR_EXPRESSIONS = "/getIntentRegularExpressions";
	public static final String GET_SERVICE_ACTIONS = "/getServiceActions";
	public static final String GET_SELECTION_LIST = "/getSelectionList";
	public static final String GET_ENTITY_DETAILS = "/getEntityDetails";
	public static final String GET_ENTITY_BY_KU = "/getEntityByKu";
	public static final String GET_INTENT_BY_KU = "/getIntentByKu";
	public static final String GET_RE_BY_KU = "/getReByKu";
	public static final String GET_SA_BY_KU = "/getSAByKu";
	
	
	public static final String GET_ENTITY_TYPES = "/getEntityTypes";
	public static final String GET_WORK_FLOW_DETAILS = "/getWorkFlowDetails";
	public static final String SCS_INIT_DIR = System.getProperty("scsprop.dir");
	public static final String SCS_CONFIG_PATH = "file:/" + SCS_INIT_DIR + "config.properties";

	public static final String KU_DROPDOWN_CODE = "002";
	
	public static final String CONVERSATION_DROPDOWN_CODE = "002";
	
	

	public static final String API = "/api";
	public static final String LOGOUT = "/signOut";
	public static final String LOGIN_SUCCESS = "/loginSuccess";
	public static final String LOGIN_FAILURE = "/loginFailure";

	public static final String BINDING_ERRORS = "++---- ERRORS---++";

	public static final String APP_SOURCE = "/api";

	public static final String API_RESPONSE = "data";

	public static final String API_REQUEST = "apiServicesRequest";
	public static final String API_RESP_ERR_CODE = "errorCode";
	public static final String API_RESP_ERR_MSG = "errorDescription";
	public static final String API_RESP_STATUS = "status";

	public static final String SUCCESS = "success";
	public static final String VERIFY = "verify";
	public static final String APP_MESSAGES_RESOURCE_BASENAME = "messages";
	public static final String WELCOME_SCS = "scs-api";

	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String EIDA_DATE_FORMAT = "yyyy/MM/dd";
	public static final String MERCHANT_SUMMARY_MONTH_FORMAT = "MMyyyy";
	public static final String HISTORY_MONTH_FORMAT = "MM/yyyy";
	public static final String HISTORY_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String MOBILE_SERVICES = "MOBILE_SERVICES_";

	public static final int SESSION_MAX_INTERVAL = 5 * 60;
	public static final int TOKEN_VALID_INTERVAL = 600;
	public static final int SESSION_MAX_CONCURRENT = 1;

	public static final int PWD_MIN_LENGTH = 8;
	
	public static final String LINE_SEPARATOR = "line.separator";
	
	public static final String ATTEMPT_FAILURE = "attempt_failure";
	public static final String LOGIN_FAILURE_STATUS = "login_failure_status";
	public static final int MAX_ATTEMPTS = 5;

	public static final String PURIST_USER_LOGINURL = "http://api.puristchat.com/login";
	public static final String PURIST_ADMIN_KEY = "ziULmsF8rdeqdWmvxAIt6p2J9XLpgWs";
	public static final String PURIST_PASSWORD = "12341234";
	public static final String PURIST_ADMIN_TOKEN_URL = "http://api.puristchat.com/admin/v1/oauth/token";
	public static final String PURIST_ADMIN_USERNAME = "admin@s4m.ae";
	public static final String PURIST_ADMIN_PASSWORD = "12341234";
	public static final String PURIST_CREATE_USER_URL = "http://api.puristchat.com/admin/v1/users";

	private ApiConstants() {

	}

}
