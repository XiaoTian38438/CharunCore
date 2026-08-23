/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ public class ItemSpawnEggFix
/*     */   extends DataFix {
/*     */   private static final String[] ID_TO_ENTITY;
/*     */   
/*     */   public ItemSpawnEggFix(Schema paramSchema, boolean paramBoolean) {
/*  23 */     super(paramSchema, paramBoolean);
/*     */   }
/*     */   static {
/*  26 */     ID_TO_ENTITY = (String[])DataFixUtils.make(new String[256], paramArrayOfString -> {
/*     */           paramArrayOfString[1] = "Item";
/*     */           paramArrayOfString[2] = "XPOrb";
/*     */           paramArrayOfString[7] = "ThrownEgg";
/*     */           paramArrayOfString[8] = "LeashKnot";
/*     */           paramArrayOfString[9] = "Painting";
/*     */           paramArrayOfString[10] = "Arrow";
/*     */           paramArrayOfString[11] = "Snowball";
/*     */           paramArrayOfString[12] = "Fireball";
/*     */           paramArrayOfString[13] = "SmallFireball";
/*     */           paramArrayOfString[14] = "ThrownEnderpearl";
/*     */           paramArrayOfString[15] = "EyeOfEnderSignal";
/*     */           paramArrayOfString[16] = "ThrownPotion";
/*     */           paramArrayOfString[17] = "ThrownExpBottle";
/*     */           paramArrayOfString[18] = "ItemFrame";
/*     */           paramArrayOfString[19] = "WitherSkull";
/*     */           paramArrayOfString[20] = "PrimedTnt";
/*     */           paramArrayOfString[21] = "FallingSand";
/*     */           paramArrayOfString[22] = "FireworksRocketEntity";
/*     */           paramArrayOfString[23] = "TippedArrow";
/*     */           paramArrayOfString[24] = "SpectralArrow";
/*     */           paramArrayOfString[25] = "ShulkerBullet";
/*     */           paramArrayOfString[26] = "DragonFireball";
/*     */           paramArrayOfString[30] = "ArmorStand";
/*     */           paramArrayOfString[41] = "Boat";
/*     */           paramArrayOfString[42] = "MinecartRideable";
/*     */           paramArrayOfString[43] = "MinecartChest";
/*     */           paramArrayOfString[44] = "MinecartFurnace";
/*     */           paramArrayOfString[45] = "MinecartTNT";
/*     */           paramArrayOfString[46] = "MinecartHopper";
/*     */           paramArrayOfString[47] = "MinecartSpawner";
/*     */           paramArrayOfString[40] = "MinecartCommandBlock";
/*     */           paramArrayOfString[50] = "Creeper";
/*     */           paramArrayOfString[51] = "Skeleton";
/*     */           paramArrayOfString[52] = "Spider";
/*     */           paramArrayOfString[53] = "Giant";
/*     */           paramArrayOfString[54] = "Zombie";
/*     */           paramArrayOfString[55] = "Slime";
/*     */           paramArrayOfString[56] = "Ghast";
/*     */           paramArrayOfString[57] = "PigZombie";
/*     */           paramArrayOfString[58] = "Enderman";
/*     */           paramArrayOfString[59] = "CaveSpider";
/*     */           paramArrayOfString[60] = "Silverfish";
/*     */           paramArrayOfString[61] = "Blaze";
/*     */           paramArrayOfString[62] = "LavaSlime";
/*     */           paramArrayOfString[63] = "EnderDragon";
/*     */           paramArrayOfString[64] = "WitherBoss";
/*     */           paramArrayOfString[65] = "Bat";
/*     */           paramArrayOfString[66] = "Witch";
/*     */           paramArrayOfString[67] = "Endermite";
/*     */           paramArrayOfString[68] = "Guardian";
/*     */           paramArrayOfString[69] = "Shulker";
/*     */           paramArrayOfString[90] = "Pig";
/*     */           paramArrayOfString[91] = "Sheep";
/*     */           paramArrayOfString[92] = "Cow";
/*     */           paramArrayOfString[93] = "Chicken";
/*     */           paramArrayOfString[94] = "Squid";
/*     */           paramArrayOfString[95] = "Wolf";
/*     */           paramArrayOfString[96] = "MushroomCow";
/*     */           paramArrayOfString[97] = "SnowMan";
/*     */           paramArrayOfString[98] = "Ozelot";
/*     */           paramArrayOfString[99] = "VillagerGolem";
/*     */           paramArrayOfString[100] = "EntityHorse";
/*     */           paramArrayOfString[101] = "Rabbit";
/*     */           paramArrayOfString[120] = "Villager";
/*     */           paramArrayOfString[200] = "EnderCrystal";
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 111 */     Schema schema = getInputSchema();
/* 112 */     Type type = schema.getType(References.ITEM_STACK);
/*     */     
/* 114 */     OpticFinder opticFinder1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 115 */     OpticFinder opticFinder2 = DSL.fieldFinder("id", DSL.string());
/* 116 */     OpticFinder opticFinder3 = type.findField("tag");
/* 117 */     OpticFinder opticFinder4 = opticFinder3.type().findField("EntityTag");
/* 118 */     OpticFinder opticFinder5 = DSL.typeFinder(schema.getTypeRaw(References.ENTITY));
/*     */     
/* 120 */     return fixTypeEverywhereTyped("ItemSpawnEggFix", type, paramTyped -> {
/*     */           Optional<Pair> optional = paramTyped.getOptional(paramOpticFinder1);
/*     */           if (optional.isPresent() && Objects.equals(((Pair)optional.get()).getSecond(), "minecraft:spawn_egg")) {
/*     */             Dynamic dynamic = (Dynamic)paramTyped.get(DSL.remainderFinder());
/*     */             short s = dynamic.get("Damage").asShort((short)0);
/*     */             Optional optional1 = paramTyped.getOptionalTyped(paramOpticFinder2);
/*     */             Optional optional2 = optional1.flatMap(());
/*     */             Optional optional3 = optional2.flatMap(());
/*     */             Optional optional4 = optional3.flatMap(());
/*     */             Typed typed = paramTyped;
/*     */             String str = ID_TO_ENTITY[s & 0xFF];
/*     */             if (str != null && (optional4.isEmpty() || !Objects.equals(optional4.get(), str))) {
/*     */               Typed typed1 = paramTyped.getOrCreateTyped(paramOpticFinder2);
/*     */               Dynamic dynamic1 = (Dynamic)DataFixUtils.orElse(typed1.getOptionalTyped(paramOpticFinder3).map(()), dynamic.emptyMap());
/*     */               dynamic1 = dynamic1.set("id", dynamic1.createString(str));
/*     */               typed = typed.set(paramOpticFinder2, ExtraDataFixUtils.readAndSet(typed1, paramOpticFinder3, dynamic1));
/*     */             } 
/*     */             if (s != 0) {
/*     */               dynamic = dynamic.set("Damage", dynamic.createShort((short)0));
/*     */               typed = typed.set(DSL.remainderFinder(), dynamic);
/*     */             } 
/*     */             return typed;
/*     */           } 
/*     */           return paramTyped;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemSpawnEggFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */