package me.astero.companions.companiondata.packets.data;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.world.entity.decoration.EntityArmorStand;


import java.util.HashMap;
import java.util.Map;

public class PacketData_1_17_R1 {
	
	@Getter @Setter private EntityArmorStand companionPacket;
	
	@Getter private Map<String, BodySkullData_1_15> bodySkullPacket = new HashMap<>();
	
	@Getter @Setter private PacketPlayOutEntityMetadata companionMetaData;

	
	@Getter @Setter private PacketPlayOutEntityEquipment skull, chest, weapon;
	
	@Getter @Setter private PacketPlayOutEntityTeleport teleportPacket;


}
