/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OminousBannerRenameFix
/*    */   extends ItemStackTagFix
/*    */ {
/*    */   public OminousBannerRenameFix(Schema paramSchema) {
/* 15 */     super(paramSchema, "OminousBannerRenameFix", paramString -> paramString.equals("minecraft:white_banner"));
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fixItemStackTag(Dynamic<T> paramDynamic) {
/* 19 */     return paramDynamic.update("display", paramDynamic -> paramDynamic.update("Name", ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fixItemStackTag(Typed<?> paramTyped) {
/* 30 */     return Util.writeAndReadTypedOrThrow(paramTyped, paramTyped.getType(), this::fixItemStackTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\OminousBannerRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */