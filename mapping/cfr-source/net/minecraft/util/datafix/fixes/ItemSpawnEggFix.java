/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.util.datafix.ExtraDataFixUtils;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import org.jspecify.annotations.Nullable;

public class ItemSpawnEggFix
extends DataFix {
    private static final @Nullable String[] ID_TO_ENTITY = DataFixUtils.make(new String[256], stringArray -> {
        stringArray[1] = "Item";
        stringArray[2] = "XPOrb";
        stringArray[7] = "ThrownEgg";
        stringArray[8] = "LeashKnot";
        stringArray[9] = "Painting";
        stringArray[10] = "Arrow";
        stringArray[11] = "Snowball";
        stringArray[12] = "Fireball";
        stringArray[13] = "SmallFireball";
        stringArray[14] = "ThrownEnderpearl";
        stringArray[15] = "EyeOfEnderSignal";
        stringArray[16] = "ThrownPotion";
        stringArray[17] = "ThrownExpBottle";
        stringArray[18] = "ItemFrame";
        stringArray[19] = "WitherSkull";
        stringArray[20] = "PrimedTnt";
        stringArray[21] = "FallingSand";
        stringArray[22] = "FireworksRocketEntity";
        stringArray[23] = "TippedArrow";
        stringArray[24] = "SpectralArrow";
        stringArray[25] = "ShulkerBullet";
        stringArray[26] = "DragonFireball";
        stringArray[30] = "ArmorStand";
        stringArray[41] = "Boat";
        stringArray[42] = "MinecartRideable";
        stringArray[43] = "MinecartChest";
        stringArray[44] = "MinecartFurnace";
        stringArray[45] = "MinecartTNT";
        stringArray[46] = "MinecartHopper";
        stringArray[47] = "MinecartSpawner";
        stringArray[40] = "MinecartCommandBlock";
        stringArray[50] = "Creeper";
        stringArray[51] = "Skeleton";
        stringArray[52] = "Spider";
        stringArray[53] = "Giant";
        stringArray[54] = "Zombie";
        stringArray[55] = "Slime";
        stringArray[56] = "Ghast";
        stringArray[57] = "PigZombie";
        stringArray[58] = "Enderman";
        stringArray[59] = "CaveSpider";
        stringArray[60] = "Silverfish";
        stringArray[61] = "Blaze";
        stringArray[62] = "LavaSlime";
        stringArray[63] = "EnderDragon";
        stringArray[64] = "WitherBoss";
        stringArray[65] = "Bat";
        stringArray[66] = "Witch";
        stringArray[67] = "Endermite";
        stringArray[68] = "Guardian";
        stringArray[69] = "Shulker";
        stringArray[90] = "Pig";
        stringArray[91] = "Sheep";
        stringArray[92] = "Cow";
        stringArray[93] = "Chicken";
        stringArray[94] = "Squid";
        stringArray[95] = "Wolf";
        stringArray[96] = "MushroomCow";
        stringArray[97] = "SnowMan";
        stringArray[98] = "Ozelot";
        stringArray[99] = "VillagerGolem";
        stringArray[100] = "EntityHorse";
        stringArray[101] = "Rabbit";
        stringArray[120] = "Villager";
        stringArray[200] = "EnderCrystal";
    });

    public ItemSpawnEggFix(Schema schema, boolean bl) {
        super(schema, bl);
    }

    @Override
    public TypeRewriteRule makeRule() {
        Schema schema = this.getInputSchema();
        Type<?> type = schema.getType(References.ITEM_STACK);
        OpticFinder<Pair<String, String>> opticFinder = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
        OpticFinder<String> opticFinder2 = DSL.fieldFinder("id", DSL.string());
        OpticFinder<?> opticFinder3 = type.findField("tag");
        OpticFinder<?> opticFinder4 = opticFinder3.type().findField("EntityTag");
        OpticFinder<?> opticFinder5 = DSL.typeFinder(schema.getTypeRaw(References.ENTITY));
        return this.fixTypeEverywhereTyped("ItemSpawnEggFix", type, typed2 -> {
            Optional optional = typed2.getOptional(opticFinder);
            if (optional.isPresent() && Objects.equals(((Pair)optional.get()).getSecond(), "minecraft:spawn_egg")) {
                Dynamic dynamic = typed2.get(DSL.remainderFinder());
                short s = dynamic.get("Damage").asShort((short)0);
                Optional optional2 = typed2.getOptionalTyped(opticFinder3);
                Optional optional3 = optional2.flatMap(typed -> typed.getOptionalTyped(opticFinder4));
                Optional optional4 = optional3.flatMap(typed -> typed.getOptionalTyped(opticFinder5));
                Optional optional5 = optional4.flatMap(typed -> typed.getOptional(opticFinder2));
                Typed<?> typed3 = typed2;
                String string = ID_TO_ENTITY[s & 0xFF];
                if (string != null && (optional5.isEmpty() || !Objects.equals(optional5.get(), string))) {
                    Typed typed4 = typed2.getOrCreateTyped(opticFinder3);
                    Dynamic dynamic2 = DataFixUtils.orElse(typed4.getOptionalTyped(opticFinder4).map(typed -> typed.write().getOrThrow()), dynamic.emptyMap());
                    dynamic2 = dynamic2.set("id", dynamic2.createString(string));
                    typed3 = typed3.set(opticFinder3, ExtraDataFixUtils.readAndSet(typed4, opticFinder4, dynamic2));
                }
                if (s != 0) {
                    dynamic = dynamic.set("Damage", dynamic.createShort((short)0));
                    typed3 = typed3.set(DSL.remainderFinder(), dynamic);
                }
                return typed3;
            }
            return typed2;
        });
    }
}

