package me.astero.companions.listener.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerCache;
import me.astero.companions.companiondata.PlayerData;
import me.astero.companions.gui.OwnedMenu;

public class OwnedMenuListener implements Listener {
	
	private CompanionsPlugin main;
	
	public OwnedMenuListener(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	@EventHandler
	public void onClick (InventoryClickEvent e)
	{
		Player player = (Player) e.getWhoClicked();
		
		try
		{
			
			boolean ownedMenu = ChatColor.translateAlternateColorCodes('&', 
					e.getView().getTitle()).equals(ChatColor.translateAlternateColorCodes('&', 
							main.getFileHandler().getOwnedCompanionsTitle()));
			
			
			if(ownedMenu)
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
							Bukkit.dispatchCommand(player, main.getFileHandler().getOwnedGoBackCommand());
						}
						else
						{
							new OwnedMenu(main, player, true);
							
							//Bukkit.dispatchCommand(player, "companions owned"); // So it resets to page 1 too.
						}
					}
					else if(getCurrent.equals(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getNextPageName())))
					{
						PlayerData.instanceOf(player).setPageNumber(PlayerData.instanceOf(player).getPageNumber() + 1);
						new OwnedMenu(main, player, true);
					}
					else if(getCurrent.equals(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getCompanionDetailName())))
					{
						if(PlayerData.instanceOf(player).isToggled())
						{
							PlayerData.instanceOf(player).setToggled(false);
						}
						
						PlayerData.instanceOf(player).removeCompanion();
						
						//main.getCompanionUtil().storeActiveYML("NONE", player);
						PlayerData.instanceOf(player).setActiveCompanionName("NONE");
						
						main.getCompanionUtil().storeActiveYML(player, "NONE");
						
						player.closeInventory();
						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getRemoveCompanionMessage()));
						
						
					}		
					for(String getCompanionName : PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().keySet())
					{
						
						if(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getCompanionDetails().get(getCompanionName).getItemName()).equals(getCurrent))
						{
								if(PlayerData.instanceOf(player).isToggled())
								{
									PlayerData.instanceOf(player).setToggled(false);
								}
							
								PlayerData.instanceOf(player).removeCompanion();
								//main.getCompanionUtil().storeActiveYML(getCompanionName, player);
								PlayerData.instanceOf(player).setActiveCompanionName(getCompanionName.toUpperCase());
								
								
								main.getCompanionUtil().storeActiveYML(player, getCompanionName);
								
								
								//main.getCompanions().summonCompanion(player);
								main.getCompanionPacket().loadCompanion(player);
								player.closeInventory();
							

						}
					}
				}
			}
				
		}
		catch(NullPointerException e1)
		{
			
		}
	}


}
