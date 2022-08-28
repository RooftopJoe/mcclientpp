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

package rooftopjoe.mcclientpp.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextUtils {
    public enum Justification {
        LEFT,
        CENTER,
        RIGHT
    }

    static final MinecraftClient client = MinecraftClient.getInstance();

    public static void drawScaledText(MatrixStack matrices, String text, int x, int y, int maxWidth, Justification justification, int color) {
        int width = client.textRenderer.getWidth(text), offset = 0;
        float scaleFactor = width <= maxWidth ? 1 : (float)maxWidth / width;

        switch (justification) {
            case LEFT: break;
            case CENTER: if (width <= maxWidth) offset = (maxWidth - width) / 2; break;
            case RIGHT: if (width <= maxWidth) offset = maxWidth - width; break;
        }

        matrices.push();
        matrices.translate(x, y, 300);
        matrices.scale(scaleFactor, scaleFactor, 1);

        client.textRenderer.drawWithShadow(matrices, text, offset, 0, color);

        matrices.pop();
    }

    public static void drawScreenTextBar(MatrixStack matrices, String text, int x, int y, int color) {
        int width = client.textRenderer.getWidth(text);

        DrawableHelper.fill(matrices, x - width - 12, y, x - 8, y + 12, Constants.SCREEN_TEXTBAR_COLOR);
        client.textRenderer.drawWithShadow(matrices, text, x - width - 10, y + 2, color);
    }

    public static NumberFormat tenthPlace() {
        NumberFormat tenthPlace = DecimalFormat.getInstance();

        tenthPlace.setMinimumFractionDigits(0);
        tenthPlace.setMaximumFractionDigits(1);
        tenthPlace.setRoundingMode(RoundingMode.HALF_UP);

        return tenthPlace;
    }
}
