package me.astero.companions.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerCache;
import me.astero.companions.companiondata.PlayerData;

public class ForceCompanionUpgradeCommand implements CommandExecutor {
	
	private CompanionsPlugin main;
	
	public ForceCompanionUpgradeCommand(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	
	// /forceupgrade {player} {ability}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender.hasPermission("companions.admin.forceupgrade"))
		{
			if(args.length >= 2)
			{
	
				try
				{
					
					Player player = Bukkit.getPlayer(args[0]);
					if(PlayerData.instanceOf(player).getActiveCompanionName() != "NONE" && PlayerData.instanceOf(player).getActiveCompanionName() != null)
					{
						if(args[1].equalsIgnoreCase("ability"))
						{
							
							boolean upgrade;
							
							try
							{
								upgrade = Boolean.valueOf(args[2]);
							}
							catch(ArrayIndexOutOfBoundsException notStated)
							{
								upgrade = true;
							}
	
							if(!upgrade)
							{
								if(PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityLevel() != 1)
								{
									main.getCompanionUtil().buyUpgradeAbility(player, false);
								}
								else
								{
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getAbilityDowngradedMaxedMessage()));
								}
							}
							else if(main.getFileHandler().getMaxAbilityLevel() != 
									PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityLevel())
							{

								
								if(upgrade)
								{
									main.getCompanionUtil().buyUpgradeAbility(player, true);
								}
								
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getForceUpgradeSuccessfulMessage()));
							}
							else
							{
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getAbilityMaxedMessage()));
							}
						}
						else if(args[1].equalsIgnoreCase("rename"))
						{
							main.getCompanionUtil().upgradeRename(player, false, false);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getForceUpgradeSuccessfulMessage()));
						}
						else if(args[1].equalsIgnoreCase("hidename"))
						{
							main.getCompanionUtil().upgradeHideName(player, false, false);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getForceUpgradeSuccessfulMessage()));
						}
						else if(args[1].equalsIgnoreCase("changeweapon"))
						{
							main.getCompanionUtil().upgradeWeapon(player, false, false);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getForceUpgradeSuccessfulMessage()));
						}
						else
						{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getInvalidUpgradeArgumentMessage()));
						}
					}
					else
					{
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getForceUpgradeNotSuccessfulMessage()));
					}
				}
				catch(NullPointerException error)
				{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getPlayerNotOnlineMessage()));
				}
			}
			else
			{
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getInvalidUsageMessage()));
			}
		}
		else
		{
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoPermissionMessage()));
		}
		
		
		return false;
	}

}
