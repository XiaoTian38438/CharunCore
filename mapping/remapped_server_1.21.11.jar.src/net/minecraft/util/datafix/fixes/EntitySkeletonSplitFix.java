/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntitySkeletonSplitFix
/*    */   extends SimpleEntityRenameFix {
/*    */   public EntitySkeletonSplitFix(Schema paramSchema, boolean paramBoolean) {
/* 11 */     super("EntitySkeletonSplitFix", paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Dynamic<?>> getNewNameAndTag(String paramString, Dynamic<?> paramDynamic) {
/* 16 */     if (Objects.equals(paramString, "Skeleton")) {
/* 17 */       int i = paramDynamic.get("SkeletonType").asInt(0);
/* 18 */       if (i == 1) {
/* 19 */         paramString = "WitherSkeleton";
/* 20 */       } else if (i == 2) {
/* 21 */         paramString = "Stray";
/*    */       } 
/*    */     } 
/* 24 */     return Pair.of(paramString, paramDynamic);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntitySkeletonSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */