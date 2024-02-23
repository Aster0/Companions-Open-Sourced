package me.astero.companions.companiondata.packets.data;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R2.EntityArmorStand;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityTeleport;

public class PacketData_1_8_R2 {
	
	@Getter @Setter private EntityArmorStand companionPacket;
	@Getter @Setter private PacketPlayOutEntityMetadata companionMetaData;
	
	
	
	@Getter @Setter private PacketPlayOutEntityEquipment skull, chest, weapon;
	
	@Getter @Setter private PacketPlayOutEntityTeleport teleportPacket;

}
