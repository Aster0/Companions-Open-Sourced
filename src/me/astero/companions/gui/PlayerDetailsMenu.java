package me.astero.companions.gui;

import org.bukkit.entity.Player;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerData;

public class PlayerDetailsMenu {
	
	private CompanionsPlugin main;
	
	public PlayerDetailsMenu(CompanionsPlugin main, Player player, boolean self)
	{
		this.main = main;
		

		openInventory(player, self);


	}
	
	private void openInventory(Player player, boolean self)
	{
		
		Player target = PlayerData.instanceOf(player).getPlayerDetailsTarget();
		main.getCompanionUtil().ownedCompanionsViewer(player, target,
				main.getFileHandler().getPlayerDetailsSound(),
				main.getFileHandler().getPlayerDetailsTitle().replace("%target%", PlayerData.instanceOf(player).getPlayerDetailsTarget().getName().toUpperCase())
						.replace("%target_l%", target.getName().substring(0, 1).toUpperCase() + target.getName().substring(1).toLowerCase())
				, self);
	}

}
