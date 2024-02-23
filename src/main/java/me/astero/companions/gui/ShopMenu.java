package me.astero.companions.gui;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerData;
import me.astero.companions.util.InventoryBuilder;
import me.astero.companions.util.PageSystem;
import net.md_5.bungee.api.ChatColor;

public class ShopMenu {
	
	private CompanionsPlugin main;
	
	public ShopMenu(CompanionsPlugin main, Player player)
	{
		this.main = main;
		
		if(player.hasPermission("companions.player.shop"))
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
		Inventory shopMenu = new InventoryBuilder(main.getFileHandler().getCompanionShopSize(), main.getFileHandler().getCompanionShopTitle())
				.setItem(main.getFileHandler().getGoBackSlot(), main.getFileHandler().getGoBack())
				.setItem(main.getFileHandler().getNextPageSlot(), main.getFileHandler().getNextPage())
				.build();
		
		ArrayList<ItemStack> itemStackArray = new ArrayList<>();
		
		for(String getCompanionName : main.getFileHandler().getCompanionDetails().keySet()) // Iterate an ArrayList that contains all the companion names.
		{
			
			if(player.hasPermission("companions.buy." + getCompanionName) && !PlayerData.instanceOf(player).getAllCompanions().contains(getCompanionName))
			{
				itemStackArray.add(main.getFileHandler().getCompanionDetails().get(getCompanionName).getItemType());

			}
		}
		
		PageSystem ps = new PageSystem(main);
		
		ps.buildPageSystem(shopMenu, player, main.getFileHandler().getCompanionShopSize(), 2, itemStackArray);
		
		try
		{
			player.playSound(player.getLocation(), 
					Sound.valueOf(main.getFileHandler().getCompanionShopSound()), 1.0F, 1.0F);
		}
		 catch(IllegalArgumentException soundNotFound)
		 {
			 System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.RED + "Shop Menu sound - " + ChatColor.YELLOW + 
					 main.getFileHandler().getCompanionShopSound() + ChatColor.RED +" is not found.");
		 }
		
		player.openInventory(shopMenu);
	}

}
