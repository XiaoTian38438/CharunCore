/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.Dynamic4CommandExceptionType;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec2Argument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ 
/*     */ public class SpreadPlayersCommand {
/*     */   private static final int MAX_ITERATION_COUNT = 10000;
/*     */   private static final Dynamic4CommandExceptionType ERROR_FAILED_TO_SPREAD_TEAMS;
/*     */   
/*     */   static {
/*  45 */     ERROR_FAILED_TO_SPREAD_TEAMS = new Dynamic4CommandExceptionType((paramObject1, paramObject2, paramObject3, paramObject4) -> Component.translatableEscape("commands.spreadplayers.failed.teams", new Object[] { paramObject1, paramObject2, paramObject3, paramObject4 }));
/*  46 */     ERROR_FAILED_TO_SPREAD_ENTITIES = new Dynamic4CommandExceptionType((paramObject1, paramObject2, paramObject3, paramObject4) -> Component.translatableEscape("commands.spreadplayers.failed.entities", new Object[] { paramObject1, paramObject2, paramObject3, paramObject4 }));
/*  47 */     ERROR_INVALID_MAX_HEIGHT = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> Component.translatableEscape("commands.spreadplayers.failed.invalid.height", new Object[] { paramObject1, paramObject2 }));
/*     */   } private static final Dynamic4CommandExceptionType ERROR_FAILED_TO_SPREAD_ENTITIES; private static final Dynamic2CommandExceptionType ERROR_INVALID_MAX_HEIGHT;
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  50 */     paramCommandDispatcher.register(
/*  51 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spreadplayers")
/*  52 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  53 */         .then(
/*  54 */           Commands.argument("center", (ArgumentType)Vec2Argument.vec2())
/*  55 */           .then(
/*  56 */             Commands.argument("spreadDistance", (ArgumentType)FloatArgumentType.floatArg(0.0F))
/*  57 */             .then((
/*  58 */               (RequiredArgumentBuilder)Commands.argument("maxRange", (ArgumentType)FloatArgumentType.floatArg(1.0F))
/*  59 */               .then(
/*  60 */                 Commands.argument("respectTeams", (ArgumentType)BoolArgumentType.bool())
/*  61 */                 .then(
/*  62 */                   Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  63 */                   .executes(paramCommandContext -> spreadPlayers((CommandSourceStack)paramCommandContext.getSource(), Vec2Argument.getVec2(paramCommandContext, "center"), FloatArgumentType.getFloat(paramCommandContext, "spreadDistance"), FloatArgumentType.getFloat(paramCommandContext, "maxRange"), ((CommandSourceStack)paramCommandContext.getSource()).getLevel().getMaxY() + 1, BoolArgumentType.getBool(paramCommandContext, "respectTeams"), EntityArgument.getEntities(paramCommandContext, "targets"))))))
/*     */ 
/*     */               
/*  66 */               .then(
/*  67 */                 Commands.literal("under")
/*  68 */                 .then(
/*  69 */                   Commands.argument("maxHeight", (ArgumentType)IntegerArgumentType.integer())
/*  70 */                   .then(
/*  71 */                     Commands.argument("respectTeams", (ArgumentType)BoolArgumentType.bool())
/*  72 */                     .then(
/*  73 */                       Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  74 */                       .executes(paramCommandContext -> spreadPlayers((CommandSourceStack)paramCommandContext.getSource(), Vec2Argument.getVec2(paramCommandContext, "center"), FloatArgumentType.getFloat(paramCommandContext, "spreadDistance"), FloatArgumentType.getFloat(paramCommandContext, "maxRange"), IntegerArgumentType.getInteger(paramCommandContext, "maxHeight"), BoolArgumentType.getBool(paramCommandContext, "respectTeams"), EntityArgument.getEntities(paramCommandContext, "targets")))))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int spreadPlayers(CommandSourceStack paramCommandSourceStack, Vec2 paramVec2, float paramFloat1, float paramFloat2, int paramInt, boolean paramBoolean, Collection<? extends Entity> paramCollection) throws CommandSyntaxException {
/*  86 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/*  87 */     int i = serverLevel.getMinY();
/*  88 */     if (paramInt < i) {
/*  89 */       throw ERROR_INVALID_MAX_HEIGHT.create(Integer.valueOf(paramInt), Integer.valueOf(i));
/*     */     }
/*     */     
/*  92 */     RandomSource randomSource = RandomSource.create();
/*  93 */     double d1 = (paramVec2.x - paramFloat2);
/*  94 */     double d2 = (paramVec2.y - paramFloat2);
/*  95 */     double d3 = (paramVec2.x + paramFloat2);
/*  96 */     double d4 = (paramVec2.y + paramFloat2);
/*     */     
/*  98 */     Position[] arrayOfPosition = createInitialPositions(randomSource, paramBoolean ? getNumberOfTeams(paramCollection) : paramCollection.size(), d1, d2, d3, d4);
/*  99 */     spreadPositions(paramVec2, paramFloat1, serverLevel, randomSource, d1, d2, d3, d4, paramInt, arrayOfPosition, paramBoolean);
/* 100 */     double d5 = setPlayerPositions(paramCollection, serverLevel, arrayOfPosition, paramInt, paramBoolean);
/*     */     
/* 102 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.spreadplayers.success." + (paramBoolean ? "teams" : "entities"), new Object[] { Integer.valueOf(paramArrayOfPosition.length), Float.valueOf(paramVec2.x), Float.valueOf(paramVec2.y), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(paramDouble) }) }), true);
/* 103 */     return arrayOfPosition.length;
/*     */   }
/*     */   
/*     */   private static int getNumberOfTeams(Collection<? extends Entity> paramCollection) {
/* 107 */     HashSet<PlayerTeam> hashSet = Sets.newHashSet();
/*     */     
/* 109 */     for (Entity entity : paramCollection) {
/* 110 */       if (entity instanceof net.minecraft.world.entity.player.Player) {
/* 111 */         hashSet.add(entity.getTeam()); continue;
/*     */       } 
/* 113 */       hashSet.add(null);
/*     */     } 
/*     */ 
/*     */     
/* 117 */     return hashSet.size();
/*     */   }
/*     */   
/*     */   private static void spreadPositions(Vec2 paramVec2, double paramDouble1, ServerLevel paramServerLevel, RandomSource paramRandomSource, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, int paramInt, Position[] paramArrayOfPosition, boolean paramBoolean) throws CommandSyntaxException {
/* 121 */     boolean bool = true;
/*     */     
/* 123 */     double d = 3.4028234663852886E38D;
/*     */     byte b;
/* 125 */     for (b = 0; b < '✐' && bool; b++) {
/* 126 */       bool = false;
/* 127 */       d = 3.4028234663852886E38D;
/*     */       
/* 129 */       for (byte b1 = 0; b1 < paramArrayOfPosition.length; b1++) {
/* 130 */         Position position1 = paramArrayOfPosition[b1];
/* 131 */         byte b2 = 0;
/* 132 */         Position position2 = new Position();
/*     */         
/* 134 */         for (byte b3 = 0; b3 < paramArrayOfPosition.length; b3++) {
/* 135 */           if (b1 != b3) {
/*     */ 
/*     */             
/* 138 */             Position position = paramArrayOfPosition[b3];
/*     */             
/* 140 */             double d1 = position1.dist(position);
/* 141 */             d = Math.min(d1, d);
/* 142 */             if (d1 < paramDouble1) {
/* 143 */               b2++;
/* 144 */               position2.x += position.x - position1.x;
/* 145 */               position2.z += position.z - position1.z;
/*     */             } 
/*     */           } 
/*     */         } 
/* 149 */         if (b2 > 0) {
/* 150 */           position2.x /= b2;
/* 151 */           position2.z /= b2;
/* 152 */           double d1 = position2.getLength();
/*     */           
/* 154 */           if (d1 > 0.0D) {
/* 155 */             position2.normalize();
/*     */             
/* 157 */             position1.moveAway(position2);
/*     */           } else {
/* 159 */             position1.randomize(paramRandomSource, paramDouble2, paramDouble3, paramDouble4, paramDouble5);
/*     */           } 
/*     */           
/* 162 */           bool = true;
/*     */         } 
/*     */         
/* 165 */         if (position1.clamp(paramDouble2, paramDouble3, paramDouble4, paramDouble5)) {
/* 166 */           bool = true;
/*     */         }
/*     */       } 
/*     */       
/* 170 */       if (!bool) {
/* 171 */         for (Position position : paramArrayOfPosition) {
/* 172 */           if (!position.isSafe((BlockGetter)paramServerLevel, paramInt)) {
/* 173 */             position.randomize(paramRandomSource, paramDouble2, paramDouble3, paramDouble4, paramDouble5);
/* 174 */             bool = true;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 181 */     if (d == 3.4028234663852886E38D) {
/* 182 */       d = 0.0D;
/*     */     }
/*     */     
/* 185 */     if (b >= '✐') {
/* 186 */       if (paramBoolean) {
/* 187 */         throw ERROR_FAILED_TO_SPREAD_TEAMS.create(Integer.valueOf(paramArrayOfPosition.length), Float.valueOf(paramVec2.x), Float.valueOf(paramVec2.y), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(d) }));
/*     */       }
/* 189 */       throw ERROR_FAILED_TO_SPREAD_ENTITIES.create(Integer.valueOf(paramArrayOfPosition.length), Float.valueOf(paramVec2.x), Float.valueOf(paramVec2.y), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(d) }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static double setPlayerPositions(Collection<? extends Entity> paramCollection, ServerLevel paramServerLevel, Position[] paramArrayOfPosition, int paramInt, boolean paramBoolean) {
/* 195 */     double d = 0.0D;
/* 196 */     byte b = 0;
/* 197 */     HashMap<PlayerTeam, Position> hashMap = Maps.newHashMap();
/*     */     
/* 199 */     for (Entity entity : paramCollection) {
/*     */       Position position;
/*     */       
/* 202 */       if (paramBoolean) {
/* 203 */         PlayerTeam playerTeam = (entity instanceof net.minecraft.world.entity.player.Player) ? entity.getTeam() : null;
/*     */         
/* 205 */         if (!hashMap.containsKey(playerTeam)) {
/* 206 */           hashMap.put(playerTeam, paramArrayOfPosition[b++]);
/*     */         }
/*     */         
/* 209 */         position = hashMap.get(playerTeam);
/*     */       } else {
/* 211 */         position = paramArrayOfPosition[b++];
/*     */       } 
/*     */       
/* 214 */       entity.teleportTo(paramServerLevel, Mth.floor(position.x) + 0.5D, position.getSpawnY((BlockGetter)paramServerLevel, paramInt), Mth.floor(position.z) + 0.5D, Set.of(), entity.getYRot(), entity.getXRot(), true);
/*     */       
/* 216 */       double d1 = Double.MAX_VALUE;
/* 217 */       for (Position position1 : paramArrayOfPosition) {
/* 218 */         if (position != position1) {
/*     */ 
/*     */ 
/*     */           
/* 222 */           double d2 = position.dist(position1);
/* 223 */           d1 = Math.min(d2, d1);
/*     */         } 
/* 225 */       }  d += d1;
/*     */     } 
/* 227 */     if (paramCollection.size() < 2) {
/* 228 */       return 0.0D;
/*     */     }
/*     */     
/* 231 */     d /= paramCollection.size();
/* 232 */     return d;
/*     */   }
/*     */   
/*     */   private static Position[] createInitialPositions(RandomSource paramRandomSource, int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 236 */     Position[] arrayOfPosition = new Position[paramInt];
/*     */     
/* 238 */     for (byte b = 0; b < arrayOfPosition.length; b++) {
/* 239 */       Position position = new Position();
/* 240 */       position.randomize(paramRandomSource, paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/* 241 */       arrayOfPosition[b] = position;
/*     */     } 
/*     */     
/* 244 */     return arrayOfPosition;
/*     */   }
/*     */   
/*     */   private static class Position {
/*     */     double x;
/*     */     double z;
/*     */     
/*     */     double dist(Position param1Position) {
/* 252 */       double d1 = this.x - param1Position.x;
/* 253 */       double d2 = this.z - param1Position.z;
/*     */       
/* 255 */       return Math.sqrt(d1 * d1 + d2 * d2);
/*     */     }
/*     */     
/*     */     void normalize() {
/* 259 */       double d = getLength();
/* 260 */       this.x /= d;
/* 261 */       this.z /= d;
/*     */     }
/*     */     
/*     */     double getLength() {
/* 265 */       return Math.sqrt(this.x * this.x + this.z * this.z);
/*     */     }
/*     */     
/*     */     public void moveAway(Position param1Position) {
/* 269 */       this.x -= param1Position.x;
/* 270 */       this.z -= param1Position.z;
/*     */     }
/*     */     
/*     */     public boolean clamp(double param1Double1, double param1Double2, double param1Double3, double param1Double4) {
/* 274 */       boolean bool = false;
/*     */       
/* 276 */       if (this.x < param1Double1) {
/* 277 */         this.x = param1Double1;
/* 278 */         bool = true;
/* 279 */       } else if (this.x > param1Double3) {
/* 280 */         this.x = param1Double3;
/* 281 */         bool = true;
/*     */       } 
/*     */       
/* 284 */       if (this.z < param1Double2) {
/* 285 */         this.z = param1Double2;
/* 286 */         bool = true;
/* 287 */       } else if (this.z > param1Double4) {
/* 288 */         this.z = param1Double4;
/* 289 */         bool = true;
/*     */       } 
/*     */       
/* 292 */       return bool;
/*     */     }
/*     */     
/*     */     public int getSpawnY(BlockGetter param1BlockGetter, int param1Int) {
/* 296 */       BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(this.x, (param1Int + 1), this.z);
/* 297 */       boolean bool1 = param1BlockGetter.getBlockState((BlockPos)mutableBlockPos).isAir();
/* 298 */       mutableBlockPos.move(Direction.DOWN);
/* 299 */       boolean bool2 = param1BlockGetter.getBlockState((BlockPos)mutableBlockPos).isAir();
/* 300 */       while (mutableBlockPos.getY() > param1BlockGetter.getMinY()) {
/* 301 */         mutableBlockPos.move(Direction.DOWN);
/* 302 */         boolean bool = param1BlockGetter.getBlockState((BlockPos)mutableBlockPos).isAir();
/*     */         
/* 304 */         if (!bool && bool2 && bool1) {
/* 305 */           return mutableBlockPos.getY() + 1;
/*     */         }
/* 307 */         bool1 = bool2;
/* 308 */         bool2 = bool;
/*     */       } 
/*     */       
/* 311 */       return param1Int + 1;
/*     */     }
/*     */     
/*     */     public boolean isSafe(BlockGetter param1BlockGetter, int param1Int) {
/* 315 */       BlockPos blockPos = BlockPos.containing(this.x, (getSpawnY(param1BlockGetter, param1Int) - 1), this.z);
/* 316 */       BlockState blockState = param1BlockGetter.getBlockState(blockPos);
/* 317 */       return (blockPos.getY() < param1Int && !blockState.liquid() && !blockState.is(BlockTags.FIRE));
/*     */     }
/*     */     
/*     */     public void randomize(RandomSource param1RandomSource, double param1Double1, double param1Double2, double param1Double3, double param1Double4) {
/* 321 */       this.x = Mth.nextDouble(param1RandomSource, param1Double1, param1Double3);
/* 322 */       this.z = Mth.nextDouble(param1RandomSource, param1Double2, param1Double4);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\SpreadPlayersCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */