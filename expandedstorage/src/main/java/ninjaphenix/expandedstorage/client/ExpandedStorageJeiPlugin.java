package ninjaphenix.expandedstorage.client;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.util.ResourceLocation;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.api.client.gui.screen.ingame.ScrollableScreen;

import java.util.Collections;
import java.util.List;

@JeiPlugin
public class ExpandedStorageJeiPlugin implements IModPlugin
{
	@Override
	public ResourceLocation getPluginUid()
	{
		return ExpandedStorage.getRl("jei_plugin");
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public void registerGuiHandlers(IGuiHandlerRegistration registration)
	{
		registration.addGuiContainerHandler(ScrollableScreen.class, new IGuiContainerHandler<ScrollableScreen>()
		{
			@Override
			public List<Rectangle2d> getGuiExtraAreas(ScrollableScreen screen)
			{
				if (screen.hasScrollbar())
				{
					return Collections.singletonList(new Rectangle2d(screen.getLeft() + 172, screen.getTop(), 29, 132));
				}
				return Collections.emptyList();
			}
		});
	}
}
