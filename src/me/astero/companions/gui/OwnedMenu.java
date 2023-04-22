package me.astero.companions.gui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.astero.companions.CompanionsPlugin;

public class OwnedMenu {
	
	private CompanionsPlugin main;
	
	public OwnedMenu(CompanionsPlugin main, Player player, boolean self)
	{
		this.main = main;
		
		if(player.hasPermission("companions.player.owned"))
		{
			openInventory(player, self);
		}
		else
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoPermissionMessage()));
		}
	}
	
	private void openInventory(Player player, boolean self)
	{
		/*ArrayList<String> setLore = new ArrayList<>();
		
		String activeCompanion;
		
		if(PlayerData.instanceOf(player).getActiveCompanionName() == null)
		{
			activeCompanion = "NONE";
		}
		else
		{
			activeCompanion = PlayerData.instanceOf(player).getActiveCompanionName();
		}
		for(String getLore : main.getFileHandler().getCompanionDetailDescription())
		{
			setLore.add(ChatColor.translateAlternateColorCodes('&', getLore.replace("%active_companion%", activeCompanion)));
		}

		
		ItemMeta companionDetailMeta = main.getFileHandler().getCompanionDetail().getItemMeta();
		companionDetailMeta.setLore(setLore);
		
		main.getFileHandler().getCompanionDetail().setItemMeta(companionDetailMeta);
		
		Inventory ownedMenu = new InventoryBuilder(main.getFileHandler().getOwnedCompanionsSize(), main.getFileHandler().getOwnedCompanionsTitle())
				.setItem(main.getFileHandler().getGoBackSlot(), main.getFileHandler().getGoBack())
				.setItem(main.getFileHandler().getNextPageSlot(), main.getFileHandler().getNextPage())
				.setItem(main.getFileHandler().getCompanionDetailSlot(), main.getFileHandler().getCompanionDetail())
				.build();
		
		ArrayList<ItemStack> itemStackArray = new ArrayList<>();
		
		for(String getCompanionName : PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().keySet())
		{
			itemStackArray.add(main.getFileHandler().getCompanionDetails().get(getCompanionName.toLowerCase()).getOwnedItemType());
		}
		
		PageSystem ps = new PageSystem(main);
		
		ps.buildPageSystem(ownedMenu, player, main.getFileHandler().getOwnedCompanionsSize(), 3, itemStackArray);
		
		try
		{
			player.playSound(player.getLocation(), 
					Sound.valueOf(main.getFileHandler().getOwnedCompanionsSound()), 1.0F, 1.0F);
		}
		 catch(IllegalArgumentException soundNotFound)
		 {
			 System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.RED + "Owned Menu sound - " + ChatColor.YELLOW + 
					 main.getFileHandler().getOwnedCompanionsSound() + ChatColor.RED +" is not found.");
		 }
		
		player.openInventory(ownedMenu);*/
		
		
		main.getCompanionUtil().ownedCompanionsViewer(player, null,
				main.getFileHandler().getOwnedCompanionsSound(),
				main.getFileHandler().getOwnedCompanionsTitle()
				, self);
	}

}
