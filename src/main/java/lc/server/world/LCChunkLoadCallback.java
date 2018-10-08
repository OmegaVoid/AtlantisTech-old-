package lc.server.world;

import java.util.List;

import com.google.common.collect.ImmutableSet;

//import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class LCChunkLoadCallback implements LoadingCallback {

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		for (Ticket t : tickets) {
			ImmutableSet<ChunkPos> chunks = t.getChunkList();
			for (ChunkPos chunk : chunks)
				ForgeChunkManager.unforceChunk(t, chunk);
			ForgeChunkManager.releaseTicket(t);
		}
	}

}
