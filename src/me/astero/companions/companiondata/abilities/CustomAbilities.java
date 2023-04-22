package me.astero.companions.companiondata.abilities;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerCache;
import me.astero.companions.companiondata.PlayerData;

public class CustomAbilities implements Listener {
	
	private CompanionsPlugin main;
	
	public CustomAbilities(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	@EventHandler
	public void giveMiningVision(PlayerMoveEvent e)
	{
		Player player = e.getPlayer();
		
		try
		{
			if(!PlayerData.instanceOf(player).isToggled())
			{
				if(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityList().contains("MINING_VISION"))
				{
					if(player.getLocation().getY() < main.getFileHandler().getMiningYLevel())
					{
						player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 
								PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityLevel() - 1));
						PlayerData.instanceOf(player).setMiningVision(true);
					}
					else
					{
						if(PlayerData.instanceOf(player).isMiningVision())
						{
							player.removePotionEffect(PotionEffectType.NIGHT_VISION);
							PlayerData.instanceOf(player).setMiningVision(false);
						}
					}
				}
			}
		}
		catch(NullPointerException e1) {}
		}
	
	
	public void giveFly(Player player)
	{
		if(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityList().contains("FLY")
				&& !PlayerData.instanceOf(player).isToggled())
		{

				player.setAllowFlight(true);
				PlayerData.instanceOf(player).setFlyMode(true);
					
		}
		else
		{
				if(!player.getGameMode().equals(GameMode.CREATIVE) && PlayerData.instanceOf(player).isFlyMode())
				{
					player.setAllowFlight(false);
					PlayerData.instanceOf(player).setFlyMode(false);
				}
					
		}
	}
	
	@EventHandler
	public void giveFireBall(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		
		Action getAction = e.getAction();
		
		try
		{
			if(!PlayerData.instanceOf(player).isToggled())
			{
				
				Random random = new Random();
				
				int chance = random.nextInt(100);
				
				if(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityList().contains("FIREBALL"))
				{
					if(getAction.equals(Action.LEFT_CLICK_AIR))
					{
						if(chance <= main.getFileHandler().getFireballChance())
						{
							player.launchProjectile(Fireball.class);
						}
				
					}
				}
			}
		}
		catch(NullPointerException noCompanion) {}
		
	}
	
	
	@EventHandler
	public void giveLightning(EntityDamageByEntityEvent e)
	{
		
		Entity hitGiver = e.getDamager();
		Entity target = e.getEntity();
		
		
		
		Player player = null;
		
		if(hitGiver instanceof Player)
		{
			player = (Player) hitGiver;
		}
		else if(hitGiver instanceof Projectile)
		{
			Projectile proj = (Projectile) hitGiver;
			
			if(proj.getShooter() instanceof Player)
			{
				player = (Player) proj.getShooter();
			}
		}


		try
		{

				
				if(!PlayerData.instanceOf(player).isToggled())
				{
					Random random = new Random();
					
					int chance = random.nextInt(100);
					
					if(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityList().contains("LIGHTNING"))
					{
		
							if(chance <= main.getFileHandler().getLightningChance())
							{
								player.getWorld().strikeLightning(target.getLocation());
							}
					
						
					}
				}
			
		}
		catch(NullPointerException notPlayer) {}
		
	}

	@EventHandler
	public void checkDrop(PlayerDropItemEvent e)
	{
		Player player = e.getPlayer();



		try {
			if (!PlayerData.instanceOf(player).isToggled()) {
				if (main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase())
						.getAbilityList().contains("LEAP")) {

					PlayerData.instanceOf(player).setDropped(true);

					Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {

						@Override
						public void run()
						{

							PlayerData.instanceOf(player).setDropped(false);
						}

					}, 10L);

				}
			}
		}
		catch(NullPointerException noCompanion) {}
	}
	
	@EventHandler
	public void giveVectorJump(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		
		Action getAction = e.getAction();

		try
		{
			if(!PlayerData.instanceOf(player).isToggled() && !PlayerData.instanceOf(player).isDropped())
			{
				if(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase())
						.getAbilityList().contains("LEAP"))
				{

					if(player.getInventory().getItemInHand().getType().equals(Material.AIR))
					{
						if(getAction.equals(Action.LEFT_CLICK_AIR))
						{
							
							long playerCooldown; 
							
							if(PlayerData.instanceOf(player).getCooldown().get("LEAP") == null)
							{
								playerCooldown = 0;
							}
							else
							{
								playerCooldown = PlayerData.instanceOf(player).getCooldown().get("LEAP");
							}
							
							if(System.currentTimeMillis() - playerCooldown 
									> (main.getFileHandler().getVectorJumpCooldown() * 1000))
							{
								if(!player.hasPermission("companions.bypass.cooldown"))
								{
									PlayerData.instanceOf(player).getCooldown().put("LEAP", System.currentTimeMillis());
									
									
									
									/* Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
											
										  @Override
										 public void run()
										 {
											  
											 PlayerData.instanceOf(player).getCooldown().remove("LEAP");
										 }
										 
									 }, main.getFileHandler().getVectorJumpCooldown() * 20);*/
								}
		
								player.setVelocity(player.getLocation().getDirection().normalize().multiply(main.getFileHandler().getVectorMultiplier() * 
										PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityLevel()));
								
								try
								{
									for(int i = 0; i <5; i+=1)
									{
										player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 30, 0, 0, 0, 1);
									}
								}
								catch(NoClassDefFoundError olderVersion)
								{
									try
									{
						                for(int i = 0; i <30; i+=1)
						                {
				
						                	player.getWorld().playEffect(player.getLocation(), Effect.valueOf("FIREWORKS_SPARK"), 51);
						                }
									}
									catch(IllegalArgumentException particleNotFound) {}
				
								}
								
								try
								{
									player.playSound(player.getLocation(), 
											Sound.valueOf(main.getFileHandler().getVectorJumpSound()), 1.0F, 1.0F);
								}
								 catch(IllegalArgumentException soundNotFound)
								 {
									 System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.RED + "Vector Jump sound - " + ChatColor.YELLOW + 
											 main.getFileHandler().getVectorJumpSound() + ChatColor.RED +" is not found.");
								 }
							}
							else
							{
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getAbilityCoolDownMessage()));
							}
								
								
								
							
					
						}
					}
				}
			}
		}
		catch(NullPointerException noCompanion) {}
		
	}
	
	@EventHandler
	public void givePhysicalAbility (EntityDamageByEntityEvent e)
	{
		Entity hitTaker = e.getEntity();
		
		Entity hitDamager = e.getDamager();

		
		if(hitTaker instanceof Player)
		{
			Player player = (Player) hitTaker;
			

			try
			{
				if(!PlayerData.instanceOf(player).isToggled())
				{
				
					
					Random random = new Random();
					
					int chance = random.nextInt(100);
					
					
					
					String activeCompanion = PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase();
				
					for(String potionEffect : main.getCompanionUtil().getPhysicalAbilities())
					{
						if(chance <= main.getFileHandler().getPhyiscalAbilityDetails().get(potionEffect).getChance())
						{
							if(main.getFileHandler().getCompanionDetails().get(activeCompanion).getAbilityList().contains(potionEffect + "_DEFENSE_CHANCE"))
							{
			
			
								player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(potionEffect), 20* main.getFileHandler().getPhyiscalAbilityDetails().get(potionEffect).getDuration(), 
										PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(activeCompanion).getAbilityLevel() - 1));
			
								
							}
							else if(main.getFileHandler().getCompanionDetails().get(activeCompanion).getAbilityList().contains(potionEffect + "_ATTACK_CHANCE"))
							{
								LivingEntity damager = main.getCompanionUtil().getDamager(hitDamager);
								
								if(damager != null)
								{
									damager.addPotionEffect(new PotionEffect(PotionEffectType.getByName(potionEffect), 20* main.getFileHandler().getPhyiscalAbilityDetails().get(potionEffect).getDuration(), 
											PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(activeCompanion).getAbilityLevel() - 1));
								}
							}
						}
					}
					
					if(chance <= main.getFileHandler().getSpeedBurstChance())
					{
						if(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityList().contains("SPEED_BURST"))
						{
							if(!PlayerData.instanceOf(player).isSpeedBoosted())
							{
								
								player.setWalkSpeed(0.5F);
								PlayerData.instanceOf(player).setSpeedBoosted(true);
								 Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
										
									  @Override
									 public void run()
									 {
										  
										 
										  player.setWalkSpeed(0.19996406F); // Default walk
										  PlayerData.instanceOf(player).setSpeedBoosted(false);
										
										
									 }
									 
								 }, PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(activeCompanion).getAbilityLevel() * main.getFileHandler().getSpeedBurstDuration()); 
							}
							 
							
						}
					}
					
					if(chance <= main.getFileHandler().getFlingChance() + 
							(PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(activeCompanion).getAbilityLevel()  - 1 ) * main.getFileHandler().getFlingUpgrade())
					{
						if(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityList().contains("FLING"))
						{
							LivingEntity damager = main.getCompanionUtil().getDamager(hitDamager);
							
							if(damager != null)
							{
								damager.setVelocity(new Vector(0, main.getFileHandler().getFlingDistance(), 0));
							}
	
						}
					}
					
					if(chance <= main.getFileHandler().getEndermanChance() + 
							(PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(activeCompanion).getAbilityLevel()  - 1 ) * main.getFileHandler().getEndermanUpgrade())
					{
						if(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityList().contains("ENDERMAN"))
						{
							int randomX = random.nextInt(5);
	
							player.launchProjectile(EnderPearl.class, new Vector(0,1,randomX));
						}
					}
				}
			}
			catch(NullPointerException noCompanion) {}
		}
			
		

		
	}
	
	public void executeCommand(Player player)
	{
		PlayerData.instanceOf(player).setCommandInterval(0);
		
		if(PlayerData.instanceOf(player).getCommandTask().isEmpty())
		{
			for(String ability : main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityList())
			{
				if(ability.startsWith("COMMAND"))
				{
					// COMMAND/10/25/kill Astero/message/broadcastmessage
	
						
						Random random = new Random();
						String[] commandParameters = ability.replace("COMMAND/", "").split("/");
						long interval;
						try
						{
							interval = Long.valueOf(commandParameters[0]) * 1200;
						}
						catch(NumberFormatException notANumber)
						{
							interval = 60 * 1200;
							System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.YELLOW + commandParameters[0] + " is not a valid number for an interval. "
									+ "(As a result, interval has been automatically set to 60 minutes.");
						}

						
						int commandChance = Integer.parseInt(commandParameters[1]);
						
						String command = commandParameters[2].replace("%player%", player.getName().replace("%companion%", PlayerData.instanceOf(player).getActiveCompanionName()));
						
	
						PlayerData.instanceOf(player).setCommandInterval((interval / 1200)-1);
						
						
					PlayerData.instanceOf(player).getCommandTask().add(Bukkit.getScheduler().runTaskTimer(main, new Runnable() {
						 
						
						@Override
						public void run() {
							
							
							int chance = random.nextInt(100);
							
	
							if(chance <= commandChance + 
									(PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityLevel()  - 1 ) 
									* main.getFileHandler().getCommandUpgrade())
							{
								Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
								
								
								try
								{
									String message = ChatColor.translateAlternateColorCodes('&', 
											commandParameters[3]);
									
									player.sendMessage(message);
								}
								catch(ArrayIndexOutOfBoundsException e)
								{
									
								}
								
								try
								{
									String message = ChatColor.translateAlternateColorCodes('&', 
											commandParameters[4]);
									
									Bukkit.broadcastMessage(message.replace("%player%", player.getName()));
								}
								catch(ArrayIndexOutOfBoundsException e)
								{
									
								}
								
							}
						}
			
					}, interval, interval));
	
					
				}
			}
		}

	}
	
	@EventHandler
	public void giveDodge (EntityDamageByEntityEvent e)
	{
		Entity hitTaker = e.getEntity();
		
		
		
		if(hitTaker instanceof Player)
		{
			Player player = (Player) hitTaker;
			
			try
			{
				if(!PlayerData.instanceOf(player).isToggled())
				{
					Random random = new Random();
					
					int chance = random.nextInt(100);
					
					if(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityList().contains("DODGE"))
					{
						if(chance <= main.getFileHandler().getDodgeChance())
						{
							e.setCancelled(true);
						}
					}
				}
			}
			catch(NullPointerException noCompanion) {}
			
		}
	}

}
