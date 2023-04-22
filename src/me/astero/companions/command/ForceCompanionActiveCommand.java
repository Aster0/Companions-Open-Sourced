package me.astero.companions.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerData;

public class ForceCompanionActiveCommand implements CommandExecutor {

	private CompanionsPlugin main;
	
	public ForceCompanionActiveCommand(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	// forceactive {sender} {companion}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length >= 2)
		{

			if(sender.hasPermission("companions.admin.forceactive"))
			{
				try
				{
					Player target = Bukkit.getPlayer(args[0]);
					try
					{
						String getCompanionName = args[1];
						
						if(PlayerData.instanceOf(target).getAllCompanions().contains(getCompanionName))
						{
							PlayerData.instanceOf(target).removeCompanion();
							
							PlayerData.instanceOf(target).setActiveCompanionName(getCompanionName.toUpperCase());
							
							main.getCompanionUtil().storeActiveDB(getCompanionName, target);
							main.getCompanionUtil().storeActiveYML(target, getCompanionName);
							
							//main.getCompanions().summonCompanion(target); non-packet Companion
							
							main.getCompanionPacket().loadCompanion(target);
							
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getForceActiveSuccessfulMessage()));
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getCompanionSetForPlayerMessage()
									.replace("%companion%", getCompanionName.toUpperCase())));
						}
						else
						{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getCompanionNotOwnedMessage()
									.replace("%player%", target.getName())));
						}
					}
					catch(ArrayIndexOutOfBoundsException notStated)
					{
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getForceActiveNotSuccessfulMessage()));
					}
				}
				catch(NullPointerException notOnline)
				{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getPlayerNotOnlineMessage()));
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
