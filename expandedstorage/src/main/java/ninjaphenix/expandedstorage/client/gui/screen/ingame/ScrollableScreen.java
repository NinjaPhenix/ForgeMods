package ninjaphenix.expandedstorage.client.gui.screen.ingame;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.client.gui.widget.SearchTextFieldWidget;
import ninjaphenix.expandedstorage.common.inventory.ScrollableContainer;

import java.util.Optional;

public class ScrollableScreen extends ContainerScreen<ScrollableContainer>
{
	private static final ResourceLocation BASE_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
	private static final ResourceLocation WIDGETS_TEXTURE = ExpandedStorage.getRl("textures/gui/container/widgets.png");
	private final int displayedRows;
	private final int totalRows;
	private int topRow;
	private double progress;
	private boolean dragging;
	private Optional<SearchTextFieldWidget> searchBox;
	private String searchBoxOldText;

	public ScrollableScreen(final ScrollableContainer container, final PlayerInventory playerInventory, final ITextComponent containerTitle)
	{
		super(container, playerInventory, containerTitle);
		totalRows = container.getRows();
		topRow = 0;
		displayedRows = hasScrollbar() ? 6 : totalRows;
		// todo: change for JEI if (hasScrollbar() && !FabricLoader.getInstance().isModLoaded("roughlyenoughitems")) containerWidth += 22;
		ySize = 114 + displayedRows * 18;
		progress = 0;
		container.setSearchTerm("");
		searchBoxOldText = "";
	}

	@Override
	protected void init()
	{
		super.init();
		if (!hasScrollbar())
		{
			searchBox = Optional.empty();
		}
		else
		{
			searchBox = Optional.of(addButton(new SearchTextFieldWidget(font, guiLeft + 82, guiTop + 127, 80, 8, "")));
			final SearchTextFieldWidget box = searchBox.get();
			box.setMaxStringLength(50);
			box.setEnableBackgroundDrawing(false);
			box.setVisible(hasScrollbar());
			box.setTextColor(16777215);
			box.setResponder(str ->
			{
				if (str.equals(searchBoxOldText)) { return; }
				container.setSearchTerm(str);
				progress = 0;
				topRow = 0;
				searchBoxOldText = str;
			});
			this.setFocused(box);
			box.setFocused2(true);
		}
	}

	@Override
	public void tick() { searchBox.ifPresent(SearchTextFieldWidget::tick); }

	@Override
	public void render(final int mouseX, final int mouseY, final float lastFrameDuration)
	{
		renderBackground();
		drawGuiContainerBackgroundLayer(lastFrameDuration, mouseX, mouseY);
		super.render(mouseX, mouseY, lastFrameDuration);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY)
	{
		font.drawString(title.getFormattedText(), 8, 6, 4210752);
		font.drawString(playerInventory.getDisplayName().getFormattedText(), 8, ySize - 94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float lastFrameDuration, final int mouseX, final int mouseY)
	{
		RenderSystem.color4f(1, 1, 1, 1);
		minecraft.getTextureManager().bindTexture(BASE_TEXTURE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		blit(x, y, 0, 0, xSize, displayedRows * 18 + 17);
		blit(x, y + displayedRows * 18 + 17, 0, 126, xSize, 96);
		if (hasScrollbar())
		{
			minecraft.getTextureManager().bindTexture(WIDGETS_TEXTURE);
			blit(x + 172, y, 0, 0, 29, 132);
			blit(x + 181, (int) (y + 18 + 91 * progress), 29, 0, 12, 15);
			blit(x + 81, y + 126, 43, 0, 90, 11);
		}
		searchBox.ifPresent(searchTextFieldWidget -> searchTextFieldWidget.render(mouseX, mouseY, lastFrameDuration));
	}

	@Override
	public boolean mouseScrolled(final double mouseX, final double mouseY, final double scrollDelta)
	{
		if (hasScrollbar())
		{
			setTopRow(topRow - (int) scrollDelta);
			progress = ((double) topRow) / ((double) (totalRows - 6));
			return true;
		}
		return false;
	}

	@Override
	protected boolean hasClickedOutside(final double mouseX, final double mouseY, final int left, final int top, final int mouseButton)
	{
		boolean left_up_down = mouseX < left || mouseY < top || mouseY > top + height;
		boolean right = mouseX > left + width;
		if (hasScrollbar()) { right = (right && mouseY > top + 132) || mouseX > left + width + 18; }
		return left_up_down || right;
	}

	@Override
	public boolean mouseDragged(final double mouseX, final double mouseY, final int button, final double deltaX, final double deltaY)
	{
		if (!dragging) { return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY); }
		progress = MathHelper.clamp((mouseY - guiTop - 25.5) / 90, 0, 1);
		setTopRow((int) (progress * (totalRows - 6)));
		return true;
	}

	@Override
	public boolean mouseClicked(final double mouseX, final double mouseY, final int button)
	{
		if (searchBox.isPresent())
		{
			final SearchTextFieldWidget box = searchBox.get();
			if (box.isFocused() && !box.mouseInBounds(mouseX, mouseY) && button == 0)
			{
				box.changeFocus(true);
				this.setFocused(null);
			}
		}
		if (button == 0 && guiLeft + 179 < mouseX && mouseX < guiLeft + 191 && guiTop + 18 < mouseY && mouseY < guiTop + 123)
		{
			dragging = true;
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(final double mouseX, final double mouseY, final int button)
	{
		if (dragging && button == 0) { dragging = false; }
		return super.mouseReleased(mouseX, mouseY, button);
	}

	private void setTopRow(final int row)
	{
		topRow = MathHelper.clamp(row, 0, totalRows - 6);
		container.updateSlotPositions(topRow, false);
	}

	@Override
	public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers)
	{
		if (keyCode == 256)
		{
			minecraft.player.closeScreen();
			return true;
		}
		if (searchBox.isPresent())
		{
			final SearchTextFieldWidget box = searchBox.get();
			if (!box.isFocused())
			{
				if (minecraft.gameSettings.keyBindChat.matchesKey(keyCode, scanCode))
				{
					box.changeFocus(true);
					this.setFocused(box);
					box.ignoreNextChar();
					return true;
				}
				return super.keyPressed(keyCode, scanCode, modifiers);
			}
			return box.keyPressed(keyCode, scanCode, modifiers);
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(final char character, int int_1)
	{
		if (searchBox.isPresent())
		{
			final SearchTextFieldWidget box = searchBox.get();
			if (box.isFocused()) {return box.charTyped(character, int_1); }
		}
		return super.charTyped(character, int_1);
	}

	@Override
	public void resize(final Minecraft client, final int width, final int height)
	{
		if (searchBox.isPresent())
		{
			final SearchTextFieldWidget box = searchBox.get();
			String text = box.getText();
			boolean focused = box.isFocused();
			super.resize(client, width, height);
			box.setText(text);
			if (focused)
			{
				box.changeFocus(true);
				setFocused(box);
			}
		}
		else
		{
			super.resize(client, width, height);
		}
	}

	public int getTop() { return this.guiTop; }

	public int getLeft() { return this.guiLeft; }

	public boolean hasScrollbar() { return totalRows > 6; }
}

