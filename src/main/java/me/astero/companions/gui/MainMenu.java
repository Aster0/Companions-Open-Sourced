package me.astero.companions.gui;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.util.InventoryBuilder;


public class MainMenu {
	
	private CompanionsPlugin main;
	
	public MainMenu(CompanionsPlugin main, Player player)
	{
		this.main = main;
		
		if(player.hasPermission("companions.player.menu"))
		{
			openInventory(player);
		}
		else
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoPermissionMessage()));
		}
	}
	
	private void openInventory(Player player)
	{
		Inventory mainMenu = new InventoryBuilder(main.getFileHandler().getOpenCompanionsSize(), main.getFileHandler().getOpenCompanionsTitle())
				.setItem(main.getFileHandler().getCompanionShopSlot(), main.getFileHandler().getCompanionShop())
				.setItem(main.getFileHandler().getOwnedCompanionsSlot(), main.getFileHandler().getOwnedCompanions())
				.setItem(main.getFileHandler().getUpgradeAbilitiesSlot(), main.getFileHandler().getUpgradeAbilities())
				.build();
		
		try
		{
			player.playSound(player.getLocation(), 
					Sound.valueOf(main.getFileHandler().getOpenCompanionsSound()), 1.0F, 1.0F);
		}
		 catch(IllegalArgumentException soundNotFound)
		 {
			 System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.RED + "Main Menu sound - " + ChatColor.YELLOW + 
					 main.getFileHandler().getOpenCompanionsSound()+ ChatColor.RED +" is not found.");
		 }

		
		player.openInventory(mainMenu);
	}

}
