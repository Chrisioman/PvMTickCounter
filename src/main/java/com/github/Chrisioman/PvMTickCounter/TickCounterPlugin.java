package com.github.Chrisioman.PvMTickCounter;


import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.kit.KitType;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.OverlayMenuClicked;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@PluginDescriptor(name = "PvM Tick Counter")
public class TickCounterPlugin extends Plugin
{
    @Inject private OverlayManager overlayManager;
    @Inject private TickCounterConfig config;
    @Inject private Client client;
    @Inject private TickCounterOverlay overlay;

    private TickCounterUtil id;
    private int amount = 0;
    private int MHCount = 0;
    private boolean initTime = false;
    private Instant startTime;

    // Activity map
    Map<String, Integer> activity = new HashMap<>();

    // QuickAnimation map for continuous weapons
    Map<Player, QuickAnimation> quickAnims = new HashMap<>();

    boolean instanced = false;
    boolean prevInstance = false;

    @Provides
    TickCounterConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(TickCounterConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        id = new TickCounterUtil();
        id.init();
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
        activity.clear();
        quickAnims.clear();
        amount = 0;
        MHCount = 0;
        initTime = false;
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied hitsplatApplied)
    {
        Actor actor = hitsplatApplied.getActor();
        if (!(actor instanceof NPC))
            return;

        Hitsplat hitsplat = hitsplatApplied.getHitsplat();
        if (hitsplat.isMine())
        {
            int hit = hitsplat.getAmount();
            amount += hit;
            if (hitsplat.getHitsplatType() == HitsplatID.DAMAGE_MAX_ME ||
                    hitsplat.getHitsplatType() == HitsplatID.DAMAGE_MAX_ME_CYAN ||
                    hitsplat.getHitsplatType() == HitsplatID.DAMAGE_MAX_ME_ORANGE ||
                    hitsplat.getHitsplatType() == HitsplatID.DAMAGE_MAX_ME_YELLOW ||
                    hitsplat.getHitsplatType() == HitsplatID.DAMAGE_MAX_ME_WHITE)
            {
                MHCount++;
            }
        }
    }

    public Integer getDamage() { return amount; }
    public Integer getMH() { return MHCount; }

    public String getDamagePerTick()
    {
        return String.format("%.2f",
                amount / (float) activity.getOrDefault(client.getLocalPlayer().getName(), 1));
    }

    public String getDPS()
    {
        if (startTime == null) return "0";
        long milli = Duration.between(startTime, Instant.now()).toMillis();
        float sec = milli / 1000f;
        float dps = amount / sec;
        return String.format("%.2f", dps);
    }

    public String getElapsedTime()
    {
        if (startTime == null || !initTime) return null;
        long seconds = Duration.between(startTime, Instant.now()).toSeconds();
        long HH = seconds / 3600;
        long MM = (seconds % 3600) / 60;
        long SS = seconds % 60;
        return String.format("%02d:%02d:%02d", HH, MM, SS);
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged e)
    {
        if (!(e.getActor() instanceof Player))
            return;

        Player p = (Player) e.getActor();
        int weapon = -1;
        if (p.getPlayerComposition() != null)
            weapon = p.getPlayerComposition().getEquipmentId(KitType.WEAPON);

        int animation = p.getAnimation();
        int currentTick = client.getTickCount();
        QuickAnimation curQuick = quickAnims.get(p);

        // Determine delta from utility
        int delta = id.getTicks(animation, weapon, p);

        // Start DPS timer for local player
        if (p == client.getLocalPlayer() && !initTime && config.showDPSCalc() && delta > 0)
        {
            initTime = true;
            startTime = Instant.now();
        }
        else if (initTime && !config.showDPSCalc())
        {
            initTime = false;
        }

        // Determine if this is a QuickAnimation weapon
        QuickAnimation newQuick = null;

        // Blowpipe / Blazing Blowpipe / Sailing Blowpipe
        if ((animation == 5061 || animation == 10656 || animation == 13144 || animation == 13145) &&
                (weapon == 12926 || weapon == 28688 || weapon == 31583 || weapon == 31579 || weapon == 31575))
        {
            newQuick = new QuickAnimation(2, animation, weapon, currentTick);
        }
        // Eye of Ayak regular attack
        else if (animation == 12397 && weapon == 31113)
        {
            newQuick = new QuickAnimation(3, animation, weapon, currentTick);
        }
        // Eye of Ayak special attack
        else if (animation == 12394 && weapon == 31113)
        {
            newQuick = new QuickAnimation(5, animation, weapon, currentTick);
        }

        // LOGIC UPDATE: Separate handling to ensure ticks are added immediately for new sequences
        if (newQuick != null)
        {
            // Update map and add initial ticks immediately
            quickAnims.put(p, newQuick);
            String name = p.getName();
            activity.put(name, activity.getOrDefault(name, 0) + newQuick.delta);
        }
        else
        {
            // Standard weapon handling
            if (delta > 0)
            {
                String name = p.getName();
                activity.put(name, activity.getOrDefault(name, 0) + delta);
            }

            // Stop QuickAnimation if animation changes or resets (and we aren't starting a new one)
            if (curQuick != null && (animation != curQuick.animation))
            {
                quickAnims.remove(p);
            }
        }
    }

    @Subscribe
    public void onGameTick(GameTick tick)
    {
        int currentTick = client.getTickCount();

        // Tick QuickAnimations
        for (Map.Entry<Player, QuickAnimation> entry : quickAnims.entrySet())
        {
            QuickAnimation q = entry.getValue();
            // Only apply if enough ticks have passed
            if (currentTick >= q.eligibleAt())
            {
                String name = entry.getKey().getName();
                activity.put(name, activity.getOrDefault(name, 0) + q.delta);
                // FIX: Add delta to lastTick instead of setting to currentTick.
                // This keeps the rhythm perfect even though we check 1 tick late.
                q.lastTick += q.delta;
            }
        }

        prevInstance = instanced;
        instanced = client.isInInstancedRegion();
        if (!prevInstance && instanced && config.instance())
        {
            activity.clear();
            quickAnims.clear();
            amount = 0;
            MHCount = 0;
            initTime = false;
        }
    }

    @Subscribe
    public void onOverlayMenuClicked(OverlayMenuClicked event)
    {
        if (event.getEntry().getMenuAction() == MenuAction.RUNELITE_OVERLAY &&
                event.getEntry().getTarget().equals("PvM Tick Counter") &&
                event.getEntry().getOption().equals("Reset"))
        {
            activity.clear();
            quickAnims.clear();
            amount = 0;
            MHCount = 0;
            initTime = false;
        }
    }

    // QuickAnimation class inside plugin
    private static class QuickAnimation
    {
        private final int delta;
        private final int animation;
        private final int weapon;
        private int lastTick;

        QuickAnimation(int delta, int animation, int weapon, int lastTick)
        {
            this.delta = delta;
            this.animation = animation;
            this.weapon = weapon;
            this.lastTick = lastTick;
        }

        int eligibleAt()
        {
            // FIX: Add +1 tick buffer.
            // This ensures we don't count the next attack if you click off exactly as it was due.
            return lastTick + delta + 1;
        }
    }
}



