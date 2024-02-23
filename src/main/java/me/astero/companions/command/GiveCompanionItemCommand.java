package me.astero.companions.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.astero.companions.CompanionsPlugin;

public class GiveCompanionItemCommand implements CommandExecutor {
	
	private CompanionsPlugin main;
	
	public GiveCompanionItemCommand(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender.hasPermission("companions.admin.item"))
		{
			//	/givecitem {player} {item} {amount}
			if(args.length > 0)
			{
				try
				{
					Player player = Bukkit.getPlayer(args[0]);
					try
					{
						if(args[1].equalsIgnoreCase("companiontoken"))
						{
							int amount;
							
							try
							{
								amount = Integer.parseInt(args[2]);
	
							
								for(int i = 0; i < amount; i++)
								{
									player.getInventory().addItem(main.getFileHandler().getCompanionToken());
								}
								
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getItemGivenMessage()));
								
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +
										main.getFileHandler().getItemReceivedMessage().replace("%item%", main.getFileHandler().getCompanionTokenName())));
							}
							catch(NumberFormatException notANumber)
							{
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + "&cYou must input a valid number!"));
							}
						}
						else
						{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + "&cYou must input a valid item!"));
						}
					}
					catch(ArrayIndexOutOfBoundsException invalidUsage)
					{
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getInvalidUsageMessage()));
					}
				}
				catch(NullPointerException notOnline)
				{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getPlayerNotOnlineMessage()));
				}
				
				
			}
			else
			{
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getInvalidUsageMessage()));
			}
		}
		return false;
	}

}
