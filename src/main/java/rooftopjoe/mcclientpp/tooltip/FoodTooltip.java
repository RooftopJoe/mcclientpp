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

import com.mojang.datafixers.util.Pair;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.autoconfig.AutoConfig;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import rooftopjoe.mcclientpp.ModConfig;
import rooftopjoe.mcclientpp.util.TextUtils;

public class FoodTooltip {
    public static void add(ItemStack stack, TooltipContext context, List<Text> lines) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        List<Text> entry = new ArrayList<>();

        if (stack.isFood()) {
            FoodComponent component = stack.getItem().getFoodComponent();
            int hunger = component.getHunger();
            float saturation = 2 * component.getSaturationModifier() * hunger;

            for (Pair<StatusEffectInstance, Float> instance : component.getStatusEffects()) {
                StatusEffectInstance key = instance.getFirst();
                StatusEffect effect = key.getEffectType();
                float value = instance.getSecond();
                String amplifiedEffect = Text.translatable("potion.withAmplifier", effect.getName(), Text.translatable("potion.potency." + key.getAmplifier())).getString().trim();

                if (value == 1) entry.add(Text.translatable("potion.withDuration", amplifiedEffect, StatusEffectUtil.durationToString(key, 1)).formatted(effect.getCategory().getFormatting()));
                else entry.add(Text.translatable("attribute.modifier.equals.1", TextUtils.tenthPlace().format(100 * value), Text.translatable("potion.withDuration", amplifiedEffect, StatusEffectUtil.durationToString(key, 1))).formatted(effect.getCategory().getFormatting()));
            }

            if (config.isCheat() || stack.getItem() != Items.SUSPICIOUS_STEW) {
                NbtCompound tag = stack.getNbt();

                if (tag != null) {
                    NbtList effects = tag.getList("Effects", NbtElement.COMPOUND_TYPE);

                    for (int i = 0; i < effects.size(); i++) {
                        StatusEffectInstance effectInstance;
                        StatusEffect effect;

                        tag = effects.getCompound(i);
                        effectInstance = new StatusEffectInstance(StatusEffect.byRawId(tag.getByte(SuspiciousStewItem.EFFECT_ID_KEY)), tag.getInt(SuspiciousStewItem.EFFECT_DURATION_KEY));
                        effect = effectInstance.getEffectType();

                        entry.add(Text.translatable("potion.withDuration", effect.getName(), StatusEffectUtil.durationToString(effectInstance, 1)).formatted(effect.getCategory().getFormatting()));
                    }
                }
            }

            Tooltips.addEntry(stack, context, lines, entry, Tooltips.Position.TOP);
            entry.clear();
            entry.add(Text.literal(""));
            entry.add(Text.translatable("tooltip.mcclientpp.foodstats").formatted(Formatting.GRAY));
            entry.add(Text.translatable("attribute.modifier.plus.0", hunger, Text.translatable("tooltip.mcclientpp.foodstats.hunger")).formatted(Formatting.DARK_GREEN));
            entry.add(Text.translatable("attribute.modifier.plus.0", TextUtils.tenthPlace().format(saturation), Text.translatable("tooltip.mcclientpp.foodstats.saturation")).formatted(Formatting.DARK_GREEN));
            Tooltips.addEntry(stack, context, lines, entry, Tooltips.Position.BOTTOM);
        }
    }
}
