package me.astero.companions.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerData;

public class ForceCompanionDeactiveCommand implements CommandExecutor {
	
	private CompanionsPlugin main;
	
	public ForceCompanionDeactiveCommand(CompanionsPlugin main)
	{
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// forcedeactive {player}
		if(args.length >= 1)
		{
			if(sender.hasPermission("companions.admin.forcedeactive"))
			{
				try
				{
					Player target = Bukkit.getPlayer(args[0]);
					
					if(PlayerData.instanceOf(target).getActiveCompanionName() != null)
					{
						if(PlayerData.instanceOf(target).getActiveCompanionName() != "NONE")
						{
							PlayerData.instanceOf(target).removeCompanion();
							main.getCompanionUtil().storeActiveDB("NONE", target);
							main.getCompanionUtil().storeActiveYML(target, "NONE");
							
							PlayerData.instanceOf(target).setActiveCompanionName("NONE");
							
							target.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getRemoveCompanionMessage()));
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionRemovedMessage()));
				
						
						}
						else
						{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getForceUpgradeNotSuccessfulMessage()));
						}
					}
					else
					{
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getForceUpgradeNotSuccessfulMessage()));
					}
				}
				catch(NullPointerException playerNotOnline)
				{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getPlayerNotOnlineMessage()));
				}
			}
			else
			{
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoPermissionMessage()));
			}
			
		}
		else
		{
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getInvalidUsageMessage()));
		}
		return false;
	}

}
