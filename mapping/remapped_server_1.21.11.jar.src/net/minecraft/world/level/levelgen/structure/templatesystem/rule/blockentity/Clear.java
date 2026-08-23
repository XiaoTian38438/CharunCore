/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class Clear
/*    */   implements RuleBlockEntityModifier {
/*  9 */   private static final Clear INSTANCE = new Clear();
/* 10 */   public static final MapCodec<Clear> CODEC = MapCodec.unit(INSTANCE);
/*    */ 
/*    */   
/*    */   public CompoundTag apply(RandomSource paramRandomSource, CompoundTag paramCompoundTag) {
/* 14 */     return new CompoundTag();
/*    */   }
/*    */ 
/*    */   
/*    */   public RuleBlockEntityModifierType<?> getType() {
/* 19 */     return RuleBlockEntityModifierType.CLEAR;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\rule\blockentity\Clear.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */