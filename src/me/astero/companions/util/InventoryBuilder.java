package me.astero.companions.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryBuilder {
	
	private Inventory inventory;
	public InventoryBuilder(Integer totalSlots, String InventoryName)
	{
		this.inventory = Bukkit.createInventory(null, totalSlots, ChatColor.translateAlternateColorCodes('&', InventoryName));
	}
	
	public InventoryBuilder setItem(Integer itemSlot, ItemStack itemStack)
	{
		this.inventory.setItem(itemSlot, itemStack);
		
		return this;
	}
	
	public InventoryBuilder addItem(ItemStack itemStack)
	{
		this.inventory.addItem(itemStack);
		
		return this;
	}
	
	public Inventory build()
	{
		return this.inventory;
	}

}
