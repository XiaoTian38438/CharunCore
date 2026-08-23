/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ public class PlacementInfo
/*    */ {
/*    */   public static final int EMPTY_SLOT = -1;
/* 13 */   public static final PlacementInfo NOT_PLACEABLE = new PlacementInfo(
/* 14 */       List.of(), 
/* 15 */       IntList.of());
/*    */   
/*    */   private final List<Ingredient> ingredients;
/*    */   
/*    */   private final IntList slotsToIngredientIndex;
/*    */   
/*    */   private PlacementInfo(List<Ingredient> paramList, IntList paramIntList) {
/* 22 */     this.ingredients = paramList;
/* 23 */     this.slotsToIngredientIndex = paramIntList;
/*    */   }
/*    */   
/*    */   public static PlacementInfo create(Ingredient paramIngredient) {
/* 27 */     if (paramIngredient.isEmpty()) {
/* 28 */       return NOT_PLACEABLE;
/*    */     }
/* 30 */     return new PlacementInfo(List.of(paramIngredient), IntList.of(0));
/*    */   }
/*    */   
/*    */   public static PlacementInfo createFromOptionals(List<Optional<Ingredient>> paramList) {
/* 34 */     int i = paramList.size();
/* 35 */     ArrayList<Ingredient> arrayList = new ArrayList(i);
/* 36 */     IntArrayList intArrayList = new IntArrayList(i);
/*    */     
/* 38 */     byte b = 0;
/* 39 */     for (Optional<Ingredient> optional : paramList) {
/* 40 */       if (optional.isPresent()) {
/* 41 */         Ingredient ingredient = optional.get();
/* 42 */         if (ingredient.isEmpty()) {
/* 43 */           return NOT_PLACEABLE;
/*    */         }
/* 45 */         arrayList.add(ingredient);
/* 46 */         intArrayList.add(b++); continue;
/*    */       } 
/* 48 */       intArrayList.add(-1);
/*    */     } 
/*    */ 
/*    */     
/* 52 */     return new PlacementInfo(arrayList, (IntList)intArrayList);
/*    */   }
/*    */   
/*    */   public static PlacementInfo create(List<Ingredient> paramList) {
/* 56 */     int i = paramList.size();
/* 57 */     IntArrayList intArrayList = new IntArrayList(i);
/*    */     
/* 59 */     for (byte b = 0; b < i; b++) {
/* 60 */       Ingredient ingredient = paramList.get(b);
/* 61 */       if (ingredient.isEmpty()) {
/* 62 */         return NOT_PLACEABLE;
/*    */       }
/* 64 */       intArrayList.add(b);
/*    */     } 
/*    */     
/* 67 */     return new PlacementInfo(paramList, (IntList)intArrayList);
/*    */   }
/*    */   
/*    */   public IntList slotsToIngredientIndex() {
/* 71 */     return this.slotsToIngredientIndex;
/*    */   }
/*    */   
/*    */   public List<Ingredient> ingredients() {
/* 75 */     return this.ingredients;
/*    */   }
/*    */   
/*    */   public boolean isImpossibleToPlace() {
/* 79 */     return this.slotsToIngredientIndex.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\PlacementInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */