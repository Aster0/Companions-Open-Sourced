package me.astero.companions.filemanager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

public class CompanionDetails {
	
	@Getter @Setter private String name, playerSkull, sound, ability, chestplate, weapon, animation, customModelData;
	
	@Getter @Setter private int leatherColorRed, leatherColorBlue, leatherColorGreen, abilityLevel;
	
	@Getter @Setter private float rightArmPose1, rightArmPose2, rightArmPose3, leftArmPose1, leftArmPose2, leftArmPose3, headPose1, headPose2,
	headPose3, bodyPose1, bodyPose2, bodyPose3;
	
	@Getter @Setter private boolean nameVisible;
	@Getter @Setter private double x, y, z;
	
	@Setter private String[] abilityList;
	
	@Getter @Setter private ItemStack itemType, ownedItemType;
	@Getter @Setter private String itemName, formatedPrice, rawPrice;
	@Getter @Setter private int itemAmount;
	@Getter @Setter private long itemPrice;
	@Getter @Setter private List<String> itemDescription, itemAlreadyPurchased;
	
	@Getter private Map<String, BodySkullData> bodySkull = new HashMap<>();
	
	public List<String> getAbilityList()
	{
		return Arrays.asList(abilityList);
	}


	

	

}
