package xyz.darke.DimensionBridgeBC.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class Portal {
    public static void createOrLinkPortal(Integer x, Integer y, Integer z, String worldname) {
        World world = Bukkit.getWorld(worldname);
        Location location = new Location(world, (double) x, (double) y, (double) z);

        Chunk center_chunk = world.getChunkAt(location);

        Location portal_location;
        portal_location = search_chunk(center_chunk);

        if (portal_location != null) {
            //TODO
            return;
        }

        // Portal not found in center chunk
        ArrayList<Chunk> search_chunks = new ArrayList<Chunk>();

        search_chunks.add(world.getChunkAt(center_chunk.getX(), center_chunk.getZ() - 1)); //NegZ
        search_chunks.add(world.getChunkAt(center_chunk.getX(), center_chunk.getZ() + 1)); //PosZ
        search_chunks.add(world.getChunkAt(center_chunk.getX() - 1, center_chunk.getZ())); //NegX
        search_chunks.add(world.getChunkAt(center_chunk.getX() + 1, center_chunk.getZ())); //PosX

        for (Chunk chunk : search_chunks) {
            portal_location = search_chunk(chunk);
            if (portal_location != null) {
                //TODO
                return;
            }
        }

        // No portal found
        //      Create new one


    }

    private static Location search_chunk(Chunk chunk) {
        ChunkSnapshot snapshot = chunk.getChunkSnapshot();
        World world = chunk.getWorld();

        Location last_found_portal;

        if (world.getEnvironment() == World.Environment.NORMAL) {
            // Overworld
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    Integer max_y = snapshot.getHighestBlockYAt(x, z);

                    for (int y = max_y; y > 0; y = y - 3) {
                        if (snapshot.getBlockType(x, y, z) == Material.NETHER_PORTAL) {
                            last_found_portal = new Location(world, x, y, z);

                            int i = 0;

                            while (snapshot.getBlockType(x, y - i, z) == Material.NETHER_PORTAL && y - i > 0) {
                                last_found_portal = new Location(world, x, y - i, z);
                                i++;
                            }

                            return last_found_portal;
                        }
                    }
                }
            }
        } else {
            // Nether (Assumed)
            //TODO


        }

        return null;
    }

    private static void form_portal_location_message(Location location, String player_uuid) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Forward"); // So BungeeCord knows to forward it
        out.writeUTF("ALL");
        out.writeUTF("WL_PortalLocation"); // The channel name to check if this your data

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            msgout.writeInt(location.getBlockX());
            msgout.writeInt(location.getBlockY());
            msgout.writeInt(location.getBlockZ());
            msgout.writeUTF(player_uuid);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());

        //TODO send message
    }

    private static void create_portal(Location location) {
        if (location.getWorld().getEnvironment() == World.Environment.NORMAL) {
            // Overworld
            //TODO

        } else {
            // Nether (Assumed)
            //TODO
        }

    }

    public static void requestCreateOrLinkPortal(Location location, String worldname) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Forward"); // So BungeeCord knows to forward it
        out.writeUTF("ALL");
        out.writeUTF("WL_CreateOrLinkPortal"); // The channel name to check if this your data

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            msgout.writeInt(location.getBlockX());
            msgout.writeInt(location.getBlockY());
            msgout.writeInt(location.getBlockZ());
            msgout.writeUTF(worldname);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());

    }


}
