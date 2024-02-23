package me.astero.companions.listener.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerCache;
import me.astero.companions.companiondata.PlayerData;
import me.astero.companions.economy.EconomyHandler;

public class UpgradeMenuListener implements Listener {
	
	private CompanionsPlugin main;
	
	public UpgradeMenuListener(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	@EventHandler
	public void onClick (InventoryClickEvent e)
	{
		Player player = (Player) e.getWhoClicked();
		
		try
		{
			
			boolean upgradeMenu = ChatColor.translateAlternateColorCodes('&', 
					e.getView().getTitle()).equals(ChatColor.translateAlternateColorCodes('&', 
							main.getFileHandler().getUpgradeAbilitiesTitle()));
			
			
			if(upgradeMenu)
			{
				if(e.getCurrentItem() != null)
				{
					e.setCancelled(true);		
					
					String getCurrent = e.getCurrentItem().getItemMeta().getDisplayName();
					
					
					if(getCurrent.equals(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getGoBackUDName())))
					{
						Bukkit.dispatchCommand(player, main.getFileHandler().getUpgradeGoBackCommand());
					}
					
					else if(PlayerData.instanceOf(player).getActiveCompanionName() != null)
					{
						if(!PlayerData.instanceOf(player).getActiveCompanionName().equals("NONE"))
						{
							if(getCurrent.equals(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getAbilityLevelName())))
							{
								if(e.getClick() == ClickType.LEFT)
								{
									main.getCompanionUtil().buyUpgradeAbility(player, true);
								}
								else if(e.getClick() == ClickType.RIGHT)
								{
									if(PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityLevel() != 1)
									{
										main.getCompanionUtil().buyUpgradeAbility(player, false);
									}
									else
									{
										player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getAbilityDowngradedMaxedMessage()));
									}
								}
	
								Bukkit.dispatchCommand(player, "companions upgrade");
							}
							else if(getCurrent.equals(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getAbilityLevelMName())))
							{
								if(e.getClick() == ClickType.RIGHT)
								{
									main.getCompanionUtil().buyUpgradeAbility(player, false);
								
									Bukkit.dispatchCommand(player, "companions upgrade");
								}
							}
							else if(getCurrent.equals(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getRenameCompanionName())))
							{
								main.getCompanionUtil().buyUpgradeRename(player);
							}
							else if(getCurrent.equals(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getHideCompanionName())))
							{
								main.getCompanionUtil().buyUpgradeHideName(player);
							}
							else if(getCurrent.equals(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getChangeWeaponName())))
							{
								main.getCompanionUtil().buyUpgradeChangeWeapon(player);
							}
						}
						else
						{
							noCompanionMessage(player);
						}
					}
					else
					{
						noCompanionMessage(player);
					}
				}
			}
				
		}
		catch(NullPointerException e1)
		{
			
		}
	}
	
	public void noCompanionMessage(Player player)
	{
		player.closeInventory();
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoActiveCompanionMessage()));
	}
	


}

