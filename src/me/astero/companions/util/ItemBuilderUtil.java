package me.astero.companions.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;



public class ItemBuilderUtil {
	
	private ItemStack itemStack;
	private ArrayList<String> setLore = new ArrayList<>();
	
	public ItemBuilderUtil(Material material, String displayName, int stackAmount)
	{

		this.itemStack = new ItemStack(material, stackAmount);

	
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		
		this.itemStack.setItemMeta(itemMeta);
		
		
	}
	
	public ItemBuilderUtil(ItemStack itemStack, String displayName)
	{

		this.itemStack = itemStack;

	
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		
		this.itemStack.setItemMeta(itemMeta);
		
		
	}
	
	public ItemBuilderUtil setLore(List<String> list)
	{
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		

		for(String lore : list)
		{
			setLore.add(ChatColor.translateAlternateColorCodes('&', lore));
		}
		itemMeta.setLore(setLore);
		
		
		this.itemStack.setItemMeta(itemMeta);
		
		return this;
	}
	
	public ItemBuilderUtil setLore(String... lore)
	{
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		
		itemMeta.setLore(Arrays.asList(lore));
		
		
		this.itemStack.setItemMeta(itemMeta);
		
		return this;
	}
	
	public ItemBuilderUtil setGlow()
	{
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		return this;
	}
	
	
	public ItemStack build()
	{
		return this.itemStack;
	}

}
