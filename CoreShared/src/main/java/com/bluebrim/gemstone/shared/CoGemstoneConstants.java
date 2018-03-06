package com.bluebrim.gemstone.shared;

/**
 */
public interface CoGemstoneConstants {
	/* Consider converting this interface to an abstrabt class, to prevent misuse
	 * (i.e. people implementing (!) thiss class. A collection of constants like this is
	 * not ment to be implemented ...
	 *
	 * Magnus Ihse <magnus.ihse@appeal.se> (2001-05-03 10:32:53)
	 *
	 * Spelling mistakes aside, I do not entirely agree. /Markus
	 */

	public static String USER			= "user";
	public static String USER_CATALOG	= "user_catalog";
	public static String USERS			= "users";
	public static String SYS_ADM		= "sys_adm";
	

	//	Server objects
	public static String SYSTEM_SERVICES				= "system_services";	
	public static String FACTORY_MANAGER				= "factory_manager";	
	public static String SYSTEM							= "system";	
	public static String SERVER							= "server";		
	//	Name contexts
	public static String CALVIN_USER_OBJECTS			= "calvin_user_objects";	
	public static String CALVIN_DEV_CLASSES				= "calvin_dev_classes";	
	public static String CALVIN_USER_CLASSES			= "calvin_user_classes";

	//	System administration privileges
	public static String CHANGE_OTHER_PASSWORD			= "Change Other Password";	
	public static String CHANGE_OWNER_AND_GROUP			= "Change Owner and Group";	
	public static String CHANGE_PERMISSIONS				= "Change Permissions";	
	public static String SESSION_ACCESS					= "Session Access";	
	public static String SYSTEM_CONTROL					= "System Control";	
	public static String USER_PASSWORD					= "User Password";	
	
	public static String[] SYSTEM_ADMINISTRATOR_PRIVILEGES = new String[] {
			CHANGE_OTHER_PASSWORD,
			CHANGE_OWNER_AND_GROUP,
			CHANGE_PERMISSIONS,
			SESSION_ACCESS,
			SYSTEM_CONTROL,
			USER_PASSWORD
	};

	// GemStone groups
	public static String DEVELOPERS						= "developers";	
	public static String END_USERS						= "end_users";	
	public static String PRIMARY_GROUP					= END_USERS;
	public static String[] DEFAULT_GROUPS = new String[] {
			DEVELOPERS,
			END_USERS
	};

	// Gemstone users
	public static String DATACURATOR					= "DataCurator";
	public static String SYSTEM_USER					= "SystemUser";
	public static String GC_USER						= "GcUser";
	public static String NAMELESS						= "Nameless";
	
	public static String[] GEMSTONE_USERS = new String[] {
		DATACURATOR,
		SYSTEM_USER,
		GC_USER,
		NAMELESS
	};

	// Default GemStone groups
	public static String SYSTEM_GROUP					= "System";
	public static String PUBLISHERS_GROUP				= "Publishers";
	public static String SUBSCRIBERS_GROUP				= "Subscribers";
	public static String USERS_GROUP					= "Users";

	// Runtime modes
	public static final String CHOOSE_RUNTIME_MODEL		= "choose_runtime_model";
	public static final String NO_RUNTIME 				= "no_runtime";
	public static final String SIMULATED_RUNTIME 		= "simulated_runtime";
	public static final String GEMSTONE_RUNTIME 		= "gemstone_runtime";
	public static final String GEMSTONE_SERVER 			= "gemstone_server";
	
}