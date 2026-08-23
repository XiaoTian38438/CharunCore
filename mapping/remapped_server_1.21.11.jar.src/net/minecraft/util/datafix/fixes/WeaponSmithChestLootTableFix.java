/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class WeaponSmithChestLootTableFix extends NamedEntityFix {
/*    */   public WeaponSmithChestLootTableFix(Schema paramSchema, boolean paramBoolean) {
/*  9 */     super(paramSchema, paramBoolean, "WeaponSmithChestLootTableFix", References.BLOCK_ENTITY, "minecraft:chest");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 14 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> {
/*    */           String str = paramDynamic.get("LootTable").asString("");
/*    */           return str.equals("minecraft:chests/village_blacksmith") ? paramDynamic.set("LootTable", paramDynamic.createString("minecraft:chests/village/village_weaponsmith")) : paramDynamic;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\WeaponSmithChestLootTableFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */