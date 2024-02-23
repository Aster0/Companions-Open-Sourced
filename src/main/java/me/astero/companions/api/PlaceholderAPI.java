package me.astero.companions.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.astero.companions.companiondata.PlayerCache;
import me.astero.companions.companiondata.PlayerData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

/**
 * This class will automatically register as a placeholder expansion 
 * when a jar including this class is added to the directory 
 * {@code /plugins/PlaceholderAPI/expansions} on your server.
 * <br>
 * <br>If you create such a class inside your own plugin, you have to
 * register it manually in your plugins {@code onEnable()} by using 
 * {@code new YourExpansionClass().register();}
 */
public class PlaceholderAPI extends PlaceholderExpansion {

    /**
     * This method should always return true unless we
     * have a dependency we need to make sure is on the server
     * for our placeholders to work!
     *
     * @return always true since we do not have any dependencies.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * 
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return "Astero";
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest 
     * method to obtain a value if a placeholder starts with our 
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "companions";
    }

    /**
     * This is the version of this expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return "1.0.0";
    }
  
    /**
     * This is the method called when a placeholder with our identifier 
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link org.bukkit.OfflinePlayer OfflinePlayer}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return Possibly-null String of the requested identifier.
     */
    @Override
    public String onRequest(OfflinePlayer player, String identifier){

    	Player onlinePlayer = (Player) player;
    	
        if(identifier.equals("activecompanion"))
        {
        	
        	
        	String activeCompanion;
        	
        	if(PlayerData.instanceOf(onlinePlayer).getActiveCompanionName() == null)
        	{
        		activeCompanion = "NONE";
        	}
        	else
        	{
        		activeCompanion = PlayerData.instanceOf(onlinePlayer).getActiveCompanionName();
        	}
        	
        	return activeCompanion;
        }
        else if(identifier.equals("companionlevel"))
        {
        	String abilityLevel;
        	

        	try
        	{
        		abilityLevel = String.valueOf(PlayerCache.instanceOf(onlinePlayer.getUniqueId()).getOwnedCache()
						.get(PlayerData.instanceOf(onlinePlayer).getActiveCompanionName().toLowerCase()).getAbilityLevel());
        	}
        	catch(NullPointerException e)
        	{
        		abilityLevel = "NOT EQUIPPED";
        	}
        	
        	return abilityLevel;
        }
        else if(identifier.equals("companionname"))
        {
        	String name;
        	
        	
        	try
        	{
	    		name = String.valueOf(PlayerCache.instanceOf(onlinePlayer.getUniqueId()).getOwnedCache()
						.get(PlayerData.instanceOf(onlinePlayer).getActiveCompanionName().toLowerCase()).getCustomName());
        	}
        	catch(NullPointerException e)
        	{
        		name = "NOT EQUIPPED";
        	}
        
        	
        	return name;
        }
        else if(identifier.equals("companioncoin"))
        {

        
        	
        	return String.valueOf(PlayerData.instanceOf(onlinePlayer).getCompanionCoin());
        }
        else if(identifier.equals("companionsize"))
        {
        	return String.valueOf(PlayerCache.instanceOf(onlinePlayer.getUniqueId()).getOwnedCache().size());
        }



        // We return null if an invalid placeholder (f.e. %example_placeholder3%) 
        // was provided
        return null;
    }
}