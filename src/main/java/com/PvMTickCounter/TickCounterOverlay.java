package com.PvMTickCounter;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TickCounterOverlay extends OverlayPanel
{
    private TickCounterPlugin plugin;
    private TickCounterConfig config;
    private Client client;

    @Inject
    public TickCounterOverlay(TickCounterPlugin plugin,Client client,TickCounterConfig config)
    {
        super(plugin);
        setPosition(OverlayPosition.DYNAMIC);
        setPosition(OverlayPosition.DETACHED);
        setPosition(OverlayPosition.BOTTOM_RIGHT);
        this.plugin = plugin;
        this.client = client;
        this.config = config;
        getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY, "Reset", "PvM Tick Counter"));
    }

    @Override
    public Dimension render(Graphics2D g)
    {
        List<LayoutableRenderableEntity> elems = panelComponent.getChildren();
        elems.clear();

        if(config.showDamage() && plugin.getDamage() > 0) {
            elems.add(TitleComponent.builder().text("Damage Dealt").color(config.damageTitleColor()).build());
            elems.add(TitleComponent.builder().text(plugin.getDamage().toString()).color(config.damageTextColor()).build());
        }
       if(config.showMaxHits() && plugin.getMH() > 0) {
            elems.add(TitleComponent.builder().text("# Max Hits").color(config.MHTitleColor()).build());
            elems.add(TitleComponent.builder().text(plugin.getMH().toString()).color(config.MHTextColor()).build());
       }
        if(config.showDamagePerTick() && Float.parseFloat(plugin.getDamagePerTick()) > 0.00) {
            elems.add(TitleComponent.builder().text("Damage/Combat Tick").color(config.DPTTitleColor()).build());
            elems.add(TitleComponent.builder().text(plugin.getDamagePerTick()).color(config.DPTTextColor()).build());
        }
        if(config.showDPSCalc() && (Float.parseFloat(plugin.getDPS()) > 0.00)) {
            elems.add(TitleComponent.builder().text("Damage/Second").color(config.DPSTitleColor()).build());
            elems.add(TitleComponent.builder().text(plugin.getDPS()).color(config.DPSTextColor()).build());
        }
        if(config.showElapsedTime() && plugin.getElapsedTime() !=null){
            elems.add(TitleComponent.builder().text("Elapsed Time").color(config.ETTitleColor()).build());
            elems.add(TitleComponent.builder().text(plugin.getElapsedTime()).color(config.ETTextColor()).build());
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(plugin.activity.entrySet());
        list.sort(new Comparator<Map.Entry<String, Integer>>()
        {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
            {
                int value = -Integer.compare(o1.getValue(), o2.getValue());
                if (value == 0)
                    value = o1.getKey().compareTo(o2.getKey());
                return value;
            }
        });
        if (list.size() != 0) elems.add(TitleComponent.builder().text("Combat Ticks").color(config.titleColor()).build());
        int total = 0;
        for (Map.Entry<String, Integer> e : list)
        {
            total += e.getValue();
            if(e.getKey().equals(client.getLocalPlayer().getName()))
            {
                elems.add(LineComponent.builder().leftColor(config.selfColor()).rightColor(config.selfColor()).left(e.getKey()).right(e.getValue().toString()).build());
            }
            else
            {
                elems.add(LineComponent.builder().left(e.getKey()).right(e.getValue().toString()).leftColor(config.otherColor()).rightColor(config.otherColor()).build());

            }
        }
        if (config.totalEnabled())
        {
            if (list.size() != 0) elems.add(LineComponent.builder().left("Total").leftColor(config.totalColor()).rightColor(config.totalColor()).right(String.valueOf(total)).build());
        }
        return super.render(g);
    }
}