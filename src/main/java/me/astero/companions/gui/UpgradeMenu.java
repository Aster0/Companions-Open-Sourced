package me.astero.companions.gui;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerCache;
import me.astero.companions.companiondata.PlayerData;
import me.astero.companions.util.InventoryBuilder;

public class UpgradeMenu {
	
	private CompanionsPlugin main;
	
	public UpgradeMenu(CompanionsPlugin main, Player player)
	{
		this.main = main;
		
		if(player.hasPermission("companions.player.upgrade"))
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
		ArrayList<String> setLore = new ArrayList<>();
		
		String activeCompanion;
		ItemStack abilityLevel = main.getFileHandler().getAbilityLevel();
		
		if(PlayerData.instanceOf(player).getActiveCompanionName() == null || PlayerData.instanceOf(player).getActiveCompanionName().equals("NONE"))
		{
			activeCompanion = "NONE";
		}
		else
		{
			activeCompanion = PlayerData.instanceOf(player).getActiveCompanionName();
			

			
			if(PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(activeCompanion.toLowerCase()).getAbilityLevel() == main.getFileHandler().getMaxAbilityLevel())
			{
				abilityLevel = main.getFileHandler().getAbilityLevelM();
			}

			
		}



		
		
		for(String getLore : main.getFileHandler().getUpgradeDetailsDescription())
		{
			
			try
			{
				setLore.add(ChatColor.translateAlternateColorCodes('&', getLore.replace("%active_companion%", activeCompanion)
						.replace("%companion_level%", String.valueOf(PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache()
								.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityLevel()))
						.replace("%companion_name%", PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache()
										.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomName())
						.replace("%active_companion_l%", activeCompanion.substring(0, 1) + activeCompanion.substring(1).toLowerCase())));
			}
			catch(NullPointerException firstJoin)
			{
				setLore.add(ChatColor.translateAlternateColorCodes('&', getLore.replace("%active_companion%", activeCompanion)
						.replace("%companion_level%", "NONE"))
						.replace("%companion_name%", "NONE")
						.replace("%active_companion_l%", activeCompanion.substring(0, 1) + activeCompanion.substring(1).toLowerCase()));
			}
		}

		
		ItemMeta upgradeDetailsMeta = main.getFileHandler().getUpgradeDetails().getItemMeta();
		upgradeDetailsMeta.setLore(setLore);
		
		main.getFileHandler().getUpgradeDetails().setItemMeta(upgradeDetailsMeta);
		
		

		
		
		
		Inventory upgradeMenu = new InventoryBuilder(main.getFileHandler().getUpgradeAbilitiesSize(), main.getFileHandler().getUpgradeAbilitiesTitle())
				.setItem(main.getFileHandler().getGoBackUDSlot(), main.getFileHandler().getGoBackUD())
				.setItem(main.getFileHandler().getUpgradeDetailsSlot(), main.getFileHandler().getUpgradeDetails())
				.setItem(main.getFileHandler().getAbilityLevelSlot(), abilityLevel)
				.setItem(main.getFileHandler().getRenameCompanionSlot(), main.getFileHandler().getRenameCompanion())
				.setItem(main.getFileHandler().getHideCompanionSlot(), main.getFileHandler().getHideCompanionN())
				.setItem(main.getFileHandler().getChangeWeaponSlot(), main.getFileHandler().getChangeWeapon())
				.build();
		
		try
		{
			player.playSound(player.getLocation(), 
					Sound.valueOf(main.getFileHandler().getUpgradeAbilitiesSound()), 1.0F, 1.0F);
		}
		 catch(IllegalArgumentException soundNotFound)
		 {
			 System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.RED + "Upgrade Menu sound - " + ChatColor.YELLOW + 
					 main.getFileHandler().getUpgradeAbilitiesSound() + ChatColor.RED +" is not found.");
		 }
		
		player.openInventory(upgradeMenu);
	}

}
