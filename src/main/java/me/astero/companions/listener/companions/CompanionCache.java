package me.astero.companions.listener.companions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerCache;

public class CompanionCache implements Listener {
	
	private CompanionsPlugin main;
	
	public CompanionCache(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	@EventHandler
	public void onAsyncPlayerJoin (AsyncPlayerPreLoginEvent e)
	{
		UUID uuid = e.getUniqueId();
		
		if(main.getFileHandler().isEnsureCache())
			PlayerCache.instanceOf(uuid).setCheckCache("YES"); // Check if the cache is successfully loaded.
	

		try
		{
			
			if(!main.getFileHandler().isDatabase())
			{
				for(String getCompanionName : main.getFileManager().getCompanionsData().getConfigurationSection("companions." + uuid + ".owned").getKeys(false))
				{
					main.getCompanionUtil().updateCache(uuid, getCompanionName);
				}
				
				PlayerCache.instanceOf(uuid).setCachedCompanionName(main.getFileManager().getCompanionsData()
						.getString("companions." + uuid + ".active"));
				
				PlayerCache.instanceOf(uuid).setCachedCompanionCoins(main.getFileManager().getCompanionsData()
						.getInt("companions." + uuid + ".coins"));
			}
			else
			{

						  
				PreparedStatement p = null;
				try(Connection conn = main.getDatabase().getHikari().getConnection())
				{

					
					p = conn.prepareStatement("SELECT * FROM " + main.getDatabase().getTablePrefix() +
							"owned WHERE UUID = ?");
					p.setString(1, uuid.toString());


					ResultSet rs = p.executeQuery();
					
					
					while(rs.next())
					{
						main.getCompanionUtil().updateCache(uuid, rs.getString("companion").toLowerCase(),
								rs.getString("customName"), rs.getString("customWeapon"), 
								rs.getBoolean("nameVisible"), rs.getInt("abilityLevel"));
		
						
					}
					
					p = conn.prepareStatement("SELECT * FROM " + main.getDatabase().getTablePrefix() +
							"active WHERE UUID = ?");
					p.setString(1, uuid.toString());
					
					rs = p.executeQuery();
					
					if(rs.next())
					{
						PlayerCache.instanceOf(uuid).setCachedCompanionName(rs.getString("companion"));

					}
					
					p = conn.prepareStatement("SELECT * FROM " + main.getDatabase().getTablePrefix() +
							"currency WHERE UUID = ?");
					p.setString(1, uuid.toString());
					
					rs = p.executeQuery();
					
					if(rs.next())
					{
						PlayerCache.instanceOf(uuid).setCachedCompanionCoins(rs.getLong("coins"));
						
						

					}
					
					
					
					main.getDatabase().close(conn, p, rs);
					
				} 
				catch (SQLException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			

			

				

		
		}
		catch(NullPointerException noCompanions) {}
		
	}

}
