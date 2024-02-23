package me.astero.companions.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerCache;
import me.astero.companions.companiondata.PlayerData;
import me.astero.companions.companiondata.packets.CompanionPacket_1_15;
import me.astero.companions.gui.MainMenu;
import me.astero.companions.gui.OwnedMenu;
import me.astero.companions.gui.PlayerDetailsMenu;
import me.astero.companions.gui.ShopMenu;
import me.astero.companions.gui.UpgradeMenu;

public class CompanionCommand implements CommandExecutor {
	
	private CompanionsPlugin main;
	
	public CompanionCommand(CompanionsPlugin main)
	{
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		

		
		if(sender instanceof Player)
		{
			Player player = (Player) sender;

			if(args.length < 1)
			{
				new MainMenu(main, player);
			}
			else if(args.length > 1)
			{
				if(args[0].equalsIgnoreCase("details"))
				{
					if(player.hasPermission("companions.player.details"))
					{
						try
						{
							Player target = Bukkit.getPlayer(args[1]);
							
							
							if(PlayerData.instanceOf(target).getAllCompanions().size() != 0)
							{
								/*String activeCompanion = PlayerData.instanceOf(target).getActiveCompanionName();
								String ownedCompanions = PlayerCache.instanceOf(target.getUniqueId()).getOwnedCache().keySet().toString().replace("[", "").replace("]", "").toUpperCase();
								
								for(String message : main.getFileHandler().getPlayerDetailsMessage())
								{
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%player%", target.getName())
											
											).replace("%companion%", activeCompanion)
											.replace("%ownedcompanions%", ownedCompanions));
								}*/
								PlayerData.instanceOf(player).setPageNumber(1);
								PlayerData.instanceOf(player).setPlayerDetailsTarget(target);
								
								new PlayerDetailsMenu(main, player, false);
							
							
	
							}
							else
							{
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoCompanionsMessage()));
							}
								
							
						}
						catch(NullPointerException notOnline)
						{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getPlayerNotOnlineMessage()));
						}
					}
					else
					{
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoPermissionMessage()));
					}
				}
				else if(PlayerData.instanceOf(player).getActiveCompanionName() != "NONE" && PlayerData.instanceOf(player).getActiveCompanionName() != null)
				{
					if(args[0].equalsIgnoreCase("upgrade")) 
					{
						if(args[1].equalsIgnoreCase("ability"))
						{
							
							boolean upgrade;
							
							try
							{
								upgrade = Boolean.valueOf(args[2]);
							}
							catch(ArrayIndexOutOfBoundsException notStated)
							{
								upgrade = true;
							}
							
							if(!upgrade)
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
							else if(main.getFileHandler().getMaxAbilityLevel() != 
									PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityLevel())
							{
								
								if(upgrade)
								{
									main.getCompanionUtil().buyUpgradeAbility(player, true);
								}
							}
							else
							{
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() +  main.getFileHandler().getAbilityMaxedMessage()));
							}
						}
						else if(args[1].equalsIgnoreCase("rename"))
						{
							main.getCompanionUtil().buyUpgradeRename(player);
						}
						else if(args[1].equalsIgnoreCase("hidename"))
						{
							main.getCompanionUtil().buyUpgradeHideName(player);
						}
						else if(args[1].equalsIgnoreCase("changeweapon"))
						{
							main.getCompanionUtil().buyUpgradeChangeWeapon(player);
						}
						else 
						{
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getInvalidUpgradeArgumentMessage()));
						}
					}
					
				}
				else
				{
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoActiveCompanionMessage()));
				}
			}
			else if(args[0].equalsIgnoreCase("owned"))
			{
				PlayerData.instanceOf(player).setPageNumber(1); // Set the first page before entering
				new OwnedMenu(main, player, true);
			}
			else if(args[0].equalsIgnoreCase("shop"))
			{
				PlayerData.instanceOf(player).setPageNumber(1);
				new ShopMenu(main, player);
			}
			else if(args[0].equalsIgnoreCase("upgrade"))
			{
				new UpgradeMenu(main, player);
			}
			else if(args[0].equalsIgnoreCase("reload"))
			{
				if(player.hasPermission("companions.admin.reload"))
				{
					main.getFileManager().reloadConfigs();
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getReloadMessage()));
				}
				else
				{
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoPermissionMessage()));
				}
			}
			else if(args[0].equalsIgnoreCase("particle"))
			{
				if(PlayerData.instanceOf(player).isPatreon() && PlayerData.instanceOf(player).isParticle())
				{
					main.getCompanionUtil().removeParticles(player);
					PlayerData.instanceOf(player).setParticle(false);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + "&cParticles has been successfully set off and removed!"));
				}
				else if(!PlayerData.instanceOf(player).isParticle())
				{
					PlayerData.instanceOf(player).setParticle(true);
					PlayerData.instanceOf(player).removeCompanion();
					main.getCompanions().summonCompanion(player);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + "&cParticles has been successfully set back!"));
				}
				
			}
			else if(args[0].equalsIgnoreCase("test"))
			{
				
				
				
				System.out.println(PlayerData.instanceOf(player).getActiveCompanionName());
				
				System.out.println(PlayerData.instanceOf(player).getActiveCompanionName().equals("NONE"));
				
				System.out.println("NONE".length());
				System.out.println(PlayerData.instanceOf(player).getActiveCompanionName().length());
				
				
				
				//main.getAnimation().executeAnimation(player);
				 /* CraftPlayer cp = (CraftPlayer) player;
				 CraftWorld cw = (CraftWorld) player.getLocation().getWorld();
				 EntityArmorStand stand = new EntityArmorStand(cw.getHandle(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

				 stand.setCustomNameVisible(true);
				 stand.setInvisible(false);
				 stand.setCustomName(new ChatComponentText("test"));

				 PacketPlayOutSpawnEntityLiving teleportPacket = new PacketPlayOutSpawnEntityLiving(stand);
				 cp.getHandle().playerConnection.sendPacket(teleportPacket); */
				
				//main.getCompanionUtil().delayCompanionSpawn(player);
				
			/*	PreparedStatement p = null;
				try(Connection conn = main.getDatabase().getHikari().getConnection())
				{
					p = conn.prepareStatement("SELECT companion FROM companions_owned WHERE UUID = ?");
					p.setString(1, player.getUniqueId().toString());

					ResultSet rs = p.executeQuery();
					
					
					while(rs.next())
					{
						System.out.println(rs.getString("companion"));
					}
					
					
					main.getDatabase().close(conn, p, rs);
					
				} 
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} */
			} 
			else if(args[0].equalsIgnoreCase("toggle"))
			{
				if(player.hasPermission("companions.player.toggle"))
				{
					if(PlayerData.instanceOf(player).getActiveCompanionName() != "NONE" && PlayerData.instanceOf(player).getActiveCompanionName() != null)
					{
						if(!PlayerData.instanceOf(player).isToggled())
						{
							PlayerData.instanceOf(player).toggleCompanion();
					    	
					    	
					    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getToggledAwayMessage()));
						}
						else
						{
							
							PlayerData.instanceOf(player).setToggled(false);
							//main.getCompanions().summonCompanion(player);
							main.getCompanionPacket().loadCompanion(player);
							
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getToggledBackMessage()));
							
						}
	
					}
					else
					{
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoActiveCompanionMessage()));
					}
				}
				else
				{
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNoPermissionMessage()));
				}
			}
			else if(args[0].equalsIgnoreCase("version"))
			{
				if(player.hasPermission("companions.admin.version"))
				{
					player.sendMessage("");
					player.sendMessage(ChatColor.GOLD + "                      ↣ ------------- ↢" + ChatColor.GRAY 
							+  "\nServer is running " + ChatColor.GOLD + "Companions version " + main.getDescription().getVersion() + ChatColor.GOLD + 
							"\n WIKI: " + ChatColor.GRAY +  "https://gitlab.com/Aster0/companions-reborn/wikis/home" + ChatColor.GOLD + "\n                      ↣ ------------- ↢" );
					player.sendMessage("");
				}
				
			}
			else if(args[0].equalsIgnoreCase("help"))
			{
				for(String message : main.getFileHandler().getHelpMessage())
				{
					
					
					if(message.contains("ADMIN:"))
					{
						if(player.hasPermission("companions.admin.help"))
						{
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
							
							
						}
					}
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
				}
			}
			else
			{
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getInvalidUsageMessage()));
			}

		}
		else
		{
			if(args.length > 0)
			{
				if(args[0].equalsIgnoreCase("reload"))
				{
					main.getFileManager().reloadConfigs();
					
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getReloadMessage()));
				}
				else if(args[0].equalsIgnoreCase("version"))
				{

					sender.sendMessage("");
					sender.sendMessage(ChatColor.GRAY 
							+  "\nServer is running " + ChatColor.GOLD + "Companions version " + main.getDescription().getVersion() + ChatColor.GOLD + 
							"\n WIKI: " + ChatColor.GRAY +  "https://gitlab.com/Aster0/companions-reborn/wikis/home");
					sender.sendMessage("");
					
				
				}
				
			}
			else
			{
				System.out.println(ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getNotPlayerMessage()));
			}
		}
		return false;
	}

}
