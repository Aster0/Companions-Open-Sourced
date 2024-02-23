package me.astero.companions.companiondata.packets;

import com.mojang.datafixers.util.Pair;
import me.astero.companions.CompanionsPlugin;
import me.astero.companions.companiondata.PlayerCache;
import me.astero.companions.companiondata.PlayerData;
import me.astero.companions.companiondata.packets.data.PacketData_1_17_R1;
import me.astero.companions.filemanager.BodySkullData;
import me.astero.companions.filemanager.CompanionDetails;
import me.astero.companions.util.ItemBuilderUtil;
import net.minecraft.core.Vector3f;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CompanionPacket_1_20_R2 implements CompanionPacket, Listener {
    private final Map<UUID, PacketData_1_17_R1> packetData = new HashMap<>();
    private final CompanionsPlugin main;

    public CompanionPacket_1_20_R2(CompanionsPlugin main) {
        this.main = main;
    }

    public ItemStack setSkull(Player player) {
        ItemStack companionSkull;
        try {
            String activeCompanionName = PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase();
            CompanionDetails details = this.main.getFileHandler().getCompanionDetails().get(activeCompanionName);
            if (details.getCustomModelData().equals("NONE")) {
                companionSkull = (new ItemBuilderUtil(Material.PLAYER_HEAD, activeCompanionName.toUpperCase() + "'S HEAD", 1)).build();
            } else {
                Material skullMaterial = Material.valueOf(details.getCustomModelData().split(":")[0]);
                companionSkull = (new ItemBuilderUtil(skullMaterial, activeCompanionName.toUpperCase() + "'S HEAD", 1)).build();
            }
        } catch (IllegalArgumentException var6) {
            companionSkull = new ItemStack(Material.PLAYER_HEAD, 1);
        }

        SkullMeta companionHeadMeta = (SkullMeta) companionSkull.getItemMeta();
        String playerSkull = this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getPlayerSkull();
        String customModelData = this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData();
        this.main.getCompanionUtil().setSkull(playerSkull, companionHeadMeta, customModelData);
        companionSkull.setItemMeta(companionHeadMeta);
        return companionSkull;
    }

    public ItemStack setSkull(Player player, String url) {
        ItemStack companionSkull;
        try {
            if (this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData().equals("NONE")) {
                companionSkull = (new ItemBuilderUtil(Material.PLAYER_HEAD, PlayerData.instanceOf(player).getActiveCompanionName().toUpperCase() + "'S HEAD", 1)).build();
            } else {
                companionSkull = (new ItemBuilderUtil(Material.valueOf(this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData().split(":")[0]), PlayerData.instanceOf(player).getActiveCompanionName().toUpperCase() + "'S HEAD", 1)).build();
            }
        } catch (NoSuchMethodError | NoSuchFieldError var5) {
            companionSkull = new ItemStack(Material.PLAYER_HEAD, 1);
        }

        SkullMeta companionHeadMeta;
        if (this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData().equals("NONE")) {
            companionHeadMeta = (SkullMeta) companionSkull.getItemMeta();
            this.main.getCompanionUtil().setSkull(this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getPlayerSkull(), companionHeadMeta, this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData());
            companionSkull.setItemMeta(companionHeadMeta);
        } else {
            companionHeadMeta = (SkullMeta) companionSkull.getItemMeta();
            this.main.getCompanionUtil().setSkull(this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getPlayerSkull(), companionHeadMeta, this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomModelData());
            companionSkull.setItemMeta(companionHeadMeta);
        }

        return companionSkull;
    }

    public void setBodySkull(Player player) {
        if (this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getChestplate().equals("BODYSKULL")) {

            for (BodySkullData bsd : this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getBodySkull().values()) {
                String[] position = bsd.getPosition().split(", ");
                String texture = bsd.getTexture();
                double x = Math.cos(Math.toRadians(player.getLocation().getYaw() - 180.0F));
                double z = Math.sin(Math.toRadians(player.getLocation().getYaw() - 180.0F));
                Location loc = player.getLocation().add(x + Double.parseDouble(position[0]), this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getY() + Double.parseDouble(position[1]), z + Double.parseDouble(position[2]));
                CraftPlayer cp = (CraftPlayer) player;
                CraftWorld cw = (CraftWorld) player.getLocation().getWorld();
                EntityArmorStand armorStand = new EntityArmorStand(cw.getHandle(), loc.getX(), loc.getY(), loc.getZ());
                PacketPlayOutSpawnEntity entity = new PacketPlayOutSpawnEntity(armorStand, 78);
                cp.getHandle().c.a(entity, null);
                armorStand.n(PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).isNameVisible());
                armorStand.j(true);
                armorStand.a(true);
                armorStand.s(true);
                armorStand.t(true);
                armorStand.e(true);
                armorStand.n(false);
                List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> test = new ArrayList<>();
                test.add(Pair.of(EnumItemSlot.f, CraftItemStack.asNMSCopy(this.setSkull(player, texture))));
                PacketPlayOutEntityEquipment head = new PacketPlayOutEntityEquipment(armorStand.ah(), test);
                cp.getHandle().c.a(head, null);
                PacketPlayOutEntityMetadata packetTeleport = new PacketPlayOutEntityMetadata(armorStand.ah(), armorStand.al().c());
                cp.getHandle().c.a(packetTeleport, null);
            }
        }

    }

    public ItemStack setChest(Player player) {
        ItemStack companionChest;
        if (this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getChestplate().equals("LEATHER_CHESTPLATE")) {
            companionChest = new ItemStack(Material.LEATHER_CHESTPLATE);
            LeatherArmorMeta companionLeatherChestMeta = (LeatherArmorMeta) companionChest.getItemMeta();
            companionLeatherChestMeta.setColor(Color.fromRGB(this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getLeatherColorRed(), this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getLeatherColorGreen(), this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getLeatherColorBlue()));
            companionChest.setItemMeta(companionLeatherChestMeta);
        } else {
            try {
                companionChest = new ItemStack(Material.valueOf(this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getChestplate()));
            } catch (IllegalArgumentException var4) {
                companionChest = new ItemStack(Material.LEGACY_DIAMOND_CHESTPLATE);
                System.out.println(ChatColor.GOLD + "COMPANIONS → " + ChatColor.YELLOW + this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getChestplate() + ChatColor.RED + " ChestPlate Material is not found - Please check if the material name is for the correct Minecraft server version. " + ChatColor.YELLOW + "(A replacement ChestPlate will be used)");
            }

            ItemMeta companionChestMeta = companionChest.getItemMeta();
            companionChest.setItemMeta(companionChestMeta);
        }

        return companionChest;
    }

    public ItemStack setWeapon(Player player) {
        return PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomWeapon();
    }

    public void setArmorPose(Player player, EntityArmorStand armorStand) {
        CompanionDetails cd = this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase());
        armorStand.b(new Vector3f(cd.getBodyPose1(), cd.getBodyPose2(), cd.getBodyPose3()));
        armorStand.a(new Vector3f(cd.getHeadPose1(), cd.getHeadPose2(), cd.getHeadPose3()));
        armorStand.c(new Vector3f(cd.getLeftArmPose1(), cd.getLeftArmPose2(), cd.getLeftArmPose3()));
        armorStand.d(new Vector3f(cd.getRightArmPose1(), cd.getRightArmPose2(), cd.getRightArmPose3()));
    }

    @Override
    public void setCustomName(Player player, String newName) {
        IChatBaseComponent iChatBaseComponent = IChatBaseComponent.a(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache()
                .get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomName()));
        this.packetData.get(player.getUniqueId()).getCompanionPacket().b(iChatBaseComponent);
        this.updateCompanion(player);
    }

    @Override
    public void setCustomNameVisible(Player player, boolean visible) {
        this.packetData.get(player.getUniqueId()).getCompanionPacket().n(visible);
        this.updateCompanion(player);
    }


    @Override
    public void setCustomWeapon(Player player, ItemStack itemStack) {
        this.packetData.get(player.getUniqueId()).getCompanionPacket().a(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack));
        this.updateCompanion(player);
    }

    public void updateCompanion(Player player) {
        this.despawnCompanion(player);
        this.loadCompanion(player);
    }

    @Override
    public void loadCompanion(final Player player) {
        if (PlayerData.instanceOf(player).getActiveCompanionName() != null && !PlayerData.instanceOf(player).getActiveCompanionName().equals("NONE") && !PlayerData.instanceOf(player).isToggled()) {
            if (!this.main.getFileHandler().getDisabledWorlds().contains(player.getWorld().getName())) {
                try {
                    this.main.getPotionEffectAbility().give(player);
                } catch (IllegalStateException ignored) {
                }

                this.main.getCustomAbility().giveFly(player);
                this.main.getCustomAbility().executeCommand(player);


                Bukkit.getScheduler().runTaskAsynchronously(this.main, () -> {
                    if (PlayerData.instanceOf(player).isPatreon() && PlayerData.instanceOf(player).isParticle()) {
                        CompanionPacket_1_20_R2.this.main.getCompanions().giveParticle(player);
                    }

                    double x = Math.cos(Math.toRadians(player.getLocation().getYaw() - 180.0F) + CompanionPacket_1_20_R2.this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getX());
                    double z = Math.sin(Math.toRadians(player.getLocation().getYaw() - 180.0F) + CompanionPacket_1_20_R2.this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getZ());
                    Location loc = player.getLocation().add(x, CompanionPacket_1_20_R2.this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getY(), z);
                    CraftPlayer cp = (CraftPlayer) player;
                    CraftWorld cw = (CraftWorld) player.getLocation().getWorld();
                    EntityArmorStand armorStand = new EntityArmorStand(cw.getHandle(), loc.getX(), loc.getY(), loc.getZ());
                    PacketPlayOutSpawnEntity entity = new PacketPlayOutSpawnEntity(armorStand, 78);
                    cp.getHandle().c.a(entity, null);
                    armorStand.n(PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).isNameVisible());
                    armorStand.j(true);
                    armorStand.a(true);
                    armorStand.s(true);
                    armorStand.t(true);
                    armorStand.e(true);
                    CompanionPacket_1_20_R2.this.setArmorPose(player, armorStand);

                    IChatBaseComponent iChatBaseComponent = IChatBaseComponent.a(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', PlayerCache.instanceOf(player.getUniqueId()).getOwnedCache()
                            .get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getCustomName()));


                    armorStand.b(iChatBaseComponent);
                    List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> headList = new ArrayList<>();
                    headList.add(Pair.of(EnumItemSlot.f, CraftItemStack.asNMSCopy(CompanionPacket_1_20_R2.this.setSkull(player))));
                    PacketPlayOutEntityEquipment head = new PacketPlayOutEntityEquipment(armorStand.ah(), headList);
                    List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> weaponList = new ArrayList<>();
                    weaponList.add(Pair.of(EnumItemSlot.a, CraftItemStack.asNMSCopy(CompanionPacket_1_20_R2.this.setWeapon(player))));
                    PacketPlayOutEntityEquipment weapon = new PacketPlayOutEntityEquipment(armorStand.ah(), weaponList);
                    cp.getHandle().c.a(head, null);
                    cp.getHandle().c.a(weapon, null);
                    PacketPlayOutEntityMetadata packetTeleport = new PacketPlayOutEntityMetadata(armorStand.ah(), armorStand.al().c());
                    cp.getHandle().c.a(packetTeleport, null);
                    PacketData_1_17_R1 pd = new PacketData_1_17_R1();
                    if (!CompanionPacket_1_20_R2.this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getChestplate().equals("BODYSKULL")) {
                        List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> chestList = new ArrayList<>();
                        chestList.add(Pair.of(EnumItemSlot.e, CraftItemStack.asNMSCopy(CompanionPacket_1_20_R2.this.setChest(player))));
                        PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(armorStand.ah(), chestList);
                        cp.getHandle().c.a(chest, null);
                        pd.setChest(chest);
                    }

                    pd.setCompanionPacket(armorStand);
                    pd.setCompanionMetaData(packetTeleport);
                    pd.setSkull(head);
                    pd.setWeapon(weapon);
                    CompanionPacket_1_20_R2.this.packetData.put(player.getUniqueId(), pd);
                });
            } else {
                PlayerData.instanceOf(player).toggleCompanion();
                player.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', main.getCompanionUtil().getPrefix() + main.getFileHandler().getPlayerInDisabledWorldMessage()));
            }
        }

    }

    public void despawnCompanion(final Player player, final Player packetPlayer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.main, () -> {
            if (CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()) != null && CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()).getCompanionPacket() != null) {
                PacketPlayOutEntityDestroy pa = new PacketPlayOutEntityDestroy(CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()).getCompanionPacket().ah());
                ((CraftPlayer) packetPlayer).getHandle().c.a(pa, null);
            }

        });
    }

    public void despawnCompanion(final Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(this.main, () -> {
            if (CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()) != null && CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()).getCompanionPacket() != null) {
                PacketPlayOutEntityDestroy pa = new PacketPlayOutEntityDestroy(CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()).getCompanionPacket().ah());
                ((CraftPlayer) player).getHandle().c.a(pa, null);

                for (Player packetPlayer : PlayerData.instanceOf(player).getPlayerPacketList().keySet()) {
                    ((CraftPlayer) packetPlayer).getHandle().c.a(pa, null);
                }

                CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()).setCompanionPacket(null);
                PlayerData.instanceOf(player).getPlayerPacketList().clear();
            }

        });
    }

    public void toggleCompanion(Player player) {
        if (this.packetData.get(player.getUniqueId()) != null && this.packetData.get(player.getUniqueId()).getCompanionPacket() != null) {
            this.main.getCompanionUtil().removeParticles(player);
            this.main.getCompanionUtil().stopCommandAbility(player);
            this.main.getPotionEffectAbility().remove(player);
            this.despawnCompanion(player);
        }

    }

    public void companionFollow(final Player player) {
        if (this.packetData.get(player.getUniqueId()) != null && this.packetData.get(player.getUniqueId()).getCompanionPacket() != null) {
            double x = Math.cos(Math.toRadians(player.getLocation().getYaw() - 180.0F) + this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getX());
            double z = Math.sin(Math.toRadians(player.getLocation().getYaw() - 180.0F) + this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getZ());
            Location loc = player.getLocation().add(x, this.main.getFileHandler().getCompanionDetails().get(PlayerData.instanceOf(player).getActiveCompanionName().toLowerCase()).getY(), z);
            final CraftPlayer cp = (CraftPlayer) player;
            this.packetData.get(player.getUniqueId()).getCompanionPacket().a(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0.0F);
            if (CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()).getCompanionPacket() != null) {
                PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()).getCompanionPacket());
                cp.getHandle().c.a(packet, null);
                CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()).setTeleportPacket(packet);
            }

            List<Entity> entityList = player.getNearbyEntities(Double.parseDouble(this.main.getFileHandler().getPacketRange()[0]), Double.parseDouble(this.main.getFileHandler().getPacketRange()[1]), Double.parseDouble(this.main.getFileHandler().getPacketRange()[2]));

            for (Entity ent : entityList) {
                if (ent.getType() == EntityType.PLAYER) {
                    final Player ePlayer = (Player) ent;
                    final CraftPlayer ecp = (CraftPlayer) ePlayer;
                    if (PlayerData.instanceOf(player).getPlayerPacketList().get(ePlayer) == null) {
                        PacketPlayOutSpawnEntity entity = new PacketPlayOutSpawnEntity(CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()).getCompanionPacket(), 78);
                        ecp.getHandle().c.a(entity, null);
                        ecp.getHandle().c.a(CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()).getCompanionMetaData(), null);
                        ecp.getHandle().c.a(CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()).getSkull(), null);
                        if (CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()).getChest() != null) {
                            ecp.getHandle().c.a(CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()).getChest(), null);
                        }

                        ecp.getHandle().c.a(CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()).getWeapon(), null);
                        PlayerData.instanceOf(player).getPlayerPacketList().put(ePlayer, true);
                    }

                    ecp.getHandle().c.a(CompanionPacket_1_20_R2.this.packetData.get(player.getUniqueId()).getTeleportPacket(), null);
                }
            }

            if (PlayerData.instanceOf(player).getPlayerPacketList().size() != entityList.size()) {

                for (Player packetPlayer : PlayerData.instanceOf(player).getPlayerPacketList().keySet()) {
                    if (!entityList.contains(packetPlayer)) {
                        this.despawnCompanion(player, packetPlayer);
                        PlayerData.instanceOf(player).setClear(true);
                        PlayerData.instanceOf(player).getPlayerPacketList().put(packetPlayer, false);
                    }
                }

                if (PlayerData.instanceOf(player).isClear()) {
                    PlayerData.instanceOf(player).getPlayerPacketList().clear();
                    PlayerData.instanceOf(player).setClear(false);
                }
            }
        }

    }
}
