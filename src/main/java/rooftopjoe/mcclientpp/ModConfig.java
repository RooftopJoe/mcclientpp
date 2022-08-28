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

package rooftopjoe.mcclientpp;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = "mcclientpp") public class ModConfig extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("general") @ConfigEntry.Gui.TransitiveObject public GeneralConfig general = new GeneralConfig();

    @ConfigEntry.Category("hud") @ConfigEntry.Gui.TransitiveObject public HudConfig hud = new HudConfig();

    public boolean isShowFuel() { return hud.screen.showFuel; }

    public boolean isShowDetailedTooltips() { return hud.tooltip.showDetailedTooltips; }
}

@Config(name = "general") class GeneralConfig implements ConfigData {
}

@Config(name = "hud") class HudConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject ScreenConfig screen = new ScreenConfig();
    @ConfigEntry.Gui.CollapsibleObject TooltipConfig tooltip = new TooltipConfig();

    static class ScreenConfig {
        @ConfigEntry.Gui.Tooltip boolean showFuel = true;
    }

    static class TooltipConfig {
        @ConfigEntry.Gui.Tooltip boolean showDetailedTooltips = true;
    }
}
