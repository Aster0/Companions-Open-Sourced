package me.astero.companions.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerCache;
import me.astero.companions.companiondata.PlayerData;
import net.md_5.bungee.api.ChatColor;

public class RemoveCompanionCommand implements CommandExecutor {

	private CompanionsPlugin main;
	
	public RemoveCompanionCommand(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		
		if(sender.hasPermission("companions.admin.remove"))
		{
			// removecompanion player companion
			if(args.length >= 2)
			{
				Player target = Bukkit.getPlayer(args[0]);
				OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);
				

				
				if(main.getFileHandler().getCompanionDetails().containsKey(args[1].toLowerCase()))
				{
					String getCompanionName = args[1].toLowerCase();
					try
					{
						if(PlayerData.instanceOf(target).getAllCompanions().contains(getCompanionName))
						{
	
								
								PlayerCache.instanceOf(target.getUniqueId()).getOwnedCache().remove(getCompanionName);
								
								if(PlayerData.instanceOf(target).getActiveCompanionName().equals(getCompanionName.toUpperCase()))
								{
									PlayerData.instanceOf(target).removeCompanion();
									PlayerData.instanceOf(target).setActiveCompanionName("NONE");
		
									

								}
								
								target.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionRemovedFromPlayerMessage()
										.replace("%companion%", getCompanionName.toUpperCase())));
								
								
								
								if(!target.getGameMode().equals(GameMode.CREATIVE) && PlayerData.instanceOf(target).isFlyMode())
								{
									target.setAllowFlight(false);
									PlayerData.instanceOf(target).setFlyMode(false);
								}
								
							
							
						}
						else
						{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionNotOwnedMessage()
									.replace("%player%", target.getDisplayName())));
						}
					}
					catch(NullPointerException notOnline)
					{
						try
						{
							if(!main.getFileManager().getCompanionsData().getConfigurationSection("companions." + offlineTarget.getUniqueId() + ".owned").getKeys(false).contains(getCompanionName))
							{
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionNotOwnedMessage()
										.replace("%player%", offlineTarget.getName())));
							}
						}
						catch(NullPointerException notFound) {}
					}
					
					finally
					{		
						try
						{

	
								
							removeActiveFromYML(getCompanionName, offlineTarget);
							
							removeFromYML(getCompanionName, offlineTarget);
							
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionRemovedMessage()));
							
							
	
						}
						catch(NullPointerException notFound) 
						{
							if(!offlineTarget.isOnline())
							{
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionNotOwnedMessage()
										.replace("%player%", offlineTarget.getName())));
							}
						}
					}
				}
				else
				{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionNotFoundMessage()));
				}
			}
			else
			{
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getInvalidRemoveUsageMessage()));
			}
		}
		else
		{
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoPermissionMessage()));
		}
		
		return false;
	}
	
	public void removeFromYML(String getCompanionName, OfflinePlayer player)
	{
		if(!main.getFileHandler().isDatabase())
		{
			if(main.getFileManager().getCompanionsData().getConfigurationSection("companions." + player.getUniqueId() + ".owned").getKeys(false).contains(getCompanionName))
			{
				main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId() + ".owned." + getCompanionName, null);
			}
		}
		
		else
		{
			 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
					
				  @Override
				 public void run()
				 {
					  
						PreparedStatement p = null;
						try(Connection conn = main.getDatabase().getHikari().getConnection())
						{
			
							p = conn.prepareStatement("DELETE FROM `" + main.getDatabase().getTablePrefix() +
									"owned` WHERE UUID=? AND companion=?");
							
							p.setString(1, player.getUniqueId().toString());
							p.setString(2, getCompanionName.toUpperCase());
							
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
	}
	
	public void removeActiveFromYML(String getCompanionName, OfflinePlayer player)
	{
		if(!main.getFileHandler().isDatabase())
		{

			if(main.getFileManager().getCompanionsData().getString("companions." + player.getUniqueId() + ".active").equals(getCompanionName.toUpperCase()))
				main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId() + ".active", "NONE");
		}
		else
		{
			 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
					
				  @Override
				 public void run()
				 {
					  
						PreparedStatement p = null;
						try(Connection conn = main.getDatabase().getHikari().getConnection())
						{
			
							p = conn.prepareStatement("SELECT * FROM " + main.getDatabase().getTablePrefix() +
									"active WHERE UUID = ?");
							p.setString(1, player.getUniqueId().toString());

							
							ResultSet rs = p.executeQuery();
							
							if(rs.next())
							{
								if(rs.getString("companion").equals(getCompanionName.toUpperCase()))
								{
									p = conn.prepareStatement("DELETE FROM `" + main.getDatabase().getTablePrefix() +
											"active` WHERE UUID=?");
									
									p.setString(1, player.getUniqueId().toString());
									
									p.execute();
								}
							}
							
							main.getDatabase().close(conn, p, rs);
							
						} 
						catch (SQLException e1) 
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				 }
			 });
		}
	}

}
