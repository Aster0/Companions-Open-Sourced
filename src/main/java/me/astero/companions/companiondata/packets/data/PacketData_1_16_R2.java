package me.astero.companions.companiondata.packets.data;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_16_R2.EntityArmorStand;
import net.minecraft.server.v1_16_R2.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_16_R2.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R2.PacketPlayOutEntityTeleport;

public class PacketData_1_16_R2 {
	
	@Getter @Setter private EntityArmorStand companionPacket;
	
	@Getter private Map<String, BodySkullData_1_15> bodySkullPacket = new HashMap<>();
	
	@Getter @Setter private PacketPlayOutEntityMetadata companionMetaData;
	
	
	
	@Getter @Setter private PacketPlayOutEntityEquipment skull, chest, weapon;
	
	@Getter @Setter private PacketPlayOutEntityTeleport teleportPacket;
	
	

}
