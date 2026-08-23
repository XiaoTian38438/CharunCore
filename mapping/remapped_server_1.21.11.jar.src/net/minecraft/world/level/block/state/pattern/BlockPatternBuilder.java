/*    */ package net.minecraft.world.level.block.state.pattern;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import it.unimi.dsi.fastutil.chars.CharOpenHashSet;
/*    */ import it.unimi.dsi.fastutil.chars.CharSet;
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import org.apache.commons.lang3.ArrayUtils;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ 
/*    */ public class BlockPatternBuilder
/*    */ {
/* 17 */   private final List<String[]> pattern = Lists.newArrayList();
/* 18 */   private final Map<Character, Predicate<BlockInWorld>> lookup = Maps.newHashMap();
/*    */   private int height;
/*    */   private int width;
/* 21 */   private final CharSet unknownCharacters = (CharSet)new CharOpenHashSet();
/*    */   
/*    */   private BlockPatternBuilder() {
/* 24 */     this.lookup.put(Character.valueOf(' '), paramBlockInWorld -> true);
/*    */   }
/*    */   
/*    */   public BlockPatternBuilder aisle(String... paramVarArgs) {
/* 28 */     if (ArrayUtils.isEmpty((Object[])paramVarArgs) || StringUtils.isEmpty(paramVarArgs[0])) {
/* 29 */       throw new IllegalArgumentException("Empty pattern for aisle");
/*    */     }
/*    */     
/* 32 */     if (this.pattern.isEmpty()) {
/* 33 */       this.height = paramVarArgs.length;
/* 34 */       this.width = paramVarArgs[0].length();
/*    */     } 
/*    */     
/* 37 */     if (paramVarArgs.length != this.height) {
/* 38 */       throw new IllegalArgumentException("Expected aisle with height of " + this.height + ", but was given one with a height of " + paramVarArgs.length + ")");
/*    */     }
/*    */     
/* 41 */     for (String str : paramVarArgs) {
/* 42 */       if (str.length() != this.width) {
/* 43 */         throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.width + ", found one with " + str.length() + ")");
/*    */       }
/* 45 */       for (char c : str.toCharArray()) {
/* 46 */         if (!this.lookup.containsKey(Character.valueOf(c))) {
/* 47 */           this.unknownCharacters.add(c);
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 52 */     this.pattern.add(paramVarArgs);
/*    */     
/* 54 */     return this;
/*    */   }
/*    */   
/*    */   public static BlockPatternBuilder start() {
/* 58 */     return new BlockPatternBuilder();
/*    */   }
/*    */   
/*    */   public BlockPatternBuilder where(char paramChar, Predicate<BlockInWorld> paramPredicate) {
/* 62 */     this.lookup.put(Character.valueOf(paramChar), paramPredicate);
/* 63 */     this.unknownCharacters.remove(paramChar);
/*    */     
/* 65 */     return this;
/*    */   }
/*    */   
/*    */   public BlockPattern build() {
/* 69 */     return new BlockPattern(createPattern());
/*    */   }
/*    */ 
/*    */   
/*    */   private Predicate<BlockInWorld>[][][] createPattern() {
/* 74 */     if (!this.unknownCharacters.isEmpty()) {
/* 75 */       throw new IllegalStateException("Predicates for character(s) " + String.valueOf(this.unknownCharacters) + " are missing");
/*    */     }
/*    */     
/* 78 */     Predicate[][][] arrayOfPredicate = (Predicate[][][])Array.newInstance(Predicate.class, new int[] { this.pattern.size(), this.height, this.width });
/*    */     
/* 80 */     for (byte b = 0; b < this.pattern.size(); b++) {
/* 81 */       for (byte b1 = 0; b1 < this.height; b1++) {
/* 82 */         for (byte b2 = 0; b2 < this.width; b2++) {
/* 83 */           arrayOfPredicate[b][b1][b2] = this.lookup.get(Character.valueOf(((String[])this.pattern.get(b))[b1].charAt(b2)));
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 88 */     return (Predicate<BlockInWorld>[][][])arrayOfPredicate;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\state\pattern\BlockPatternBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */