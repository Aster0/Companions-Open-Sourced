package me.astero.companions.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;

import com.zaxxer.hikari.HikariDataSource;

import lombok.Getter;
import me.astero.companions.CompanionsPlugin;

public class Database {
	
	private CompanionsPlugin main;
	
	
	@Getter private HikariDataSource hikari;
	private String host;
	private String databaseName;
	private String username;
	private String password;
	@Getter private String tablePrefix;
	private int port;
	
	public Database(CompanionsPlugin main, String source)
	{
		this.main = main;
		
		if(main.getFileHandler().isDatabase())
		{
			
	
			System.out.println(ChatColor.GOLD + ">" + ChatColor.GRAY + " Connecting to the database..");
			
			initiate();
			setupDatabase(source);
			createTable();
			
			
			System.out.println("");
		}
		

	}
	
	public void initiate()
	{
		host = main.getConfig().getString("database.hostname");
		port = main.getConfig().getInt("database.port");
		username = main.getConfig().getString("database.username");
		password = main.getConfig().getString("database.password");
		databaseName = main.getConfig().getString("database.databaseName");
		tablePrefix = main.getConfig().getString("database.tablePrefix");
	}
	
	public void setupDatabase(String source)
	{
		hikari = new HikariDataSource();



        //hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		hikari.setDataSourceClassName(source);
        hikari.addDataSourceProperty("serverName", host);
        hikari.addDataSourceProperty("port", port);
        hikari.addDataSourceProperty("user", username);
        hikari.addDataSourceProperty("password", password);
        hikari.addDataSourceProperty("databaseName", databaseName);
        
        
	}
	
	
	public void createTable()
	{
		PreparedStatement p = null;

		try(Connection connection = hikari.getConnection())
        {


			p = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + tablePrefix + "owned" +
					"(UUID varchar(36), name text, companion text, customWeapon text, "
					+ "customName text, nameVisible boolean, abilityLevel int)");

			p.execute();

			p = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + tablePrefix + "active" +
					"(UUID varchar(36) PRIMARY KEY, name VARCHAR(26), companion TEXT)");

			p.execute();


			p = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + tablePrefix + "currency" +
					"(UUID varchar(36) PRIMARY KEY, name VARCHAR(26), coins int)");

			p.execute();

			close(connection, p, null);


			System.out.println(ChatColor.GRAY + "  Successfully connected to the database.");
        }
        catch (SQLException e)
        {

        	e.printStackTrace();
		}
	}
	
    public void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
        if (res != null) try { res.close(); } catch (SQLException ignored) {}
    }
    
    public void onDisabled()
    {
    	
    	if(hikari != null)
    		hikari.close();
    }
    

	

}
