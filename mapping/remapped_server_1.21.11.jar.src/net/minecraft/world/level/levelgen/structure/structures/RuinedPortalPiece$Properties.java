/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
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
/*    */ public class Properties
/*    */ {
/*    */   public static final Codec<Properties> CODEC;
/*    */   public boolean cold;
/*    */   public float mossiness;
/*    */   public boolean airPocket;
/*    */   public boolean overgrown;
/*    */   public boolean vines;
/*    */   public boolean replaceWithBlackstone;
/*    */   
/*    */   static {
/* 60 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.BOOL.fieldOf("cold").forGetter(()), (App)Codec.FLOAT.fieldOf("mossiness").forGetter(()), (App)Codec.BOOL.fieldOf("air_pocket").forGetter(()), (App)Codec.BOOL.fieldOf("overgrown").forGetter(()), (App)Codec.BOOL.fieldOf("vines").forGetter(()), (App)Codec.BOOL.fieldOf("replace_with_blackstone").forGetter(())).apply((Applicative)paramInstance, Properties::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Properties() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Properties(boolean paramBoolean1, float paramFloat, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5) {
/* 80 */     this.cold = paramBoolean1;
/* 81 */     this.mossiness = paramFloat;
/* 82 */     this.airPocket = paramBoolean2;
/* 83 */     this.overgrown = paramBoolean3;
/* 84 */     this.vines = paramBoolean4;
/* 85 */     this.replaceWithBlackstone = paramBoolean5;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\RuinedPortalPiece$Properties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */