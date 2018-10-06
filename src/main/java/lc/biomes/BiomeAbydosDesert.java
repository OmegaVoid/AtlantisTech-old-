package lc.biomes;

import java.util.Random;

import lc.api.defs.IBiomeDefinition;
import lc.api.defs.IDefinitionReference;
import lc.common.impl.registry.DefinitionReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
//import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenDesertWells;

/**
 * Abydos desert biome implementation
 *
 * @author AfterLifeLochie
 *
 */
public class BiomeAbydosDesert extends Biome implements IBiomeDefinition {
	/**
	 * Default constructor
	 *
	 * @param biomeId
	 *            The biome ID to use
	 */
	public BiomeAbydosDesert(int biomeId) {
		super(new BiomeProperties("BiomeAbydosDesert"));
		topBlock = (IBlockState) Blocks.SAND;
		//biomeName = getName();
		fillerBlock = (IBlockState) Blocks.SAND;
		decorator.generateFalls = false;
		decorator.treesPerChunk = -999;
		decorator.deadBushPerChunk = -999;
		decorator.reedsPerChunk = -999;
		decorator.cactiPerChunk = -999;
		spawnableCreatureList.clear();
	}

	@Override
	public String getName() {
		return "abydos_desert";
	}

	@Override
	public IDefinitionReference ref() {
		return new DefinitionReference(this);
	}

	@Override
	public void decorate(World par1World, Random par2Random, BlockPos par3) {
		super.decorate(par1World, par2Random, par3);
		if (par2Random.nextInt(1000) == 0) {
			int k = par3.getX() + par2Random.nextInt(16) + 8;
			int l = par3.getZ() + par2Random.nextInt(16) + 8;
			WorldGenDesertWells worldgendesertwells = new WorldGenDesertWells();
			worldgendesertwells.generate(par1World, par2Random, new BlockPos(k, par1World.getHeight(k, l) + 1, l));
		}
	}

}
