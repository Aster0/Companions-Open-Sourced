package me.astero.companions.filemanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


import lombok.Getter;
import lombok.Setter;
import me.astero.companions.CompanionsPlugin;
import me.astero.companions.util.ItemBuilderUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;


public class FileHandler {
	
	@Getter private Map<String, CompanionDetails> companionDetails;
	@Getter private Map<String, PhysicalAbilitiesDetails> phyiscalAbilityDetails = new HashMap<>();
	
	private String mainPath = "companions.%name%.";
	private String[] secondaryPath = {"name", "playerSkull", "sound", "ability", "chestplate", "leatherColor.red", "leatherColor.blue", "leatherColor.green",
			"rightArmPose.angle1", "rightArmPose.angle2", "rightArmPose.angle3", "leftArmPose.angle1", "leftArmPose.angle2", "leftArmPose.angle3",
			"headPose.angle1", "headPose.angle2", "headPose.angle3", "bodyPose.angle1", "bodyPose.angle2", "bodyPose.angle3", "GUI.shop.name", "GUI.shop.price",
			"GUI.shop.description", "GUI.shop.alreadyPurchased", "GUI.shop.amount", "weapon", "nameVisible", "animation", "bodySkull", "customModelData"};
	
	@Getter private String[] vanishCommands, packetRange;

	@Getter private long interactCooldown;
	
	@Getter private int fireballChance, dodgeChance, vectorJumpCooldown, lightningChance, miningYLevel, speedBurstDuration, speedBurstChance,
	flingChance, flingUpgrade, commandUpgrade, endermanChance, endermanUpgrade, vectorMultiplier;
	
	@Getter @Setter private boolean vault, database, ensureCache = false, actionBarMessage;
	
	@Getter private double flingDistance;
	
	@Getter private String notPlayerMessage, notEnoughMoneyMessage, itemBoughtMessage, abilityBoughtMessage, 
	noPermissionMessage, noUpgradeBuyPermissionMessage, removeCompanionMessage, reloadMessage, companionNotFoundMessage,
	weaponNotFoundMessage, companionGivenMessage, companionReceivedMessage, companionRemovedMessage, companionRemovedFromPlayerMessage, companionSetMessage,
	companionSetForPlayerMessage, noActiveCompanionMessage, inRenamingMessage, inChangingWeaponMessage, renamedCompanionMessage, actionSuccessMessage, 
	changedCompanionWeaponMessage, hideCompanionMessage, abilityCoolDownMessage, invalidUsageMessage, invalidGiveUsageMessage, invalidRemoveUsageMessage,
	companionAlreadyOwnedMessage, companionNotOwnedMessage, toggledBackMessage, toggledAwayMessage, playerInDisabledWorldMessage, playerInVanishMessage, playerNotInVanishMessage,
	playerNotFoundMessage, playerNotOnlineMessage, itemGivenMessage, itemReceivedMessage, invalidUpgradeArgumentMessage, forceActiveSuccessfulMessage, forceActiveNotSuccessfulMessage,
	forceUpgradeSuccessfulMessage, forceUpgradeNotSuccessfulMessage, abilityMaxedMessage, noCompanionsMessage, abilityDowngradedMaxedMessage,
	tradeUnsuccessfulMessage, tradeSuccessfulMessage, tradeAlreadyOwnMessage, companionCoinGivenSuccessfulMessage, companionCoinRemovedSuccessfulMessage,
	companionCoinGivenMessage, companionCoinRemovedMessage, companionCoinStatsMessage, blacklistedNameMessage, companionsShopGoBackCommand;
	
	@Getter private String openCompanionsTitle, openCompanionsSound, companionShopName, ownedCompanionsName,
	upgradeAbilitiesName, ownedCompanionsTitle, ownedCompanionsSound, goBackName, nextPageName, companionDetailName, companionShopTitle, 
	companionShopSound, upgradeAbilitiesTitle, upgradeAbilitiesSound, upgradeDetailsName, goBackUDName, abilityLevelName, abilityLevelMName,
	renameCompanionName, hideCompanionName, changeWeaponName, vectorJumpSound, companionTokenName, companionTokenParticle, companionTokenSound,
	upgradeGoBackCommand, ownedGoBackCommand, vanishPerms, playerDetailsTitle, playerDetailsSound,
	abilityRawLevelPrice, changeRawWeaponPrice, hideRawCompanionPrice, renameRawCompanionPrice;
	
	@Getter private int openCompanionsSize, companionShopSlot, ownedCompanionsSlot, upgradeAbilitiesSlot, ownedCompanionsAmount, companionShopAmount,
	upgradeAbilitiesAmount, ownedCompanionsSize, goBackAmount, goBackSlot, nextPageAmount, nextPageSlot, companionDetailSlot, companionDetailAmount,
	companionShopSize, upgradeAbilitiesSize, upgradeDetailsAmount, upgradeDetailsSlot, goBackUDSlot, goBackUDAmount, abilityLevelSlot, abilityLevelAmount,
	renameCompanionAmount, renameCompanionSlot, hideCompanionAmount, hideCompanionSlot, changeWeaponAmount, changeWeaponSlot, maxAbilityLevel,
	maxNameLength;
	
	@Getter private long abilityLevelPrice, changeWeaponPrice, hideCompanionPrice, renameCompanionPrice;
	
	@Getter private ItemStack companionShop, ownedCompanions, upgradeAbilities, goBack, nextPage, companionDetail, alreadyBought, upgradeDetails, goBackUD,
	abilityLevel, abilityLevelM, renameCompanion, hideCompanionN, changeWeapon, companionToken;
	
	@Getter private List<String> companionShopDescription, ownedCompanionsDescription, upgradeAbilitiesDescription, goBackDescription, nextPageDescription, companionDetailDescription,
	companionTalk, upgradeDetailsDescription, goBackUDDescription, abilityLevelDescription, abilityLevelMDescription, renameCompanionDescription, hideCompanionDescription,
	changeWeaponDescription, helpMessage, disabledWorlds, replacedOwnedLores, companionTokenDescription, playerDetailsMessage, playerDetailsDescription,
	blacklistedNames;

	
	
    private final CompanionsPlugin main;
    
    public FileHandler(CompanionsPlugin main) {
    	this.main = main;
    	
    	cache();
    }
    
    
	public void cache() 
	{

		maxNameLength = main.getConfig().getInt("settings.maxNameLength");
		companionsShopGoBackCommand = main.getConfig().getString("GUI.open-companions-shop.goBackCommand");
		
		packetRange = main.getConfig().getString("settings.packetSearchRange").split(", ");
		
		actionBarMessage = main.getConfig().getBoolean("settings.actionBarMessage");
		
		blacklistedNames = main.getConfig().getStringList("settings.blacklistedNames");
		blacklistedNameMessage = main.getFileManager().getMessagesData().getString("messages.blacklistedName");
		ensureCache = main.getConfig().getBoolean("settings.ensureCache");
		database = main.getConfig().getBoolean("database.use");
		upgradeGoBackCommand = main.getConfig().getString("GUI.upgrade-abilities.goBackCommand");
		ownedGoBackCommand = main.getConfig().getString("GUI.owned-companions.goBackCommand");
		disabledWorlds = main.getConfig().getStringList("settings.disabledWorlds");
		replacedOwnedLores = main.getConfig().getStringList("settings.replacedOwnedLores");
		
		vectorJumpCooldown = main.getFileManager().getCustomAbility().getInt("ability.vectorjump.cooldown");
		vectorJumpSound = main.getFileManager().getCustomAbility().getString("ability.vectorjump.sound");
		fireballChance = main.getFileManager().getCustomAbility().getInt("ability.fireball.chance");
		miningYLevel = main.getFileManager().getCustomAbility().getInt("ability.mining_vision.yLevel");
		lightningChance = main.getFileManager().getCustomAbility().getInt("ability.lightning.chance");
		dodgeChance = main.getFileManager().getCustomAbility().getInt("ability.dodge.chance");
		
		vectorMultiplier  = main.getFileManager().getCustomAbility().getInt("ability.vectorjump.distanceMultiplier");
		
		speedBurstChance = main.getFileManager().getCustomAbility().getInt("ability.speed_burst.chance");
		speedBurstDuration = main.getFileManager().getCustomAbility().getInt("ability.speed_burst.duration") * 20;
		
		flingChance = main.getFileManager().getCustomAbility().getInt("ability.fling.chance");
		flingDistance = main.getFileManager().getCustomAbility().getDouble("ability.fling.distance");
		flingUpgrade = main.getFileManager().getCustomAbility().getInt("ability.fling.increaseChanceByLevel");
		
		endermanChance = main.getFileManager().getCustomAbility().getInt("ability.enderman.chance");
		endermanUpgrade = main.getFileManager().getCustomAbility().getInt("ability.enderman.increaseChanceByLevel");
		
		commandUpgrade = main.getFileManager().getCustomAbility().getInt("ability.command.increaseChanceByLevel");
		
		abilityRawLevelPrice = main.getConfig().getString("GUI.upgrade-abilities.items.abilityLevel.price");
		changeRawWeaponPrice = main.getConfig().getString("GUI.upgrade-abilities.items.changeWeapon.price");
		renameRawCompanionPrice = main.getConfig().getString("GUI.upgrade-abilities.items.renameCompanion.price");
		hideRawCompanionPrice = main.getConfig().getString("GUI.upgrade-abilities.items.hideCompanionName.price");
		
		abilityLevelPrice = Long.valueOf(abilityRawLevelPrice.replace("C", ""));
		changeWeaponPrice = Long.valueOf(changeRawWeaponPrice.replace("C", ""));
		renameCompanionPrice = Long.valueOf(renameRawCompanionPrice.replace("C", ""));
		hideCompanionPrice = Long.valueOf(hideRawCompanionPrice.replace("C", ""));
		
		
		maxAbilityLevel = main.getConfig().getInt("settings.maxAbilityLevel");
		
		upgradeAbilitiesTitle = main.getConfig().getString("GUI.upgrade-abilities.title");
		upgradeAbilitiesSound = main.getConfig().getString("GUI.upgrade-abilities.openSound");
		upgradeAbilitiesSize = main.getConfig().getInt("GUI.upgrade-abilities.size");
		upgradeDetailsName = main.getConfig().getString("GUI.upgrade-abilities.items.upgradeDetails.name");
		upgradeDetailsAmount = main.getConfig().getInt("GUI.upgrade-abilities.items.upgradeDetails.amount");
		upgradeDetailsSlot = main.getConfig().getInt("GUI.upgrade-abilities.items.upgradeDetails.slot");
		upgradeDetailsDescription = main.getConfig().getStringList("GUI.upgrade-abilities.items.upgradeDetails.description");
		goBackUDName = main.getConfig().getString("GUI.upgrade-abilities.items.goBack.name");
		goBackUDAmount = main.getConfig().getInt("GUI.upgrade-abilities.items.goBack.amount");
		goBackUDSlot = main.getConfig().getInt("GUI.upgrade-abilities.items.goBack.slot");
		goBackUDDescription = main.getConfig().getStringList("GUI.upgrade-abilities.items.goBack.description");
		abilityLevelName = main.getConfig().getString("GUI.upgrade-abilities.items.abilityLevel.name");
		abilityLevelAmount = main.getConfig().getInt("GUI.upgrade-abilities.items.abilityLevel.amount");
		abilityLevelSlot = main.getConfig().getInt("GUI.upgrade-abilities.items.abilityLevel.slot");
		abilityLevelDescription = main.getConfig().getStringList("GUI.upgrade-abilities.items.abilityLevel.description");
		abilityLevelMName = main.getConfig().getString("GUI.upgrade-abilities.items.abilityLevel.maxedName");
		abilityLevelMDescription = main.getConfig().getStringList("GUI.upgrade-abilities.items.abilityLevel.maxedDescription");
		
		playerDetailsTitle = main.getConfig().getString("GUI.player-details.title");
		playerDetailsSound = main.getConfig().getString("GUI.player-details.openSound");
		
		renameCompanionName = main.getConfig().getString("GUI.upgrade-abilities.items.renameCompanion.name");
		renameCompanionAmount = main.getConfig().getInt("GUI.upgrade-abilities.items.renameCompanion.amount");
		renameCompanionSlot = main.getConfig().getInt("GUI.upgrade-abilities.items.renameCompanion.slot");
		renameCompanionDescription = main.getConfig().getStringList("GUI.upgrade-abilities.items.renameCompanion.description");
		
		changeWeaponName = main.getConfig().getString("GUI.upgrade-abilities.items.changeWeapon.name");
		changeWeaponAmount = main.getConfig().getInt("GUI.upgrade-abilities.items.changeWeapon.amount");
		changeWeaponSlot = main.getConfig().getInt("GUI.upgrade-abilities.items.changeWeapon.slot");
		changeWeaponDescription = main.getConfig().getStringList("GUI.upgrade-abilities.items.changeWeapon.description");
		
		hideCompanionName = main.getConfig().getString("GUI.upgrade-abilities.items.hideCompanionName.name");
		hideCompanionAmount = main.getConfig().getInt("GUI.upgrade-abilities.items.hideCompanionName.amount");
		hideCompanionSlot = main.getConfig().getInt("GUI.upgrade-abilities.items.hideCompanionName.slot");
		hideCompanionDescription = main.getConfig().getStringList("GUI.upgrade-abilities.items.hideCompanionName.description");
		
		vanishCommands = main.getConfig().getString("settings.vanish.commands").split(", ");
		vanishPerms = main.getConfig().getString("settings.vanish.perms");
		
		
		companionTalk = main.getConfig().getStringList("settings.companionTalk");
		interactCooldown = main.getConfig().getLong("settings.interactCooldown");
		
		notPlayerMessage = main.getFileManager().getMessagesData().getString("messages.notPlayer");
		notEnoughMoneyMessage = main.getFileManager().getMessagesData().getString("messages.notEnoughMoney");
		itemBoughtMessage = main.getFileManager().getMessagesData().getString("messages.itemBought");
		abilityBoughtMessage = main.getFileManager().getMessagesData().getString("messages.abilityBought");
		noPermissionMessage = main.getFileManager().getMessagesData().getString("messages.noPermission");
		noUpgradeBuyPermissionMessage = main.getFileManager().getMessagesData().getString("messages.noUpgradeBuyPermission");
		removeCompanionMessage = main.getFileManager().getMessagesData().getString("messages.removeCompanion"); 
		reloadMessage = main.getFileManager().getMessagesData().getString("messages.reload");
		companionNotFoundMessage = main.getFileManager().getMessagesData().getString("messages.companionNotFound");
		weaponNotFoundMessage = main.getFileManager().getMessagesData().getString("messages.weaponNotFound");
		companionGivenMessage = main.getFileManager().getMessagesData().getString("messages.companionGiven");
		companionReceivedMessage = main.getFileManager().getMessagesData().getString("messages.companionReceived");
		companionRemovedMessage = main.getFileManager().getMessagesData().getString("messages.companionRemoved");
		companionRemovedFromPlayerMessage = main.getFileManager().getMessagesData().getString("messages.companionRemovedFromPlayer");
		companionSetMessage = main.getFileManager().getMessagesData().getString("messages.companionSet");
		companionSetForPlayerMessage = main.getFileManager().getMessagesData().getString("messages.companionSetForPlayer");
		noActiveCompanionMessage = main.getFileManager().getMessagesData().getString("messages.noActiveCompanion");
		inRenamingMessage = main.getFileManager().getMessagesData().getString("messages.inRenaming");
		inChangingWeaponMessage = main.getFileManager().getMessagesData().getString("messages.inChangingWeapon");
		renamedCompanionMessage = main.getFileManager().getMessagesData().getString("messages.renamedCompanion");
		actionSuccessMessage = main.getFileManager().getMessagesData().getString("messages.actionSuccess");
		changedCompanionWeaponMessage = main.getFileManager().getMessagesData().getString("messages.changedCompanionWeapon");
		hideCompanionMessage = main.getFileManager().getMessagesData().getString("messages.hideCompanion");
		abilityCoolDownMessage = main.getFileManager().getMessagesData().getString("messages.abilityCooldown");
		invalidUsageMessage = main.getFileManager().getMessagesData().getString("messages.invalidUsage");
		invalidGiveUsageMessage = main.getFileManager().getMessagesData().getString("messages.invalidGiveUsage");
		invalidRemoveUsageMessage = main.getFileManager().getMessagesData().getString("messages.invalidRemoveUsage");
		companionAlreadyOwnedMessage = main.getFileManager().getMessagesData().getString("messages.companionAlreadyOwned");
		companionNotOwnedMessage = main.getFileManager().getMessagesData().getString("messages.companionNotOwned");
		toggledBackMessage = main.getFileManager().getMessagesData().getString("messages.toggledBack");
		toggledAwayMessage = main.getFileManager().getMessagesData().getString("messages.toggledAway");
		playerInDisabledWorldMessage = main.getFileManager().getMessagesData().getString("messages.playerInDisabledWorld");
		playerInVanishMessage = main.getFileManager().getMessagesData().getString("messages.playerInVanish");
		playerNotInVanishMessage = main.getFileManager().getMessagesData().getString("messages.playerNotInVanish");
		playerNotFoundMessage = main.getFileManager().getMessagesData().getString("messages.playerNotFound");
		invalidUpgradeArgumentMessage = main.getFileManager().getMessagesData().getString("messages.invalidUpgradeArgument");
		helpMessage = main.getFileManager().getMessagesData().getStringList("messages.helpMessage");
		forceActiveSuccessfulMessage = main.getFileManager().getMessagesData().getString("messages.forceActiveSuccess");
		forceActiveNotSuccessfulMessage = main.getFileManager().getMessagesData().getString("messages.forceActiveNotSuccess");
		forceUpgradeSuccessfulMessage = main.getFileManager().getMessagesData().getString("messages.forceUpgradeSuccessful");
		forceUpgradeNotSuccessfulMessage = main.getFileManager().getMessagesData().getString("messages.forceUpgradeNotSuccessful");
		abilityMaxedMessage = main.getFileManager().getMessagesData().getString("messages.abilityMaxed");
		abilityDowngradedMaxedMessage = main.getFileManager().getMessagesData().getString("messages.abilityDowngradedMaxed");
		playerDetailsMessage = main.getFileManager().getMessagesData().getStringList("messages.playerDetails");
		tradeUnsuccessfulMessage = main.getFileManager().getMessagesData().getString("messages.tradeUnSuccessful");
		tradeSuccessfulMessage = main.getFileManager().getMessagesData().getString("messages.tradeSuccessful");
		tradeAlreadyOwnMessage = main.getFileManager().getMessagesData().getString("messages.tradeAlreadyOwn");
		
		playerNotOnlineMessage = main.getFileManager().getMessagesData().getString("messages.playerNotOnline");
		itemGivenMessage = main.getFileManager().getMessagesData().getString("messages.itemGiven");
		itemReceivedMessage = main.getFileManager().getMessagesData().getString("messages.itemReceived");
		noCompanionsMessage = main.getFileManager().getMessagesData().getString("messages.noCompanions");
		
		companionCoinGivenSuccessfulMessage = main.getFileManager().getMessagesData().getString("messages.companionCoinGivenSuccessful");
		companionCoinRemovedSuccessfulMessage = main.getFileManager().getMessagesData().getString("messages.companionCoinRemovedSuccessful");
		companionCoinRemovedMessage = main.getFileManager().getMessagesData().getString("messages.companionCoinRemoved");
		companionCoinGivenMessage = main.getFileManager().getMessagesData().getString("messages.companionCoinGiven");
		
		companionCoinStatsMessage = main.getFileManager().getMessagesData().getString("messages.companionCoinStats");
		
		
		
		companionShopTitle = main.getConfig().getString("GUI.open-companions-shop.title");
		companionShopSize = main.getConfig().getInt("GUI.open-companions-shop.size");
		companionShopSound = main.getConfig().getString("GUI.open-companions-shop.openSound");
		
		
		companionDetailName = main.getConfig().getString("GUI.owned-companions.items.companionDetails.name");
		companionDetailAmount = main.getConfig().getInt("GUI.owned-companions.items.companionDetails.amount");
		companionDetailSlot = main.getConfig().getInt("GUI.owned-companions.items.companionDetails.slot");
		companionDetailDescription = main.getConfig().getStringList("GUI.owned-companions.items.companionDetails.description");
		
		playerDetailsDescription = main.getConfig().getStringList("GUI.player-details.items.companionDetails.description");
		
		goBackName = main.getConfig().getString("GUI.goBack.name");
		goBackAmount = main.getConfig().getInt("GUI.goBack.amount");
		goBackSlot = main.getConfig().getInt("GUI.goBack.slot");
		goBackDescription = main.getConfig().getStringList("GUI.goBack.description");
		
		companionTokenName = main.getConfig().getString("items.companionToken.name");
		companionTokenParticle = main.getConfig().getString("items.companionToken.particleAnimation");
		companionTokenDescription = main.getConfig().getStringList("items.companionToken.description");
		companionTokenSound = main.getConfig().getString("items.companionToken.soundOnUse");
		
		nextPageName = main.getConfig().getString("GUI.nextPage.name");
		nextPageAmount = main.getConfig().getInt("GUI.nextPage.amount");
		nextPageSlot = main.getConfig().getInt("GUI.nextPage.slot");
		nextPageDescription = main.getConfig().getStringList("GUI.nextPage.description");
		
		openCompanionsTitle = main.getConfig().getString("GUI.open-companions.title");
		openCompanionsSize = main.getConfig().getInt("GUI.open-companions.size");
		openCompanionsSound = main.getConfig().getString("GUI.open-companions.openSound");
		

		
		companionShopName = main.getConfig().getString("GUI.open-companions.items.companion-shop.name");
		companionShopAmount = main.getConfig().getInt("GUI.open-companions.items.companion-shop.amount");
		companionShopSlot = main.getConfig().getInt("GUI.open-companions.items.companion-shop.slot");
		companionShopDescription = main.getConfig().getStringList("GUI.open-companions.items.companion-shop.description");
		
		ownedCompanionsName = main.getConfig().getString("GUI.open-companions.items.owned-companions.name");
		ownedCompanionsAmount = main.getConfig().getInt("GUI.open-companions.items.owned-companions.amount");
		ownedCompanionsSlot = main.getConfig().getInt("GUI.open-companions.items.owned-companions.slot");
		ownedCompanionsDescription = main.getConfig().getStringList("GUI.open-companions.items.owned-companions.description");
		
		upgradeAbilitiesName = main.getConfig().getString("GUI.open-companions.items.upgrade-abilities.name");
		upgradeAbilitiesAmount = main.getConfig().getInt("GUI.open-companions.items.upgrade-abilities.amount");
		upgradeAbilitiesSlot = main.getConfig().getInt("GUI.open-companions.items.upgrade-abilities.slot");
		upgradeAbilitiesDescription = main.getConfig().getStringList("GUI.open-companions.items.upgrade-abilities.description");
		
		ownedCompanionsTitle = main.getConfig().getString("GUI.owned-companions.title");
		ownedCompanionsSize = main.getConfig().getInt("GUI.owned-companions.size");
		ownedCompanionsSound = main.getConfig().getString("GUI.owned-companions.openSound");
		
		
		companionDetails = new HashMap<>();
		
		 for(String getCompanionName : main.getFileManager().getCompanions().getConfigurationSection("companions").getKeys(false))
		 {

			 

				// companionDetails.put(getCompanionName + "_" + thingsToSave[i].replace('.', '_'), main.getFileManager().getCompanions().getString("companions." + getCompanionName + "." + thingsToSave[i]));
				 CompanionDetails cd = new CompanionDetails();
				 
				 cd.setName(main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) + secondaryPath[0]));
				 cd.setPlayerSkull(main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) + secondaryPath[1]));
				 cd.setSound(main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) + secondaryPath[2]));
				 cd.setAbility(main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) + secondaryPath[3]));
				 cd.setAbilityList(main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) + secondaryPath[3]).split("; "));
				 cd.setChestplate(main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) + secondaryPath[4]));
				 
				 cd.setLeatherColorRed(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[5]));
				 cd.setLeatherColorBlue(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[6]));
				 cd.setLeatherColorGreen(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[7]));
				 
				 cd.setRightArmPose1(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[8]));
				 cd.setRightArmPose2(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[9]));
				 cd.setRightArmPose3(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[10]));
				 
				 cd.setLeftArmPose1(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[11]));
				 cd.setLeftArmPose2(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[12]));
				 cd.setLeftArmPose3(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[13]));
				 
				 cd.setHeadPose1(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[14]));
				 cd.setHeadPose2(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[15]));
				 cd.setHeadPose3(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[16]));
				 
				 cd.setBodyPose1(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[17]));
				 cd.setBodyPose2(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[18]));
				 cd.setBodyPose3(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[19]));
				 
				 cd.setItemName(main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) + secondaryPath[20]));
				 cd.setRawPrice(main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) + secondaryPath[21]));
				
				 //cd.setItemPrice(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[21]));

				 cd.setItemPrice(Long.valueOf(cd.getRawPrice().replace("C", "")));
				 cd.setFormatedPrice(main.getFormatNumbers().format(cd.getItemPrice()));
				 
				 List<String> itemDescription = new ArrayList<>();
				 for(String itemDesc : main.getFileManager().getCompanions().getStringList(mainPath.replace("%name%", getCompanionName) + secondaryPath[22]))
				 {
					 itemDescription.add(itemDesc.replace("%price%", cd.getFormatedPrice()));
				 }
				 
				 cd.setItemDescription(itemDescription);
				 cd.setItemAlreadyPurchased(main.getFileManager().getCompanions().getStringList(mainPath.replace("%name%", getCompanionName) + secondaryPath[22]));
				 cd.setItemAmount(main.getFileManager().getCompanions().getInt(mainPath.replace("%name%", getCompanionName) + secondaryPath[24]));
				 cd.setWeapon(main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) + secondaryPath[25]));
				 cd.setNameVisible(main.getFileManager().getCompanions().getBoolean(mainPath.replace("%name%", getCompanionName) + secondaryPath[26]));
				 
				 cd.setAnimation(main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) + secondaryPath[27]));
				 
				 try
				 {
					 for(String information : main.getFileManager().getCompanions().getConfigurationSection(mainPath.replace("%name%", getCompanionName) 
							 + secondaryPath[28]).getKeys(false))
					 {

						 BodySkullData bsd = new BodySkullData();
						 
						 bsd.setPosition(main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) 
										 + secondaryPath[28] + "." + information + ".position"));
						 
						 
						 bsd.setTexture(main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName)
										 + secondaryPath[28] + "." + information + ".texture"));
						 
						 bsd.setId(information);
						 
						 cd.getBodySkull().put(information, bsd);
							 
						/* System.out.println(main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) 
								 + secondaryPath[28] + "." + information + ".position")
						 + main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName)
								 + secondaryPath[28] + "." + information + ".texture"));*/
						 
						
					 }
				 }
				 catch(NullPointerException notFound) {}
				 
				 cd.setCustomModelData(main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) + secondaryPath[29]));
				 
				 cd.setX(main.getFileManager().getCompanions().getDouble(mainPath.replace("%name%", getCompanionName) + "placement.x"));
				 cd.setY(main.getFileManager().getCompanions().getDouble(mainPath.replace("%name%", getCompanionName) + "placement.y"));
				 cd.setZ(main.getFileManager().getCompanions().getDouble(mainPath.replace("%name%", getCompanionName) + "placement.z"));
				 
				 String itemMaterial = main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) + "GUI.shop.type").split(":")[0];


				 int abilityLevel = 1;
				 try {
					 abilityLevel = Integer.valueOf(main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) + secondaryPath[3]).split("@")[0]);
				 }
				 catch(NumberFormatException e) {

				 } // potion level not specified

			 	cd.setAbilityLevel(abilityLevel);
				 
				 // for version 1.8 to see if custom model data has been added to the user's yml
				try // if field not found
				{
					cd.getCustomModelData().equals("");
				}
				catch(NullPointerException e)
				{
					cd.setCustomModelData("NONE");
				}
				
				 if(!itemMaterial.equalsIgnoreCase("COMPANION_SKULL"))
				 {
					try
					{
						cd.setItemType(new ItemBuilderUtil(
								Material.valueOf(itemMaterial),
								cd.getItemName(), 
										cd.getItemAmount())
								.setLore(cd.getItemDescription()).build());
						
						try
						{
							int modelData = Integer.valueOf(
									main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName) 
											+ "GUI.shop.type").split(":")[1]);

							ItemMeta customItemMeta = cd.getItemType().getItemMeta();



							customItemMeta.setCustomModelData(modelData);
							cd.getItemType().setItemMeta(customItemMeta);

						}
						catch(ArrayIndexOutOfBoundsException e) {} // if no custom model data
						
					}
					catch(IllegalArgumentException itemNotFound)
					{
						cd.setItemType(new ItemBuilderUtil(
								Material.STONE,
								cd.getItemName(), 
										cd.getItemAmount())
								.setLore(cd.getItemDescription()).build());
						
						System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.YELLOW + getCompanionName + ChatColor.RED + "'s ItemStack failed to load. - "
								+ "Please check if the material name is for the correct Minecraft server version. " + ChatColor.YELLOW + "(A replacement itemstack will be used)");
					}
				 }
				 else
				 {
					 try
					 {
							cd.setItemType(new ItemBuilderUtil(
									Material.PLAYER_HEAD,
									cd.getItemName(), 
											cd.getItemAmount())
									.setLore(cd.getItemDescription()).build());
					 }
					catch(NoSuchFieldError | NoSuchMethodError error)
					 {
						ItemStack is = new ItemStack(Material.valueOf("SKULL_ITEM"), cd.getItemAmount(), (short) 3);
						
						cd.setItemType(new ItemBuilderUtil(is, cd.getItemName()).setLore(cd.getItemDescription()).build());
					 }
					
					ItemMeta itemMeta = cd.getItemType().getItemMeta();
					
					main.getCompanionUtil().setSkull(cd.getPlayerSkull(), itemMeta, cd.getCustomModelData());
					
					cd.getItemType().setItemMeta(itemMeta);
							
							
						
				 }
					
					
					List<String> ownedItemDescription = new ArrayList<>();
					
					
					for(String description : cd.getItemDescription())
					{
						if(!replacedOwnedLores.contains(description.replace(getCompanionName.toUpperCase(), "%companion%")
								.replace(getCompanionName.substring(0, 1).toUpperCase() + getCompanionName.substring(1).toLowerCase(), "%companion_l%")
								.replace(cd.getFormatedPrice(), "%price%")))
						{
							ownedItemDescription.add(description);

						}
					}
					
					if(!itemMaterial.equals("COMPANION_SKULL"))
					{
						try
						{
							
	
							
	
							
							
							cd.setOwnedItemType(new ItemBuilderUtil(
									Material.valueOf(itemMaterial),
									cd.getItemName(), 
											cd.getItemAmount())
									.setLore(ownedItemDescription).build());
							
							try
							{
								int modelData = Integer.valueOf(
										main.getFileManager().getCompanions().getString(mainPath.replace("%name%", getCompanionName)
												+ "GUI.shop.type").split(":")[1]);

								ItemMeta customItemMeta = cd.getItemType().getItemMeta();



								customItemMeta.setCustomModelData(modelData);
								cd.getItemType().setItemMeta(customItemMeta);
							}
							catch(ArrayIndexOutOfBoundsException e) {} // if no custom model data
							
	
							
						}
						catch(IllegalArgumentException itemNotFound)
						{
							cd.setOwnedItemType(new ItemBuilderUtil(
									Material.STONE,
									cd.getItemName(), 
											cd.getItemAmount())
									.setLore(ownedItemDescription).build());
	
						}
					}
					else
					{
						 try
						 {
								cd.setOwnedItemType(new ItemBuilderUtil(
										Material.PLAYER_HEAD,
										cd.getItemName(), 
												cd.getItemAmount())
										.setLore(ownedItemDescription).build());
						 }
						catch(NoSuchFieldError | NoSuchMethodError error)
						 {
							ItemStack is = new ItemStack(Material.valueOf("SKULL_ITEM"), cd.getItemAmount(), (short) 3);
							
							cd.setOwnedItemType(new ItemBuilderUtil(is, cd.getItemName()).setLore(ownedItemDescription).build());
						 }
						
						ItemMeta itemMeta = cd.getOwnedItemType().getItemMeta();
						
						main.getCompanionUtil().setSkull(cd.getPlayerSkull(), itemMeta, cd.getCustomModelData());
						
						cd.getOwnedItemType().setItemMeta(itemMeta);
					}


					
					
				 
				 companionDetails.put(getCompanionName.toLowerCase(), cd); // Put each Companions in the class to get the initial Companion details. 
		 }
		 
		 try
		 {
			 companionShop = new ItemBuilderUtil(
					 Material.valueOf(main.getConfig().getString("GUI.open-companions.items.companion-shop.type")),
					 companionShopName,
					 companionShopAmount)
					 .setLore(companionShopDescription).build();
		 }
		 catch(IllegalArgumentException itemNotFound)
		 {
			 companionShop = new ItemBuilderUtil(
					 Material.STONE,
					 companionShopName,
					 companionShopAmount)
					 .setLore(companionShopDescription).build();
		 }
		 
		 try
		 {
			 ownedCompanions = new ItemBuilderUtil(
					 Material.valueOf(main.getConfig().getString("GUI.open-companions.items.owned-companions.type")),
					 ownedCompanionsName,
					 ownedCompanionsAmount)
					 .setLore(ownedCompanionsDescription).build();
		 }
		 catch(IllegalArgumentException itemNotFound)
		 {
			 ownedCompanions = new ItemBuilderUtil(
					 Material.STONE,
					 ownedCompanionsName,
					 ownedCompanionsAmount)
					 .setLore(ownedCompanionsDescription).build();
		 }
		 
		 try
		 {
			 upgradeAbilities = new ItemBuilderUtil(
					 Material.valueOf(main.getConfig().getString("GUI.open-companions.items.upgrade-abilities.type")),
					 upgradeAbilitiesName,
					 upgradeAbilitiesAmount)
					 .setLore(upgradeAbilitiesDescription).build();
		 }
		 catch(IllegalArgumentException itemNotFound)
		 {
			 upgradeAbilities = new ItemBuilderUtil(
					 Material.STONE,
					 upgradeAbilitiesName,
					 upgradeAbilitiesAmount)
					 .setLore(upgradeAbilitiesDescription).build();
		 }
		 
		 try
		 {
			 goBack = new ItemBuilderUtil(
					 Material.valueOf(main.getConfig().getString("GUI.goBack.type")),
					 goBackName,
					 goBackAmount)
					 .setLore(goBackDescription).build();
		 }
		 catch(IllegalArgumentException itemNotFound)
		 {
			 goBack = new ItemBuilderUtil(
					 Material.STONE,
					 goBackName,
					 goBackAmount)
					 .setLore(goBackDescription).build();
		 }
		 
		 try
		 {
			 nextPage = new ItemBuilderUtil(
					 Material.valueOf(main.getConfig().getString("GUI.nextPage.type")),
					 nextPageName,
					 nextPageAmount)
					 .setLore(nextPageDescription).build();
		 }
		 catch(IllegalArgumentException itemNotFound)
		 {
			 nextPage = new ItemBuilderUtil(
					 Material.STONE,
					 nextPageName,
					 nextPageAmount)
					 .setLore(nextPageDescription).build();
		 }
		 
		 try
		 {
			 companionDetail = new ItemBuilderUtil(
					 Material.valueOf(main.getConfig().getString("GUI.owned-companions.items.companionDetails.type")),
					 companionDetailName,
					 companionDetailAmount)
					 .build();
		 }
		 catch(IllegalArgumentException itemNotFound)
		 {
			 companionDetail = new ItemBuilderUtil(
					 Material.STONE,
					 companionDetailName,
					 companionDetailAmount)
					 .build();
		 }
		 
		 try
		 {
			 upgradeDetails = new ItemBuilderUtil(
					 Material.valueOf(main.getConfig().getString("GUI.upgrade-abilities.items.upgradeDetails.type")),
					 upgradeDetailsName,
					 upgradeDetailsAmount)
					 .build();
		 }
		 catch(IllegalArgumentException itemNotFound)
		 {
			 upgradeDetails = new ItemBuilderUtil(
					 Material.STONE,
					 upgradeDetailsName,
					 upgradeDetailsAmount)
					 .build();
		 }
		 
		 try
		 {
			 goBackUD = new ItemBuilderUtil(
					 Material.valueOf(main.getConfig().getString("GUI.upgrade-abilities.items.goBack.type")),
					 goBackUDName,
					 goBackUDAmount)
					 .setLore(goBackUDDescription)
					 .build();
		 }
		 catch(IllegalArgumentException itemNotFound)
		 {
			 goBackUD = new ItemBuilderUtil(
					 Material.STONE,
					 goBackUDName,
					 goBackUDAmount)
					 .setLore(goBackUDDescription)
					 .build();
		 }
		 
		 try
		 {
			 abilityLevel = new ItemBuilderUtil(
					 Material.valueOf(main.getConfig().getString("GUI.upgrade-abilities.items.abilityLevel.type")),
					 abilityLevelName,
					 abilityLevelAmount)
					 .setLore(abilityLevelDescription)
					 .build();
		 }
		 catch(IllegalArgumentException itemNotFound)
		 {
			 abilityLevel = new ItemBuilderUtil(
					 Material.STONE,
					 abilityLevelName,
					 abilityLevelAmount)
					 .setLore(abilityLevelDescription)
					 .build();
		 }
		 
		 try
		 {
			 abilityLevelM = new ItemBuilderUtil(
					 Material.valueOf(main.getConfig().getString("settings.abilityMaxedOutItem")),
					 abilityLevelMName,
					 abilityLevelAmount)
					 .setLore(abilityLevelMDescription)
					 .build();
		 }
		 catch(IllegalArgumentException itemNotFound)
		 {
			 abilityLevelM = new ItemBuilderUtil(
					 Material.STONE,
					 abilityLevelMName,
					 abilityLevelAmount)
					 .setLore(abilityLevelMDescription)
					 .build();
		 }
		 
		 try
		 {
			 renameCompanion = new ItemBuilderUtil(
					 Material.valueOf(main.getConfig().getString("GUI.upgrade-abilities.items.renameCompanion.type")),
					 renameCompanionName,
					 renameCompanionAmount)
					 .setLore(renameCompanionDescription)
					 .build();
		 }
		 catch(IllegalArgumentException itemNotFound)
		 {
			 renameCompanion = new ItemBuilderUtil(
					 Material.STONE,
					 renameCompanionName,
					 renameCompanionAmount)
					 .setLore(renameCompanionDescription)
					 .build();
		 }
		 
		 try
		 {
			 hideCompanionN = new ItemBuilderUtil(
					 Material.valueOf(main.getConfig().getString("GUI.upgrade-abilities.items.hideCompanionName.type")),
					 hideCompanionName,
					 hideCompanionAmount)
					 .setLore(hideCompanionDescription)
					 .build();
		 }
		 catch(IllegalArgumentException itemNotFound)
		 {
			 hideCompanionN = new ItemBuilderUtil(
					 Material.STONE,
					 hideCompanionName,
					 hideCompanionAmount)
					 .setLore(hideCompanionDescription)
					 .build();
		 }
		 
		 try
		 {
			 changeWeapon = new ItemBuilderUtil(
					 Material.valueOf(main.getConfig().getString("GUI.upgrade-abilities.items.changeWeapon.type")),
					 changeWeaponName,
					 changeWeaponAmount)
					 .setLore(changeWeaponDescription)
					 .build();
		 }
		 catch(IllegalArgumentException itemNotFound)
		 {
			 changeWeapon = new ItemBuilderUtil(
					 Material.STONE,
					 changeWeaponName,
					 changeWeaponAmount)
					 .setLore(changeWeaponDescription)
					 .build();
		 }
		 
		 try
		 {
			 companionToken = new ItemBuilderUtil(
					 Material.valueOf(main.getConfig().getString("items.companionToken.type")),
					 companionTokenName,
					 1)
					 .setLore(companionTokenDescription)
					 .build();
		 }
		 catch(IllegalArgumentException itemNotFound)
		 {
			 companionToken = new ItemBuilderUtil(
					 Material.STONE,
					 companionTokenName,
					 1)
					 .setLore(companionTokenDescription)
					 .build();
		 }
		 
		 
		 for(String abilityName : main.getCompanionUtil().getPhysicalAbilities())
		 {
			 PhysicalAbilitiesDetails pa = new PhysicalAbilitiesDetails();
			 pa.setChance(main.getFileManager().getCustomAbility().getInt("ability." + abilityName.toLowerCase() + ".chance"));
			 pa.setDuration(main.getFileManager().getCustomAbility().getInt("ability." + abilityName.toLowerCase() + ".duration"));
			 
			 phyiscalAbilityDetails.put(abilityName, pa);

		 }
		 
		 
	


	}
	

	

	
}
