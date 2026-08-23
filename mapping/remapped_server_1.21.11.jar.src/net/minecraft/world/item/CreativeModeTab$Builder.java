/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*     */   private static final CreativeModeTab.DisplayItemsGenerator EMPTY_GENERATOR = (paramItemDisplayParameters, paramOutput) -> {
/*     */     
/*     */     };
/*     */   private final CreativeModeTab.Row row;
/*     */   private final int column;
/* 149 */   private Component displayName = (Component)Component.empty();
/*     */   private Supplier<ItemStack> iconGenerator = () -> ItemStack.EMPTY;
/* 151 */   private CreativeModeTab.DisplayItemsGenerator displayItemsGenerator = EMPTY_GENERATOR;
/*     */   private boolean canScroll = true;
/*     */   private boolean showTitle = true;
/*     */   private boolean alignedRight = false;
/* 155 */   private CreativeModeTab.Type type = CreativeModeTab.Type.CATEGORY;
/* 156 */   private Identifier backgroundTexture = CreativeModeTab.DEFAULT_BACKGROUND;
/*     */   
/*     */   public Builder(CreativeModeTab.Row paramRow, int paramInt) {
/* 159 */     this.row = paramRow;
/* 160 */     this.column = paramInt;
/*     */   }
/*     */   
/*     */   public Builder title(Component paramComponent) {
/* 164 */     this.displayName = paramComponent;
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   public Builder icon(Supplier<ItemStack> paramSupplier) {
/* 169 */     this.iconGenerator = paramSupplier;
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public Builder displayItems(CreativeModeTab.DisplayItemsGenerator paramDisplayItemsGenerator) {
/* 174 */     this.displayItemsGenerator = paramDisplayItemsGenerator;
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public Builder alignedRight() {
/* 179 */     this.alignedRight = true;
/* 180 */     return this;
/*     */   }
/*     */   
/*     */   public Builder hideTitle() {
/* 184 */     this.showTitle = false;
/* 185 */     return this;
/*     */   }
/*     */   
/*     */   public Builder noScrollBar() {
/* 189 */     this.canScroll = false;
/* 190 */     return this;
/*     */   }
/*     */   
/*     */   protected Builder type(CreativeModeTab.Type paramType) {
/* 194 */     this.type = paramType;
/* 195 */     return this;
/*     */   }
/*     */   
/*     */   public Builder backgroundTexture(Identifier paramIdentifier) {
/* 199 */     this.backgroundTexture = paramIdentifier;
/* 200 */     return this;
/*     */   }
/*     */   
/*     */   public CreativeModeTab build() {
/* 204 */     if ((this.type == CreativeModeTab.Type.HOTBAR || this.type == CreativeModeTab.Type.INVENTORY) && this.displayItemsGenerator != EMPTY_GENERATOR) {
/* 205 */       throw new IllegalStateException("Special tabs can't have display items");
/*     */     }
/*     */     
/* 208 */     CreativeModeTab creativeModeTab = new CreativeModeTab(this.row, this.column, this.type, this.displayName, this.iconGenerator, this.displayItemsGenerator);
/* 209 */     creativeModeTab.alignedRight = this.alignedRight;
/* 210 */     creativeModeTab.showTitle = this.showTitle;
/* 211 */     creativeModeTab.canScroll = this.canScroll;
/* 212 */     creativeModeTab.backgroundTexture = this.backgroundTexture;
/* 213 */     return creativeModeTab;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\CreativeModeTab$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */