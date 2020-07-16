package ninjaphenix.expandedstorage.client;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.util.ResourceLocation;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.client.screen.ScrollableScreen;

import java.util.List;

@JeiPlugin
public class ExpandedStorageJeiPlugin implements IModPlugin
{
    @Override
    public ResourceLocation getPluginUid() { return ExpandedStorage.getRl("jei_plugin"); }

    @Override
    public void registerGuiHandlers(final IGuiHandlerRegistration registration)
    {
        registration.addGuiContainerHandler(ScrollableScreen.class, new IGuiContainerHandler<ScrollableScreen>()
        {
            @Override
            public List<Rectangle2d> getGuiExtraAreas(final ScrollableScreen screen) { return screen.getJeiRectangle(); }
        });
    }
}