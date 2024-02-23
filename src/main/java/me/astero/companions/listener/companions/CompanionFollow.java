package me.astero.companions.listener.companions;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerData;
	
public class CompanionFollow implements Listener {
	
	private CompanionsPlugin main;
	


	
	public CompanionFollow(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerMove (PlayerMoveEvent e)
	{
		Player player = e.getPlayer();
		
		
		main.getCompanionPacket().companionFollow(player);
			

		
		/*if(PlayerData.instanceOf(player).getActiveCompanion() != null)
		{
			double x = Math.cos(Math.toRadians(player.getLocation().getYaw() - 180));
			double z = Math.sin(Math.toRadians(player.getLocation().getYaw() - 180));
			
			
			Location loc = player.getLocation().add(x, main.getFileHandler().getCompanionHeight(), z);
			
			PlayerData.instanceOf(player).getActiveCompanion().teleport(loc);


		}
		

		if(PlayerData.instanceOf(player).isRespawned() || PlayerData.instanceOf(player).isTeleport())
		{

			PlayerData.instanceOf(player).setStepsCount(PlayerData.instanceOf(player).getStepsCount() + 1);

			if(PlayerData.instanceOf(player).getStepsCount() == 5) // So some plugin don't instantly move the player after TP.
			{
				
				PlayerData.instanceOf(player).setRespawned(false);
				PlayerData.instanceOf(player).setTeleport(false);
				
				main.getCompanions().summonCompanion(player);

				
				PlayerData.instanceOf(player).setStepsCount(0);

			}

			


		}
		

		

		
		
		
		if(PlayerData.instanceOf(player).isSpeedBoosted())
		{
			try
			{

				player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 1, 0, 0, 0, 1);
				
			}
			catch(NoClassDefFoundError olderVersion)
			{
				try
				{


                	player.getWorld().playEffect(player.getLocation(), Effect.valueOf("FIREWORKS_SPARK"), 1);
	                
				}
				catch(IllegalArgumentException particleNotFound) {}
			}
		}*/
		

		
		
		
	}

}
