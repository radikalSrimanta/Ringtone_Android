package com.utility.orm_utility;

import java.io.IOException;
import java.sql.SQLException;


import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.User;


public class DatabaseConfigUtil extends OrmLiteConfigUtil{

	private static final Class<?> classes[] = new Class[]{Contacts.class,User.class};   

	public static void main(String arg[]) throws SQLException, IOException{
		writeConfigFile("ormlite_config.txt", classes);
	}
}
