package me.astero.companions.companiondata;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.astero.companions.CompanionsPlugin;

public class PlayerCache {
	
	
	private Player player;
	
	@Getter private final static Map<UUID, PlayerCache> players = new HashMap<>(); // One static to control the class.
	
	@Getter private final Map<String, CustomCompanion> ownedCache = new HashMap<>();
	
	@Getter @Setter private String cachedCompanionName, checkCache;
	@Getter @Setter private long cachedCompanionCoins;
	
	private UUID playerUUID;
	
    public void remove()
    {
    	players.remove(getPlayer().getUniqueId()); 
    }
	
	public PlayerCache(UUID uuid)
	{
		this.playerUUID = uuid;
	}
	

	public void updatePlayer(UUID uuid)
	{
		this.playerUUID = uuid;
	}
	
	
    public Player getPlayer() {
    	
    	this.player = Bukkit.getPlayer(playerUUID);
    	
        return player;
    }

    public static PlayerCache instanceOf(UUID uuid) 
    {	
        players.putIfAbsent(uuid, new PlayerCache(uuid));
        
        if (players.containsKey(uuid))
        {
        	players.get(uuid).updatePlayer(uuid);
        }
        return players.get(uuid);
    }
    
    

    

    

}
