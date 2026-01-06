package com.github.Chrisioman.PvMTickCounter;



import net.runelite.api.Player;
import java.util.HashMap;

public class TickCounterUtil
{
    HashMap<Integer, Integer> aniTM;

    public void init()
    {
        aniTM = new HashMap<>();

        aniTM.put(7855, -1); // Mixed ticks - surge (5) / harm orb (4)
        aniTM.put(426, -1); // bow shoot
        aniTM.put(390, -1); // Generic Slash (4) + swift blade (3) + Osmumten's Fang (5)
        aniTM.put(8288, -1); // dhl Stab (4) + swift blade (3)
        aniTM.put(393, -1); // Staff bash (5) + Claw Scratch (4)
        aniTM.put(400, -1); // Pickaxe smash (5) + Inquisitor's Mace Stab (4)
        aniTM.put(401, -1); // dwh bop (6) + Ham Joint (3) + (axe + pickaxe) (5)
        aniTM.put(428, -1); // Chally swipe + Zamorakian Hasta Swipe
        aniTM.put(440, -1); // Chally jab
        aniTM.put(7045, -1); // Sara Sword + Godsword
        aniTM.put(7054, -1); // Sara Sword + Godsword
        aniTM.put(1378, -1); // Voidwaker special and ??
        aniTM.put(1711, -1); // Zammy Spear + Blue Moon
        aniTM.put(8145, -1); // Rapier + Belle's Folly

        aniTM.put(7617, 2); // rune knife
        aniTM.put(8194, 2); // dragon knife
        aniTM.put(5061, -1); // blowpipe (QuickAnimation handled)
        aniTM.put(7554, 2); // Dart throw

        aniTM.put(2323, 3); // Event RPG
        aniTM.put(7618, 3); // Chinchompa
        aniTM.put(2075, 3); // Karil's CB
        aniTM.put(9964, 3); // Webweaver Spec
        aniTM.put(12397, -1); // Eye of Ayak regular attack (QuickAnimation handled)
        aniTM.put(12394, -1); // Eye of Ayak special attack (QuickAnimation handled)
        aniTM.put(11057, 3); // Eclipse Atlatl

        aniTM.put(376, 4); // dds stab
        aniTM.put(245, 4); // Ursine Mace
        aniTM.put(9963, 4); // Ursine Spec
        aniTM.put(377, 4); // dds slash
        aniTM.put(422, 4); // punch
        aniTM.put(423, 4); // kick
        aniTM.put(381, 4); // Zamorakian Hasta
        aniTM.put(386, 4); // lunge
        aniTM.put(419, 4); // Keris Smash/Zamorakian Hasta Pound
        aniTM.put(1062, 4); // dds spec
        aniTM.put(1067, 4); // claw stab
        aniTM.put(1167, 4); // trident cast
        aniTM.put(1658, 4); // whip
        aniTM.put(2890, 4); // Arclight Special
        aniTM.put(3294, 4); // Abby Dagger Slash
        aniTM.put(3297, 4); // Abby Dagger Poke
        aniTM.put(3298, 4); // Bludgeon Attack
        aniTM.put(3299, 4); // Bludgeon Spec
        aniTM.put(3300, 4); // Abby Dagger Spec
        aniTM.put(7514, 4); // Claw Spec
        aniTM.put(7515, 4); // Dragon Sword Spec
        aniTM.put(8289, 4); // Dhl Slash
        aniTM.put(8290, 4); // Dhl Crush
        aniTM.put(4503, 4); // Inquisitor's Mace Crush
        aniTM.put(10501, 4); // Warped Sceptre
        aniTM.put(10989, 4); // Dual Macuahuitl
        aniTM.put(11275, 4); // Voidwaker Spec
        aniTM.put(1132, 4); // Sara Sword Spec
        aniTM.put(11133, 4); // Scorching Bow Spec

        aniTM.put(395, 5); // Axe
        aniTM.put(708, 5); // Iban's Blast
        aniTM.put(2062, 5); // Verac's Flail
        aniTM.put(2068, 5); // Torag's Hammers
        aniTM.put(2080, 5); // Guthans Warspear
        aniTM.put(2081, 5);
        aniTM.put(2082, 5);
        aniTM.put(1162, 5); // strike/bolt spells
        aniTM.put(10091, 5); // Bursting/Blitz spells
        aniTM.put(10092, 5); // Barrage Spell
        aniTM.put(6118, 5); // Osmumten's Fang Special
        aniTM.put(9471, 5); // Osmumten's Fang Stab
        aniTM.put(7552, 5); // Generic Crossbow
        aniTM.put(9493, 5); // Tumeken's Shadow
        aniTM.put(8056, 5); // Scythe Swing
        aniTM.put(8010, 5); // Blisterwood Flail
        aniTM.put(7004, 5); // Leaf-Bladed B-Axe chop
        aniTM.put(3852, 5); // Leaf-Bladed B-Axe Smash
        aniTM.put(9168, 5); // Zaryte Crossbow
        aniTM.put(10171, 5); // Soulreaper Axe Smash
        aniTM.put(10172, 5); // Soulreaper Axe Chop/Hack/Block
        aniTM.put(10173, 5); // Soulreaper Axe Spec
        aniTM.put(1710, 5); // Blue Moon Spear
        aniTM.put(1712, 5); // Blue Moon Spear


        aniTM.put(2078, 6); // Ahrim's Staff
        aniTM.put(5865, 6); // Barrelchest Anchor
        aniTM.put(5870, 6); // Barrelchest Special
        aniTM.put(7055, 6); // godsword autos
        aniTM.put(7511, 6); // dinh's attack
        aniTM.put(7516, 6); // maul attack
        aniTM.put(7555, 6); // ballista attack
        aniTM.put(7638, 6); // zgs spec
        aniTM.put(7640, 6); // sgs spec
        aniTM.put(7642, 6); // bgs spec
        aniTM.put(7643, 6); // bgs spec
        aniTM.put(7644, 6); // ags spec
        aniTM.put(10923, 6); // T. Ralos
        aniTM.put(10914, 6); // T. Ralos Spec
        aniTM.put(11430, 6); // Twinflame Staff

        aniTM.put(1203, 7); // Chally spec
        aniTM.put(2066, 7); // DH axe
        aniTM.put(2067, 7); // DH Axe Smash

        aniTM.put(9544, 12); // Keris of Curruption Spec
    }

    public Integer getTicks(Integer animationID, Integer weaponID, Player p)
    {
        // Blowpipe handled by QuickAnimation
        if(animationID == 5061 || animationID == 10656)
            return 0;

        // Eye of Ayak handled by QuickAnimation
        if(animationID == 12397 || animationID == 12394)
            return 0;

        // Sailing Blowpipe handled by QuickAnimation
        if(animationID == 13144 || animationID == 13145)
            return 0;

        // Default ticks from map
        Integer ticks = aniTM.getOrDefault(animationID, 0);
        if(ticks > 0){
            return ticks;
        }else{
            if(animationID == 7855){
                if(weaponID == 24423)
                    return 4;
                else
                    return 5;
            }
            if(animationID == 426){
                if(weaponID == 25886 || weaponID == 25867 || weaponID == 25869 || weaponID == 25884 || weaponID == 25888 ||
                        weaponID == 25890 || weaponID == 25892 || weaponID == 25894 || weaponID == 25896 || weaponID == 25865 ||
                        weaponID == 23855 || weaponID == 23856 || weaponID == 23857 || weaponID == 23901 || weaponID == 23902 || weaponID == 23903 || weaponID == 29591)
                    return 4;
                else if(weaponID == 20997)
                    return 5;
                else
                    return 3;
            }
            if(animationID == 390){
                if(weaponID == 26219 || weaponID == 27246 || weaponID == 31248)
                    return 5;
                else if(weaponID == 24219)
                    return 3;
                else
                    return 4;
            }
            if(animationID == 8145){
                if(weaponID == 31248)
                    return 5;
                else
                    return 4;
            }
            if(animationID == 8288){
                if(weaponID == 24219)
                    return 3;
                else
                    return 4;
            }
            if(animationID == 393){
                if(weaponID == 13652)
                    return 4;
                else
                    return 5;
            }
            if(animationID == 1711){
                if(weaponID == 28988)
                    return 5;
                else
                    return 4;
            }
            if(animationID == 400){
                if(weaponID == 24417)
                    return 4;
                else
                    return 5;
            }
            if(animationID == 401){
                if(weaponID == 13576) //dwh
                    return 6;
                else if(weaponID == 23360) // ham joint
                    return 3;
                else
                    return 5;
            }
            if(animationID == 440){
                if(weaponID == 23895 || weaponID == 23896 || weaponID == 23897 || weaponID == 23849 || weaponID == 23850 || weaponID == 23851 || weaponID == 11889) //CG Chally
                    return 4;
                else if(weaponID == 29796) // Noxious Halberd
                    return 5;
                else
                    return 7;
            }
            if(animationID == 428){
                if(weaponID == 23895 || weaponID == 23896 || weaponID == 23897 || weaponID == 23849 || weaponID == 23850 || weaponID == 23851) //CG Chally
                    return 4;
                else if(weaponID == 29796) // Noxious Halberd
                    return 5;
                else
                    return 7;
            }
            if(animationID == 1378){
                if(weaponID == 27690) //voidwaker special
                    return 4;
                else
                    return 6;
            }
            if(animationID == 7045){
                if(weaponID == 11838) // Sara Sword
                    return 4;
                else
                    return 6;
            }

            if(animationID == 7054){
                if(weaponID == 11838) // Sara Sword
                    return 4;
                else
                    return 6;
            }
        }

        return 0;
    }
}