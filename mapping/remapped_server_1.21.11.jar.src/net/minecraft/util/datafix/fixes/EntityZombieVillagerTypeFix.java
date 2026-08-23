/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class EntityZombieVillagerTypeFix extends NamedEntityFix {
/*    */   private static final int PROFESSION_MAX = 6;
/*    */   
/*    */   public EntityZombieVillagerTypeFix(Schema paramSchema, boolean paramBoolean) {
/* 13 */     super(paramSchema, paramBoolean, "EntityZombieVillagerTypeFix", References.ENTITY, "Zombie");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 17 */     if (paramDynamic.get("IsVillager").asBoolean(false)) {
/* 18 */       if (paramDynamic.get("ZombieType").result().isEmpty()) {
/* 19 */         int i = getVillagerProfession(paramDynamic.get("VillagerProfession").asInt(-1));
/* 20 */         if (i == -1) {
/* 21 */           i = getVillagerProfession(RandomSource.create().nextInt(6));
/*    */         }
/*    */         
/* 24 */         paramDynamic = paramDynamic.set("ZombieType", paramDynamic.createInt(i));
/*    */       } 
/*    */       
/* 27 */       paramDynamic = paramDynamic.remove("IsVillager");
/*    */     } 
/* 29 */     return paramDynamic;
/*    */   }
/*    */   
/*    */   private int getVillagerProfession(int paramInt) {
/* 33 */     if (paramInt < 0 || paramInt >= 6) {
/* 34 */       return -1;
/*    */     }
/* 36 */     return paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 41 */     return paramTyped.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityZombieVillagerTypeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */