package com.github.dcysteine.nesql.exporter.plugin.gregtech.machineprops;

import com.github.dcysteine.nesql.sql.gregtech.machineprops.GregTechMultiblockBonus;
import gregtech.api.util.tooltip.TooltipTier;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matches tooltip lines against the resolved {@code GT5U.MBTT.*} templates that
 * {@code MultiblockTooltipBuilder} renders bonuses with. Freeform bonus prose does not match
 * and is left for the curated overlay.
 */
public class MultiblockBonusParser {
    private static final class Template {
        private final String kind;
        private final Pattern pattern;
        private final boolean tiered;

        private Template(String kind, Pattern pattern, boolean tiered) {
            this.kind = kind;
            this.pattern = pattern;
            this.tiered = tiered;
        }
    }

    private static final Pattern FORMAT_SPECIFIER = Pattern.compile("%(?:\\d+\\$)?[sd]");
    private static final Pattern COLOR_CODE = Pattern.compile("§.");

    private final List<Template> templates = new ArrayList<>();
    private final Map<String, String> axisByValue = new HashMap<>();

    public MultiblockBonusParser() {
        addTemplate("PARALLEL", "GT5U.MBTT.Parallel.Base", false);
        addTemplate("PARALLEL_PER_TIER", "GT5U.MBTT.Parallel.Additional", true);
        addTemplate("PARALLEL_PER_TIER", "GT5U.MBTT.Parallel.Singular", true);
        addTemplate("SPEED", "GT5U.MBTT.Speed.Base", false);
        addTemplate("SPEED_BONUS_PER_TIER", "GT5U.MBTT.Speed.Additional", true);
        addTemplate("SPEED_PER_TIER", "GT5U.MBTT.Speed.Absolute", true);
        addTemplate("EU_DISCOUNT", "GT5U.MBTT.EuDiscount.Base", false);
        addTemplate("EU_DISCOUNT_PER_TIER", "GT5U.MBTT.EuDiscount.Additional", true);
        addTemplate("STEAM_DISCOUNT", "GT5U.MBTT.SteamDiscount.Base", false);

        for (TooltipTier tier : TooltipTier.values()) {
            axisByValue.put(stripCodes(tier.getValue()).trim(), tier.name());
        }
    }

    private void addTemplate(String kind, String langKey, boolean tiered) {
        String template = StatCollector.translateToLocal(langKey);
        if (template == null || template.contains("GT5U.MBTT")) {
            return;
        }

        String[] literals = FORMAT_SPECIFIER.split(template, -1);
        StringBuilder regex = new StringBuilder();
        for (int i = 0; i < literals.length; i++) {
            if (i > 0) {
                regex.append("(.+?)");
            }
            regex.append(Pattern.quote(literals[i]));
        }
        templates.add(new Template(kind, Pattern.compile(regex.toString()), tiered));
    }

    public Optional<GregTechMultiblockBonus> parse(String line) {
        for (Template template : templates) {
            Matcher matcher = template.pattern.matcher(line.trim());
            if (!matcher.matches()) {
                continue;
            }

            String valueText = null;
            String axis = null;
            for (int group = 1; group <= matcher.groupCount(); group++) {
                String text = stripCodes(matcher.group(group)).trim();
                String matchedAxis = axisByValue.get(text);
                if (template.tiered && matchedAxis != null) {
                    axis = matchedAxis;
                } else if (valueText == null) {
                    valueText = text;
                } else {
                    // A tiered template whose axis text matches no known TooltipTier.
                    axis = text;
                }
            }
            if (valueText == null) {
                continue;
            }

            boolean multiplicative = valueText.endsWith("x");
            String number = valueText
                    .replace("x", "")
                    .replace("%", "")
                    .replace("+", "")
                    .replace(",", "")
                    .trim();
            try {
                double value = Double.parseDouble(number);
                return Optional.of(
                        new GregTechMultiblockBonus(
                                template.kind, value, multiplicative, axis,
                                stripCodes(line).trim()));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private static String stripCodes(String text) {
        return COLOR_CODE.matcher(text).replaceAll("");
    }
}
