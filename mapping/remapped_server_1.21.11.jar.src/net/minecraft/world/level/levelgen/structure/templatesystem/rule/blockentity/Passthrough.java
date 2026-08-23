/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class Passthrough
/*    */   implements RuleBlockEntityModifier {
/*  9 */   public static final Passthrough INSTANCE = new Passthrough();
/* 10 */   public static final MapCodec<Passthrough> CODEC = MapCodec.unit(INSTANCE);
/*    */ 
/*    */   
/*    */   public CompoundTag apply(RandomSource paramRandomSource, CompoundTag paramCompoundTag) {
/* 14 */     return paramCompoundTag;
/*    */   }
/*    */ 
/*    */   
/*    */   public RuleBlockEntityModifierType<?> getType() {
/* 19 */     return RuleBlockEntityModifierType.PASSTHROUGH;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\rule\blockentity\Passthrough.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */