package me.astero.companions.api;

import org.bukkit.entity.Player;

import me.astero.companions.companiondata.PlayerData;

public class CAPI {
	
	
	/* 
	 *  If the player has a Companion summoned.
	 * 
	 *  @param Player instance
	 *  @return true if summoned, false if not summoned
	 */
    public static boolean isSummoned(Player player)
    {
        if(PlayerData.instanceOf(player).getActiveCompanionName() == null ||
                PlayerData.instanceOf(player).getActiveCompanionName() == "NONE")
        {
            return false;
        }

        return true;
    }
	
	/* 
	 *  Gets the Summoned Companion's Name.
	 * 
	 *  @param Player instance
	 *  @return the Companion's name, null if not summoned before, "NONE" if summoned before but removed.
	 */
	public String getActiveCompanionName(Player player) 
	{
		return PlayerData.instanceOf(player).getActiveCompanionName();
	}
	
	
	


}
