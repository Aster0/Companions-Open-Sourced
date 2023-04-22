package me.astero.companions.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import lombok.Getter;
import lombok.Setter;
import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.CustomCompanion;
import me.astero.companions.companiondata.PlayerCache;
import me.astero.companions.companiondata.PlayerData;
import me.astero.companions.economy.EconomyHandler;
import me.astero.companionsapi.api.CAPI;
import net.md_5.bungee.api.ChatColor;

public class CompanionUtil {
	
	private CompanionsPlugin main;
	@Setter @Getter private String prefix;
	
	@Getter private List<String> patreonList = new ArrayList<>();
	private String[] customAbilities = { "FLY", "FIREBALL", "DODGE", "LEAP", "LIGHTNING", "MINING_VISION", "SPEED_BURST", "FLING", "ENDERMAN", "NONE"};
	private String[] physicalAbilities = {"REGENERATION", "INCREASE_DAMAGE", "GLOWING", "INVISIBILITY", "SPEED", "FIRE_RESISTANCE", "JUMP",
			"DAMAGE_RESISTANCE", "FAST_DIGGING", "ABSORPTION", "LUCK", "WITHER", "SLOW", "SLOW_DIGGING", "CONFUSION", "WEAKNESS", "LEVITATION", "POISON", "WATER_BREATHING",
			"DOLPHINS_GRACE", "HUNGER"};
	
	
	public CompanionUtil(CompanionsPlugin main)
	{
		this.main = main;
		prefix = main.getFileManager().getMessagesData().getString("messages.prefix");
	}
	
	
	public void removeParticles(Player player)
	{
		if(PlayerData.instanceOf(player).getParticleTask() != null)
		{
			PlayerData.instanceOf(player).getParticleTask().cancel();
			PlayerData.instanceOf(player).setParticleTask(null);
		}
	}
	
	public void stopCommandAbility(Player player)
	{
		if(!PlayerData.instanceOf(player).getCommandTask().isEmpty())
		{
			for(BukkitTask tasks : PlayerData.instanceOf(player).getCommandTask())
			{
				tasks.cancel();
			}

			PlayerData.instanceOf(player).getCommandTask().clear();
		}
	}
	

	public void delayCompanionSpawn(Player player)
	{

		 Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				
			  @Override
			 public void run()
			 {
				  
				  	
					//main.getCompanions().summonCompanion(player); - non packet companion
				  
				  main.getCompanionPacket().loadCompanion(player);
	

				
			 }
			 
		 }, 15L); // Runnable to wait for the player to fully JOIN the server then summon the companion.
		
	}
	
	public void delayRemoveCompanion(Player player)
	{

		 Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				
			  @Override
			 public void run()
			 {
				  
				  	
					PlayerData.instanceOf(player).removeCompanion();
					PlayerData.instanceOf(player).setJustJoined(false);

	

				
			 }
			 
		 }, 15L); // Runnable to wait for the player to fully JOIN the server then summon the companion.
		
	}
	

	
	
	public void upgradeAbility(Player player, boolean withdraw, boolean check, boolean upgrade)
	{


		if(check)
		{
			if(!player.hasPermission("companions.upgrade.ability"))
			{
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoUpgradeBuyPermissionMessage()));
				return;
			}
			
			if(main.getFileHandler().getAbilityRawLevelPrice().contains("C"))
			{
				
				long amount = main.getFileHandler().getAbilityLevelPrice();
				
				if(main.getCompanionCoin().has(player, amount))
					main.getCompanionCoin().withdrawPlayer(player, amount);
				
				else
				{
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNotEnoughMoneyMessage()
							.replace("%price%", String.valueOf(main.getFileHandler().getAbilityLevelPrice()))));
					
					return;
				}
			}
			else
			{
				if(main.getFileHandler().isVault())
				{
					if(withdraw && EconomyHandler.getEconomy().has(player, main.getFileHandler().getAbilityLevelPrice()))
					{
						EconomyHandler.getEconomy().withdrawPlayer(player, main.getFileHandler().getAbilityLevelPrice());
					}
					else if(withdraw && !EconomyHandler.getEconomy().has(player, main.getFileHandler().getAbilityLevelPrice()))
					{
						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNotEnoughMoneyMessage()
								.replace("%price%", String.valueOf(main.getFileHandler().getAbilityLevelPrice()))));
						return;
					}
				}
			}
		}


		PlayerData.instanceOf(player).removeCompanion();

		
		String getCompanionName = PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase();
		int abilityLevel = PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(getCompanionName).getAbilityLevel();
		
		int abilityIncrement;
		
		if(upgrade)
		{
			abilityIncrement = +1;
			
			if(CAPI.getSpawnListener() != null)
				CAPI.getSpawnListener().onCompanionAbilityUpgrade(main.getFileHandler().getCompanionDetails().get(
						PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityList(), player, abilityLevel);
		}
		else
		{
			abilityIncrement = -1;
			
			if(CAPI.getSpawnListener() != null)
				CAPI.getSpawnListener().onCompanionAbilityDeUpgrade(main.getFileHandler().getCompanionDetails().get(
						PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityList(), player, abilityLevel);
		}
		
		PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(getCompanionName).setAbilityLevel(abilityLevel + abilityIncrement);

		storeYML(getCompanionName, player, abilityLevel + abilityIncrement);
		
		//main.getCompanions().summonCompanion(player); non packet companion
		main.getCompanionPacket().loadCompanion(player);
		
	
		
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getAbilityBoughtMessage()
				.replace("%companion%", getCompanionName.toUpperCase())).replace("%price%", String.valueOf(main.getFileHandler().getAbilityLevelPrice())));
		
		
	




	}
	
	public void upgradeRename(Player player, boolean withdraw, boolean check)
	{

		if(check)
		{
			if(!player.hasPermission("companions.upgrade.rename"))
			{
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoUpgradeBuyPermissionMessage()));
				return;
			}
			
			if(main.getFileHandler().getRenameRawCompanionPrice().contains("C"))
			{
				
				long amount = main.getFileHandler().getRenameCompanionPrice();
				
				if(main.getCompanionCoin().has(player, amount))
					main.getCompanionCoin().withdrawPlayer(player, amount);
				
				else
				{
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNotEnoughMoneyMessage()
							.replace("%price%", String.valueOf(main.getFileHandler().getRenameCompanionPrice()))));
					
					return;
				}
			}
			else
			{
				if(main.getFileHandler().isVault())
				{
					if(withdraw && EconomyHandler.getEconomy().has(player, main.getFileHandler().getRenameCompanionPrice()))
					{
						EconomyHandler.getEconomy().withdrawPlayer(player, main.getFileHandler().getRenameCompanionPrice());
					}
					else if(withdraw && !EconomyHandler.getEconomy().has(player, main.getFileHandler().getRenameCompanionPrice()))
					{
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNotEnoughMoneyMessage()
								.replace("%price%", String.valueOf(main.getFileHandler().getRenameCompanionPrice()))));
						return;
					}
				}
			}
		}
		
		player.closeInventory();
		PlayerData.instanceOf(player).setRenamingCompanion(true);
		

		

		player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getInRenamingMessage()));
	


	}
	
	public void upgradeHideName(Player player, boolean withdraw, boolean check)
	{


		player.closeInventory();
		
		if(check)
		{
			if(!player.hasPermission("companions.upgrade.hidename"))
			{
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoUpgradeBuyPermissionMessage()));
				return;
			}
			
			if(main.getFileHandler().getHideRawCompanionPrice().contains("C"))
			{
				
				long amount = main.getFileHandler().getHideCompanionPrice();
				
				if(main.getCompanionCoin().has(player, amount))
					main.getCompanionCoin().withdrawPlayer(player, amount);
				
				else
				{
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNotEnoughMoneyMessage()
							.replace("%price%", String.valueOf(main.getFileHandler().getHideCompanionPrice()))));
					
					return;
				}
			}
			else
			{
				if(main.getFileHandler().isVault())
				{
					if(withdraw && EconomyHandler.getEconomy().has(player, main.getFileHandler().getHideCompanionPrice()))
					{
						EconomyHandler.getEconomy().withdrawPlayer(player, main.getFileHandler().getHideCompanionPrice());
					}
					else if(withdraw && !EconomyHandler.getEconomy().has(player, main.getFileHandler().getHideCompanionPrice()))
					{
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNotEnoughMoneyMessage()
								.replace("%price%", String.valueOf(main.getFileHandler().getHideCompanionPrice()))));
						return;
					}
				}
			}
		}
		
		String getCompanionName = PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase();
		boolean nameVisible = PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(getCompanionName).isNameVisible();
		
		
		if(nameVisible)
		{
			PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(getCompanionName).setNameVisible(false);

		}
		else
		{
			PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(getCompanionName).setNameVisible(true);

		}
		boolean updatedNameVisible = PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(getCompanionName).isNameVisible();
		
		storeHideYML(getCompanionName, player, updatedNameVisible);
		
		//PlayerData.instanceOf(player).getActiveCompanion().setCustomNameVisible(updatedNameVisible); non packet companion
		
		main.getCompanionPacket().setCustomNameVisible(player, updatedNameVisible);
		
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getHideCompanionMessage()
				.replace("%price%", String.valueOf(main.getFileHandler().getHideCompanionPrice()))));

	}
	
	public void upgradeWeapon(Player player, boolean withdraw, boolean check)
	{

		if(check)
		{
			if(!player.hasPermission("companions.upgrade.changeweapon"))
			{
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoUpgradeBuyPermissionMessage()));
				return;
			}
			
			if(main.getFileHandler().getChangeRawWeaponPrice().contains("C"))
			{
				
				long amount = main.getFileHandler().getChangeWeaponPrice();
				
				if(main.getCompanionCoin().has(player, amount))
					main.getCompanionCoin().withdrawPlayer(player, amount);
				
				else
				{
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNotEnoughMoneyMessage()
							.replace("%price%", String.valueOf(main.getFileHandler().getChangeWeaponPrice()))));
					
					return;
				}
			}
			else
			{
				if(main.getFileHandler().isVault())
				{
					if(withdraw && EconomyHandler.getEconomy().has(player, main.getFileHandler().getChangeWeaponPrice()))
					{
						EconomyHandler.getEconomy().withdrawPlayer(player, main.getFileHandler().getChangeWeaponPrice());
					}
					else if(withdraw && !EconomyHandler.getEconomy().has(player, main.getFileHandler().getChangeWeaponPrice()))
					{
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNotEnoughMoneyMessage()
								.replace("%price%", String.valueOf(main.getFileHandler().getChangeWeaponPrice()))));
						
						return;
					}
				}
			}
		}

		player.closeInventory();
		PlayerData.instanceOf(player).setChangingWeapon(true);
		

		

		player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getFileHandler().getInChangingWeaponMessage()));



	}
	
	public void buyUpgradeAbility(Player player, boolean upgrade)
	{
		if(upgrade)
		{
			main.getCompanionUtil().upgradeAbility(player, true, true, true);
		}
		else
		{
			main.getCompanionUtil().upgradeAbility(player, true, true, false);
		}

	}
	
	public void buyUpgradeRename(Player player)
	{

		main.getCompanionUtil().upgradeRename(player, true, true);

	}
	
	public void buyUpgradeHideName(Player player)
	{

		main.getCompanionUtil().upgradeHideName(player, true, true);

	}
	
	public void buyUpgradeChangeWeapon(Player player)
	{

		main.getCompanionUtil().upgradeWeapon(player, true, true);

	}
	
	public void storeYML(String getCompanionName, Player player, int abilityLevel)
	{
		if(!main.getFileHandler().isDatabase())
		main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId() + ".owned." + getCompanionName + ".abilityLevel", 
				abilityLevel);
		
		else
		{
			 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
					
				  @Override
				 public void run()
				 {
					  
						PreparedStatement p = null;
						try(Connection conn = main.getDatabase().getHikari().getConnection())
						{
						
							
							p = conn.prepareStatement("UPDATE `" + main.getDatabase().getTablePrefix() +
									"owned` SET abilityLevel=? WHERE UUID=? AND companion=?");
					
							p.setInt(1, abilityLevel);
							p.setString(2, player.getUniqueId().toString());
							p.setString(3, getCompanionName.toUpperCase());
							
							p.execute();
							
							main.getDatabase().close(conn, p, null);
							
						} 
						catch (SQLException e1) 
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				 }
			 });
		}
	}
	public void storeHideYML(String getCompanionName, Player player, boolean hideName)
	{
		if(!main.getFileHandler().isDatabase())
		main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId() + ".owned." + getCompanionName + ".nameVisible", 
				hideName);
		
		else
		{
			 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
					
				  @Override
				 public void run()
				 {
					  
						PreparedStatement p = null;
						try(Connection conn = main.getDatabase().getHikari().getConnection())
						{
							
							
							p = conn.prepareStatement("UPDATE `" + main.getDatabase().getTablePrefix() +
									"owned` SET nameVisible=? WHERE UUID=? AND companion=?");
					
							p.setBoolean(1, hideName);
							p.setString(2, player.getUniqueId().toString());
							p.setString(3, getCompanionName.toUpperCase());
							
							p.execute();
							
							main.getDatabase().close(conn, p, null);
							
						} 
						catch (SQLException e1) 
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				 }
			 });
		}
	}
	
	public void storeActiveDB(String getCompanionName, Player player)
	{
		
		if(main.getFileHandler().isDatabase())
		{
			
			 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
					
				  @Override
				 public void run()
				 {
					  
						PreparedStatement p = null;
						try(Connection conn = main.getDatabase().getHikari().getConnection())
						{
						
							
							p = conn.prepareStatement("INSERT INTO `" + main.getDatabase().getTablePrefix() 
									+"active` (`UUID`,`name`,`companion`) VALUES (?,?,?)" + 
									"  ON DUPLICATE KEY UPDATE companion=\"" + getCompanionName.toUpperCase() + "\"");
							p.setString(1, player.getUniqueId().toString());
							p.setString(2, player.getName().toString());
							p.setString(3, getCompanionName.toUpperCase());
							//p.setString(4, player.getUniqueId().toString());
		
							p.execute();
							
							main.getDatabase().close(conn, p, null);
							
						} 
						catch (SQLException e1) 
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				 }  	
				});
		}
	}
	public void storeActiveYML(Player player, String getCompanionName)
	{
		if(!main.getFileHandler().isDatabase())
		{
			main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId()
			+ ".active" , getCompanionName.toUpperCase());
		}
	}
	
	public void storeNewYML(String getCompanionName, Player player)
	{
		
		
		if(!main.getFileHandler().isDatabase())
		{
			main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId() + ".owned." + getCompanionName + ".customWeapon", 
					main.getFileHandler().getCompanionDetails().get(getCompanionName).getWeapon());
			
			main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId() + ".owned." + getCompanionName + ".customName", 
					main.getFileHandler().getCompanionDetails().get(getCompanionName).getName());
			
			main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId() + ".owned." + getCompanionName + ".nameVisible", 
					main.getFileHandler().getCompanionDetails().get(getCompanionName).isNameVisible());
			
			main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId() + ".owned." + getCompanionName + ".abilityLevel",
					main.getFileHandler().getCompanionDetails().get(getCompanionName).getAbilityLevel());
		}
		else
		{
			
			 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
					
				  @Override
				 public void run()
				 {
					  
					  	
						PreparedStatement p = null;
						try(Connection conn = main.getDatabase().getHikari().getConnection())
						{
				

							
							p = conn.prepareStatement("INSERT INTO `" + main.getDatabase().getTablePrefix() + "owned`(`UUID`, `name`, `companion`,"
									+ " `customWeapon`, `customName`, `nameVisible`, `abilityLevel`) "
									+ "VALUES (?,?,?,?,?,?,?)");
							
							p.setString(1, player.getUniqueId().toString());
							p.setString(2, player.getName().toString());
							p.setString(3, getCompanionName.toUpperCase());
							p.setString(4, main.getFileHandler().getCompanionDetails().get(getCompanionName).getWeapon());
							p.setString(5, main.getFileHandler().getCompanionDetails().get(getCompanionName).getName());
							p.setBoolean(6, main.getFileHandler().getCompanionDetails().get(getCompanionName).isNameVisible());
							p.setInt(7, 1);
							
							p.execute();

							
							main.getDatabase().close(conn, p, null);
							
							
						} 
						catch (SQLException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		

					
				 }
				 
			 });
			 
		}
	}
	
	public void storeNewYML(String getCompanionName, OfflinePlayer player)
	{
		if(!main.getFileHandler().isDatabase())
		{
			main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId() + ".owned." + getCompanionName + ".customWeapon", 
					main.getFileHandler().getCompanionDetails().get(getCompanionName).getWeapon());
			
			main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId() + ".owned." + getCompanionName + ".customName", 
					main.getFileHandler().getCompanionDetails().get(getCompanionName).getName());
			
			main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId() + ".owned." + getCompanionName + ".nameVisible", 
					main.getFileHandler().getCompanionDetails().get(getCompanionName).isNameVisible());
			
			main.getFileManager().getCompanionsData().set("companions." + player.getUniqueId() + ".owned." + getCompanionName + ".abilityLevel", 
					1);
		}
		else
		{
			
			 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
					
				  @Override
				 public void run()
				 {
					  
					  	
						PreparedStatement p = null;
						try(Connection conn = main.getDatabase().getHikari().getConnection())
						{
							
				

							
							p = conn.prepareStatement("INSERT INTO `" + main.getDatabase().getTablePrefix() + "owned`(`UUID`, `name`, `companion`,"
									+ " `customWeapon`, `customName`, `nameVisible`, `abilityLevel`) "
									+ "VALUES (?,?,?,?,?,?,?)");
							
							p.setString(1, player.getUniqueId().toString());
							p.setString(2, player.getName().toString());
							p.setString(3, getCompanionName.toUpperCase());
							p.setString(4, main.getFileHandler().getCompanionDetails().get(getCompanionName).getWeapon());
							p.setString(5, main.getFileHandler().getCompanionDetails().get(getCompanionName).getName());
							p.setBoolean(6, main.getFileHandler().getCompanionDetails().get(getCompanionName).isNameVisible());
							p.setInt(7, 1);
							
							p.execute();

							
							main.getDatabase().close(conn, p, null);
							
							
						} 
						catch (SQLException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		

					
				 }
				 
			 });
			 
		}
	}
	
	public List<String> getCustomAbilities()
	{
		return Arrays.asList(customAbilities);
	}
	
	public List<String> getPhysicalAbilities()
	{
		return Arrays.asList(physicalAbilities);
	}
	
	
	public void updateCache(UUID uuid, String getCompanionName)
	{
		CustomCompanion cc = new CustomCompanion();
		
		String customWeapon = null;
		
		if(!main.getFileHandler().isDatabase())
		{
			cc.setCustomName(main.getFileManager().getCompanionsData().getString("companions." + uuid + ".owned." + getCompanionName + ".customName"));
			
			
			customWeapon = main.getFileManager().getCompanionsData().getString("companions." + uuid + ".owned." + getCompanionName + ".customWeapon");
			
			cc.setNameVisible(main.getFileManager().getCompanionsData().getBoolean("companions." + uuid + ".owned." + getCompanionName + ".nameVisible"));
			cc.setAbilityLevel(main.getFileManager().getCompanionsData().getInt("companions." + uuid + ".owned." + getCompanionName + ".abilityLevel"));
		}
		else
		{
			cc.setCustomName(main.getFileManager().getCompanions().getString("companions." + getCompanionName + ".name"));
			
			customWeapon = main.getFileManager().getCompanions().getString("companions." + getCompanionName + ".weapon");
			
			cc.setNameVisible(main.getFileManager().getCompanions().getBoolean("companions." + getCompanionName + ".nameVisible"));
			cc.setAbilityLevel(Integer.valueOf(main.getFileManager().getCompanions().getString("companions." + getCompanionName + ".ability").split("@")[1]));
		}
		
		if(!customWeapon.equals("NONE"))
		{
			try
			{
				cc.setCustomWeapon(new ItemBuilderUtil(
						Material.valueOf(customWeapon),
						getCompanionName.toUpperCase() + "'S WEAPON", 
						1).build());
			}
			catch(IllegalArgumentException itemNotFound)
			{
				cc.setCustomWeapon(new ItemBuilderUtil(
						Material.STONE,
						getCompanionName.toUpperCase() + "'S WEAPON", 
						1).build());
				
				System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.GRAY + getCompanionName + "'s Weapon failed to load. - "
						+ "Please check if the material name is for the correct Minecraft server version.");
			}
		}

		
		PlayerCache.instanceOf(uuid).getOwnedCache().put(getCompanionName, cc); // Cache all owned Player companions.
	}
	
	public void updateCache(UUID uuid, String getCompanionName, String customName, String customWeaponc, boolean nameVisible, int abilityLevel)
	{
		CustomCompanion cc = new CustomCompanion();
		
		cc.setCustomName(customName);
		
		
		String customWeapon = customWeaponc;
		
		if(!customWeapon.equals("NONE"))
		{
			try
			{
				cc.setCustomWeapon(new ItemBuilderUtil(
						Material.valueOf(customWeapon),
						getCompanionName.toUpperCase() + "'S WEAPON", 
						1).build());
			}
			catch(IllegalArgumentException itemNotFound)
			{
				cc.setCustomWeapon(new ItemBuilderUtil(
						Material.STONE,
						getCompanionName.toUpperCase() + "'S WEAPON", 
						1).build());
				
				System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.GRAY + getCompanionName + "'s Weapon failed to load. - "
						+ "Please check if the material name is for the correct Minecraft server version.");
			}
		}
		cc.setNameVisible(nameVisible);
		cc.setAbilityLevel(abilityLevel);
		
		PlayerCache.instanceOf(uuid).getOwnedCache().put(getCompanionName, cc); // Cache all owned Player companions.
	}
	
	public LivingEntity getDamager(Entity hitDamager)
	{
		
		LivingEntity damager = null;
		
		try
		{
			damager = (LivingEntity) hitDamager;
			
		}
		catch(ClassCastException error)
		{
			try
			{
				Projectile arrowDamager = (Projectile) hitDamager;
				
				damager = (LivingEntity) arrowDamager.getShooter();
			}
			catch(ClassCastException error1) {}
				

		}
		
		return damager;
	}
	
	public void setTexture(String textureURL, ItemMeta itemMeta)
	{
		String url = textureURL;
		
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		
		profile.getProperties().put("textures", new Property("textures", url));
		
		try
		{
			Field profileField = itemMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(itemMeta, profile);
		}
		catch(IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error)
		{
			error.printStackTrace();
		}
	}
	
	public void setSkull(String textureURL, ItemMeta itemMeta, String customModelData)
	{

		if(customModelData.equals("NONE")) // no custom model data.
		{
			setTexture(textureURL, itemMeta);
		}
		else
		{
			itemMeta.setCustomModelData(Integer.valueOf(customModelData.split(":")[1]));
		}

	}
	
	public void ownedCompanionsViewer(Player player, Player target, String menuSound, String menuName, boolean self)
	{
		
		if(target == null)
		{
			target = player;
		}
		
		ArrayList<String> setLore = new ArrayList<>();
		
		String activeCompanion;
		
		List<String> description;
		
		if(self)
		{
			description = main.getFileHandler().getCompanionDetailDescription();
		}
		else
		{
			description = main.getFileHandler().getPlayerDetailsDescription();
		}
		
		if(PlayerData.instanceOf(target).getActiveCompanionName() == null)
		{
			activeCompanion = "NONE";
		}
		else
		{
			activeCompanion = PlayerData.instanceOf(target).getActiveCompanionName();
		}
		for(String getLore : description)
		{
			setLore.add(ChatColor.translateAlternateColorCodes('&', getLore.replace("%active_companion%", activeCompanion)
					.replace("%active_companion_l%", activeCompanion.substring(0, 1) + activeCompanion.substring(1).toLowerCase())
					.replace("%companions_coins%", String.valueOf(PlayerData.instanceOf(target).getCompanionCoin()))
					.replace("%tbacommand_duration%", String.valueOf(PlayerData.instanceOf(player).getCommandInterval()))));
		}

		
		ItemMeta companionDetailMeta = main.getFileHandler().getCompanionDetail().getItemMeta();
		companionDetailMeta.setLore(setLore);
		
		main.getFileHandler().getCompanionDetail().setItemMeta(companionDetailMeta);
		
		Inventory ownedMenu = new InventoryBuilder(main.getFileHandler().getOwnedCompanionsSize(), menuName)
				.setItem(main.getFileHandler().getGoBackSlot(), main.getFileHandler().getGoBack())
				.setItem(main.getFileHandler().getNextPageSlot(), main.getFileHandler().getNextPage())
				.setItem(main.getFileHandler().getCompanionDetailSlot(), main.getFileHandler().getCompanionDetail())
				.build();
		
		ArrayList<ItemStack> itemStackArray = new ArrayList<>();
		
		for(String getCompanionName : PlayerCache.instanceOf(target.getUniqueId()).getOwnedCache().keySet())
		{
			try
			{
				itemStackArray.add(main.getFileHandler().getCompanionDetails().get(getCompanionName.toLowerCase()).getOwnedItemType());
			}
			catch(NullPointerException companionError) 
			{
				System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY+ " is trying to access the Companion Owned Menu with a deleted Companion. (" + ChatColor.YELLOW + getCompanionName + ")" 
						+ ChatColor.GRAY + " THIS IS NOT AN ERROR!");
			}
		}
		
		PageSystem ps = new PageSystem(main);
		
		ps.buildPageSystem(ownedMenu, player, main.getFileHandler().getOwnedCompanionsSize(), 3, itemStackArray);
		
		try
		{
			player.playSound(player.getLocation(), 
					Sound.valueOf(menuSound), 1.0F, 1.0F);
		}
		 catch(IllegalArgumentException soundNotFound)
		 {
			 System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.RED + "Owned Menu sound - " + ChatColor.YELLOW + 
					 menuSound + ChatColor.RED +" is not found.");
		 }
		
		player.openInventory(ownedMenu);
	}
	
	
	public void saveCache(Player player)
	{
		
		if(PlayerData.instanceOf(player).getActiveCompanionName() != PlayerCache.instanceOf(player.getUniqueId()).getCachedCompanionName())
		{
			
			PlayerCache.instanceOf(player.getUniqueId()).setCachedCompanionName(PlayerData.instanceOf(player).getActiveCompanionName());
			
			
			if(PlayerData.instanceOf(player).getActiveCompanionName() != null)
				main.getCompanionUtil().storeActiveDB(PlayerData.instanceOf(player).getActiveCompanionName().toUpperCase(), player);
		}
		
		if(PlayerData.instanceOf(player).getCompanionCoin() != PlayerCache.instanceOf(player.getUniqueId()).getCachedCompanionCoins())
		{
			
			PlayerCache.instanceOf(player.getUniqueId()).setCachedCompanionCoins(PlayerData.instanceOf(player).getCompanionCoin());
			
			
			if(String.valueOf(PlayerData.instanceOf(player).getCompanionCoin()) != null)
				main.getCompanionCoin().updateCoinsCache(player, PlayerData.instanceOf(player).getCompanionCoin());
		}


	}
	
	public void saveCache(Player player, PlayerData pd, PreparedStatement p, Connection conn)
	{
		
		if(PlayerData.instanceOf(player).getActiveCompanionName() != PlayerCache.instanceOf(player.getUniqueId()).getCachedCompanionName())
		{
			PlayerCache.instanceOf(player.getUniqueId()).setCachedCompanionName(PlayerData.instanceOf(player).getActiveCompanionName());

			
			if(pd.getActiveCompanionName() != null)
				main.saveActiveCompanion(pd.getActiveCompanionName(), pd.getPlayer(), p, conn);
		}
		
		if(PlayerData.instanceOf(player).getCompanionCoin() != PlayerCache.instanceOf(player.getUniqueId()).getCachedCompanionCoins())
		{
			
			PlayerCache.instanceOf(player.getUniqueId()).setCachedCompanionCoins(PlayerData.instanceOf(player).getCompanionCoin());

			
			if(String.valueOf(PlayerData.instanceOf(pd.getPlayer()).getCompanionCoin()) != null)
				main.getCompanionCoin().updateCoinsCache(pd.getPlayer(), PlayerData.instanceOf(pd.getPlayer()).getCompanionCoin(), p, conn);
		}
		
		
	}
	

	

}
