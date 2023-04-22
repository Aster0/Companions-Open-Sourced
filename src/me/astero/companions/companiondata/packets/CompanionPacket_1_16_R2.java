package me.astero.companions.companiondata.packets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.datafixers.util.Pair;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerCache;
import me.astero.companions.companiondata.PlayerData;
import me.astero.companions.companiondata.packets.data.PacketData_1_16_R2;
import me.astero.companions.filemanager.BodySkullData;
import me.astero.companions.filemanager.CompanionDetails;
import me.astero.companions.util.ItemBuilderUtil;
import me.astero.companionsapi.api.CAPI;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R2.ChatComponentText;
import net.minecraft.server.v1_16_R2.ChatMessageType;
import net.minecraft.server.v1_16_R2.EntityArmorStand;
import net.minecraft.server.v1_16_R2.EnumItemSlot;
import net.minecraft.server.v1_16_R2.PacketPlayOutChat;
import net.minecraft.server.v1_16_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R2.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_16_R2.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R2.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_16_R2.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_16_R2.Vector3f;

public class CompanionPacket_1_16_R2 implements CompanionPacket, Listener{
	
   private final Map<UUID, PacketData_1_16_R2> packetData = new HashMap<>();
    
    private CompanionsPlugin main;
	
	public CompanionPacket_1_16_R2(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	public ItemStack setSkull(Player player)
	{
		ItemStack companionSkull;
		
		try
		{
			if(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData().equals("NONE"))
				companionSkull = new ItemBuilderUtil(Material.PLAYER_HEAD, PlayerData.instanceOf(player).getActiveCompanionName().toUpperCase() + "'S HEAD", 1).build();
			else
				companionSkull = new ItemBuilderUtil(Material.valueOf(
						main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData().split(":")[0]), PlayerData.instanceOf(player).getActiveCompanionName().toUpperCase() + "'S HEAD", 1).build();
			
			
		}
		catch(NoSuchFieldError | NoSuchMethodError error)
		{
			companionSkull = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
		}
		
		if(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData().equals("NONE"))
		{
			SkullMeta companionHeadMeta = (SkullMeta) companionSkull.getItemMeta();	
			
			main.getCompanionUtil().setSkull(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getPlayerSkull(), companionHeadMeta,
					main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData());
	
			companionSkull.setItemMeta(companionHeadMeta);
		}
		else
		{
			ItemMeta companionHeadMeta = companionSkull.getItemMeta();	
			
			main.getCompanionUtil().setSkull(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getPlayerSkull(), companionHeadMeta,
					main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData());
	
			companionSkull.setItemMeta(companionHeadMeta);
		}
		
		return companionSkull;
	}
	
	public ItemStack setSkull(Player player, String url)
	{
		ItemStack companionSkull;
		
		try
		{
			if(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData().equals("NONE"))
				companionSkull = new ItemBuilderUtil(Material.PLAYER_HEAD, PlayerData.instanceOf(player).getActiveCompanionName().toUpperCase() + "'S HEAD", 1).build();
			else
				companionSkull = new ItemBuilderUtil(Material.valueOf(
						main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData().split(":")[0]), PlayerData.instanceOf(player).getActiveCompanionName().toUpperCase() + "'S HEAD", 1).build();
			
			
		}
		catch(NoSuchFieldError | NoSuchMethodError error)
		{
			companionSkull = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
		}
		
		if(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData().equals("NONE"))
		{
			SkullMeta companionHeadMeta = (SkullMeta) companionSkull.getItemMeta();	
			
			main.getCompanionUtil().setSkull(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getPlayerSkull(), companionHeadMeta,
					main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData());
	
			companionSkull.setItemMeta(companionHeadMeta);
		}
		else
		{
			ItemMeta companionHeadMeta = companionSkull.getItemMeta();	
			
			main.getCompanionUtil().setSkull(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getPlayerSkull(), companionHeadMeta,
					main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData());
	
			companionSkull.setItemMeta(companionHeadMeta);
		}
		
		return companionSkull;
	}
	
	public void setBodySkull(Player player)
	{
		if(main.getFileHandler().getCompanionDetails().get(
				PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getChestplate().equals("BODYSKULL"))
		{
			for(BodySkullData bsd : main.getFileHandler().getCompanionDetails()
					.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getBodySkull().values())
			{
				String position[] = bsd.getPosition().split(", ");
				String texture = bsd.getTexture();
				
				double x = Math.cos(Math.toRadians(player.getLocation().getYaw() - 180));
				double z = Math.sin(Math.toRadians(player.getLocation().getYaw() - 180));
				
				
				Location loc = player.getLocation().add(x + Double.valueOf(position[0]), 
						main.getFileHandler().getCompanionDetails()
						.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getY() + Double.valueOf(position[1]),
						z + Double.valueOf(position[2]));
				
				 CraftPlayer cp = (CraftPlayer) player;
				 CraftWorld cw = (CraftWorld) player.getLocation().getWorld();
				 
				 EntityArmorStand armorStand = 
						 new EntityArmorStand(cw.getHandle(), loc.getX(), loc.getY(), loc.getZ());
				 

				 PacketPlayOutSpawnEntity entity = new PacketPlayOutSpawnEntity(armorStand, 78);
				 cp.getHandle().playerConnection.sendPacket(entity);

				 
				 armorStand.setCustomNameVisible(PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache()
							.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).isNameVisible());
				 
				 armorStand.setInvisible(true);
				
				 armorStand.setSmall(true);
				 armorStand.setBasePlate(true);
				 
				 armorStand.setMarker(false);
				 armorStand.setNoGravity(true);
				 armorStand.setCustomNameVisible(false);
				// setArmorPose(player, armorStand);
				 

					// PacketPlayOutEntityEquipment head = new PacketPlayOutEntityEquipment(armorStand.getId(), EnumItemSlot.HEAD, 
						//	 CraftItemStack.asNMSCopy(setSkull(player, texture)));
					 
				 	List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R2.ItemStack>> test = new ArrayList<>();
			        
			        test.add(Pair.of(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(setSkull(player, texture))));
		

			        PacketPlayOutEntityEquipment head = new PacketPlayOutEntityEquipment(armorStand.getId(),
			                test);
				 

				 cp.getHandle().playerConnection.sendPacket(head);
				 PacketPlayOutEntityMetadata  packetTeleport = new PacketPlayOutEntityMetadata (armorStand.getId(), armorStand.getDataWatcher(), false);
				 //PlayerData.instanceOf(player).setCompanionMetaData(packetTeleport);
				 cp.getHandle().playerConnection.sendPacket(packetTeleport);
				 
		

				
			}
			
		}
	}
	public ItemStack setChest(Player player)
	{
		
		
		
		
		ItemStack companionChest;

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
		
		return companionChest;
	}
	
	public ItemStack setWeapon(Player player)
	{
		ItemStack weapon = PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomWeapon();
		
		if(weapon != null)
		{
			return weapon;
		}
		
		return null;
	}
	
	public void setArmorPose(Player player, EntityArmorStand armorStand)
	{
		CompanionDetails cd = main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase());
		
		armorStand.setBodyPose(new Vector3f(
				(float) cd.getBodyPose1(), 
				(float) cd.getBodyPose2(), 
				(float) cd.getBodyPose3()));
		
		armorStand.setHeadPose(new Vector3f(
				(float) cd.getHeadPose1(), 
				(float) cd.getHeadPose2(), 
				(float) cd.getHeadPose3()));
		
		
		armorStand.setLeftArmPose(new Vector3f(
				(float) cd.getLeftArmPose1(), 
				(float) cd.getLeftArmPose2(), 
				(float) cd.getLeftArmPose3()));
		
		armorStand.setRightArmPose(new Vector3f(
				cd.getRightArmPose1(), 
				cd.getRightArmPose2(), 
				cd.getRightArmPose3()));
	}
	
	@Override
	public void setCustomName(Player player, String newName)
	{
		
		
		
		packetData.get(player.getUniqueId())
			.getCompanionPacket().setCustomName(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', newName)));
		
		updateCompanion(player);
		
	}
	
	@Override
	public void setCustomNameVisible(Player player, boolean visible)
	{
		packetData.get(player.getUniqueId())
			.getCompanionPacket().setCustomNameVisible(visible);
		
		updateCompanion(player);
	}
	
	@Override
	public void setCustomWeapon(Player player, ItemStack itemStack)
	{
		//test
		packetData.get(player.getUniqueId())
			.getCompanionPacket().setEquipment(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(itemStack));
		
		updateCompanion(player);
	}
	
	public void updateCompanion(Player player)
	{
		/*CraftPlayer cp = (CraftPlayer) player;
		 PacketPlayOutEntityMetadata  packetTeleport = new PacketPlayOutEntityMetadata (
				 packetData.get(player.getUniqueId()).getCompanionPacket().getId(),
				 packetData.get(player.getUniqueId()).getCompanionPacket().getDataWatcher(), false);
		 
		 packetData.get(player.getUniqueId()).setCompanionMetaData(packetTeleport);
		 
		 cp.getHandle().playerConnection.sendPacket(packetTeleport);*/
		
		despawnCompanion(player);
		
		
		
		loadCompanion(player);
	}
	
	@Override
	public void loadCompanion(Player player)
	{
		if(PlayerData.instanceOf(player).getActiveCompanionName() != null)
		{
			if(!PlayerData.instanceOf(player).getActiveCompanionName().equals("NONE") && !PlayerData.instanceOf(player).isToggled())
			{
				
				if(!main.getFileHandler().getDisabledWorlds().contains(player.getWorld().getName()))
				{
					
					try
					{
						main.getPotionEffectAbility().give(player); 
					}
					catch(IllegalStateException e)
					{
						// For async chat
					}
						main.getCustomAbility().giveFly(player);
						main.getCustomAbility().executeCommand(player);
						
						
						if(CAPI.getSpawnListener() != null)
							CAPI.getSpawnListener().onCompanionSpawn(main.getFileHandler().getCompanionDetails().get(
									PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityList(), player);
						
			
					
						
						 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
								
							  @Override
							 public void run()
							 {
				
									
				
		
											
											if(PlayerData.instanceOf(player).isPatreon() && PlayerData.instanceOf(player).isParticle())
											{
						
												
												main.getCompanions().giveParticle(player);
												
											}
		

											double x = Math.cos(Math.toRadians(player.getLocation().getYaw() - 180)
													+ main.getFileHandler().getCompanionDetails()
													.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getX());
											
											double z = Math.sin(Math.toRadians(player.getLocation().getYaw() - 180)
													+ main.getFileHandler().getCompanionDetails()
													.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getZ());
											
											
											Location loc = player.getLocation().add(x, main.getFileHandler().getCompanionDetails()
													.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getY(), z);
											
											 CraftPlayer cp = (CraftPlayer) player;
											 CraftWorld cw = (CraftWorld) player.getLocation().getWorld();
											 
											 EntityArmorStand armorStand = 
													 new EntityArmorStand(cw.getHandle(), loc.getX(), loc.getY(), loc.getZ());
											 
						
											 PacketPlayOutSpawnEntity entity = new PacketPlayOutSpawnEntity(armorStand, 78);
											 cp.getHandle().playerConnection.sendPacket(entity);
						
											 
											 armorStand.setCustomNameVisible(PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache()
														.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).isNameVisible());
											 
											 armorStand.setInvisible(true);
											
											 armorStand.setSmall(true);
											 armorStand.setBasePlate(true);
											 
											 armorStand.setMarker(false);
											 armorStand.setNoGravity(true);
											 setArmorPose(player, armorStand);
											 
											 
											 armorStand.setCustomName(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache()
														.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomName())));
											 
						
											
						
										/*	 PacketPlayOutEntityEquipment head = new PacketPlayOutEntityEquipment(armorStand.getId(), EnumItemSlot.HEAD, 
													 CraftItemStack.asNMSCopy(setSkull(player)));
											 

											 
											 PacketPlayOutEntityEquipment weapon = new PacketPlayOutEntityEquipment(armorStand.getId(), EnumItemSlot.MAINHAND, 
													 CraftItemStack.asNMSCopy(setWeapon(player)));*/
											 
											 	List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R2.ItemStack>> headList = new ArrayList<>();
										        
											 	headList.add(Pair.of(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(setSkull(player))));


										        PacketPlayOutEntityEquipment head = new PacketPlayOutEntityEquipment(armorStand.getId(),
										        		headList);
										        
											 	List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R2.ItemStack>> weaponList = new ArrayList<>();
										        
											 	weaponList.add(Pair.of(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(setWeapon(player))));
											 	
									


										        PacketPlayOutEntityEquipment weapon = new PacketPlayOutEntityEquipment(armorStand.getId(),
										        		weaponList);
											 
											 cp.getHandle().playerConnection.sendPacket(head);
											
											 cp.getHandle().playerConnection.sendPacket(weapon);
											 
										       
											
											 if(main.getFileHandler().isActionBarMessage())
											 {
												 PacketPlayOutChat bar = new PacketPlayOutChat(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache()
														.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomName())), ChatMessageType.GAME_INFO, player.getUniqueId());
			
												 cp.getHandle().playerConnection.sendPacket(bar);
											 }
											 
						
											 
											 PacketPlayOutEntityMetadata  packetTeleport = new PacketPlayOutEntityMetadata (armorStand.getId(), armorStand.getDataWatcher(), false);
											 //PlayerData.instanceOf(player).setCompanionMetaData(packetTeleport);
											 cp.getHandle().playerConnection.sendPacket(packetTeleport);
						
											 
											 
											 //PlayerData.instanceOf(player).setCompanionPacket(armorStand);
											
											 
											 PacketData_1_16_R2 pd = new PacketData_1_16_R2();
											 
											 if(!main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player)
													.getActiveCompanionName().toLowerCase()).getChestplate().equals("BODYSKULL"))
											 {
												 /*PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(armorStand.getId(), EnumItemSlot.CHEST, 
														 CraftItemStack.asNMSCopy(setChest(player)));*/
												 
												 	List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R2.ItemStack>> chestList = new ArrayList<>();
											        
												 	chestList.add(Pair.of(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(setChest(player))));


											        PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(armorStand.getId(),
											        		chestList);
											        
								
											        
												 cp.getHandle().playerConnection.sendPacket(chest);
												 pd.setChest(chest);
												 
											 }
			
											 
											 pd.setCompanionPacket(armorStand);
											 pd.setCompanionMetaData(packetTeleport);
											 
											 pd.setSkull(head);
											 
											 pd.setWeapon(weapon);
											 
											 packetData.put(player.getUniqueId(), pd);
											 
											 //setBodySkull(player);
										
					
								 
							 }
						 });
				}
				else
				{
					PlayerData.instanceOf(player).toggleCompanion();
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getPlayerInDisabledWorldMessage()));
					
				}
			}
		}
		
		 

		 
		 /*for(Entity e : player.getNearbyEntities(10, 10, 10))
		 {
			 if(e.getType() == EntityType.PLAYER)
			 {
				 CraftPlayer cPlayer = (CraftPlayer) e;
				 
				 PacketPlayOutSpawnEntityLiving packetTeleport1 = new PacketPlayOutSpawnEntityLiving(PlayerData.instanceOf(player).getCompanionPacket());
				 
				 cPlayer.getHandle().playerConnection.sendPacket(packetTeleport1);
				 
				 System.out.println(e);
			 }
		 } */
		 
	}
	
	
	@Override
	public void despawnCompanion(Player player, Player packetPlayer)
	{
		
		 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
				
			  @Override
			 public void run()
			 {
				  
					if(packetData.get(player.getUniqueId()) != null)
					{
						if(packetData.get(player.getUniqueId()).getCompanionPacket() != null)
						{
					        PacketPlayOutEntityDestroy pa =
					        		new PacketPlayOutEntityDestroy(packetData.get(player.getUniqueId()).getCompanionPacket().getId());
					         
					        ((CraftPlayer) packetPlayer).getHandle().playerConnection.sendPacket(pa);
					        
					        
						}
					}
			 }
		 });

	}
	
	@Override
	public void despawnCompanion(Player player)
	{
		
		
		 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
				
			  @Override
			 public void run()
			 {
				  
					if(packetData.get(player.getUniqueId()) != null)
					{
						if(packetData.get(player.getUniqueId()).getCompanionPacket() != null)
						{
					        PacketPlayOutEntityDestroy pa =
					        		new PacketPlayOutEntityDestroy(packetData.get(player.getUniqueId()).getCompanionPacket().getId());
					         
					        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(pa);
					        
					        for(Player packetPlayer : PlayerData.instanceOf(player).getPlayerPacketList().keySet())
					        {
					        	((CraftPlayer) packetPlayer).getHandle().playerConnection.sendPacket(pa);
					        }
					        
					        packetData.get(player.getUniqueId()).setCompanionPacket(null);
					        PlayerData.instanceOf(player).getPlayerPacketList().clear();
						}
					}
			 }
		 });

	}
	
	@Override 
	public void toggleCompanion(Player player)
	{
		if(packetData.get(player.getUniqueId()) != null)
		{
			if(packetData.get(player.getUniqueId()).getCompanionPacket() != null)
			{		
				main.getCompanionUtil().removeParticles(player);
				//main.getAnimation().removeAnimation(player);
				main.getCompanionUtil().stopCommandAbility(player);
				main.getPotionEffectAbility().remove(player);
				despawnCompanion(player);
			}
		}
	}
	
	
	@Override
	public void companionFollow(Player player)
	{
		

				  
		if(packetData.get(player.getUniqueId()) != null)
		{
			if(packetData.get(player.getUniqueId()).getCompanionPacket() != null)
			{
				
				double x = Math.cos(Math.toRadians(player.getLocation().getYaw() - 180)
						+ main.getFileHandler().getCompanionDetails()
						.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getX());
				
				double z = Math.sin(Math.toRadians(player.getLocation().getYaw() - 180)
						+ main.getFileHandler().getCompanionDetails()
						.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getZ());
				
				
				Location loc = player.getLocation().add(x, + main.getFileHandler().getCompanionDetails()
						.get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getY(), z);
				
				CraftPlayer cp = (CraftPlayer) player;
			
				
				
				packetData.get(player.getUniqueId()).getCompanionPacket().setLocation(loc.getX(), 
						loc.getY(), 
						loc.getZ(), loc.getYaw(), 0);
				
				
				/*if(packetData.get(player.getUniqueId()).getBodySkullPacket().size() > 0)
				{
					for(BodySkullData_1_15 bsdD : packetData.get(player.getUniqueId()).getBodySkullPacket().values())
					{
		
						
						String[] position = bsdD.getPosition().split(", ");
	
						
						bsdD.getCompanionPacket().setLocation(loc.getX()
								+ Double.valueOf(position[0]), 
								packetData.get(player.getUniqueId()).getCompanionPacket().locY() + Double.valueOf(position[1]), 
								loc.getZ() + Double.valueOf(position[2]), (float) z, 0); */
						
						/*bsdD.getCompanionPacket().setLocation(player.getLocation().getX()
								+ Double.valueOf(position[0]), 
								packetData.get(player.getUniqueId()).getCompanionPacket().locY() + Double.valueOf(position[1]), 
								player.getLocation().getZ() + Double.valueOf(position[2]),
								packetData.get(player.getUniqueId()).getCompanionPacket().getBukkitYaw(),
								0);*/
						
						
						/*PacketPlayOutEntityTeleport bodyPacket = new PacketPlayOutEntityTeleport(bsdD.getCompanionPacket());
						cp.getHandle().playerConnection.sendPacket(bodyPacket);
					}
				}*/
				
	
				 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
						
					  @Override
					 public void run()
					 {
						  if(packetData.get(player.getUniqueId()).getCompanionPacket() != null)
						  {
							PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(packetData.get(player.getUniqueId()).getCompanionPacket());
							cp.getHandle().playerConnection.sendPacket(packet);
							
	
							
							packetData.get(player.getUniqueId()).setTeleportPacket(packet);
						  }
					 }
				 });
	
				 /*PacketPlayOutEntityMetadata  packetTeleport = new PacketPlayOutEntityMetadata (PlayerData.instanceOf(player).getCompanionPacket().getId(),
						 PlayerData.instanceOf(player).getCompanionPacket().getDataWatcher(), false);
				 cp.getHandle().playerConnection.sendPacket(packetTeleport);*/
				 
	             /*PacketPlayOutEntityDestroy pa = new PacketPlayOutEntityDestroy(stand.getId());
	             ((CraftPlayer)p).getHandle().playerConnection.sendPacket(pa);*/
				
				List<Entity> entityList = player.getNearbyEntities(Double.valueOf(main.getFileHandler().getPacketRange()[0]), 
						Double.valueOf(main.getFileHandler().getPacketRange()[1]),
						Double.valueOf(main.getFileHandler().getPacketRange()[2]));
				
				
	
				  
				 for(Entity ent : entityList)
				 {
					 
	
					 
					 if(ent.getType() == EntityType.PLAYER)
					 {
						 Player ePlayer = (Player) ent;
						 
	
						 
						 
						 CraftPlayer eCP = (CraftPlayer) ePlayer;
							
						 Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
								
							  @Override
							 public void run()
							 {
								  
								 if(PlayerData.instanceOf(player).getPlayerPacketList().get(ePlayer) == null)
								 {
									 
									 PacketPlayOutSpawnEntity entity = new PacketPlayOutSpawnEntity(packetData.get(player.getUniqueId()).getCompanionPacket(), 78);
									 eCP.getHandle().playerConnection.sendPacket(entity);
									 
									 eCP.getHandle().playerConnection.sendPacket(packetData.get(player.getUniqueId()).getCompanionMetaData());
									 
									 
									 eCP.getHandle().playerConnection.sendPacket(packetData.get(player.getUniqueId()).getSkull());
									 
									 if(packetData.get(player.getUniqueId()).getChest() != null)
										 eCP.getHandle().playerConnection.sendPacket(packetData.get(player.getUniqueId()).getChest());
									 
									 eCP.getHandle().playerConnection.sendPacket(packetData.get(player.getUniqueId()).getWeapon());
									 
									 PlayerData.instanceOf(player).getPlayerPacketList().put(ePlayer, true);
									 
									 
								 }
							 
								
								eCP.getHandle().playerConnection.sendPacket(packetData.get(player.getUniqueId()).getTeleportPacket());
							 }
						 });
						
	
						 
						 
					 }
				 }
				 
	
				 
				if(PlayerData.instanceOf(player).getPlayerPacketList().size() != entityList.size())
				{
					
					
					for(Player packetPlayer : PlayerData.instanceOf(player).getPlayerPacketList().keySet())
					{
	
						if(!entityList.contains(packetPlayer))
						{
							
							despawnCompanion(player, packetPlayer);
				         
				             
				             PlayerData.instanceOf(player).setClear(true);
				             
				             
				             PlayerData.instanceOf(player).getPlayerPacketList().put(packetPlayer, false);
						 }
					 }
					
					
					if(PlayerData.instanceOf(player).isClear())
					{
						PlayerData.instanceOf(player).getPlayerPacketList().clear();
						PlayerData.instanceOf(player).setClear(false);
					}
					
	
					
			
				}
				
				
				
				
				
				
				 
			}
		}
		

	}

}
