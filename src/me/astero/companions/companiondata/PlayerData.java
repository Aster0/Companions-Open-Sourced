package me.astero.companions.companiondata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import lombok.Setter;
import me.astero.companions.CompanionsPlugin;
import me.astero.companionsapi.api.CAPI;
import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_15_R1.PacketPlayOutSpawnEntityLiving;

public class PlayerData {
	
	private static CompanionsPlugin main = CompanionsPlugin.getPlugin(CompanionsPlugin.class);
	
	private Player player;
	
	@Getter private final static Map<UUID, PlayerData> players = new HashMap<>(); // One static to control the class.
	
	@Getter private final HashMap<Player, Boolean> playerPacketList  = new HashMap<>();

	
	@Getter @Setter private ArmorStand activeCompanion, mysteryCompanion;
	
	
	@Getter @Setter private BukkitTask particleTask, animationTask;
	
	@Getter private List<BukkitTask> commandTask = new ArrayList<>();
	
	@Getter @Setter private boolean renamingCompanion, hidingCompanionName, changingWeapon, flyMode, toggled, miningVision, speedBoosted,
			patreon, particle = true, respawned, teleport, justJoined, mounted, clear, dropped;
	
	@Getter @Setter private int stepsCount;
	@Getter @Setter private long commandInterval;
	@Getter @Setter private int bodyPose = 0, headPose = 0;
	
	@Getter private Map<String, Long> cooldown = new HashMap<>();
	
	private int pageNumber = 1;
	
	@Getter @Setter private String activeCompanionName;
	
	@Getter @Setter private Player playerDetailsTarget;
	
	
	@Getter @Setter private long companionCoin;

	
    public void remove()
    {
    	players.remove(getPlayer().getUniqueId()); 
    }
    
    public List<String> getAllCompanions()
    {
    	List<String> allCompanions = new ArrayList<>();
    	try
    	{
    		for(String getCompanionName : PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().keySet())
	    	{
    			allCompanions.add(getCompanionName);
	    	}
    	}
    	catch(NullPointerException e)
    	{
    		
    	}
	    	
    	return allCompanions;
    }
    
    public void removeCompanion()
    {
    	

    	main.getCompanionUtil().stopCommandAbility(player);
    	main.getPotionEffectAbility().remove(player);
    	main.getCompanionUtil().removeParticles(player);
    	
		main.getCompanionPacket().despawnCompanion(player);
		
		if(CAPI.getSpawnListener() != null)
			CAPI.getSpawnListener().onCompanionDespawn(main.getFileHandler().getCompanionDetails().get(
					PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityList(), player);
		
    	
    	
		/*if(PlayerData.instanceOf(player).getActiveCompanion() != null)  // Check if there's an active Companion. non-packet companion
		{
			activeCompanion.remove();
			main.getCompanionUtil().removeParticles(player);
			main.getAnimation().removeAnimation(player);
			main.getPotionEffectAbility().remove(player);
			main.getAnimation().removeAnimation(player);
	    	
	    	activeCompanion = null;
	    	
		} */

		
    }
    
    public void removeCompanionTemporarily()
    {
		if(PlayerData.instanceOf(player).getActiveCompanion() != null)  // Check if there's an active Companion.
		{
			main.getCompanionUtil().removeParticles(player);
			main.getAnimation().removeAnimation(player);
			main.getPotionEffectAbility().remove(player);
	    	activeCompanion.remove();
	    	activeCompanion = null;
	    	
		}
    }
    
    public void toggleCompanion()
    {

		/*if(PlayerData.instanceOf(player).getActiveCompanion() != null)  // Check if there's an active Companion. non-packet companion
		{
			main.getCompanionUtil().removeParticles(player);
			main.getAnimation().removeAnimation(player);
			main.getCompanionUtil().stopCommandAbility(player);
			main.getPotionEffectAbility().remove(player);
	    	//PlayerData.instanceOf(player).getActiveCompanion().remove();
			main.getCompanionPacket().despawnCompanion(player);
	    	
		} */
    	
    	
		if(CAPI.getSpawnListener() != null)
			CAPI.getSpawnListener().onCompanionToggle(main.getFileHandler().getCompanionDetails().get(
					PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getAbilityList(), player);
		
		
    	main.getCompanionPacket().toggleCompanion(player);
    	this.toggled = true;
    	main.getCustomAbility().giveFly(player);


    }
	
	public PlayerData(Player player)
	{
		this.player = player;
	}
	

	public void updatePlayer(Player player)
	{
		this.player = player;
	}
	
	
    public Player getPlayer() {
        return player;
    }

    public static PlayerData instanceOf(Player player) 
    {	
        players.putIfAbsent(player.getUniqueId(), new PlayerData(player));
        
        if (players.containsKey(player.getUniqueId()))
        {
        	players.get(player.getUniqueId()).updatePlayer(player);
        }
        return players.get(player.getUniqueId());
    }
    
	public int getPageNumber()
	{
		return pageNumber;
	}
	
	public void setPageNumber(int number)
	{
		this.pageNumber = number;
	}
	
	
	
    
    
    


    

    

}
