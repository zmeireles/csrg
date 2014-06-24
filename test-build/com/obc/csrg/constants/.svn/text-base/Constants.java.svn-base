package com.obc.csrg.constants;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.obc.csrg.util.TimeUtil;

/**
 * This class holds constant values used throughout the system.
 *
 * <p>
 * Its intention is to have a "one thing, in one place, with one name".
 *
 * <p> Copyright (c) May 18, 2007 Nano Network Engines, Inc. All rights reserved.
 *     Nano Network Engines PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author jmeireles
 * @version $Id: Constants.java,v 1.3 2009/12/20 16:56:52 jmeireles Exp $
 * @since May 18, 2007 5:24:02 PM
 */

public final class Constants implements Serializable {

	private static final long serialVersionUID = 200912282028L;
	
    private Constants() { }
    
    // This variable will be expanded out to the CVS tag value when QA creates the build.  It
    // will be the value that is stored in the 'description' column of the version_details table.
    public static final String TAG_NAME = "$Name:  $";
    
    public static final String CSRG_HOME = "C:/csrg/";
    public static final String CSRG_DIR_IMPORT = "import/";
    public static final String CSRG_DIR_DOCS = "docs/";
    public static final String CSRG_DIR_TMP = "tmp/";
    public static final String CSRG_PERSON_DATA_PROTECTION_DOC = "personDataProtectionDoc";
    
    public static final Object[]   EMPTY_OBJECTS   = new Object[0];
    public static final String[]   EMPTY_STRINGS   = new String[0];
    
    public static final String OS_NAME  = System.getProperty("os.name", "");
    
    public static final String CHAT_URL				="127.0.0.1:80/ochs/";
    
    public static final String ADMIN_LOGIN_NAME = "admin";
    public static final String ADMIN_PWD = "csrg";
    public static final String SYS_ADMIN_LOGIN_NAME = "sysadmin";
    public static final String SYS_ADMIN_PWD = "syscsrg";
    public static final QuestionRecoverPasswordEnum ADMIN_QRP_PERGUNTA = QuestionRecoverPasswordEnum.cidadeNascimento;
    public static final String ADMIN_QRP_RESPOSTA = "csrgwedlqwejifnewrgmkwçle+-,.12";
    public static final String ADMIN_NAME = "Administrador";
    public static final String ADMIN_EMAIL = "admin@csrg.com";
    public static final String SYS_ADMIN_NAME = "Administrador de sistema";
    public static final String SYS_ADMIN_EMAIL = "duarte.romao@objectconnection.pt";
    
    // expressoes regulares utilizadas na validacao do hibernate
    public static final String REGEXP_EMAIL = "^()|([\\w\\.]+@[^\\x00-\\x1F\\x7F \\t\\n\\x0B\\f\\r\\!\"#\\$%\\&'\\(\\)\\*\\+\\,/:;\\<\\=\\>\\?@\\[\\]\\^_\\`\\{\\|\\}~]+\\.[\\w]+)$";
    public static final String REGEXP_PHONE_NUMBER = "^()|(\\d{7,})$";
    public static final String REGEXP_FAX = "^()|(\\d{7,})$";
    public static final String REGEXP_COUNTRY_PHONE_PREFIX = "^()|((\\x2B)?\\d{1,})$";
    public static final String REGEXP_USERNAME = "^\\w{5,20}$";
    public static final String REGEXP_NIPC = "^()|(\\d{1,})$";
    public static final String REGEXP_NIF = "^()|(\\d{1,})$";
    
    // Any "PROPERTY_" constant is used as a key into either the system or user properties files.
    public static final String PROPERTY_BROADCAST_PORT          = "BROADCAST_HANDLER_ID";
    public static final String PROPERTY_BUFER_SIZE              = "READ_BUFFER_BYTE_SIZE";
    public static final String PROPERTY_CONFIGURATION_PORT      = "CONF_HANDLER_ID";
    public static final String PROPERTY_DT_FORMAT               = "DT_FORMAT";
    public static final String PROPERTY_LOGVIEW_MESSAGES_LIMIT  = "LOGVIEW_MESSAGES_LIMIT";
    public static final String PROPERTY_LOGVIEW_SHOW_DEBUG      = "LOGVIEW_SHOW_DEBUG";
    public static final String PROPERTY_LOGVIEW_SHOW_ERROR      = "LOGVIEW_SHOW_ERROR";
    public static final String PROPERTY_LOGVIEW_SHOW_FATAL      = "LOGVIEW_SHOW_FATAL";
    public static final String PROPERTY_LOGVIEW_SHOW_INFO       = "LOGVIEW_SHOW_INFO";
    public static final String PROPERTY_LOGVIEW_SHOW_WARN       = "LOGVIEW_SHOW_WARN";
    public static final String PROPERTY_MAX_TABLE_ROWS          = "MAX_TABLE_ROWS";
    
    public static final long AUTO_LOGOFF_AFTER_MINS = TimeUtil.MILLIS_IN_1_MINUTE;
    
    public static final String JMS_CONNECTION_FACTORY = "ConnectionFactory";
    
    
    public static final String HIBERNATE_KEY_DRIVER   = "hibernate.connection.driver_class";
    public static final String HIBERNATE_KEY_URL      = "hibernate.connection.url";
    public static final String HIBERNATE_KEY_USERNAME = "hibernate.connection.username";
    public static final String HIBERNATE_KEY_PASSWORD = "hibernate.connection.password";
    
    
    public static final String GUI_DATE_AND_TIME_FORMAT_PATTERN = "dd/MM/yyyy HH:mm:ss";
    
    public static final DateFormat dateAndTimeFormat = new SimpleDateFormat(Constants.GUI_DATE_AND_TIME_FORMAT_PATTERN);
    
    public static final String G_NORMAL_MAP		= "G_NORMAL_MAP";
    public static final String G_SATELLITE_MAP	= "G_SATELLITE_MAP";
    public static final String G_HYBRID_MAP		= "G_HYBRID_MAP";
    public static final String G_DEFAULT_MAP	= "G_SATELLITE_MAP";
    
    public static final String MSG_ENTIDADE_PATRONAL			= "Entidade_Patronal";
    public static final String MSG_ENTIDADE_GESTORA				= "Entidade_Gestora";
    public static final String MSG_ENTIDADE_EMPREGO				= "Entidade_Emprego";
    public static final String MSG_ENTIDADE_FORMACAO			= "Entidade_Formacao";
    public static final String MSG_ENTIDADE_EMPREGO_FORMACAO	= "Entidade_Emprego_Formacao";
    public static final String MSG_ENTIDADE_PARCEIRA			= "Entidade_Parceira";
    public static final String MSG_ENTIDADE_FINANCIADORA		= "Entidade_Financiadora";
    public static final String MSG_ENTIDADE_NOSI				= "Entidade_Nosi";
    
    public static final int OFERTA_EMPREGO					= 0;
    public static final int OFERTA_FORMACAO					= 1;
    public static final int OFERTA_ESTAGIO_PROFISSIONAL		= 2;
    
    public static final String[] PROFILES_DIR = {"user","admin","public_user",
    	"person","content_manager","access_manager","online_support","sys_admin"};
    
    public static final String TIPO_DADO_NUMERICO_LONGO			="TD_NUMERICO_LONGO";
    public static final String TIPO_DADO_DECIMAL				="TD_DECIMAL";
    public static final String TIPO_DADO_DATA					="TD_DATA";
    public static final String TIPO_DADO_IMAGEM					="TD_IMAGEM";
    public static final String TIPO_DADO_TEXTO					="TD_TEXTO";
    public static final String TIPO_DADO_BOOL					="TD_BOOL";
    public static final String TIPO_DADO_FICHEIRO				="TD_FICHEIRO";
    public static final String TIPO_DADO_NUMERICO_INTEIRO		="TD_NUMERICO_INTEIRO";
    //Objectos de certificação
    public static final String OBJ_CRT_UTENTE					="OC_UTENTE";
    public static final String OBJ_CRT_FORMADOR					="OC_FORMADOR";
    public static final String OBJ_CRT_OFERTA					="OC_OFERTA";
    public static final String OBJ_CRT_FORMACAO					="OC_FORMACAO";
    public static final String OBJ_CRT_FORMACAO_INDIVIDUO		="OC_FORMACAO_INDIVIDUO";
    public static final String OBJ_CRT_INDIVIDUO				="OC_INDIVIDUO";
    
    //operadores sql
    public static final String OP_SQL_EQUAL					="OP_SQL_EQUAL";
    public static final String OP_SQL_GREATER				="OP_SQL_GREATER";
    public static final String OP_SQL_GREATER_OR_EQUAL		="OP_SQL_GREATER_OR_EQUAL";
    public static final String OP_SQL_LESS					="OP_SQL_LESS";
    public static final String OP_SQL_LESS_OR_EQUAL			="OP_SQL_LESS_OR_EQUAL";
    public static final String OP_SQL_NOT_EQUAL				="OP_SQL_NOT_EQUAL";
    public static final String OP_SQL_LIKE					="OP_SQL_LIKE";
    public static final String OP_SQL_NOT					="OP_SQL_NOT";
    
    public static final String PROFILE_USER							="PROFILE_USER";
    public static final String PROFILE_ADMIN						="PROFILE_ADMIN";
    public static final String PROFILE_PUBLIC_USER					="PROFILE_PUBLIC_USER";
    public static final String PROFILE_PERSON						="PROFILE_PERSON";
    public static final String PROFILE_CONTENT_MANAGER				="PROFILE_CONTENT_MANAGER";
    public static final String PROFILE_ACCESS_MANAGER				="PROFILE_ACCESS_MANAGER";
    public static final String PROFILE_ONLINE_SUPPORT				="PROFILE_ONLINE_SUPPORT";
    public static final String PROFILE_NOT_LOGGED_IN				="PROFILE_NOT_LOGGED_IN";
    public static final String PROFILE_SYS_ADMIN					="PROFILE_SYS_ADMIN";
    
    public static final String TIPO_FILTRO_LIVRE_PESQUISA			="TIPO_FILTRO_LIVRE_PESQUISA";
    public static final String TIPO_FILTRO_LIVRE_ALERTA				="TIPO_FILTRO_LIVRE_ALERTA";
    public static final String TIPO_FILTRO_LIVRE_CRITERIO			="TIPO_FILTRO_LIVRE_CRITERIO";
    
    public static final String ACTION_REPRESENTAR_UTENTE			="ACTION_REPRESENTAR_UTENTE";
    public static final String ACTION_OFERTAS_EMPREGO_ELEGIVEIS		="ACTION_OFERTAS_EMPREGO_ELEGIVEIS";
    public static final String ACTION_OFERTAS_FORMACAO_ELEGIVEIS	="ACTION_OFERTAS_FORMACAO_ELEGIVEIS";
    public static final String ACTION_REPRESENTAR_ENTIDADE_PATRONAL	="ACTION_REPRESENTAR_ENTIDADE_PATRONAL";
    
    //estado civil
    public static final String CIVIL_STATE_MARRRIED					="CIVIL_STATE_MARRRIED";
    public static final String CIVIL_STATE_SINGLE					="CIVIL_STATE_SINGLE";
    public static final String CIVIL_STATE_OTHER					="CIVIL_STATE_OTHER";
    
    //genero
    public static final String GENDER_MALE							="GENDER_MALE";
    public static final String GENDER_FEMALE						="GENDER_FEMALE";
    
    //tipo de accao do percurso
    public static final String PERCURSO_CANDIDATURA					="PERCURSO_CANDIDATURA";
    public static final String PERCURSO_COLOCACAO					="PERCURSO_COLOCACAO";
    
    //noticias
    public static final String	NOTICIA_LIST_TEXT_SIZE				="NOTICIA_LIST_TEXT_SIZE";
    
    //Política de atribuição de centros de emprego
    public static final String POLITICA_ATRIBUICAO_EE_LIVRE					="POLITICA_ATRIBUICAO_EE_LIVRE";
    public static final String POLITICA_ATRIBUICAO_EE_ASC					="POLITICA_ATRIBUICAO_EE_ASC";
    public static final String POLITICA_ATRIBUICAO_EE_DESC					="POLITICA_ATRIBUICAO_EE_DESC";
    public static final String POLITICA_ATRIBUICAO_EE_ASC_DESC				="POLITICA_ATRIBUICAO_EE_ASC_DESC";
    public static final String POLITICA_ATRIBUICAO_EE_MAIS_PROXIMO			="POLITICA_ATRIBUICAO_EE_MAIS_PROXIMO";
    
    //situação do utente face ao emprego
    public static final String SITUACAO_UTENTE_DESEMPREGADO_PRIMEIRO_EMPREGO	="SITUACAO_UTENTE_DESEMPREGADO_PRIMEIRO_EMPREGO";
    public static final String SITUACAO_UTENTE_DESEMPREGADO						="SITUACAO_UTENTE_DESEMPREGADO";
    public static final String SITUACAO_UTENTE_EMPREGADO						="SITUACAO_UTENTE_EMPREGADO";
    
    //tipo da area geografica
    public static final String GA_WORLD											="GA_WORLD";
    public static final String GA_COUNTRY										="GA_COUNTRY";
    public static final String GA_REGION										="GA_REGION";
    public static final String GA_ISLAND_DISTRICT								="GA_ISLAND_DISTRICT";
    public static final String GA_COUNTY										="GA_COUNTY";
    public static final String GA_FREGUESIA										="GA_FREGUESIA";
    public static final String GA_PLACE											="GA_PLACE";
    
    //tipo de formacao
    public static final String TIPO_FORMACAO_ACADEMICA							="TIPO_FORMACAO_ACADEMICA";
    public static final String TIPO_FORMACAO_PROFISSIONAL						="TIPO_FORMACAO_PROFISSIONAL";
    
	//questao para a recuperacao da password
    public static final String QUESTION_RECOVER_PASSWORD_CIDADE_NASCIMENTO		="QUESTION_RECOVER_PASSWORD_CIDADE_NASCIMENTO";
    public static final String QUESTION_RECOVER_PASSWORD_NOME_CONJUGE			="QUESTION_RECOVER_PASSWORD_NOME_CONJUGE";
    public static final String QUESTION_RECOVER_PASSWORD_NOME_FILHO				="QUESTION_RECOVER_PASSWORD_NOME_FILHO";
    public static final String QUESTION_RECOVER_PASSWORD_COR_CARRO				="QUESTION_RECOVER_PASSWORD_COR_CARRO";
    public static final String QUESTION_RECOVER_PASSWORD_MES_NASCIMENTO			="QUESTION_RECOVER_PASSWORD_MES_NASCIMENTO";
    public static final String QUESTION_RECOVER_PASSWORD_NOME_SOLTEIRA_MAE		="QUESTION_RECOVER_PASSWORD_NOME_SOLTEIRA_MAE";
    public static final String QUESTION_RECOVER_PASSWORD_ANIMAL_PREFERIDO		="QUESTION_RECOVER_PASSWORD_ANIMAL_PREFERIDO";
    public static final String QUESTION_RECOVER_PASSWORD_COR_FAVORITA			="QUESTION_RECOVER_PASSWORD_COR_FAVORITA";
    public static final String QUESTION_RECOVER_PASSWORD_EQUIPA_FUTEBOL			="QUESTION_RECOVER_PASSWORD_EQUIPA_FUTEBOL";
    
    //tipo de contrato de trabalho
    public static final String TIPO_CONTRATO_TRABALHO_EFECTIVO					="TIPO_CONTRATO_TRABALHO_EFECTIVO";
    public static final String TIPO_CONTRATO_TRABALHO_A_TERMO					="TIPO_CONTRATO_TRABALHO_A_TERMO";
    public static final String TIPO_CONTRATO_TRABALHO_OUTROS					="TIPO_CONTRATO_TRABALHO_OUTROS";
    
    //classificacao do idioma
    public static final String LINGUA_CLASSIFICACAO_NAO_APLICAVEL				="LINGUA_CLASSIFICACAO_NAO_APLICAVEL";
    public static final String LINGUA_CLASSIFICACAO_MUITO_FRACO					="LINGUA_CLASSIFICACAO_MUITO_FRACO";
    public static final String LINGUA_CLASSIFICACAO_FRACO						="LINGUA_CLASSIFICACAO_FRACO";
    public static final String LINGUA_CLASSIFICACAO_SUFICIENTE					="LINGUA_CLASSIFICACAO_SUFICIENTE";
    public static final String LINGUA_CLASSIFICACAO_BOM							="LINGUA_CLASSIFICACAO_BOM";
    public static final String LINGUA_CLASSIFICACAO_MUITO_BOM					="LINGUA_CLASSIFICACAO_MUITO_BOM";
    
    //valor boolean
    public static final String BOOLEAN_VALUE_INDIFFERENT						="BOOLEAN_VALUE_INDIFFERENT";
    public static final String BOOLEAN_VALUE_YES								="BOOLEAN_VALUE_YES";
    public static final String BOOLEAN_VALUE_NO									="BOOLEAN_VALUE_NO";
    
    //contrato de trabalho a tempo
    public static final String TRABALHO_A_TEMPO_COMPLETO						="TRABALHO_A_TEMPO_COMPLETO";
    public static final String TRABALHO_A_TEMPO_PARCIAL							="TRABALHO_A_TEMPO_PARCIAL";
    public static final String TRABALHO_A_TEMPO_OUTROS							="TRABALHO_A_TEMPO_OUTROS";
    
    //tipo de periodo temporal
    public static final String PERIOD_TYPE_SECOND								="PERIOD_TYPE_SECOND";
    public static final String PERIOD_TYPE_MINUTE								="PERIOD_TYPE_MINUTE";
    public static final String PERIOD_TYPE_HOUR									="PERIOD_TYPE_HOUR";
    public static final String PERIOD_TYPE_DAY									="PERIOD_TYPE_DAY";
    public static final String PERIOD_TYPE_WEEK									="PERIOD_TYPE_WEEK";
    public static final String PERIOD_TYPE_MONTH								="PERIOD_TYPE_MONTH";
    public static final String PERIOD_TYPE_YEAR									="PERIOD_TYPE_YEAR";
    
    //contextos de keywords
    public static final String CT_KW_AREA_GEOGRAFICA							="CT_KW_AREA_GEOGRAFICA";
    public static final String CT_KW_IDIOMA										="CT_KW_IDIOMA";
    public static final String CT_KW_CNP										="CT_KW_CNP";
    public static final String CT_KW_DADOS_DIRECTOS								="CT_KW_DADOS_DIRECTOS";
    public static final String CT_KW_CONHECIMENTOS_PROFISSIONAIS				="CT_KW_CONHECIMENTOS_PROFISSIONAIS";
    public static final String CT_KW_PROFISSAO									="CT_KW_PROFISSAO";
    public static final String CT_KW_FORMACAO									="CT_KW_FORMACAO";
    public static final String CT_KW_CAE										="CT_KW_CAE";
    public static final String CT_KW_NIVEL_ACADEMICO							="CT_KW_NIVEL_ACADEMICO";
    public static final String CT_KW_NIVEL_FORMACAO								="CT_KW_NIVEL_FORMACAO";
    public static final String CT_KW_DADOS_DISCRETOS							="CT_KW_DADOS_DISCRETOS";
    public static final String CT_KW_HORARIO_TRABALHO							="CT_KW_HORARIO_TRABALHO";
    public static final String CT_KW_TIPO_OFERTA								="CT_KW_TIPO_OFERTA";
    public static final String CT_KW_IDADE										="CT_KW_IDADE";
    public static final String CT_KW_MESES_EXPERIENCIA							="CT_KW_MESES_EXPERIENCIA";
    
    public static final String SENTENCE_DIVIDER									=";";	
    
    //drag and drop constants
    public static final String DND_AVAILABLE_ITEMS_DROP_BOX							="availableItemsDropBox";
    public static final String DND_SELECTED_ITEMS_DROP_BOX							="selectedItemsDropBox";
    public static final String DND_EMPTY_AVAILABLE_ITEMS_DROP_BOX					="emptyAvailableItemsDropBox";
    public static final String DND_EMPTY_SELECTED_ITEMS_DROP_BOX					="emptySelectedItemsDropBox";
    
    //date formats
    //public static final String DT_FORMAT_GLOBAL									="yyyy-mm-dd hh:mm:ss";
    public static final String DT_FORMAT_GLOBAL									="dd-MM-yyyy";
    public static final String DT_FORMAT_CSRG									="yyyy-MM-dd hh:mm:dd";
    
    //estado civil
    public static final String NOTIFICATION_NONE								="NOTIFICATION_NONE";
    public static final String NOTIFICATION_EMAIL								="NOTIFICATION_EMAIL";
    public static final String NOTIFICATION_SMS									="NOTIFICATION_SMS";
    public static final String NOTIFICATION_NEWS								="NOTIFICATION_NEWS";
    public static final String NOTIFICATION_FAX									="NOTIFICATION_FAX";
    
    //Search object type
    public static final String SOT_MODEL										="SOT_MODEL";
    public static final String SOT_XHTML_PAGE									="SOT_XHTML_PAGE";
    public static final String SOT_HTML_PAGE									="SOT_HTML_PAGE";
    
    public static final String DEFAULT_LOCALE									="pt";
    
    //models enumerator
    public static final String MODEL_ALL										="MODEL_ALL";
    public static final String MODEL_USERS										="MODEL_USERS";
    public static final String MODEL_DEPARTMENTS								="MODEL_DEPARTMENTS";
    public static final String MODEL_GEOGRAPHIC_AREAS							="MODEL_GEOGRAPHIC_AREAS";
    public static final String MODEL_NEWS										="MODEL_NEWS";
    public static final String MODEL_NOTIFICATIONS								="MODEL_NOTIFICATIONS";
    public static final String MODEL_COMPOSED_NOTIFICATIONS						="MODEL_COMPOSED_NOTIFICATIONS";
    public static final String MODEL_PERSONS									="MODEL_PERSONS";
    public static final String MODEL_PROF_CATEGORIES							="MODEL_PROF_CATEGORIES";
    public static final String MODEL_SERVICE_AREAS								="MODEL_SERVICE_AREAS";
    public static final String MODEL_SERVICE_CLASSIFICATIONS					="MODEL_SERVICE_CLASSIFICATIONS";
    public static final String MODEL_VISUAL_ITEMS								="MODEL_VISUAL_ITEMS";
    public static final String MODEL_WEB_PAGES									="MODEL_WEB_PAGES";
    public static final String MODEL_DATA_CHANGE_MODELS							="MODEL_DATA_CHANGE_MODELS";
}
