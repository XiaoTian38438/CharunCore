/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.HoverEvent;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ public interface ScoreHolder
/*    */ {
/* 11 */   public static final ScoreHolder WILDCARD = new ScoreHolder()
/*    */     {
/*    */       public String getScoreboardName() {
/* 14 */         return "*";
/*    */       }
/*    */     };
/*    */   
/*    */   public static final String WILDCARD_NAME = "*";
/*    */   
/*    */   default Component getDisplayName() {
/* 21 */     return null;
/*    */   }
/*    */   
/*    */   default Component getFeedbackDisplayName() {
/* 25 */     Component component = getDisplayName();
/* 26 */     if (component != null) {
/* 27 */       return (Component)component.copy().withStyle(paramStyle -> paramStyle.withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.literal(getScoreboardName()))));
/*    */     }
/* 29 */     return (Component)Component.literal(getScoreboardName());
/*    */   }
/*    */   
/*    */   static ScoreHolder forNameOnly(final String name) {
/* 33 */     if (name.equals("*")) {
/* 34 */       return WILDCARD;
/*    */     }
/*    */     
/* 37 */     final MutableComponent feedbackName = Component.literal(name);
/* 38 */     return new ScoreHolder()
/*    */       {
/*    */         public String getScoreboardName() {
/* 41 */           return name;
/*    */         }
/*    */ 
/*    */         
/*    */         public Component getFeedbackDisplayName() {
/* 46 */           return feedbackName;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static ScoreHolder fromGameProfile(GameProfile paramGameProfile) {
/* 52 */     final String name = paramGameProfile.name();
/* 53 */     return new ScoreHolder()
/*    */       {
/*    */         public String getScoreboardName() {
/* 56 */           return name;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   String getScoreboardName();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\scores\ScoreHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */