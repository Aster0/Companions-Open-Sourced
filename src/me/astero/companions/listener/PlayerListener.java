package me.astero.companions.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerCache;
import me.astero.companions.companiondata.PlayerData;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;

public class PlayerListener implements Listener {
	
	private CompanionsPlugin main;
	
	public PlayerListener(CompanionsPlugin main)
	{
		this.main = main;
	}


	@EventHandler
	public void onPotionConsume(PlayerItemConsumeEvent e)
	{

		Player player = e.getPlayer();
		if(PlayerData.instanceOf(player).getActiveCompanionName() != "NONE" && PlayerData.instanceOf(player).getActiveCompanionName() != null)
		{
			PlayerData.instanceOf(player).removeCompanion();

			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {

				@Override
				public void run()
				{






					main.getCompanionUtil().delayCompanionSpawn(player);

					if (e.getItem().getItemMeta() instanceof PotionMeta) {

						Potion potion = Potion.fromItemStack(e.getItem());

						int durationLeftInTicks = 0;

						if(potion.getEffects().size() == 0)
						{
							PotionMeta potionMeta = (PotionMeta) e.getItem().getItemMeta();


							durationLeftInTicks = Integer.valueOf(player.getPotionEffect(potionMeta.getBasePotionData().getType().getEffectType()).toString().split(":")[1].split("-")[0].replace("t", ""));
						}
						else
						{
							durationLeftInTicks = Integer.valueOf(potion.getEffects()
									.toString().split(":")[1].split("-")[0].replace("t", "")
									.replace("(", ""));
						}





						Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {

							@Override
							public void run() {


								PlayerData.instanceOf(player).removeCompanion();

								main.getCompanionUtil().delayCompanionSpawn(player);

							}
						}, durationLeftInTicks);




					}






				}
			}, 20L);
		}



	}

	
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		Player player = e.getEntity();
		PlayerData.instanceOf(player).removeCompanion();
		
		/*if(PlayerData.instanceOf(player).isJustJoined())
		{
			main.getCompanionUtil().delayRemoveCompanion(player);
		}*/
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e)
	{
		Player player = e.getPlayer();
		
		PlayerData.instanceOf(player).removeCompanion();
		main.getCompanionUtil().delayCompanionSpawn(player);
		
		
		PlayerData.instanceOf(player).setRespawned(true);
		
		/*PlayerData.instanceOf(player).removeCompanion(); // End Portal removal in the END
		
		if(!PlayerData.instanceOf(player).isRespawned())
		{
			PlayerData.instanceOf(player).setRespawned(true);
		}*/
		
		
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e)
	{
		Player player = e.getPlayer();
		
		
		if(!PlayerData.instanceOf(player).isRespawned())
		{
			PlayerData.instanceOf(player).removeCompanion();
			
			main.getCompanionUtil().delayCompanionSpawn(player);
		}
		else
		{
			PlayerData.instanceOf(player).setRespawned(false);
		}
		
		
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e)
	{
		Player player = e.getPlayer();
		
		main.getCompanionUtil().saveCache(player);

		PlayerData.instanceOf(player).removeCompanion();
		
		PlayerCache.instanceOf(player.getUniqueId()).remove();
		
		
		 Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				
			  @Override
			 public void run()
			 {
				  
				PlayerData.instanceOf(player).remove();
				
		
				
			 }
		 }, 20L);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		Player player = e.getPlayer();
		
		
		if(PlayerCache.instanceOf(player.getUniqueId()).getCheckCache() == null && main.getFileHandler().isEnsureCache())
		{
			player.kickPlayer("Failed to load Companions Cache! Please rejoin.");
			
			return; // To ensure player loaded the cache because he/she has a companion.
		}

		if(PlayerCache.instanceOf(player.getUniqueId()).getCachedCompanionName() != null)
		{
			
			
			if(main.getCompanionUtil().getPatreonList().contains(player.getName()))
			{
				PlayerData.instanceOf(player).setPatreon(true);
				//PlayerData.instanceOf(player).isPatreon();
			}
			//PlayerData.instanceOf(player).setJustJoined(true);
			
		
			

				PlayerData.instanceOf(player)
					.setActiveCompanionName(PlayerCache.instanceOf(player.getUniqueId()).getCachedCompanionName());
				
				PlayerData.instanceOf(player)
					.setCompanionCoin(PlayerCache.instanceOf(player.getUniqueId()).getCachedCompanionCoins());
				
			
				main.getCompanionUtil().delayCompanionSpawn(player);
			
			/*if(!player.isDead()) - non packet companion code
			{
				main.getCompanionUtil().delayCompanionSpawn(player);
			}*/
		}
		


		
		 
		 
		 

	
		
	}
	

	
	/*@EventHandler
	public void onTeleport(PlayerTeleportEvent e)
	{
		Player player = e.getPlayer();
		
		if(player.isOnline()) // Prevents NPCs from Citizens from creating PlayerData files.
		{
				

				if(!e.getCause().toString().equals("UNKNOWN"))
				{
					if(PlayerData.instanceOf(player).getActiveCompanion() != null)
					{
						PlayerData.instanceOf(player).removeCompanionTemporarily();
						
						if(!PlayerData.instanceOf(player).isTeleport())
						{
							PlayerData.instanceOf(player).setTeleport(true);
						}
						

						

					}
	
				}

			
		
		}
		
		
		

	

		

	}*/
	
	/*@EventHandler
	public void onInteract(PlayerInteractEntityEvent e)
	{
		Player player = e.getPlayer();
		

		try
		{
			if(player.getInventory().getItemInMainHand().getType().equals(Material.NAME_TAG))
			{
				for(PlayerData pd : PlayerData.getPlayers().values())
				{
					if(e.getRightClicked().equals(pd.getActiveCompanion()))
					{
		
						e.setCancelled(true);
						
					}
				}
			}
		}
		catch(NoSuchMethodError olderVersion)
		{
			
		}
		
		


	}*/
	

	


}
