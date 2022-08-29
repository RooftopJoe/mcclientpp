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

import java.util.List;

import me.shedaniel.autoconfig.AutoConfig;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import rooftopjoe.mcclientpp.ModConfig;

public class Tooltips {
    protected enum Position {
        TOP,
        BOTTOM
    }

    public static void addTooltips() {
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

            if (!config.isShowDetailedTooltips() || stack.isEmpty())
                return;

            AxolotlBucketTooltip.add(stack, context, lines);
            BlockTooltip.add(stack, context, lines);
            CompostTooltip.add(stack, context, lines);
        });
    }

    protected static void addEntry(ItemStack stack, TooltipContext context, List<Text> tooltip, List<Text> lines, Position position) {
        int index = 0;

        switch (position) {
            case TOP: index = Math.min(1, tooltip.size()); break;
            case BOTTOM: index = Math.max(1, tooltip.size() - (context.isAdvanced() ? (stack.isDamaged() ? 1 : 0) + (stack.hasNbt() ? 1 : 0) + 1 : 0)); break;
        }

        tooltip.addAll(index, lines);
    }
}
