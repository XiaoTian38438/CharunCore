/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityTippedArrowFix
/*    */   extends SimplestEntityRenameFix {
/*    */   public EntityTippedArrowFix(Schema paramSchema, boolean paramBoolean) {
/*  9 */     super("EntityTippedArrowFix", paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String rename(String paramString) {
/* 14 */     return Objects.equals(paramString, "TippedArrow") ? "Arrow" : paramString;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityTippedArrowFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */