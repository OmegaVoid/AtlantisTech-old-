package gcewing.sg.util;

import net.minecraft.creativetab.CreativeTabs;

/**
 * Creative tab helper
 * 
 * @author AfterLifeLochie
 * 
 */
public class HelperCreativeTab extends CreativeTabs {
	public HelperCreativeTab(int par1, String par2Str) {
		super(par1, par2Str);
	}

	@Override
	public String getTranslatedTabLabel() {
		return getTabLabel();
	}
}
