package me.astero.companions.listener;

import org.bukkit.event.Listener;

import me.astero.companions.CompanionsPlugin;

public class VehicleListener implements Listener {
	
	private CompanionsPlugin main;
	
	public VehicleListener(CompanionsPlugin main)
	{
		this.main = main;
	}
	
	/*@EventHandler
	private void onVehicleEnter(VehicleEnterEvent e)
	{
		
		if(e.getEntered() instanceof Player)
		{
			Player player = (Player) e.getEntered();
			
			PlayerData.instanceOf(player).removeCompanionTemporarily();
			
			if(PlayerData.instanceOf(player).getActiveCompanionName() != "NONE" && PlayerData.instanceOf(player).getActiveCompanionName() != null)
			{
				PlayerData.instanceOf(player).setMounted(true);
			}
			
		}
	}
	
	@EventHandler
	private void onVehicleLeave(VehicleExitEvent e)
	{
		if(e.getExited() instanceof Player)
		{
			Player player = (Player) e.getExited();
			
			if(PlayerData.instanceOf(player).getActiveCompanionName() != "NONE" && PlayerData.instanceOf(player).getActiveCompanionName() != null)
			{
				PlayerData.instanceOf(player).setMounted(false);
				main.getCompanions().summonCompanion(player);
				
			}
			
		}
	}*/

}
