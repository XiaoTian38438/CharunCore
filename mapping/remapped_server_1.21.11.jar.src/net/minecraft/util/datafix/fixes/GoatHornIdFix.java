/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class GoatHornIdFix extends ItemStackTagRemainderFix {
/*  7 */   private static final String[] INSTRUMENTS = new String[] { "minecraft:ponder_goat_horn", "minecraft:sing_goat_horn", "minecraft:seek_goat_horn", "minecraft:feel_goat_horn", "minecraft:admire_goat_horn", "minecraft:call_goat_horn", "minecraft:yearn_goat_horn", "minecraft:dream_goat_horn" };
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
/*    */   public GoatHornIdFix(Schema paramSchema) {
/* 19 */     super(paramSchema, "GoatHornIdFix", paramString -> paramString.equals("minecraft:goat_horn"));
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixItemStackTag(Dynamic<T> paramDynamic) {
/* 24 */     int i = paramDynamic.get("SoundVariant").asInt(0);
/* 25 */     String str = INSTRUMENTS[(i >= 0 && i < INSTRUMENTS.length) ? i : 0];
/* 26 */     return paramDynamic.remove("SoundVariant").set("instrument", paramDynamic.createString(str));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\GoatHornIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */