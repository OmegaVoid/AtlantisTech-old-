/**
 * This file is part of the official LanteaCraft API. Please see the usage guide and
 * restrictions on use in the package-info file.
 */
package lc.api.world;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/**
 * Ore type list
 *
 * @author AfterLifeLochie
 *
 */
public enum OreType {
	/** Naquadah */
	NAQUADAH("naquadahOre"),
	/** Naquadriah */
	NAQUADRIAH("naqadriahOre"),
	/** Trinium */
	TRINIUM("triniumOre");

	private String tex;

	private TextureAtlasSprite textureOreBlock;
	private TextureAtlasSprite textureItem;
	private TextureAtlasSprite textureAlloyItem;
	private TextureAtlasSprite textureAlloyBlock;

	private OreType(String tex) {
		this.tex = tex;
	}

	/**
	 * Get an ore type from an ordinal
	 *
	 * @param ord
	 *            The ordinal number
	 * @return The ore type
	 */
	public static OreType fromOrdinal(int ord) {
		if (0 > ord || ord > OreType.values().length)
			return null;
		return OreType.values()[ord];
	}

	/**
	 * Get an ore type from a string
	 *
	 * @param string
	 *            The ore name
	 * @return The ore type
	 */
	public static OreType fromString(String string) {
		for (OreType ore : OreType.values())
			if (ore.name().equalsIgnoreCase(string))
				return ore;
		return null;
	}

	/**
	 * Get the texture name
	 *
	 * @return The texture name
	 */
	public String tex() {
		return tex;
	}

	/**
	 * Get the ore texture
	 *
	 * @return The ore texture
	 */
	public TextureAtlasSprite getOreTexture() {
		return textureOreBlock;
	}

	/**
	 * Set the ore texture
	 *
	 * @param texture
	 *            The ore texture
	 */
	public void setOreTexture(TextureAtlasSprite texture) {
		textureOreBlock = texture;
	}

	/**
	 * Get the item texture
	 *
	 * @return The item texture
	 */
	public TextureAtlasSprite getItemTexture() {
		return textureItem;
	}

	/**
	 * Set the item texture
	 *
	 * @param texture
	 *            The item texture
	 */
	public void setItemTexture(TextureAtlasSprite texture) {
		textureItem = texture;
	}

	/**
	 * Get the ingot texture
	 *
	 * @return The ingot texture
	 */
	public TextureAtlasSprite getIngotItemTexture() {
		return textureAlloyItem;
	}

	/**
	 * Set the ingot texture
	 *
	 * @param texture
	 *            The ingot texture
	 */
	public void setIngotItemTexture(TextureAtlasSprite texture) {
		textureAlloyItem = texture;
	}

	/**
	 * Get the itemblock texture
	 *
	 * @return The itemblock texture
	 */
	public TextureAtlasSprite getItemAsBlockTexture() {
		return textureAlloyBlock;
	}

	/**
	 * Set the itemblock texture
	 *
	 * @param texture
	 *            The itemblock texture
	 */
	public void setItemAsBlockTexture(TextureAtlasSprite texture) {
		textureAlloyBlock = texture;
	}
}
