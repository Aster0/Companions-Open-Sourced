package me.astero.companions.companiondata.packets;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface CompanionPacket {
	
	public void loadCompanion(Player player);
	
	public void companionFollow(Player player);
	
	public void despawnCompanion(Player player, Player packetPlayer);
	
	public void despawnCompanion(Player player);
	public void toggleCompanion(Player player);
	
	public void setCustomName(Player player, String newName);
	public void setCustomNameVisible(Player player, boolean visible);
	public void setCustomWeapon(Player player, ItemStack itemStack);

}
