/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.util.StringRepresentable;
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
/*    */ 
/*    */ 
/*    */ public interface Type
/*    */   extends StringRepresentable
/*    */ {
/* 33 */   public static final Map<String, Type> TYPES = (Map<String, Type>)new Object2ObjectArrayMap();
/*    */   
/* 35 */   public static final Codec<Type> CODEC = Codec.stringResolver(StringRepresentable::getSerializedName, TYPES::get); static { Objects.requireNonNull(TYPES); }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SkullBlock$Type.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */