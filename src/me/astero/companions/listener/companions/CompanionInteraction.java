package me.astero.companions.listener.companions;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerData;
import net.md_5.bungee.api.ChatColor;

public class CompanionInteraction implements Listener{
	
	private CompanionsPlugin main;

	
	public CompanionInteraction(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	@EventHandler
	public void onInteract(PlayerArmorStandManipulateEvent e)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{

			/*if(e.getRightClicked().equals(PlayerData.instanceOf(player).getActiveCompanion()))
			{
				e.setCancelled(true);
				
				
				if(PlayerData.instanceOf(player).getCooldown().get("INTERACTION") == null)
				{
					PlayerData.instanceOf(player).getCooldown().put("INTERACTION", true);
					
					try
					{
						player.playSound(player.getLocation(), 
								Sound.valueOf(main.getFileHandler().getCompanionDetails()
										.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getSound()), 1.0F, 1.0F);
					}
					 catch(IllegalArgumentException soundNotFound)
					 {
						 System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.RED + "Companion Interaction sound - " + ChatColor.YELLOW + 
								 main.getFileHandler().getCompanionDetails()
									.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getSound() + ChatColor.RED +" is not found.");
					 }
					
					Random rand = new Random();
					
					String companionSpeech = main.getFileHandler().getCompanionTalk().get(rand.nextInt(main.getFileHandler().getCompanionTalk().size()));
					e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', companionSpeech));
					
					
					 Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
	
						  @Override
						 public void run()
						 {
							  
							  PlayerData.instanceOf(player).getCooldown().remove("INTERACTION");
						 }
						 
					 }, main.getFileHandler().getInteractCooldown() * 20);
				}
				
				
			}*/
			if(e.getRightClicked().equals(PlayerData.instanceOf(player).getMysteryCompanion()))
			{
				e.setCancelled(true);
			}
			
		}
	}

}
