package me.astero.companions.companiondata;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.EulerAngle;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.filemanager.CompanionDetails;
import me.astero.companions.util.ItemBuilderUtil;
import net.md_5.bungee.api.ChatColor;

public class Companions {
	
	private ArmorStand companion = null;

	private CompanionsPlugin main;

	private ItemStack companionSkull, companionChest;
	
	public Companions(CompanionsPlugin main)
	{
		this.main = main;
	}

	public void summonCompanion(Player player)
	{
		try
		{
			if(!PlayerData.instanceOf(player).isRespawned() && !PlayerData.instanceOf(player).isTeleport())
			{
					if(!PlayerData.instanceOf(player).getActiveCompanionName().equals("NONE") && !PlayerData.instanceOf(player).isToggled() && 
							!PlayerData.instanceOf(player).isMounted())
					{
						if(!main.getFileHandler().getDisabledWorlds().contains(player.getWorld().getName()))
						{
							
							if(PlayerData.instanceOf(player).isPatreon() && PlayerData.instanceOf(player).isParticle())
							{
		
								
								giveParticle(player);
								
							}
							double x = Math.cos(Math.toRadians(player.getLocation().getYaw() - 180));
							double z = Math.sin(Math.toRadians(player.getLocation().getYaw() - 180));
							
							
							companion = player.getWorld().spawn(player.getLocation().add(x, main.getFileHandler().getCompanionDetails()
									.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getY(), z), ArmorStand.class); 
							PlayerData.instanceOf(player).setActiveCompanion(companion);
							
							
							PlayerData.instanceOf(player).getActiveCompanion().setBasePlate(false);
							PlayerData.instanceOf(player).getActiveCompanion().setVisible(false);
							PlayerData.instanceOf(player).getActiveCompanion().setCanPickupItems(false);
							PlayerData.instanceOf(player).getActiveCompanion().setSmall(true);
							PlayerData.instanceOf(player).getActiveCompanion().setGravity(false);
							
				
					
							setSkull(player);
							setChestPlate(player);
							setWeapon(player);
							setNameVisible(player);
							setCustomName(player);
	
							
							setArmorPose(player);
							
							
							main.getPotionEffectAbility().give(player);
							main.getCustomAbility().giveFly(player);
							main.getCustomAbility().executeCommand(player);
							main.getAnimation().executeAnimation(player);
	
						}
						else
						{
							PlayerData.instanceOf(player).toggleCompanion();
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getPlayerInDisabledWorldMessage()));
							
						}
					}
			}
			

			
			
		}
		catch(NullPointerException noActiveCompanion) {}

	}
	
	public void giveParticle(Player player)
	{
		PlayerData.instanceOf(player).setParticleTask(Bukkit.getScheduler().runTaskTimerAsynchronously(main, new Runnable() {
			 
			
			@Override
			public void run() {

				double x = Math.cos(Math.toRadians(player.getLocation().getYaw() - 180));
				double z = Math.sin(Math.toRadians(player.getLocation().getYaw() - 180));
				
				

				try
				{

					
	                for(int i = 0; i <40; i+=5)
	                {
	    				Location newLoc = player.getLocation().add(x, main.getFileHandler().getCompanionDetails()
								.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getY() + 0.4, z);
	    				

	                    //Location newLoc = PlayerData.instanceOf(player).getActiveCompanion().getLocation().add(0,0.4,0); non packet companion
	                	
	                    newLoc.setZ(newLoc.getZ() + Math.cos(i)*0.5);
	                    newLoc.setX(newLoc.getX() + Math.sin(i)*0.5);
	                    
						player.getWorld().spawnParticle(Particle.DRIP_LAVA, newLoc, 1, 0, 0, 0, 10);
	                }
					
				}
				catch(NoClassDefFoundError olderVersion)
				{
					try
					{
	
		                for(int i = 0; i <40; i+=5)
		                {
		    				Location newLoc = player.getLocation().add(x, main.getFileHandler().getCompanionDetails()
									.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getY() + 0.4, z);
		    				
		    				//Location newLoc = loc.add(0,0.4,0);
		    				
		                   // Location newLoc = PlayerData.instanceOf(player).getActiveCompanion().getLocation().add(0,0.4,0); non packet companion
		                	//Location newLoc = player.getLocation().add(0,0.4,0);
		                    newLoc.setZ(newLoc.getZ() + Math.cos(i)*0.5);
		                    newLoc.setX(newLoc.getX() + Math.sin(i)*0.5);
		                    player.getWorld().playEffect(newLoc, Effect.valueOf("LAVADRIP"), 1);
		                }
		                
					}
					catch(IllegalArgumentException particleNotFound) {}
				}

	
				
				
			}

		}, 60L, 60L));

	}
	
	public void summonMysteryCompanion(Player player, String companionName, Location loc)
	{
		if(PlayerData.instanceOf(player).getMysteryCompanion() == null)
		{
			player.getInventory().removeItem(main.getFileHandler().getCompanionToken());
			
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "givecompanion " + player.getName() + " " + companionName);
			
			companion = player.getWorld().spawn(loc.add(0, 1, 0), ArmorStand.class); 
			PlayerData.instanceOf(player).setMysteryCompanion(companion);
			

			
			PlayerData.instanceOf(player).getMysteryCompanion().setBasePlate(false);
			PlayerData.instanceOf(player).getMysteryCompanion().setVisible(false);
			PlayerData.instanceOf(player).getMysteryCompanion().setCanPickupItems(false);
			PlayerData.instanceOf(player).getMysteryCompanion().setSmall(true);
			PlayerData.instanceOf(player).getMysteryCompanion().setGravity(false);
			
			PlayerData.instanceOf(player).getMysteryCompanion().teleport(PlayerData.instanceOf(player).getMysteryCompanion().getLocation());
			
			
			
			setSkull(player, companionName);
			setChestPlate(player, companionName);
			setWeapon(player, companionName);
			PlayerData.instanceOf(player).getMysteryCompanion().setCustomNameVisible(true);
			setCustomName(player, companionName);
			
			setArmorPose(player, companionName);
			


			
			
			try
			{
				for(int i = 0; i <5; i+=1)
				{
					player.getWorld().spawnParticle(Particle.valueOf(main.getFileHandler().getCompanionTokenParticle()), loc.add(0,1,0), 30, 0, 0, 0, 1);
				}
			}
			catch(NoClassDefFoundError olderVersion)
			{
				try
				{
	                for(int i = 0; i <30; i+=1)
	                {

	                	player.getWorld().playEffect(loc.add(0,1,0), Effect.valueOf(main.getFileHandler().getCompanionTokenParticle()), 51);
	                }
				}
				catch(IllegalArgumentException particleNotFound) {}
			}
			
			
			try
			{
				player.playSound(player.getLocation(), 
						Sound.valueOf(main.getFileHandler().getCompanionTokenSound()), 1.0F, 1.0F);
			}
			 catch(IllegalArgumentException soundNotFound)
			 {
				 System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.RED + "Main Menu sound - " + ChatColor.YELLOW + 
						 main.getFileHandler().getCompanionTokenSound() + ChatColor.RED +" is not found.");
			 }
			
			 Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
					
				  @Override
				 public void run()
				 {
					  
					 
						PlayerData.instanceOf(player).getMysteryCompanion().remove();
						PlayerData.instanceOf(player).setMysteryCompanion(null);
					
				 }
				 
			 }, 60L); 
		}

		
		
	}
	
	private void setSkull(Player player)
	{
		try
		{
			companionSkull = new ItemBuilderUtil(Material.PLAYER_HEAD, PlayerData.instanceOf(player).getActiveCompanionName().toUpperCase() + "'S HEAD", 1).build();
			
			PlayerData.instanceOf(player).getActiveCompanion().setInvulnerable(true);
		}
		catch(NoSuchFieldError | NoSuchMethodError error)
		{
			companionSkull = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
		}
		
		SkullMeta companionHeadMeta = (SkullMeta) companionSkull.getItemMeta();	
		
		main.getCompanionUtil().setSkull( main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getPlayerSkull(), companionHeadMeta,
				main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData());

		companionSkull.setItemMeta(companionHeadMeta);
		
		PlayerData.instanceOf(player).getActiveCompanion().setHelmet(companionSkull);
	}
	
	private void setSkull(Player player, String companionName)
	{
		try
		{
			companionSkull = new ItemBuilderUtil(Material.PLAYER_HEAD, companionName.toUpperCase() + "'S HEAD", 1).build();
			
			PlayerData.instanceOf(player).getMysteryCompanion().setInvulnerable(true);
		}
		catch(NoSuchFieldError | NoSuchMethodError error)
		{
			companionSkull = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
		}
		
		SkullMeta companionHeadMeta = (SkullMeta) companionSkull.getItemMeta();	
		
		String url = main.getFileHandler().getCompanionDetails().get(companionName.toLowerCase()).getPlayerSkull();
		
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		
		profile.getProperties().put("textures", new Property("textures", url));
		
		try
		{
			Field profileField = companionHeadMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(companionHeadMeta, profile);
		}
		catch(IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error)
		{
			error.printStackTrace();
		}

		companionSkull.setItemMeta(companionHeadMeta);
		
		PlayerData.instanceOf(player).getMysteryCompanion().setHelmet(companionSkull);
	}
	
	private void setChestPlate(Player player)
	{
		
		if(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getChestplate().equals("LEATHER_CHESTPLATE"))
		{
			companionChest = new ItemStack(Material.LEATHER_CHESTPLATE);
			LeatherArmorMeta companionLeatherChestMeta = (LeatherArmorMeta) companionChest.getItemMeta();
			companionLeatherChestMeta.setColor(Color.fromRGB(
					main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getLeatherColorRed(),
					main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getLeatherColorGreen(),
					main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getLeatherColorBlue()));
			
			companionChest.setItemMeta(companionLeatherChestMeta);
		}
		else
		{
			try
			{
				companionChest = new ItemStack(Material.valueOf(
						main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getChestplate()));

			}
			catch(IllegalArgumentException armorNotFound)
			{
				
				companionChest = new ItemStack(Material.LEGACY_DIAMOND_CHESTPLATE);
				System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.YELLOW + main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getChestplate()
						+ ChatColor.RED + " ChestPlate Material is not found - "
						+ "Please check if the material name is for the correct Minecraft server version. " + ChatColor.YELLOW + "(A replacement ChestPlate will be used)");
			}
			
			ItemMeta companionChestMeta = (ItemMeta) companionChest.getItemMeta();
			
			companionChest.setItemMeta(companionChestMeta);
		}

		PlayerData.instanceOf(player).getActiveCompanion().setChestplate(companionChest);
	}
	
	private void setChestPlate(Player player, String companionName)
	{
		
		if(main.getFileHandler().getCompanionDetails().get(companionName.toLowerCase()).getChestplate().equals("LEATHER_CHESTPLATE"))
		{
			companionChest = new ItemStack(Material.LEATHER_CHESTPLATE);
			LeatherArmorMeta companionLeatherChestMeta = (LeatherArmorMeta) companionChest.getItemMeta();
			companionLeatherChestMeta.setColor(Color.fromRGB(
					main.getFileHandler().getCompanionDetails().get(companionName.toLowerCase()).getLeatherColorRed(),
					main.getFileHandler().getCompanionDetails().get(companionName.toLowerCase()).getLeatherColorGreen(),
					main.getFileHandler().getCompanionDetails().get(companionName.toLowerCase()).getLeatherColorBlue()));
			
			companionChest.setItemMeta(companionLeatherChestMeta);
		}
		else
		{
			try
			{
				companionChest = new ItemStack(Material.valueOf(
						main.getFileHandler().getCompanionDetails().get(companionName.toLowerCase()).getChestplate()));

			}
			catch(IllegalArgumentException armorNotFound)
			{
				
				companionChest = new ItemStack(Material.LEGACY_DIAMOND_CHESTPLATE);
				System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.YELLOW + main.getFileHandler().getCompanionDetails().get(companionName.toLowerCase()).getChestplate()
						+ ChatColor.RED + " ChestPlate Material is not found - "
						+ "Please check if the material name is for the correct Minecraft server version. " + ChatColor.YELLOW + "(A replacement ChestPlate will be used)");
			}
			
			ItemMeta companionChestMeta = (ItemMeta) companionChest.getItemMeta();
			
			companionChest.setItemMeta(companionChestMeta);
		}

		PlayerData.instanceOf(player).getMysteryCompanion().setChestplate(companionChest);
	}
	
	public void setWeapon(Player player)
	{

			ItemStack weapon = PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomWeapon();
			
			if(weapon != null)
			{
				PlayerData.instanceOf(player).getActiveCompanion().setItemInHand(weapon);
			}
		
	}
	
	public void setWeapon(Player player, String companionName)
	{

			ItemStack weapon;
			String weaponName = main.getFileHandler().getCompanionDetails().get(companionName.toLowerCase()).getWeapon();
			
			if(!weaponName.equals("NONE"))
			{
				 try
				 {
					 weapon = new ItemBuilderUtil(
							 Material.valueOf(weaponName),
							 "",
							 1)
							 .build();
				 }
				 catch(IllegalArgumentException itemNotFound)
				 {
					 weapon = new ItemBuilderUtil(
							 Material.AIR,
							 "",
							 1)
							 .build();
				 }
				
				if(weapon != null)
				{
					PlayerData.instanceOf(player).getMysteryCompanion().setItemInHand(weapon);
				}
			}
		
	}
	
	public void setCustomName(Player player)
	{
		PlayerData.instanceOf(player).getActiveCompanion().setCustomName(ChatColor.translateAlternateColorCodes('&', PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache()
				.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomName()));
	}
	
	public void setCustomName(Player player, String companionName)
	{
		PlayerData.instanceOf(player).getMysteryCompanion().setCustomName(ChatColor.translateAlternateColorCodes('&', 
				main.getFileHandler().getCompanionDetails().get(companionName.toLowerCase()).getName()));
	}
	
	public void setNameVisible(Player player)
	{
		PlayerData.instanceOf(player).getActiveCompanion().setCustomNameVisible(PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache()
				.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).isNameVisible());
	}
	
	public void setArmorPose(Player player)
	{
		CompanionDetails cd = main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase());
		
		PlayerData.instanceOf(player).getActiveCompanion().setRightArmPose(new EulerAngle(
				Math.toRadians(cd.getRightArmPose1()), 
				Math.toRadians(cd.getRightArmPose2()),
				Math.toRadians(cd.getRightArmPose3())));
		
		PlayerData.instanceOf(player).getActiveCompanion().setHeadPose(new EulerAngle(
				Math.toRadians(cd.getHeadPose1()),
				Math.toRadians(cd.getHeadPose2()),
				Math.toRadians(cd.getHeadPose3())));
		
		PlayerData.instanceOf(player).getActiveCompanion().setBodyPose(new EulerAngle(
				Math.toRadians(cd.getBodyPose1()), 
				Math.toRadians(cd.getBodyPose2()), 
				Math.toRadians(cd.getBodyPose3())));
		
		PlayerData.instanceOf(player).getActiveCompanion().setLeftArmPose(new EulerAngle(
				Math.toRadians(cd.getLeftArmPose1()),
				Math.toRadians(cd.getLeftArmPose2()), 
				Math.toRadians(cd.getLeftArmPose3())));
		
		PlayerData.instanceOf(player).getActiveCompanion().setLeftLegPose(new EulerAngle(Math.toRadians(178), Math.toRadians(0), Math.toRadians(0)));
		PlayerData.instanceOf(player).getActiveCompanion().setRightLegPose(new EulerAngle(Math.toRadians(178), Math.toRadians(0), Math.toRadians(0)));
	}
	
	public void setArmorPose(Player player, String companionName)
	{
		CompanionDetails cd = main.getFileHandler().getCompanionDetails().get(companionName.toLowerCase());
		
		PlayerData.instanceOf(player).getMysteryCompanion().setRightArmPose(new EulerAngle(
				Math.toRadians(cd.getRightArmPose1()), 
				Math.toRadians(cd.getRightArmPose2()),
				Math.toRadians(cd.getRightArmPose3())));
		
		PlayerData.instanceOf(player).getMysteryCompanion().setHeadPose(new EulerAngle(
				Math.toRadians(cd.getHeadPose1()),
				Math.toRadians(cd.getHeadPose2()),
				Math.toRadians(cd.getHeadPose3())));
		
		PlayerData.instanceOf(player).getMysteryCompanion().setBodyPose(new EulerAngle(
				Math.toRadians(cd.getBodyPose1()), 
				Math.toRadians(cd.getBodyPose2()), 
				Math.toRadians(cd.getBodyPose3())));
		
		PlayerData.instanceOf(player).getMysteryCompanion().setLeftArmPose(new EulerAngle(
				Math.toRadians(cd.getLeftArmPose1()),
				Math.toRadians(cd.getLeftArmPose2()), 
				Math.toRadians(cd.getLeftArmPose3())));
		
		PlayerData.instanceOf(player).getMysteryCompanion().setLeftLegPose(new EulerAngle(Math.toRadians(178), Math.toRadians(0), Math.toRadians(0)));
		PlayerData.instanceOf(player).getMysteryCompanion().setRightLegPose(new EulerAngle(Math.toRadians(178), Math.toRadians(0), Math.toRadians(0)));
	}

}
