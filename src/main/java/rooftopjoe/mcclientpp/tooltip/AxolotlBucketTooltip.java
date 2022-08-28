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
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class AxolotlBucketTooltip {
    public static void add(ItemStack stack, TooltipContext context, List<Text> lines) {
        List<Text> entry = new ArrayList<>();

        if (stack.getItem() != Items.AXOLOTL_BUCKET || stack.getNbt() == null || !stack.getNbt().contains(AxolotlEntity.VARIANT_KEY, NbtElement.INT_TYPE))
            return;

        entry.add(Text.literal(AxolotlEntity.Variant.VARIANTS[stack.getNbt().getInt(AxolotlEntity.VARIANT_KEY)].getName()).formatted(Formatting.ITALIC, Formatting.GRAY));
        Tooltips.addEntry(stack, context, lines, entry, Tooltips.Position.TOP);
    }
}
