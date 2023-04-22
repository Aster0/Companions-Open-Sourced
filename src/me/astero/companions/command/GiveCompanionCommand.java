package me.astero.companions.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerData;
import net.md_5.bungee.api.ChatColor;

public class GiveCompanionCommand implements CommandExecutor {

	private CompanionsPlugin main;
	private 	boolean set, create;
	
	public GiveCompanionCommand(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		
		if(sender.hasPermission("companions.admin.give"))
		{
			// givecompanions player companion <true/false>
			if(args.length >= 2)
			{
				Player target = Bukkit.getPlayer(args[0]);
				OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);
				
				if(main.getFileHandler().getCompanionDetails().containsKey(args[1].toLowerCase()))
				{
					String getCompanionName = args[1].toLowerCase();
					create = true;
					
					try
					{
						if(!PlayerData.instanceOf(target).getAllCompanions().contains(getCompanionName))
						{

								
							
								main.getCompanionUtil().storeNewYML(getCompanionName, offlineTarget);
								
								main.getCompanionUtil().updateCache(target.getUniqueId(), getCompanionName);
								
								
								

							
								
							try
							{
								set = Boolean.valueOf(args[2]);
							}
							catch(ArrayIndexOutOfBoundsException notStated)
							{
								set = false;
							}
							
						
							if(set)
							{
	
								
								
								PlayerData.instanceOf(target).removeCompanion();
								main.getCompanionUtil().storeActiveDB(getCompanionName, target);
								main.getCompanionUtil().storeActiveYML(target, getCompanionName);
								
								
								PlayerData.instanceOf(target).setActiveCompanionName(getCompanionName.toUpperCase());
								
								//main.getCompanions().summonCompanion(target); // non packet Companion
								main.getCompanionPacket().loadCompanion(target);
								

								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionGivenMessage()));
								
								target.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionSetForPlayerMessage()
										.replace("%companion%", getCompanionName.toUpperCase())));
								

							}
							else
							{
								target.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionReceivedMessage()
										.replace("%companion%", getCompanionName.toUpperCase())));
								
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
										main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionGivenMessage()));
							}
							
							
						}
						else
						{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionAlreadyOwnedMessage()
									.replace("%player%", target.getDisplayName())));
						}
					}
					catch(NullPointerException notOnline)
					{
						try
						{
							if(main.getFileManager().getCompanionsData().getConfigurationSection("companions." + offlineTarget.getUniqueId() + ".owned").getKeys(false).contains(getCompanionName))
							{
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionAlreadyOwnedMessage()
										.replace("%player%", offlineTarget.getName())));
	
							}
						}
						catch(NullPointerException notFound) {}
					}
					finally
					{
			
						
						try
						{
							if(!offlineTarget.isOnline())
							{
								if(!main.getFileManager().getCompanionsData().getConfigurationSection("companions." + offlineTarget.getUniqueId() + ".owned").getKeys(false).contains(getCompanionName))
								{
									main.getCompanionUtil().storeNewYML(getCompanionName, offlineTarget);
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionGivenMessage()));
									
							
								}
							}

						}
						catch(NullPointerException notFound)
						{
							if(create)
							{
								main.getCompanionUtil().storeNewYML(getCompanionName, offlineTarget);
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getPlayerNotFoundMessage()));
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
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getInvalidGiveUsageMessage()));
			}
		}
		else
		{
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoPermissionMessage()));
		}
		return false;
	}

}
