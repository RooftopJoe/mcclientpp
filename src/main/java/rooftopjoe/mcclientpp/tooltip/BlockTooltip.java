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

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import rooftopjoe.mcclientpp.util.TextUtils;

public class BlockTooltip {
    public static void add(ItemStack stack, TooltipContext context, List<Text> lines) {
        List<Text> entry = new ArrayList<>();

        if (stack.getItem() instanceof BlockItem) {
            float blastResistance = ((BlockItem)stack.getItem()).getBlock().getBlastResistance();
            float hardness = ((BlockItem)stack.getItem()).getBlock().getHardness();
            int luminance = ((BlockItem)stack.getItem()).getBlock().getDefaultState().getLuminance();

            if (blastResistance < 3600000) entry.add(Text.translatable("tooltip.mcclientpp.blastresistance", TextUtils.tenthPlace().format(blastResistance)).formatted(Formatting.GRAY));
            if (hardness != -1) entry.add(Text.translatable("tooltip.mcclientpp.hardness", TextUtils.tenthPlace().format(hardness)).formatted(Formatting.GRAY));
            if (luminance > 0) entry.add(Text.translatable("tooltip.mcclientpp.luminance", luminance).formatted(Formatting.GRAY));

            Tooltips.addEntry(stack, context, lines, entry, Tooltips.Position.TOP);
        }
    }
}
