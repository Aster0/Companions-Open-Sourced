package me.astero.companions.listener.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerData;
import me.astero.companions.gui.OwnedMenu;
import me.astero.companions.gui.PlayerDetailsMenu;

public class PlayerDetailsMenuListener implements Listener {

	private CompanionsPlugin main;
	
	public PlayerDetailsMenuListener(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	@EventHandler
	public void onClick (InventoryClickEvent e)
	{
		Player player = (Player) e.getWhoClicked();
		
		try
		{
			
			boolean playerDetailsMenu = ChatColor.translateAlternateColorCodes('&', 
					e.getView().getTitle()).equals(ChatColor.translateAlternateColorCodes('&', 
							main.getFileHandler().getPlayerDetailsTitle().replace("%target%",
									PlayerData.instanceOf(player).getPlayerDetailsTarget().getName().toUpperCase())
							.replace("%target_l%", PlayerData.instanceOf(player).getPlayerDetailsTarget().getName().substring(0, 1).toUpperCase() + PlayerData.instanceOf(player).getPlayerDetailsTarget().getName().substring(1).toLowerCase())));
			
			
			if(playerDetailsMenu)
			{
				if(e.getCurrentItem() != null)
				{
					e.setCancelled(true);		
					
					String getCurrent = e.getCurrentItem().getItemMeta().getDisplayName();
					
					
					if(getCurrent.equals(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getGoBackName())))
					{
						PlayerData.instanceOf(player).setPageNumber(PlayerData.instanceOf(player).getPageNumber() - 1);
						
						if(PlayerData.instanceOf(player).getPageNumber() == 0)
						{
							player.closeInventory();
						}
						else
						{
							new PlayerDetailsMenu(main, player, false);
						}
					}
					else if(getCurrent.equals(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getNextPageName())))
					{
						PlayerData.instanceOf(player).setPageNumber(PlayerData.instanceOf(player).getPageNumber() + 1);
						new PlayerDetailsMenu(main, player, false);
					}
				}
			}
				
		}
		catch(NullPointerException e1)
		{
			
		}
	}

}
