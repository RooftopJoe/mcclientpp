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

import com.google.common.collect.Ordering;

import java.util.stream.Stream;
import java.util.Collection;

import me.shedaniel.autoconfig.AutoConfig;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.util.Formatting;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import rooftopjoe.mcclientpp.ModConfig;
import rooftopjoe.mcclientpp.util.TextUtils;

@Environment(EnvType.CLIENT) @Mixin(InGameHud.class) public abstract class InGameHudMixin extends DrawableHelper {
    @Shadow @Final private MinecraftClient client;

    @Inject(
        method = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay(Lnet/minecraft/client/util/math/MatrixStack;)V",
        at = @At("TAIL")
    ) private void showStatusEffectInfo(MatrixStack matrices, CallbackInfo ci) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        Collection<StatusEffectInstance> effects = client.player.getStatusEffects();

        if (!config.isShowStatusEffectInfo() || effects.isEmpty())
            return;

        for (StatusEffectInstance instance : Ordering.natural().reverse().sortedCopy(effects)) {
            int x = client.getWindow().getScaledWidth(), y = client.isDemo() ? 16 : 1;
            int beneficial = 0, unbeneficial = 0;
            int amplifier = instance.getAmplifier();
            String amplifierText = I18n.hasTranslation("potion.potency." + amplifier) ? I18n.translate("potion.potency." + amplifier) : String.valueOf(amplifier);
            String durationText = StatusEffectUtil.durationToString(instance, 1);

            if (!instance.shouldShowIcon())
                continue;

            if (instance.getEffectType().isBeneficial())
                x -= 25 * ++beneficial;
            else {
                x -= 25 * ++unbeneficial;
                y += 26;
            }

            TextUtils.drawScaledText(matrices, amplifierText, x, y + 3, 25, TextUtils.Justification.CENTER, Formatting.WHITE.getColorValue());
            TextUtils.drawScaledText(matrices, durationText, x, y + 14, 25, TextUtils.Justification.CENTER, Formatting.GRAY.getColorValue());
        }
    }
}
