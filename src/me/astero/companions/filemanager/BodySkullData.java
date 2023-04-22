package me.astero.companions.filemanager;

import lombok.Getter;
import lombok.Setter;

public class BodySkullData {
	
	@Getter @Setter private String texture, position;
	
	@Getter @Setter private float rightArmPose1, rightArmPose2, rightArmPose3, leftArmPose1, leftArmPose2, leftArmPose3, headPose1, headPose2,
	headPose3, bodyPose1, bodyPose2, bodyPose3;
	
	@Getter @Setter private String id;

}
