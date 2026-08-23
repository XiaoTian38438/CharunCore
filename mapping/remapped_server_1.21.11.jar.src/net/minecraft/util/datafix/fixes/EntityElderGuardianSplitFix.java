/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityElderGuardianSplitFix
/*    */   extends SimpleEntityRenameFix {
/*    */   public EntityElderGuardianSplitFix(Schema paramSchema, boolean paramBoolean) {
/* 11 */     super("EntityElderGuardianSplitFix", paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Dynamic<?>> getNewNameAndTag(String paramString, Dynamic<?> paramDynamic) {
/* 16 */     return Pair.of((Objects.equals(paramString, "Guardian") && paramDynamic.get("Elder").asBoolean(false)) ? "ElderGuardian" : paramString, paramDynamic);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityElderGuardianSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */