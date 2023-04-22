package me.astero.companions.currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerData;

public class CompanionCoin {
	
	private CompanionsPlugin main;
	
	public CompanionCoin(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	
	public boolean has(Player player, long amount)
	{
		if(PlayerData.instanceOf(player).getCompanionCoin() >= amount)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public void withdrawPlayer(Player player, long amount)
	{
		long companionCoin = PlayerData.instanceOf(player).getCompanionCoin();
		

		long newAmount = companionCoin - amount;
		
		PlayerData.instanceOf(player).setCompanionCoin(newAmount);
		
		if(!main.getFileHandler().isDatabase())
			main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId().toString() + ".coins", newAmount);
		

	}
	

	
	public void withdrawPlayer(OfflinePlayer player, long amount)
	{
		long companionCoin = main.getFileManager().getCompanionsData().getLong("companions." + player.getUniqueId().toString() + ".coins");
		

		long newAmount = companionCoin - amount;
		
		
		if(!main.getFileHandler().isDatabase())
			main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId().toString() + ".coins", newAmount);
		
		else
		{
			updateCoins(player, newAmount);
		}
	}
	
	
	public void depositPlayer(Player player, long amount)
	{
		long companionCoin = PlayerData.instanceOf(player).getCompanionCoin();

		long newAmount = companionCoin + amount;
		
		PlayerData.instanceOf(player).setCompanionCoin(newAmount);
		
		if(!main.getFileHandler().isDatabase())
			main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId().toString() + ".coins", newAmount);
		
	}
	
	public void depositPlayer(OfflinePlayer player, long amount)
	{
		long companionCoin = main.getFileManager().getCompanionsData().getLong("companions." + player.getUniqueId().toString() + ".coins");

		long newAmount = companionCoin + amount;
		
		
		if(!main.getFileHandler().isDatabase())
			main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId().toString() + ".coins", newAmount);
		
		else
		{
			updateCoins(player, newAmount);
		}
	}
	
	
	public void updateCoins(Player player, long companionCoin)
	{

		 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
				
			  @Override
			 public void run()
			 {
				  
					PreparedStatement p = null;
					try(Connection conn = main.getDatabase().getHikari().getConnection())
					{
					
						
						p = conn.prepareStatement("INSERT INTO `" + main.getDatabase().getTablePrefix() 
								+"currency` (`UUID`,`name`,`coins`) VALUES (?,?,?)" + 
								"  ON DUPLICATE KEY UPDATE coins=\"" + companionCoin + "\"");
						p.setString(1, player.getUniqueId().toString());
						p.setString(2, player.getName().toString());
						p.setLong(3, companionCoin);
						//p.setString(4, player.getUniqueId().toString());
	
						p.execute();
						
						main.getDatabase().close(conn, p, null);
						
					} 
					catch (SQLException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			 }  	
			});
		
	}
	
	public void updateCoins(OfflinePlayer player, long companionCoin)
	{

		 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
				
			  @Override
			 public void run()
			 {
				  
					PreparedStatement p = null;
					try(Connection conn = main.getDatabase().getHikari().getConnection())
					{
						
						
						p = conn.prepareStatement("INSERT INTO `" + main.getDatabase().getTablePrefix() 
								+"currency` (`UUID`,`name`,`coins`) VALUES (?,?,?)" + 
								"  ON DUPLICATE KEY UPDATE coins=\"" + companionCoin + "\"");
						p.setString(1, player.getUniqueId().toString());
						p.setString(2, player.getName().toString());
						p.setLong(3, companionCoin);
						//p.setString(4, player.getUniqueId().toString());
	
						p.execute();
						
						main.getDatabase().close(conn, p, null);
						
					} 
					catch (SQLException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			 }  	
			});
		
	}
	
	public void updateCoinsNA(OfflinePlayer player, long companionCoin, PreparedStatement p, Connection conn)
	{


			
			try
			{
			
				conn = main.getDatabase().getHikari().getConnection();
				p = conn.prepareStatement("INSERT INTO `" + main.getDatabase().getTablePrefix() 
						+"currency` (`UUID`,`name`,`coins`) VALUES (?,?,?)" + 
						"  ON DUPLICATE KEY UPDATE coins=\"" + companionCoin + "\"");
				p.setString(1, player.getUniqueId().toString());
				p.setString(2, player.getName().toString());
				p.setLong(3, companionCoin);
				//p.setString(4, player.getUniqueId().toString());

				p.execute();
				
		
			} 
			catch (SQLException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		
	}
	
	
	public void updateCoinsCache(Player player, long amount)
	{
		if(main.getFileHandler().isDatabase())
			updateCoins(player, amount);
			
		
	}
	
	public void updateCoinsCache(Player player, long amount, PreparedStatement p, Connection conn)
	{
		if(main.getFileHandler().isDatabase())
			updateCoinsNA(player, amount, p, conn);
			
		
	}

}
