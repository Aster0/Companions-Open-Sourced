package me.astero.companions.companiondata.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerCache;
import me.astero.companions.companiondata.PlayerData;
import net.md_5.bungee.api.ChatColor;

public class PotionEffectAbility {
	private CompanionsPlugin main;
	
	public PotionEffectAbility(CompanionsPlugin main)
	{
		this.main = main;
	}


	public void give(Player player)
	{
		String activeCompanion = PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase();
		for(String potionEffect : main.getFileHandler().getCompanionDetails().get(activeCompanion).getAbilityList())
		{
			if(!potionEffect.equals("NONE") && !main.getCompanionUtil().getCustomAbilities().contains(potionEffect) && !potionEffect.contains("_DEFENSE_CHANCE") 
					&& !potionEffect.contains("_ATTACK_CHANCE") && !potionEffect.contains("COMMAND") )
			{
				try
				{

					String potionName = getPotionName(potionEffect);

					player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(potionName), Integer.MAX_VALUE,
							PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(activeCompanion).getAbilityLevel() - 1));
				}
				catch(IllegalArgumentException noPotionFound)
				{
					System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.YELLOW + potionEffect + ChatColor.GRAY + " potion effect has failed to load. - "
							+ "Please check if the potion effect name is for the correct Minecraft server version. ");
				}
			}
		}
	}
	
	public void remove(Player player)
	{
		try
		{
			String activeCompanion = PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase();


			for(String potionEffect : main.getFileHandler().getCompanionDetails().get(activeCompanion).getAbilityList())
			{
				if(!main.getCompanionUtil().getCustomAbilities().contains(potionEffect))
				{
					try
					{

						String potionName = getPotionName(potionEffect);

						if(player.hasPotionEffect(PotionEffectType.getByName(potionName)))
						{
		
							player.removePotionEffect(PotionEffectType.getByName(potionName));
		
		
						}
					}
					catch(NullPointerException noPotions) {}
				}
				else
				{
					if(potionEffect.equals("MINING_VISION"))
					{
						player.removePotionEffect(PotionEffectType.getByName("NIGHT_VISION"));
					}
				}
				
			}
		}
		catch(NullPointerException noActiveCompanion) {}
	}

	public String getPotionName(String potionEffect)
	{
		String potionName;
		try
		{
			potionName = potionEffect.split("@")[1];
		}
		catch(ArrayIndexOutOfBoundsException e ) {
			potionName = potionEffect;


		} // potion start level not specified

		return potionName;
	}
}
