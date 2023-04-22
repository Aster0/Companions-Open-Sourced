package me.astero.companions.companiondata;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

public class CustomCompanion { // Textures, etc. in companiondetails.yml
	
	
	@Getter @Setter private String customName;
	@Getter @Setter private boolean nameVisible;
	@Getter @Setter private int abilityLevel;
	@Getter @Setter private ItemStack customWeapon;
	

	




}
