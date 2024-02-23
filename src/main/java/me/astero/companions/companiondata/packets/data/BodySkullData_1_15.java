package me.astero.companions.companiondata.packets.data;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityTeleport;

public class BodySkullData_1_15 {
	
	@Getter @Setter private EntityArmorStand companionPacket;
	
	@Getter @Setter private PacketPlayOutEntityMetadata companionMetaData;
	
	@Getter @Setter private PacketPlayOutEntityTeleport teleportPacket;
	
	@Getter @Setter private String position;
	

	

}
