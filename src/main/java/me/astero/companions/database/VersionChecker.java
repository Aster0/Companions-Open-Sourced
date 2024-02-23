package me.astero.companions.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;

import com.zaxxer.hikari.HikariDataSource;

import lombok.Getter;
import me.astero.companions.CompanionsPlugin;

public class VersionChecker {
	
	
	
	@Getter private HikariDataSource hikari;
	private String host;
	private String databaseName;
	private String username;
	private String password;
	@Getter private String tablePrefix;
	private int port;
	private String currentPluginVersion;
	
	private CompanionsPlugin main;
	
	public VersionChecker(CompanionsPlugin main)
	{
		this.main = main;

		if(main.getConfig().getBoolean("settings.versionChecking"))
		{
			System.out.println(ChatColor.GOLD + ">" + ChatColor.GRAY + " Checking plugin's version.");
			
			System.out.println(ChatColor.GRAY + "  This might take a while, hold tight.");
			System.out.println("");
			
			initiate();
			setupDatabase();
			checkVersion();
			
			if(hikari != null)
				hikari.close();
		}
		
		


		

	}
	
	public void initiate()
	{
		currentPluginVersion = main.getDescription().getVersion();
		
		host = "checker.astero.me";
		port = 3306;
		username = "information";
		password = "3_Utx8zhucJRqpcbp";
		databaseName = "plugin_information";
	}
	
	public void setupDatabase()
	{
		hikari = new HikariDataSource();
		
	
		
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", host);
        hikari.addDataSourceProperty("port", port);
        hikari.addDataSourceProperty("user", username);
        hikari.addDataSourceProperty("password", password);
        hikari.addDataSourceProperty("databaseName", databaseName);

		
        
        
        
        
        
        
	}
	
	
	public void checkVersion()
	{
		PreparedStatement p = null;
		Connection connection = null;
		
        try
        {

        	connection = hikari.getConnection();
        	
			p = connection.prepareStatement("SELECT * FROM versions WHERE PLUGIN = ?");
			p.setString(1, main.getDescription().getName().toLowerCase());
			
			
			ResultSet rs = p.executeQuery();
			

			
			if(rs.next())
			{
				String latestPluginVersion = rs.getString("VERSION");

				if(!latestPluginVersion.equals(currentPluginVersion))
				{
					System.out.println(ChatColor.RED + "  You're not using the latest plugin version! \n\n"
							+ ChatColor.YELLOW + "YOUR VERSION: " + currentPluginVersion + "           " +
							ChatColor.DARK_RED + "LATEST VERSION: " + latestPluginVersion + "\n");
					System.out.println(ChatColor.RED + "  " + rs.getString("SITE"));
				}
				else
				{
					System.out.println("\n" + ChatColor.GREEN + "  You're using the latest plugin version!");
				}
				
				System.out.println("");
			}
			
			p = connection.prepareStatement("SELECT * FROM patreons");
			
			rs = p.executeQuery();
			
			while(rs.next())
			{
				main.getCompanionUtil().getPatreonList().add(rs.getString("name"));
				
				
			}
			
			
			close(connection, p, rs);
			

        }
        catch (SQLException e)
        {
        	
        	System.out.println(ChatColor.GRAY + "  Failed to read plugin's version.");
		}
	}
	
    public void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
        if (res != null) try { res.close(); } catch (SQLException ignored) {}
    }
    

    

	

}
