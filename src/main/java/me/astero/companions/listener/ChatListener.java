package me.astero.companions.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerCache;
import me.astero.companions.companiondata.PlayerData;
import me.astero.companions.util.ItemBuilderUtil;

public class ChatListener implements Listener {
	
	private CompanionsPlugin main;
	
	public ChatListener(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		Player player = e.getPlayer();
		


		if(PlayerData.instanceOf(player).isRenamingCompanion())
		{

			
			e.setCancelled(true);
			
			if(e.getMessage().equalsIgnoreCase("cancel"))
			{
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getActionSuccessMessage()));
			}
			else
			{
				
				String customName = e.getMessage();
				
				
				if(!player.hasPermission("companions.admin.blacklist"))
				{
					if(main.getFileHandler().getBlacklistedNames().contains(customName))
					{
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + 
								main.getFileHandler().getBlacklistedNameMessage()));
						
						return;
					}
				}

				String getCompanionName = PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase();
				
				String companionCustomName;
				
				if(customName.length() > main.getFileHandler().getMaxNameLength())
				{
					companionCustomName = customName.substring(0, main.getFileHandler().getMaxNameLength());
				}
				else
				{
					companionCustomName = customName;
				}
			
				
				PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(getCompanionName).setCustomName(companionCustomName);
				storeNameYML(getCompanionName, player, companionCustomName);
				
				
				// Vault doesn't support async withdrawl for 1.14+ no more :(
				
				//PlayerData.instanceOf(player).getActiveCompanion().setCustomName(ChatColor.translateAlternateColorCodes('&', customName)); non packet companion
				
				main.getCompanionPacket().setCustomName(player, companionCustomName);
				
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getRenamedCompanionMessage()
						.replace("%newname%", customName)).replace("%price%", String.valueOf(main.getFileHandler().getRenameCompanionPrice())));

				
				
				
			}
					
			
				PlayerData.instanceOf(player).setRenamingCompanion(false);
			
			
			
			
		}
		else if(PlayerData.instanceOf(player).isChangingWeapon())
		{
			e.setCancelled(true);
			
			if(e.getMessage().equalsIgnoreCase("cancel"))
			{
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getActionSuccessMessage()));
				PlayerData.instanceOf(player).setChangingWeapon(false);
			}
			else
			{
				
				String newWeapon = e.getMessage();
				String getCompanionName = PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase();
				
				try
				{
					PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(getCompanionName).setCustomWeapon(new ItemBuilderUtil(
							Material.valueOf(newWeapon.toUpperCase()),
							getCompanionName.toUpperCase() + "'S WEAPON", 
							1).build());
					
					storeWeaponYML(getCompanionName, player, newWeapon);
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getChangedCompanionWeaponMessage()
							.replace("%newweapon%", newWeapon)).replace("%price%", String.valueOf(main.getFileHandler().getChangeWeaponPrice())));
					
					//PlayerData.instanceOf(player).getActiveCompanion().setItemInHand(PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(getCompanionName).getCustomWeapon()); non packet companion
					
					main.getCompanionPacket().setCustomWeapon(player, PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(getCompanionName).getCustomWeapon());
					PlayerData.instanceOf(player).setChangingWeapon(false); // Makes it so if item is not found, player can try again.
					
				}
				catch(IllegalArgumentException itemNotFound)
				{
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getWeaponNotFoundMessage()
							.replace("%newweapon%", newWeapon)));
				}
				

				
			}
			
	
		}
	}
	
	public void storeNameYML(String getCompanionName, Player player, String customName)
	{
		if(!main.getFileHandler().isDatabase())
			main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId() + ".owned." + getCompanionName + ".customName", 
					customName);
		
		else
		{
			 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
					
				  @Override
				 public void run()
				 {
					  
						PreparedStatement p = null;
						try(Connection conn = main.getDatabase().getHikari().getConnection())
						{
							p = conn.prepareStatement("UPDATE `" + main.getDatabase().getTablePrefix() +
									"owned` SET customName=? WHERE UUID=? AND companion=?");
					
							p.setString(1, customName);
							p.setString(2, player.getUniqueId().toString());
							p.setString(3, getCompanionName.toUpperCase());
							
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
	
	public void storeWeaponYML(String getCompanionName, Player player, String customWeapon)
	{
		if(!main.getFileHandler().isDatabase())
			main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId() + ".owned." + getCompanionName + ".customWeapon", 
					customWeapon.toUpperCase());
		
		else
		{
			 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
					
				  @Override
				 public void run()
				 {
					  
						PreparedStatement p = null;
						try(Connection conn = main.getDatabase().getHikari().getConnection())
						{
							p = conn.prepareStatement("UPDATE `" + main.getDatabase().getTablePrefix() +
									"owned` SET customWeapon=? WHERE UUID=? AND companion=?");
					
							p.setString(1, customWeapon.toUpperCase());
							p.setString(2, player.getUniqueId().toString());
							p.setString(3, getCompanionName.toUpperCase());
							
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

}
