package me.astero.companions.listener;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerData;

public class VanishListener implements Listener {
	
	private CompanionsPlugin main;
	
	public VanishListener(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	@EventHandler
	public void onVanish(PlayerCommandPreprocessEvent e)
	{
		Player player = e.getPlayer();
		
		
		if(player.hasPermission(main.getFileHandler().getVanishPerms()))
		{
			if(PlayerData.instanceOf(player).getActiveCompanion() != null)
			{
				if(Arrays.asList(main.getFileHandler().getVanishCommands()).contains(e.getMessage()))
				{
					PlayerData.instanceOf(player).removeCompanion();
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +
							main.getFileHandler().getPlayerInVanishMessage()));
				}
			}
		}
		
	}

}
