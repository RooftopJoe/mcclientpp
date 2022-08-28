/*
 * Copyright 2022 Bogdan Barbu
 *
 * This file is part of MC Client++.
 *
 * MC Client++ is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *aa
 * MC Client++ is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * MC Client++. If not, see <https://www.gnu.org/licenses/>.
 */

package rooftopjoe.mcclientpp.mixin;

import me.shedaniel.autoconfig.AutoConfig;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import rooftopjoe.mcclientpp.ModConfig;
import rooftopjoe.mcclientpp.util.TextUtils;

@Environment(EnvType.CLIENT) @Mixin(BrewingStandScreen.class) public abstract class BrewingStandScreenMixin extends HandledScreen<BrewingStandScreenHandler> {
    public BrewingStandScreenMixin(BrewingStandScreenHandler handler, PlayerInventory inventory, Text title) { super(handler, inventory, title); }

    @Inject(
        method = "Lnet/minecraft/client/gui/Drawable;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
        at = @At("RETURN")
    ) public void showFuel(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        BrewingStandScreenHandler handler = getScreenHandler();
        int fuel = 60 * handler.getSlot(4).getStack().getCount() + 3 * handler.getFuel();

        if (config.isShowFuel() && fuel > 0)
            TextUtils.drawScreenTextBar(matrices, I18n.translate("container.mcclientpp.fuel.brewingstand", fuel), this.x + this.backgroundWidth, this.y + 70, Formatting.WHITE.getColorValue());
    }
}
