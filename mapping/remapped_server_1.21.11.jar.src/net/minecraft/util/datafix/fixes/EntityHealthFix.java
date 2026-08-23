/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class EntityHealthFix extends DataFix {
/*    */   public EntityHealthFix(Schema paramSchema, boolean paramBoolean) {
/* 15 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */   
/* 18 */   private static final Set<String> ENTITIES = Sets.newHashSet((Object[])new String[] { "ArmorStand", "Bat", "Blaze", "CaveSpider", "Chicken", "Cow", "Creeper", "EnderDragon", "Enderman", "Endermite", "EntityHorse", "Ghast", "Giant", "Guardian", "LavaSlime", "MushroomCow", "Ozelot", "Pig", "PigZombie", "Rabbit", "Sheep", "Shulker", "Silverfish", "Skeleton", "Slime", "SnowMan", "Spider", "Squid", "Villager", "VillagerGolem", "Witch", "WitherBoss", "Wolf", "Zombie" });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/*    */     float f;
/* 58 */     Optional<Number> optional1 = paramDynamic.get("HealF").asNumber().result();
/* 59 */     Optional<Number> optional2 = paramDynamic.get("Health").asNumber().result();
/* 60 */     if (optional1.isPresent()) {
/* 61 */       f = ((Number)optional1.get()).floatValue();
/* 62 */       paramDynamic = paramDynamic.remove("HealF");
/* 63 */     } else if (optional2.isPresent()) {
/* 64 */       f = ((Number)optional2.get()).floatValue();
/*    */     } else {
/* 66 */       return paramDynamic;
/*    */     } 
/* 68 */     return paramDynamic.set("Health", paramDynamic.createFloat(f));
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 73 */     return fixTypeEverywhereTyped("EntityHealthFix", getInputSchema().getType(References.ENTITY), paramTyped -> paramTyped.update(DSL.remainderFinder(), this::fixTag));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityHealthFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */