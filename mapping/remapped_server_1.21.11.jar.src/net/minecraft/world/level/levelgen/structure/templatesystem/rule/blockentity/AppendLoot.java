/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ public class AppendLoot implements RuleBlockEntityModifier {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)LootTable.KEY_CODEC.fieldOf("loot_table").forGetter(())).apply((Applicative)paramInstance, AppendLoot::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<AppendLoot> CODEC;
/*    */   private final ResourceKey<LootTable> lootTable;
/*    */   
/*    */   public AppendLoot(ResourceKey<LootTable> paramResourceKey) {
/* 20 */     this.lootTable = paramResourceKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag apply(RandomSource paramRandomSource, CompoundTag paramCompoundTag) {
/* 25 */     CompoundTag compoundTag = (paramCompoundTag == null) ? new CompoundTag() : paramCompoundTag.copy();
/*    */     
/* 27 */     compoundTag.store("LootTable", LootTable.KEY_CODEC, this.lootTable);
/* 28 */     compoundTag.putLong("LootTableSeed", paramRandomSource.nextLong());
/*    */     
/* 30 */     return compoundTag;
/*    */   }
/*    */ 
/*    */   
/*    */   public RuleBlockEntityModifierType<?> getType() {
/* 35 */     return RuleBlockEntityModifierType.APPEND_LOOT;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\rule\blockentity\AppendLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */