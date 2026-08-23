/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ 
/*    */ public class AppendStatic implements RuleBlockEntityModifier {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)CompoundTag.CODEC.fieldOf("data").forGetter(())).apply((Applicative)paramInstance, AppendStatic::new));
/*    */   }
/*    */   public static final MapCodec<AppendStatic> CODEC;
/*    */   private final CompoundTag tag;
/*    */   
/*    */   public AppendStatic(CompoundTag paramCompoundTag) {
/* 16 */     this.tag = paramCompoundTag;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag apply(RandomSource paramRandomSource, CompoundTag paramCompoundTag) {
/* 21 */     return (paramCompoundTag == null) ? this.tag.copy() : paramCompoundTag.merge(this.tag);
/*    */   }
/*    */ 
/*    */   
/*    */   public RuleBlockEntityModifierType<?> getType() {
/* 26 */     return RuleBlockEntityModifierType.APPEND_STATIC;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\rule\blockentity\AppendStatic.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */