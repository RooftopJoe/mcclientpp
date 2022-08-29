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

package rooftopjoe.mcclientpp.tooltip;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import rooftopjoe.mcclientpp.util.TextUtils;

public class ToolTooltip {
    public static void add(ItemStack stack, TooltipContext context, List<Text> lines) {
        List<Text> entry = new ArrayList<>();
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (stack.getItem() instanceof ToolItem) {
            float multiplier = ((ToolItem)stack.getItem()).getMaterial().getMiningSpeedMultiplier();
            int level = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, stack);
            double speed = multiplier + Math.pow(level, 2) + (level > 0 ? 1 : 0);

            if (StatusEffectUtil.hasHaste(player)) speed *= 0.2 * (StatusEffectUtil.getHasteAmplifier(player) + 1) + 1;
            if (player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) speed *= Math.pow(0.3, Math.min(player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier() + 1, 4));
            if (player.isSubmergedInWater() && !EnchantmentHelper.hasAquaAffinity(player)) speed /= 5;
            if (!player.isOnGround()) speed /= 5;

            entry.add(Text.translatable("tooltip.mcclientpp.miningspeed", TextUtils.tenthPlace().format(speed)).formatted(Formatting.DARK_GREEN));
            Tooltips.addEntry(stack, context, lines, entry, Tooltips.Position.BOTTOM);
        }
    }
}
