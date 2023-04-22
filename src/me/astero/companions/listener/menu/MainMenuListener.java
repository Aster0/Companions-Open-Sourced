package me.astero.companions.listener.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.astero.companions.CompanionsPlugin;

public class MainMenuListener implements Listener {

	private CompanionsPlugin main;
	
	public MainMenuListener(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	@EventHandler
	public void onClick (InventoryClickEvent e)
	{
		Player player = (Player) e.getWhoClicked();
		
		try
		{
			
			boolean mainMenu = ChatColor.translateAlternateColorCodes('&', 
					e.getView().getTitle()).equals(ChatColor.translateAlternateColorCodes('&', 
							main.getFileHandler().getOpenCompanionsTitle()));
			
			
			if(mainMenu)
			{
				if(e.getCurrentItem() != null)
				{
					e.setCancelled(true);		
					
					String getCurrent = e.getCurrentItem().getItemMeta().getDisplayName();
					
					
					if(getCurrent.equals(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getCompanionShopName())))
					{
						Bukkit.dispatchCommand(player, "companions shop");
					}
					else if(getCurrent.equals(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getOwnedCompanionsName())))
					{
						Bukkit.dispatchCommand(player, "companions owned");
					}
					else if(getCurrent.equals(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getUpgradeAbilitiesName())))
					{
						Bukkit.dispatchCommand(player, "companions upgrade");
					}
				}
			}
				
		}
		catch(NullPointerException e1)
		{
			
		}
	}

}
