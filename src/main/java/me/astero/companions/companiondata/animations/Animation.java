package me.astero.companions.companiondata.animations;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerData;

public class Animation {
	
	private CompanionsPlugin main;
	
	public Animation(CompanionsPlugin main)
	{
		this.main = main;
		
	}
	
	public void executeAnimation(Player player)
	{
		
		
		List<String> animationDetails = 
				
				Arrays.asList(main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player)
						.getActiveCompanionName().toLowerCase()).getAnimation().split("; "));
		
		
		PlayerData.instanceOf(player).setAnimationTask(Bukkit.getScheduler().runTaskTimerAsynchronously(main, new Runnable() {
			 
			
			@Override
			public void run() {
				

				if(animationDetails.contains("HEADSHAKE"))
				{
					
					
					
					int pose1 = PlayerData.instanceOf(player).getHeadPose();
					
					if(pose1 == 0)
						PlayerData.instanceOf(player).setHeadPose(15);
	
					else if(pose1 == 15)
						PlayerData.instanceOf(player).setHeadPose(0);
					
					
					PlayerData.instanceOf(player).getActiveCompanion().setHeadPose(new EulerAngle(
							Math.toRadians(pose1),
							Math.toRadians(0),
							Math.toRadians(0)));
					
					
					
				}
				
				
				
				if(animationDetails.contains("GHOSTLY"))
				{
					
				
					
					int pose1 = PlayerData.instanceOf(player).getBodyPose();
					
					if(pose1 == 0)
						pose1 = 37;
					
					
					
					if(pose1 == 37)
						PlayerData.instanceOf(player).setBodyPose(57);
					else if(pose1 == 57)
						PlayerData.instanceOf(player).setBodyPose(37);
					
					PlayerData.instanceOf(player).getActiveCompanion().setBodyPose(new EulerAngle(
							Math.toRadians(pose1),
							Math.toRadians(0),
							Math.toRadians(0)));
					
					
					
				}
				
				
				
				

				


	
				
				
			}

		}, 15L, 15L));
	}
	
	public void removeAnimation(Player player)
	{
		if(PlayerData.instanceOf(player).getAnimationTask() != null)
		{
			PlayerData.instanceOf(player).getAnimationTask().cancel();
			PlayerData.instanceOf(player).setAnimationTask(null);
		}
	}

}
