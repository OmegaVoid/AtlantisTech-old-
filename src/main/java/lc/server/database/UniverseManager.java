package lc.server.database;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Semaphore;

import lc.common.stargate.StargateCharsetHelper;
import lc.common.util.math.ChunkPos;
import lc.common.util.math.DimensionPos;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

public class UniverseManager {

	/** Default Stargate address size */
	private final static int ADDRESS_WIDTH = 9;
	
	/** Disk IO provider */
	private final RecordIO jsonAgent;

	/** Heap for all registered addresses */
	private final ArrayList<StargateRecord> recordHeap = new ArrayList<StargateRecord>();

	/** Dictionary of all records to integer dimensions */
	private final HashMap<Integer, ArrayList<WeakReference<StargateRecord>>> dimensionMap = new HashMap<Integer, ArrayList<WeakReference<StargateRecord>>>();

	/** Dictionary of all allocated character maps */
	private final ArrayList<Long> characterMap = new ArrayList<Long>();

	private Random worldRandom;

	/** Default constructor */
	public UniverseManager() {
		jsonAgent = new RecordIO();
	}

	/**
	 * Called by the system when a server is being started.
	 * 
	 * @param event
	 *            The server event.
	 */
	public void loadUniverse(FMLServerStartingEvent event) {
		
	}

	/**
	 * Called by the system when a server is being stopped.
	 * 
	 * @param event
	 *            The server event.
	 */
	public void unloadUniverse(FMLServerStoppingEvent event) {
	}

	/**
	 * Called by the system when a dimension is being loaded.
	 * 
	 * @param load
	 *            The load event.
	 */
	public void loadGalaxy(WorldEvent.Load load) {
	}

	/**
	 * Called by the system when a dimension is being unloaded.
	 * 
	 * @param unload
	 *            The unload event.
	 */
	public void unloadGalaxy(WorldEvent.Unload unload) {
	}

	/**
	 * Called by the system when the dimension is being auto-saved.
	 * 
	 * @param save
	 *            The save event.
	 */
	public void autosaveGalaxy(WorldEvent.Save save) {

	}

	/**
	 * Build the reference cache.
	 */
	public void buildIndex() {
		dimensionMap.clear();
		for (StargateRecord record : recordHeap) {
			int dimId = record.dimension;
			if (dimensionMap.get(dimId) == null)
				dimensionMap.put(dimId, new ArrayList<WeakReference<StargateRecord>>());
			dimensionMap.get(dimId).add(new WeakReference<StargateRecord>(record));
			characterMap.add(StargateCharsetHelper.singleton().addressToLong(record.address));
		}
	}

	/**
	 * Prune the reference cache.
	 */
	public void pruneIndex() {
		ArrayList<WeakReference<StargateRecord>> dead = new ArrayList<WeakReference<StargateRecord>>();
		Iterator<Integer> dimList = dimensionMap.keySet().iterator();
		while (dimList.hasNext()) {
			dead.clear();
			ArrayList<WeakReference<StargateRecord>> refs = dimensionMap.get(dimList.next());
			if (refs.size() != 0) {
				for (WeakReference<StargateRecord> record : refs)
					if (record.get() == null)
						dead.add(record);
				if (dead.size() != 0)
					for (WeakReference<StargateRecord> deadRef : dead)
						refs.remove(deadRef);
			}
		}
	}

	public boolean isAddressAllocated(char[] address) {
		return characterMap.contains(StargateCharsetHelper.singleton().addressToLong(address));
	}

	public char[] putAddress(char[] address, int dimension, ChunkPos chunk) {
		StargateRecord record = new StargateRecord();
		record.address = address;
		record.dimension = dimension;
		record.chunk = chunk;
		recordHeap.add(record);
		if (dimensionMap.get(dimension) == null)
			dimensionMap.put(dimension, new ArrayList<WeakReference<StargateRecord>>());
		dimensionMap.get(dimension).add(new WeakReference<StargateRecord>(record));
		characterMap.add(StargateCharsetHelper.singleton().addressToLong(address));
		return record.address;
	}

	public char[] findAddress(int dimension, ChunkPos chunk) {
		ArrayList<WeakReference<StargateRecord>> records = dimensionMap.get(dimension);
		if (records == null)
			return putAddress(getFreeAddress(ADDRESS_WIDTH), dimension, chunk);
		for (WeakReference<StargateRecord> record : records) {
			StargateRecord theRecord = record.get();
			if (theRecord == null)
				continue;
			if (theRecord.chunk.equals(chunk))
				return theRecord.address;
		}
		return putAddress(getFreeAddress(ADDRESS_WIDTH), dimension, chunk);
	}

	public char[] getFreeAddress(int width) {
		while (true) {
			char[] address = new char[width];
			for (int i = 0; i < width; i++)
				address[i] = StargateCharsetHelper.singleton().index(worldRandom.nextInt(36));
			if (!isAddressAllocated(address))
				return address;
		}
	}

}