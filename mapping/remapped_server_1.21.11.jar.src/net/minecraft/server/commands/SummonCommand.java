/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.CompoundTagArgument;
/*    */ import net.minecraft.commands.arguments.ResourceArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*    */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class SummonCommand {
/* 35 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.summon.failed"));
/* 36 */   private static final SimpleCommandExceptionType ERROR_FAILED_PEACEFUL = new SimpleCommandExceptionType((Message)Component.translatable("commands.summon.failed.peaceful"));
/* 37 */   private static final SimpleCommandExceptionType ERROR_DUPLICATE_UUID = new SimpleCommandExceptionType((Message)Component.translatable("commands.summon.failed.uuid"));
/* 38 */   private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType((Message)Component.translatable("commands.summon.invalidPosition"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/* 41 */     paramCommandDispatcher.register(
/* 42 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("summon")
/* 43 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 44 */         .then((
/* 45 */           (RequiredArgumentBuilder)Commands.argument("entity", (ArgumentType)ResourceArgument.resource(paramCommandBuildContext, Registries.ENTITY_TYPE))
/* 46 */           .suggests(SuggestionProviders.cast(SuggestionProviders.SUMMONABLE_ENTITIES))
/* 47 */           .executes(paramCommandContext -> spawnEntity((CommandSourceStack)paramCommandContext.getSource(), ResourceArgument.getSummonableEntityType(paramCommandContext, "entity"), ((CommandSourceStack)paramCommandContext.getSource()).getPosition(), new CompoundTag(), true)))
/* 48 */           .then((
/* 49 */             (RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)Vec3Argument.vec3())
/* 50 */             .executes(paramCommandContext -> spawnEntity((CommandSourceStack)paramCommandContext.getSource(), ResourceArgument.getSummonableEntityType(paramCommandContext, "entity"), Vec3Argument.getVec3(paramCommandContext, "pos"), new CompoundTag(), true)))
/* 51 */             .then(
/* 52 */               Commands.argument("nbt", (ArgumentType)CompoundTagArgument.compoundTag())
/* 53 */               .executes(paramCommandContext -> spawnEntity((CommandSourceStack)paramCommandContext.getSource(), ResourceArgument.getSummonableEntityType(paramCommandContext, "entity"), Vec3Argument.getVec3(paramCommandContext, "pos"), CompoundTagArgument.getCompoundTag(paramCommandContext, "nbt"), false))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Entity createEntity(CommandSourceStack paramCommandSourceStack, Holder.Reference<EntityType<?>> paramReference, Vec3 paramVec3, CompoundTag paramCompoundTag, boolean paramBoolean) throws CommandSyntaxException {
/* 61 */     BlockPos blockPos = BlockPos.containing((Position)paramVec3);
/* 62 */     if (!Level.isInSpawnableBounds(blockPos)) {
/* 63 */       throw INVALID_POSITION.create();
/*    */     }
/*    */     
/* 66 */     if (paramCommandSourceStack.getLevel().getDifficulty() == Difficulty.PEACEFUL && !((EntityType)paramReference.value()).isAllowedInPeaceful()) {
/* 67 */       throw ERROR_FAILED_PEACEFUL.create();
/*    */     }
/*    */     
/* 70 */     CompoundTag compoundTag = paramCompoundTag.copy();
/* 71 */     compoundTag.putString("id", paramReference.key().identifier().toString());
/*    */     
/* 73 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 74 */     Entity entity = EntityType.loadEntityRecursive(compoundTag, (Level)serverLevel, EntitySpawnReason.COMMAND, paramEntity -> {
/*    */           paramEntity.snapTo(paramVec3.x, paramVec3.y, paramVec3.z, paramEntity.getYRot(), paramEntity.getXRot());
/*    */           return paramEntity;
/*    */         });
/* 78 */     if (entity == null) {
/* 79 */       throw ERROR_FAILED.create();
/*    */     }
/*    */     
/* 82 */     if (paramBoolean && entity instanceof Mob) { Mob mob = (Mob)entity;
/* 83 */       mob.finalizeSpawn((ServerLevelAccessor)paramCommandSourceStack.getLevel(), paramCommandSourceStack.getLevel().getCurrentDifficultyAt(entity.blockPosition()), EntitySpawnReason.COMMAND, null); }
/*    */ 
/*    */     
/* 86 */     if (!serverLevel.tryAddFreshEntityWithPassengers(entity)) {
/* 87 */       throw ERROR_DUPLICATE_UUID.create();
/*    */     }
/* 89 */     return entity;
/*    */   }
/*    */   
/*    */   private static int spawnEntity(CommandSourceStack paramCommandSourceStack, Holder.Reference<EntityType<?>> paramReference, Vec3 paramVec3, CompoundTag paramCompoundTag, boolean paramBoolean) throws CommandSyntaxException {
/* 93 */     Entity entity = createEntity(paramCommandSourceStack, paramReference, paramVec3, paramCompoundTag, paramBoolean);
/*    */     
/* 95 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.summon.success", new Object[] { paramEntity.getDisplayName() }), true);
/* 96 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\SummonCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */