/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BaseCommandBlock;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class CommandBlock extends BaseEntityBlock implements GameMasterBlock {
/*     */   static {
/*  35 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.BOOL.fieldOf("automatic").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, CommandBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<CommandBlock> CODEC;
/*     */   
/*     */   public MapCodec<CommandBlock> codec() {
/*  42 */     return CODEC;
/*     */   }
/*     */   
/*  45 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  46 */   public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
/*  47 */   public static final BooleanProperty CONDITIONAL = BlockStateProperties.CONDITIONAL;
/*     */   private final boolean automatic;
/*     */   
/*     */   public CommandBlock(boolean paramBoolean, BlockBehaviour.Properties paramProperties) {
/*  51 */     super(paramProperties);
/*  52 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)CONDITIONAL, Boolean.valueOf(false)));
/*  53 */     this.automatic = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  58 */     CommandBlockEntity commandBlockEntity = new CommandBlockEntity(paramBlockPos, paramBlockState);
/*  59 */     commandBlockEntity.setAutomatic(this.automatic);
/*  60 */     return (BlockEntity)commandBlockEntity;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/*  65 */     if (paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/*  69 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/*  70 */     if (blockEntity instanceof CommandBlockEntity) { CommandBlockEntity commandBlockEntity = (CommandBlockEntity)blockEntity;
/*  71 */       setPoweredAndUpdate(paramLevel, paramBlockPos, commandBlockEntity, paramLevel.hasNeighborSignal(paramBlockPos)); }
/*     */   
/*     */   }
/*     */   
/*     */   private void setPoweredAndUpdate(Level paramLevel, BlockPos paramBlockPos, CommandBlockEntity paramCommandBlockEntity, boolean paramBoolean) {
/*  76 */     boolean bool = paramCommandBlockEntity.isPowered();
/*  77 */     if (paramBoolean == bool) {
/*     */       return;
/*     */     }
/*     */     
/*  81 */     paramCommandBlockEntity.setPowered(paramBoolean);
/*     */     
/*  83 */     if (paramBoolean) {
/*  84 */       if (paramCommandBlockEntity.isAutomatic() || paramCommandBlockEntity.getMode() == CommandBlockEntity.Mode.SEQUENCE) {
/*     */         return;
/*     */       }
/*     */       
/*  88 */       paramCommandBlockEntity.markConditionMet();
/*     */       
/*  90 */       paramLevel.scheduleTick(paramBlockPos, this, 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  96 */     BlockEntity blockEntity = paramServerLevel.getBlockEntity(paramBlockPos);
/*  97 */     if (blockEntity instanceof CommandBlockEntity) { CommandBlockEntity commandBlockEntity = (CommandBlockEntity)blockEntity;
/*  98 */       BaseCommandBlock baseCommandBlock = commandBlockEntity.getCommandBlock();
/*  99 */       boolean bool = !StringUtil.isNullOrEmpty(baseCommandBlock.getCommand()) ? true : false;
/* 100 */       CommandBlockEntity.Mode mode = commandBlockEntity.getMode();
/*     */       
/* 102 */       boolean bool1 = commandBlockEntity.wasConditionMet();
/* 103 */       if (mode == CommandBlockEntity.Mode.AUTO) {
/* 104 */         commandBlockEntity.markConditionMet();
/*     */         
/* 106 */         if (bool1) {
/* 107 */           execute(paramBlockState, paramServerLevel, paramBlockPos, baseCommandBlock, bool);
/* 108 */         } else if (commandBlockEntity.isConditional()) {
/* 109 */           baseCommandBlock.setSuccessCount(0);
/*     */         } 
/*     */         
/* 112 */         if (commandBlockEntity.isPowered() || commandBlockEntity.isAutomatic()) {
/* 113 */           paramServerLevel.scheduleTick(paramBlockPos, this, 1);
/*     */         }
/* 115 */       } else if (mode == CommandBlockEntity.Mode.REDSTONE) {
/* 116 */         if (bool1) {
/* 117 */           execute(paramBlockState, paramServerLevel, paramBlockPos, baseCommandBlock, bool);
/* 118 */         } else if (commandBlockEntity.isConditional()) {
/* 119 */           baseCommandBlock.setSuccessCount(0);
/*     */         } 
/*     */       } 
/*     */       
/* 123 */       paramServerLevel.updateNeighbourForOutputSignal(paramBlockPos, this); }
/*     */   
/*     */   }
/*     */   
/*     */   private void execute(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, BaseCommandBlock paramBaseCommandBlock, boolean paramBoolean) {
/* 128 */     if (paramBoolean) {
/* 129 */       paramBaseCommandBlock.performCommand(paramServerLevel);
/*     */     } else {
/* 131 */       paramBaseCommandBlock.setSuccessCount(0);
/*     */     } 
/*     */     
/* 134 */     executeChain(paramServerLevel, paramBlockPos, (Direction)paramBlockState.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 139 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 140 */     if (blockEntity instanceof CommandBlockEntity && paramPlayer.canUseGameMasterBlocks()) {
/* 141 */       paramPlayer.openCommandBlock((CommandBlockEntity)blockEntity);
/* 142 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 145 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 150 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 155 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 156 */     if (blockEntity instanceof CommandBlockEntity) {
/* 157 */       return ((CommandBlockEntity)blockEntity).getCommandBlock().getSuccessCount();
/*     */     }
/* 159 */     return 0;
/*     */   }
/*     */   
/*     */   public void setPlacedBy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/*     */     CommandBlockEntity commandBlockEntity;
/* 164 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 165 */     if (blockEntity instanceof CommandBlockEntity) { commandBlockEntity = (CommandBlockEntity)blockEntity; }
/*     */     else
/*     */     { return; }
/*     */     
/* 169 */     BaseCommandBlock baseCommandBlock = commandBlockEntity.getCommandBlock();
/*     */     
/* 171 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 172 */       if (!paramItemStack.has(DataComponents.BLOCK_ENTITY_DATA)) {
/* 173 */         baseCommandBlock.setTrackOutput(((Boolean)serverLevel.getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK)).booleanValue());
/* 174 */         commandBlockEntity.setAutomatic(this.automatic);
/*     */       } 
/*     */       
/* 177 */       boolean bool = paramLevel.hasNeighborSignal(paramBlockPos);
/* 178 */       setPoweredAndUpdate(paramLevel, paramBlockPos, commandBlockEntity, bool); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 184 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 189 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 194 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)CONDITIONAL });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 199 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getNearestLookingDirection().getOpposite());
/*     */   }
/*     */   
/*     */   private static void executeChain(ServerLevel paramServerLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 203 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/*     */     
/* 205 */     GameRules gameRules = paramServerLevel.getGameRules();
/* 206 */     int i = ((Integer)gameRules.get(GameRules.MAX_COMMAND_SEQUENCE_LENGTH)).intValue();
/* 207 */     while (i-- > 0) {
/* 208 */       mutableBlockPos.move(paramDirection);
/*     */       
/* 210 */       BlockState blockState = paramServerLevel.getBlockState((BlockPos)mutableBlockPos);
/* 211 */       Block block = blockState.getBlock();
/* 212 */       if (!blockState.is(Blocks.CHAIN_COMMAND_BLOCK)) {
/*     */         break;
/*     */       }
/*     */       
/* 216 */       BlockEntity blockEntity = paramServerLevel.getBlockEntity((BlockPos)mutableBlockPos);
/* 217 */       if (!(blockEntity instanceof CommandBlockEntity)) {
/*     */         break;
/*     */       }
/*     */       
/* 221 */       CommandBlockEntity commandBlockEntity = (CommandBlockEntity)blockEntity;
/* 222 */       if (commandBlockEntity.getMode() != CommandBlockEntity.Mode.SEQUENCE) {
/*     */         break;
/*     */       }
/*     */       
/* 226 */       if (commandBlockEntity.isPowered() || commandBlockEntity.isAutomatic()) {
/* 227 */         BaseCommandBlock baseCommandBlock = commandBlockEntity.getCommandBlock();
/* 228 */         if (commandBlockEntity.markConditionMet()) {
/* 229 */           if (baseCommandBlock.performCommand(paramServerLevel)) {
/* 230 */             paramServerLevel.updateNeighbourForOutputSignal((BlockPos)mutableBlockPos, block);
/*     */           } else {
/*     */             break;
/*     */           } 
/* 234 */         } else if (commandBlockEntity.isConditional()) {
/* 235 */           baseCommandBlock.setSuccessCount(0);
/*     */         } 
/*     */       } 
/*     */       
/* 239 */       paramDirection = (Direction)blockState.getValue((Property)FACING);
/*     */     } 
/* 241 */     if (i <= 0) {
/* 242 */       int j = Math.max(((Integer)gameRules.get(GameRules.MAX_COMMAND_SEQUENCE_LENGTH)).intValue(), 0);
/* 243 */       LOGGER.warn("Command Block chain tried to execute more than {} steps!", Integer.valueOf(j));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CommandBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */