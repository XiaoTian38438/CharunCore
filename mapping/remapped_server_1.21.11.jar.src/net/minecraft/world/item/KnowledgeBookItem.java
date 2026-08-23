/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ import net.minecraft.world.item.crafting.RecipeManager;
/*    */ import net.minecraft.world.level.Level;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class KnowledgeBookItem
/*    */   extends Item {
/* 21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public KnowledgeBookItem(Item.Properties paramProperties) {
/* 24 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 29 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 30 */     List list = (List)itemStack.getOrDefault(DataComponents.RECIPES, List.of());
/*    */     
/* 32 */     itemStack.consume(1, (LivingEntity)paramPlayer);
/*    */     
/* 34 */     if (list.isEmpty()) {
/* 35 */       return (InteractionResult)InteractionResult.FAIL;
/*    */     }
/*    */     
/* 38 */     if (!paramLevel.isClientSide()) {
/* 39 */       RecipeManager recipeManager = paramLevel.getServer().getRecipeManager();
/* 40 */       ArrayList<RecipeHolder> arrayList = new ArrayList(list.size());
/*    */       
/* 42 */       for (ResourceKey resourceKey : list) {
/* 43 */         Optional<RecipeHolder> optional = recipeManager.byKey(resourceKey);
/* 44 */         if (optional.isPresent()) {
/* 45 */           arrayList.add(optional.get()); continue;
/*    */         } 
/* 47 */         LOGGER.error("Invalid recipe: {}", resourceKey);
/* 48 */         return (InteractionResult)InteractionResult.FAIL;
/*    */       } 
/*    */ 
/*    */       
/* 52 */       paramPlayer.awardRecipes(arrayList);
/* 53 */       paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/*    */     } 
/*    */     
/* 56 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\KnowledgeBookItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */