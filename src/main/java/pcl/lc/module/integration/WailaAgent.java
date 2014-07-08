package pcl.lc.module.integration;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;

import mcp.mobius.waila.api.IWailaRegistrar;
import cpw.mods.fml.common.event.FMLInterModComms;
import pcl.common.helpers.RegistrationHelper;
import pcl.common.helpers.RegistrationHelper.BlockItemMapping;
import pcl.lc.LanteaCraft;
import pcl.lc.api.internal.Agent;
import pcl.lc.api.internal.IIntegrationAgent;

@Agent(modname = "Waila")
public class WailaAgent implements IIntegrationAgent {

	@Override
	public String modName() {
		return "Waila";
	}

	@Override
	public void init() {
		LanteaCraft.getLogger().log(Level.INFO, "Requesting Waila integration...");
		FMLInterModComms.sendMessage("Waila", "register", "pcl.lc.module.integration.WailaAgent.callbackRegister");
	}
	
	@Override
	public void postInit() {
		
	}
	
	public static void callbackRegister(IWailaRegistrar registrar) {
		Map<Integer, BlockItemMapping> ablock = RegistrationHelper.getBlockMappings();
		for (Entry<Integer, BlockItemMapping> amapping : ablock.entrySet()) {
			WailaHook hook = new WailaHook(amapping.getValue().blockClass);
			registrar.registerHeadProvider(hook, amapping.getValue().blockClass);
			registrar.registerBodyProvider(hook, amapping.getValue().blockClass);
		}
		LanteaCraft.getLogger().log(Level.INFO, "Waila integration loaded!");
	}

}
