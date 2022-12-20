package com.PvMTickCounter;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Hitsplat;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
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
@PluginDescriptor(name="PvM Tick Counter")

public class TickCounterPlugin extends Plugin{
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private TickCounterConfig config;
    @Inject
    private Client client;

    private TickCounterUtil id;
    private Integer amount=0;

    private Integer MHCount=0;

    private boolean initTime = false;

    private Instant startTime;


    public TickCounterPlugin() {
    }

    @Provides
    TickCounterConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(TickCounterConfig.class);
    }
    @Inject
    private TickCounterOverlay overlay;

    Map<String, Integer> activity = new HashMap<>();
    boolean instanced = false;
    boolean prevInstance = false;

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
        id.clearBP();
    }
    @Subscribe
    public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
        Player player = client.getLocalPlayer();
        Actor actor = hitsplatApplied.getActor();
        if (!(actor instanceof NPC)) {
            return;
        }

        Hitsplat hitsplat = hitsplatApplied.getHitsplat();

        if (hitsplat.isMine()) {
            int hit = hitsplat.getAmount();

            amount += hit;
        }


        if (hitsplat.isMine() && hitsplat.getHitsplatType() == HitsplatID.DAMAGE_MAX_ME || hitsplat.getHitsplatType() == HitsplatID.DAMAGE_MAX_ME_CYAN || hitsplat.getHitsplatType() == HitsplatID.DAMAGE_MAX_ME_ORANGE || hitsplat.getHitsplatType() == HitsplatID.DAMAGE_MAX_ME_YELLOW || hitsplat.getHitsplatType() == HitsplatID.DAMAGE_MAX_ME_WHITE) {
            MHCount++;

        }
    }
    public Integer getDamage() {
        return amount;
    }
    public Integer getMH() {
        return MHCount;

    }
    public String getDamagePerTick() {
        return String.format("%.2f", amount / Float.parseFloat(String.valueOf(this.activity.getOrDefault(client.getLocalPlayer().getName(), 0))));
    }

    public String getDPS(){
        if(startTime == null)
            return "0";
        Instant now = Instant.now();
        long milli = Duration.between(startTime, now).toMillis();
        float sec = (float)milli / 1000;
        float dps = amount / sec;


        return String.format("%.2f", dps);
    }

    public String getElapsedTime(){
        if(startTime == null || !initTime)
            return null;
        long seconds = Duration.between(startTime, Instant.now()).toMillis() / 1000;
        long HH = seconds / 3600;
        long MM = (seconds % 3600) / 60;
        long SS = seconds % 60;
        return String.format("%02d:%02d:%02d", HH, MM, SS);
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged e){
        if (!(e.getActor() instanceof Player))
            return;
        Player p = (Player) e.getActor();
        int weapon = -1;
        if (p.getPlayerComposition() != null)
            weapon = p.getPlayerComposition().getEquipmentId(KitType.WEAPON);
        int delta = 0;

        delta = id.getTicks(p.getAnimation(),weapon, p);

        if (p == client.getLocalPlayer() && !initTime && config.showDPSCalc() && delta > 0) {
            initTime = true;
            startTime = Instant.now();
        }else if(initTime && !config.showDPSCalc()) {
            initTime = false;
        }

        if (delta > 0)
        {
            String name = p.getName();
            this.activity.put(name, this.activity.getOrDefault(name, 0) + delta);
        }

    }




    @Subscribe
    public void onGameTick(GameTick tick)
    {
        for(Map.Entry<Player, Boolean> entry : id.getBPing().entrySet()){
            if(entry.getValue()){
                String name = entry.getKey().getName();
                int activity = this.activity.getOrDefault(name, 0).intValue();
                this.activity.put(name, activity + 2);
                id.addToBP(entry.getKey(), Boolean.FALSE);
            }else{
                id.addToBP(entry.getKey(), Boolean.TRUE);
            }
        }

        if (!config.instance())return;
        prevInstance = instanced;
        instanced = client.isInInstancedRegion();
        if (!prevInstance && instanced)
        {
            activity.clear();
            id.clearBP();
            amount = 0;
            initTime = false;
            MHCount = 0;
        }
    }
    @Subscribe
    public void onOverlayMenuClicked(OverlayMenuClicked event) {
        if (event.getEntry().getMenuAction() == MenuAction.RUNELITE_OVERLAY &&
                event.getEntry().getTarget().equals("PvM Tick Counter") &&
                event.getEntry().getOption().equals("Reset")) {
            activity.clear();
            id.clearBP();
            amount = 0;
            MHCount = 0;
            initTime = false;
        }
    }

}