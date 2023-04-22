package me.astero.companions.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerData;

public class CompanionCoinCommand implements CommandExecutor {
	
	private CompanionsPlugin main;
	
	
	public CompanionCoinCommand(CompanionsPlugin main)
	{
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		
		if(args.length > 0)
		{
			if(sender.hasPermission("companions.admin.coin"))
			{
				if(args.length > 2) // /coin give {player} {amount}
				{

					int amount;
					try
					{
						amount = Integer.valueOf(args[2]);
					}
					catch(NumberFormatException e)
					{
						
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + "&cYou need to enter a valid number!"));
						return false;
					}
					
					if(args[0].equalsIgnoreCase("give"))
					{
						
						try
						{
							Player target = Bukkit.getPlayer(args[1]);
							main.getCompanionCoin().depositPlayer(target, amount);
							target.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionCoinGivenMessage().replace("%amount%", String.valueOf(amount))));
						
		
						}
						catch(NullPointerException e)
						{
							OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
							main.getCompanionCoin().depositPlayer(target, amount);
							

						}
						
						

						
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionCoinGivenSuccessfulMessage()));
						
					}
					else if(args[0].equalsIgnoreCase("remove"))
					{
						try
						{
							Player target = Bukkit.getPlayer(args[1]);
							main.getCompanionCoin().withdrawPlayer(target, amount);
							target.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionCoinRemovedMessage().replace("%amount%", String.valueOf(amount))));
						}
						catch(NullPointerException e)
						{
							OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
							main.getCompanionCoin().withdrawPlayer(target, amount);
						}
						
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getCompanionCoinRemovedSuccessfulMessage()));
						
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
		}
		else
		{
			if(sender instanceof Player)
			{
				Player player = (Player) sender;
				
				if(player.hasPermission("companions.player.coin"))
				{
					long coins = PlayerData.instanceOf(player).getCompanionCoin();
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getCompanionCoinStatsMessage().replace("%amount%", String.valueOf(coins))));
				}
			}
			else
			{
				System.out.println(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNotPlayerMessage()));
			}
			
		
		}
		
		return false;
	}

}
