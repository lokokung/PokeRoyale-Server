package core.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import core.server.login.Login;

public class DBModule extends AbstractModule{

	@Override
	protected void configure() {
		
	}
	
	@Provides
	StringBuffer provideStringBuffer(){
		return new StringBuffer();
	}
	
	@Provides
	@Singleton
	DBLoginToken provideDBLogin(){
		String hostname = null;
		String username = null;
		String password = null;
        try {
            BufferedReader hostReader =
                    new BufferedReader(new FileReader("DBServer.txt"));
            hostname = hostReader.readLine();
            hostReader.close();
            
            BufferedReader userReader =
                    new BufferedReader(new FileReader("DBUsername.txt"));
            username = userReader.readLine();
            userReader.close();
            
            BufferedReader passwordReader =
                    new BufferedReader(new FileReader("DBPassword.txt"));
            password = passwordReader.readLine();
            passwordReader.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return new DBLoginToken(hostname, username, password);
	}
	
	@Provides
	@Database
	DBQueries provideDBQueries(Gson gson){
		String queryJson = null;
		try {
            BufferedReader queryReader =
                    new BufferedReader(new FileReader("DBQueries.txt"));
            queryJson = queryReader.readLine();
            queryReader.close();
		} catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
		
		DBQueries queries = gson.fromJson(queryJson, DBQueries.class);
		return queries;
	}
	
	@Provides
	@Database
	@Singleton
	Connection provideDBConnection(DBLoginToken token){
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// Driver not found.
			e.printStackTrace();
		}
		Connection dbConnection = null;
		
		try {
			dbConnection = DriverManager.getConnection(
					token.getHost(), token.getUsername(), token.getPassword());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbConnection;
	}
	
	@Provides
	@Login
	PreparedStatement provideLoginPStatement(@Database Connection dbConnection, @Database DBQueries queries){
		PreparedStatement loginStatement = null;
		try {
			loginStatement = dbConnection.prepareStatement(queries.getLoginQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return loginStatement;
	}

}
