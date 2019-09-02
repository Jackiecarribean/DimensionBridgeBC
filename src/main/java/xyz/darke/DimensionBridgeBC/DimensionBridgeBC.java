package xyz.darke.DimensionBridgeBC;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import xyz.darke.DimensionBridgeBC.utils.Portal;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public final class DimensionBridgeBC extends JavaPlugin implements PluginMessageListener {

    public static String world_overworld;
    public static String world_nether;

    @Override
    public void onEnable() {
        // Plugin startup logic
        List<World> worlds = Bukkit.getWorlds();
        for (World world : worlds) {
            if (world.getEnvironment() == World.Environment.NORMAL) {
                world_overworld = world.getName();
            } else if (world.getEnvironment() == World.Environment.NETHER) {
                world_nether = world.getName();
            }
        }


    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("WL_CreateOrLinkPortal")) {
            short len = in.readShort();
            byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);

            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            try {
                Integer x = msgin.readInt();
                Integer y = msgin.readInt();
                Integer z = msgin.readInt();
                String worldname = msgin.readUTF();

                Portal.createOrLinkPortal(x, y, z, worldname);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        // TODO Handle WL_PortalLocation msg
    }

    public void sendServerMsg(String channel, ByteArrayDataOutput msg) {
        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        player.sendPluginMessage(this, "BungeeCord", msg.toByteArray());
    }
}
