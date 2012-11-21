package com.mavsel;


import java.sql.Statement;
import java.sql.Connection;

/**
 * 
 * @author Pablo Sicilia
 * @version Mavsel Workbench 1.0 15-8-2012
 */
public class MavselConnection {
	public final static String SCHEMA_NAME = "RapidMinerMavelTemp";

	/**
	 * 
	 */
	public MavselConnection() {

	}

	/**
	 * 
	 * @param connection
	 */
	public void createSchema(Connection connection) {

		Statement stmt;
		try {

			if (connection != null) {
				stmt = connection.createStatement();
				stmt.execute("CREATE SCHEMA IF NOT EXISTS " + SCHEMA_NAME);
				stmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 
	 * @param connection
	 */
	public void createTableCourse(Connection connection) {
		Statement stmt;

		try {
			stmt = connection.createStatement();
			stmt.executeUpdate("CREATE  TABLE IF NOT EXISTS `"+SCHEMA_NAME+"`.`courses` (`id` VARCHAR(50) ,`fullname` VARCHAR(1500), `shortname` VARCHAR(100) , `summary` VARCHAR(1700) , PRIMARY KEY (`id`) ) ENGINE = INNODB;");			
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param connection
	 */
	public void createTableForum(Connection connection) {
		Statement stmt;
	    
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate("CREATE  TABLE IF NOT EXISTS `"+SCHEMA_NAME+"`.`forums` (`id` VARCHAR(50) ,`idCourse` VARCHAR(50), `title` VARCHAR(400) , `description` VARCHAR(1700) , `type` VARCHAR(1700) , PRIMARY KEY (`id`) ) ENGINE = INNODB;");			
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param connection
	 */
	public void createTableDiscussion(Connection connection) {
		Statement stmt;

		try {
			stmt = connection.createStatement();
			stmt.executeUpdate("CREATE  TABLE IF NOT EXISTS `"+SCHEMA_NAME+"`.`discussions` (`id` varchar(50) , `idCourse` varchar(50) , `idForum` varchar(50) , `idGroup` varchar(50) ,`idParticipant` varchar(50) ,`title` VARCHAR(1000) , PRIMARY KEY (`id`) ) ENGINE = INNODB;");			
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param connection
	 */
	public void createTablePost(Connection connection) {
		Statement stmt;

		try {
			stmt = connection.createStatement();
			stmt.executeUpdate("CREATE  TABLE IF NOT EXISTS `"+SCHEMA_NAME+"`.`courses` (`id` varchar(50) ,`fullname` VARCHAR(1500), `shortname` VARCHAR(100) , `summary` VARCHAR(1500) , PRIMARY KEY (`id`) ) ENGINE = INNODB;");			
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param connection
	 */
	public void createTableUser(Connection connection) {
		Statement stmt;

		try {
			stmt = connection.createStatement();
			stmt.executeUpdate("CREATE  TABLE IF NOT EXISTS `"+SCHEMA_NAME+"`.`courses` (`id` varchar(50) ,`fullname` VARCHAR(1500), `shortname` VARCHAR(100) , `summary` VARCHAR(1500) , PRIMARY KEY (`id`) ) ENGINE = INNODB;");			
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
