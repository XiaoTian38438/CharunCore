/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class PlayerHeadBlockProfileFix
/*    */   extends NamedEntityFix {
/*    */   public PlayerHeadBlockProfileFix(Schema paramSchema) {
/* 12 */     super(paramSchema, false, "PlayerHeadBlockProfileFix", References.BLOCK_ENTITY, "minecraft:skull");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 17 */     return paramTyped.update(DSL.remainderFinder(), this::fix);
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fix(Dynamic<T> paramDynamic) {
/* 21 */     Optional optional1 = paramDynamic.get("SkullOwner").result();
/* 22 */     Optional optional2 = paramDynamic.get("ExtraType").result();
/*    */     
/* 24 */     Optional<Dynamic> optional = optional1.or(() -> paramOptional);
/* 25 */     if (optional.isEmpty()) {
/* 26 */       return paramDynamic;
/*    */     }
/* 28 */     paramDynamic = paramDynamic.remove("SkullOwner").remove("ExtraType");
/* 29 */     paramDynamic = paramDynamic.set("profile", ItemStackComponentizationFix.fixProfile(optional.get()));
/* 30 */     return paramDynamic;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\PlayerHeadBlockProfileFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */