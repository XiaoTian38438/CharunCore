/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
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
/*    */ final class StructureFeatureConfiguration
/*    */ {
/*    */   public static final Codec<StructureFeatureConfiguration> CODEC;
/*    */   final int spacing;
/*    */   final int separation;
/*    */   final int salt;
/*    */   
/*    */   static {
/* 81 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.INT.fieldOf("spacing").forGetter(()), (App)Codec.INT.fieldOf("separation").forGetter(()), (App)Codec.INT.fieldOf("salt").forGetter(())).apply((Applicative)paramInstance, StructureFeatureConfiguration::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StructureFeatureConfiguration(int paramInt1, int paramInt2, int paramInt3) {
/* 92 */     this.spacing = paramInt1;
/* 93 */     this.separation = paramInt2;
/* 94 */     this.salt = paramInt3;
/*    */   }
/*    */   
/*    */   public <T> Dynamic<T> serialize(DynamicOps<T> paramDynamicOps) {
/* 98 */     return new Dynamic(paramDynamicOps, CODEC.encodeStart(paramDynamicOps, this).result().orElse(paramDynamicOps.emptyMap()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\WorldGenSettingsFix$StructureFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */