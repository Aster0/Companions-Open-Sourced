package me.astero.companions.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.astero.companions.CompanionsPlugin;

public class CompanionToken implements Listener {
	
	private CompanionsPlugin main;
	
	public CompanionToken(CompanionsPlugin main)
	{
		this.main = main;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		Action action = e.getAction();
		
		Player player = e.getPlayer();
		
		if(player.getInventory().getItemInHand().isSimilar(main.getFileHandler().getCompanionToken()))
		{
			e.setCancelled(true);
			if(action.equals(Action.RIGHT_CLICK_BLOCK))
			{
				List<String> companionNames = new ArrayList<>();
				Random random = new Random();
				Location loc = e.getClickedBlock().getLocation();
				
				
				for(String companionName : main.getFileHandler().getCompanionDetails().keySet())
				{
					companionNames.add(companionName);
				}
				

				
				main.getCompanions().summonMysteryCompanion(player, companionNames.get(random.nextInt(companionNames.size())), loc);


			}
		}
	}
}
