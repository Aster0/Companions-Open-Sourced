package me.astero.companions.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerCache;
import me.astero.companions.companiondata.PlayerData;

public class ClearCompanionDataCommand implements CommandExecutor {
	
	private CompanionsPlugin main;
	
	public ClearCompanionDataCommand(CompanionsPlugin main)
	{
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender.hasPermission("companions.admin.cleardata"))
		{
			if(args.length > 0)
			{
				if(args[0].equalsIgnoreCase("all"))
				{
					
					if(!main.getFileHandler().isDatabase())
					{
						main.getFileManager().getCompanionsData().set("companions", null);
						main.getFileManager().saveFile();
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
												"owned` WHERE 1");
										
										p.execute();
										
										p = conn.prepareStatement("DELETE FROM `" + main.getDatabase().getTablePrefix() +
												"active` WHERE 1");

										
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
						
					for(UUID uuid : PlayerCache.getPlayers().keySet())
					{

						PlayerCache.instanceOf(uuid).getOwnedCache().clear();
						PlayerData.instanceOf(Bukkit.getPlayer(uuid)).removeCompanion();
						PlayerData.instanceOf(Bukkit.getPlayer(uuid)).setActiveCompanionName("NONE");

					}
					
					sender.sendMessage(ChatColor.GOLD + "COMPANIONS → " + ChatColor.GRAY + "You have successfully cleared the " + ChatColor.YELLOW + "companions.yml" +
							ChatColor.GRAY + ".");
				}
				else
				{
					OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
					
					
					if(!main.getFileHandler().isDatabase())
					{
						main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId(), null);
						main.getFileManager().saveFile();
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
												"owned` WHERE UUID=?");
										
										p.setString(1, player.getUniqueId().toString());
										
										p.execute();
										
										p = conn.prepareStatement("DELETE FROM `" + main.getDatabase().getTablePrefix() +
												"active` WHERE UUID=?");
										
										p.setString(1, player.getUniqueId().toString());
										
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
					
					try
					{
						PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().clear();
						PlayerData.instanceOf(Bukkit.getPlayer(player.getUniqueId())).removeCompanion();
						PlayerData.instanceOf(Bukkit.getPlayer(player.getUniqueId())).setActiveCompanionName("NONE");
					}
					catch(NullPointerException notOnline) {}
					
					sender.sendMessage(ChatColor.GOLD + "COMPANIONS → " + ChatColor.GRAY + "You have successfully cleared Player " + ChatColor.YELLOW + args[0] +
							ChatColor.GRAY + "'s data!");

				}
			}
			else
			{
				sender.sendMessage(ChatColor.GOLD + "COMPANIONS → " + ChatColor.YELLOW +
			"Please enter whose do you want to clear, if you want to clear all data, just write all as the next argument!");
			}
		}
		else
		{
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoPermissionMessage()));
		}
		
		return false;
	}

}
