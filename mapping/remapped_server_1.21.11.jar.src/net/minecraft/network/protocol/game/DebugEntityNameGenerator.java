/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DebugEntityNameGenerator
/*    */ {
/* 17 */   private static final String[] NAMES_FIRST_PART = new String[] { "Slim", "Far", "River", "Silly", "Fat", "Thin", "Fish", "Bat", "Dark", "Oak", "Sly", "Bush", "Zen", "Bark", "Cry", "Slack", "Soup", "Grim", "Hook", "Dirt", "Mud", "Sad", "Hard", "Crook", "Sneak", "Stink", "Weird", "Fire", "Soot", "Soft", "Rough", "Cling", "Scar" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   private static final String[] NAMES_SECOND_PART = new String[] { "Fox", "Tail", "Jaw", "Whisper", "Twig", "Root", "Finder", "Nose", "Brow", "Blade", "Fry", "Seek", "Wart", "Tooth", "Foot", "Leaf", "Stone", "Fall", "Face", "Tongue", "Voice", "Lip", "Mouth", "Snail", "Toe", "Ear", "Hair", "Beard", "Shirt", "Fist" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getEntityName(Entity paramEntity) {
/* 31 */     if (paramEntity instanceof net.minecraft.world.entity.player.Player) {
/* 32 */       return paramEntity.getPlainTextName();
/*    */     }
/* 34 */     Component component = paramEntity.getCustomName();
/* 35 */     if (component != null) {
/* 36 */       return component.getString();
/*    */     }
/* 38 */     return getEntityName(paramEntity.getUUID());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getEntityName(UUID paramUUID) {
/* 47 */     RandomSource randomSource = getRandom(paramUUID);
/* 48 */     return getRandomString(randomSource, NAMES_FIRST_PART) + getRandomString(randomSource, NAMES_FIRST_PART);
/*    */   }
/*    */   
/*    */   private static String getRandomString(RandomSource paramRandomSource, String[] paramArrayOfString) {
/* 52 */     return (String)Util.getRandom((Object[])paramArrayOfString, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   private static RandomSource getRandom(UUID paramUUID) {
/* 57 */     return RandomSource.create((paramUUID.hashCode() >> 2));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\DebugEntityNameGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */