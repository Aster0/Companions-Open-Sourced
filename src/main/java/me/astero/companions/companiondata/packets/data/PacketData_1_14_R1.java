package me.astero.companions.companiondata.packets.data;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_14_R1.EntityArmorStand;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityTeleport;

public class PacketData_1_14_R1 {
	
	@Getter @Setter private EntityArmorStand companionPacket;
	@Getter @Setter private PacketPlayOutEntityMetadata companionMetaData;
	
	
	
	@Getter @Setter private PacketPlayOutEntityEquipment skull, chest, weapon;
	
	@Getter @Setter private PacketPlayOutEntityTeleport teleportPacket;

}
