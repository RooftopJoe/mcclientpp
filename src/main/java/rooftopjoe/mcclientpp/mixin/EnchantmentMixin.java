/*
 * Copyright 2022 Bogdan Barbu
 *
 * This file is part of MC Client++.
 *
 * MC Client++ is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * MC Client++ is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * MC Client++. If not, see <https://www.gnu.org/licenses/>.
 */

package rooftopjoe.mcclientpp.mixin;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import rooftopjoe.mcclientpp.ModConfig;

import me.shedaniel.autoconfig.AutoConfig;

@Environment(EnvType.CLIENT) @Mixin(Enchantment.class) public abstract class EnchantmentMixin {
    @Shadow public abstract int getMaxLevel();

    @Inject(
        method = "Lnet/minecraft/enchantment/Enchantment;getName(I)Lnet/minecraft/text/Text;",
        at = @At("TAIL"),
        locals = LocalCapture.CAPTURE_FAILSOFT
    ) private void appendMaxEnchantmentLevel(int level, CallbackInfoReturnable<Text> ci, MutableText name) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        String key = "enchantment.level." + getMaxLevel();

        if (config.isShowDetailedTooltips() && getMaxLevel() > 1)
            name.append("/").append(I18n.hasTranslation(key) ? Text.translatable(key) : Text.literal(String.valueOf(getMaxLevel())));
    }
}
