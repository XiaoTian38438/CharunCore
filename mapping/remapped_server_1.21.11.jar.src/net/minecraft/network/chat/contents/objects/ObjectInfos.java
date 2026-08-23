/*    */ package net.minecraft.network.chat.contents.objects;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class ObjectInfos {
/*  8 */   private static final ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends ObjectInfo>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
/*    */   
/* 10 */   public static final MapCodec<ObjectInfo> CODEC = ComponentSerialization.createLegacyComponentMatcher(ID_MAPPER, ObjectInfo::codec, "object");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 17 */     ID_MAPPER.put("atlas", AtlasSprite.MAP_CODEC);
/* 18 */     ID_MAPPER.put("player", PlayerSprite.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\contents\objects\ObjectInfos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */