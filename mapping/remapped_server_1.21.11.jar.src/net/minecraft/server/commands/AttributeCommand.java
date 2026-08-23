/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.commands.arguments.ResourceArgument;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeMap;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ 
/*     */ public class AttributeCommand {
/*     */   private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY;
/*     */   private static final Dynamic2CommandExceptionType ERROR_NO_SUCH_ATTRIBUTE;
/*     */   
/*     */   static {
/*  37 */     ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.attribute.failed.entity", new Object[] { paramObject }));
/*  38 */     ERROR_NO_SUCH_ATTRIBUTE = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> Component.translatableEscape("commands.attribute.failed.no_attribute", new Object[] { paramObject1, paramObject2 }));
/*  39 */     ERROR_NO_SUCH_MODIFIER = new Dynamic3CommandExceptionType((paramObject1, paramObject2, paramObject3) -> Component.translatableEscape("commands.attribute.failed.no_modifier", new Object[] { paramObject2, paramObject1, paramObject3 }));
/*  40 */     ERROR_MODIFIER_ALREADY_PRESENT = new Dynamic3CommandExceptionType((paramObject1, paramObject2, paramObject3) -> Component.translatableEscape("commands.attribute.failed.modifier_already_present", new Object[] { paramObject3, paramObject2, paramObject1 }));
/*     */   } private static final Dynamic3CommandExceptionType ERROR_NO_SUCH_MODIFIER; private static final Dynamic3CommandExceptionType ERROR_MODIFIER_ALREADY_PRESENT;
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/*  43 */     paramCommandDispatcher.register(
/*  44 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("attribute")
/*  45 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  46 */         .then(
/*  47 */           Commands.argument("target", (ArgumentType)EntityArgument.entity())
/*  48 */           .then((
/*  49 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("attribute", (ArgumentType)ResourceArgument.resource(paramCommandBuildContext, Registries.ATTRIBUTE))
/*  50 */             .then((
/*  51 */               (LiteralArgumentBuilder)Commands.literal("get")
/*  52 */               .executes(paramCommandContext -> getAttributeValue((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), (Holder<Attribute>)ResourceArgument.getAttribute(paramCommandContext, "attribute"), 1.0D)))
/*  53 */               .then(
/*  54 */                 Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg())
/*  55 */                 .executes(paramCommandContext -> getAttributeValue((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), (Holder<Attribute>)ResourceArgument.getAttribute(paramCommandContext, "attribute"), DoubleArgumentType.getDouble(paramCommandContext, "scale"))))))
/*     */ 
/*     */             
/*  58 */             .then((
/*  59 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("base")
/*  60 */               .then(
/*  61 */                 Commands.literal("set")
/*  62 */                 .then(
/*  63 */                   Commands.argument("value", (ArgumentType)DoubleArgumentType.doubleArg())
/*  64 */                   .executes(paramCommandContext -> setAttributeBase((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), (Holder<Attribute>)ResourceArgument.getAttribute(paramCommandContext, "attribute"), DoubleArgumentType.getDouble(paramCommandContext, "value"))))))
/*     */ 
/*     */               
/*  67 */               .then((
/*  68 */                 (LiteralArgumentBuilder)Commands.literal("get")
/*  69 */                 .executes(paramCommandContext -> getAttributeBase((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), (Holder<Attribute>)ResourceArgument.getAttribute(paramCommandContext, "attribute"), 1.0D)))
/*  70 */                 .then(
/*  71 */                   Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg())
/*  72 */                   .executes(paramCommandContext -> getAttributeBase((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), (Holder<Attribute>)ResourceArgument.getAttribute(paramCommandContext, "attribute"), DoubleArgumentType.getDouble(paramCommandContext, "scale"))))))
/*     */ 
/*     */               
/*  75 */               .then(
/*  76 */                 Commands.literal("reset")
/*  77 */                 .executes(paramCommandContext -> resetAttributeBase((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), (Holder<Attribute>)ResourceArgument.getAttribute(paramCommandContext, "attribute"))))))
/*     */ 
/*     */             
/*  80 */             .then((
/*  81 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("modifier")
/*  82 */               .then(
/*  83 */                 Commands.literal("add")
/*  84 */                 .then(
/*  85 */                   Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/*  86 */                   .then((
/*  87 */                     (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("value", (ArgumentType)DoubleArgumentType.doubleArg())
/*  88 */                     .then(
/*  89 */                       Commands.literal("add_value")
/*  90 */                       .executes(paramCommandContext -> addModifier((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), (Holder<Attribute>)ResourceArgument.getAttribute(paramCommandContext, "attribute"), IdentifierArgument.getId(paramCommandContext, "id"), DoubleArgumentType.getDouble(paramCommandContext, "value"), AttributeModifier.Operation.ADD_VALUE))))
/*     */                     
/*  92 */                     .then(
/*  93 */                       Commands.literal("add_multiplied_base")
/*  94 */                       .executes(paramCommandContext -> addModifier((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), (Holder<Attribute>)ResourceArgument.getAttribute(paramCommandContext, "attribute"), IdentifierArgument.getId(paramCommandContext, "id"), DoubleArgumentType.getDouble(paramCommandContext, "value"), AttributeModifier.Operation.ADD_MULTIPLIED_BASE))))
/*     */                     
/*  96 */                     .then(
/*  97 */                       Commands.literal("add_multiplied_total")
/*  98 */                       .executes(paramCommandContext -> addModifier((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), (Holder<Attribute>)ResourceArgument.getAttribute(paramCommandContext, "attribute"), IdentifierArgument.getId(paramCommandContext, "id"), DoubleArgumentType.getDouble(paramCommandContext, "value"), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 103 */               .then(
/* 104 */                 Commands.literal("remove")
/* 105 */                 .then(Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/* 106 */                   .suggests((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggestResource(getAttributeModifiers(EntityArgument.getEntity(paramCommandContext, "target"), (Holder<Attribute>)ResourceArgument.getAttribute(paramCommandContext, "attribute")), paramSuggestionsBuilder))
/* 107 */                   .executes(paramCommandContext -> removeModifier((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), (Holder<Attribute>)ResourceArgument.getAttribute(paramCommandContext, "attribute"), IdentifierArgument.getId(paramCommandContext, "id"))))))
/*     */ 
/*     */               
/* 110 */               .then(
/* 111 */                 Commands.literal("value")
/* 112 */                 .then(
/* 113 */                   Commands.literal("get")
/* 114 */                   .then((
/* 115 */                     (RequiredArgumentBuilder)Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/* 116 */                     .suggests((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggestResource(getAttributeModifiers(EntityArgument.getEntity(paramCommandContext, "target"), (Holder<Attribute>)ResourceArgument.getAttribute(paramCommandContext, "attribute")), paramSuggestionsBuilder))
/* 117 */                     .executes(paramCommandContext -> getAttributeModifier((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), (Holder<Attribute>)ResourceArgument.getAttribute(paramCommandContext, "attribute"), IdentifierArgument.getId(paramCommandContext, "id"), 1.0D)))
/* 118 */                     .then(
/* 119 */                       Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg())
/* 120 */                       .executes(paramCommandContext -> getAttributeModifier((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), (Holder<Attribute>)ResourceArgument.getAttribute(paramCommandContext, "attribute"), IdentifierArgument.getId(paramCommandContext, "id"), DoubleArgumentType.getDouble(paramCommandContext, "scale")))))))))));
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
/*     */   private static AttributeInstance getAttributeInstance(Entity paramEntity, Holder<Attribute> paramHolder) throws CommandSyntaxException {
/* 132 */     AttributeInstance attributeInstance = getLivingEntity(paramEntity).getAttributes().getInstance(paramHolder);
/* 133 */     if (attributeInstance == null) {
/* 134 */       throw ERROR_NO_SUCH_ATTRIBUTE.create(paramEntity.getName(), getAttributeDescription(paramHolder));
/*     */     }
/* 136 */     return attributeInstance;
/*     */   }
/*     */   
/*     */   private static LivingEntity getLivingEntity(Entity paramEntity) throws CommandSyntaxException {
/* 140 */     if (!(paramEntity instanceof LivingEntity)) {
/* 141 */       throw ERROR_NOT_LIVING_ENTITY.create(paramEntity.getName());
/*     */     }
/* 143 */     return (LivingEntity)paramEntity;
/*     */   }
/*     */   
/*     */   private static LivingEntity getEntityWithAttribute(Entity paramEntity, Holder<Attribute> paramHolder) throws CommandSyntaxException {
/* 147 */     LivingEntity livingEntity = getLivingEntity(paramEntity);
/* 148 */     if (!livingEntity.getAttributes().hasAttribute(paramHolder)) {
/* 149 */       throw ERROR_NO_SUCH_ATTRIBUTE.create(paramEntity.getName(), getAttributeDescription(paramHolder));
/*     */     }
/* 151 */     return livingEntity;
/*     */   }
/*     */   
/*     */   private static int getAttributeValue(CommandSourceStack paramCommandSourceStack, Entity paramEntity, Holder<Attribute> paramHolder, double paramDouble) throws CommandSyntaxException {
/* 155 */     LivingEntity livingEntity = getEntityWithAttribute(paramEntity, paramHolder);
/* 156 */     double d = livingEntity.getAttributeValue(paramHolder);
/* 157 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.attribute.value.get.success", new Object[] { getAttributeDescription(paramHolder), paramEntity.getName(), Double.valueOf(paramDouble) }), false);
/* 158 */     return (int)(d * paramDouble);
/*     */   }
/*     */   
/*     */   private static int getAttributeBase(CommandSourceStack paramCommandSourceStack, Entity paramEntity, Holder<Attribute> paramHolder, double paramDouble) throws CommandSyntaxException {
/* 162 */     LivingEntity livingEntity = getEntityWithAttribute(paramEntity, paramHolder);
/* 163 */     double d = livingEntity.getAttributeBaseValue(paramHolder);
/* 164 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.attribute.base_value.get.success", new Object[] { getAttributeDescription(paramHolder), paramEntity.getName(), Double.valueOf(paramDouble) }), false);
/* 165 */     return (int)(d * paramDouble);
/*     */   }
/*     */   
/*     */   private static int getAttributeModifier(CommandSourceStack paramCommandSourceStack, Entity paramEntity, Holder<Attribute> paramHolder, Identifier paramIdentifier, double paramDouble) throws CommandSyntaxException {
/* 169 */     LivingEntity livingEntity = getEntityWithAttribute(paramEntity, paramHolder);
/*     */     
/* 171 */     AttributeMap attributeMap = livingEntity.getAttributes();
/*     */     
/* 173 */     if (!attributeMap.hasModifier(paramHolder, paramIdentifier)) {
/* 174 */       throw ERROR_NO_SUCH_MODIFIER.create(paramEntity.getName(), getAttributeDescription(paramHolder), paramIdentifier);
/*     */     }
/*     */     
/* 177 */     double d = attributeMap.getModifierValue(paramHolder, paramIdentifier);
/* 178 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.attribute.modifier.value.get.success", new Object[] { Component.translationArg(paramIdentifier), getAttributeDescription(paramHolder), paramEntity.getName(), Double.valueOf(paramDouble) }), false);
/* 179 */     return (int)(d * paramDouble);
/*     */   }
/*     */   
/*     */   private static Stream<Identifier> getAttributeModifiers(Entity paramEntity, Holder<Attribute> paramHolder) throws CommandSyntaxException {
/* 183 */     AttributeInstance attributeInstance = getAttributeInstance(paramEntity, paramHolder);
/* 184 */     return attributeInstance.getModifiers().stream().map(AttributeModifier::id);
/*     */   }
/*     */   
/*     */   private static int setAttributeBase(CommandSourceStack paramCommandSourceStack, Entity paramEntity, Holder<Attribute> paramHolder, double paramDouble) throws CommandSyntaxException {
/* 188 */     getAttributeInstance(paramEntity, paramHolder).setBaseValue(paramDouble);
/* 189 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.attribute.base_value.set.success", new Object[] { getAttributeDescription(paramHolder), paramEntity.getName(), Double.valueOf(paramDouble) }), false);
/* 190 */     return 1;
/*     */   }
/*     */   
/*     */   private static int resetAttributeBase(CommandSourceStack paramCommandSourceStack, Entity paramEntity, Holder<Attribute> paramHolder) throws CommandSyntaxException {
/* 194 */     LivingEntity livingEntity = getLivingEntity(paramEntity);
/* 195 */     if (!livingEntity.getAttributes().resetBaseValue(paramHolder)) {
/* 196 */       throw ERROR_NO_SUCH_ATTRIBUTE.create(paramEntity.getName(), getAttributeDescription(paramHolder));
/*     */     }
/* 198 */     double d = livingEntity.getAttributeBaseValue(paramHolder);
/* 199 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.attribute.base_value.reset.success", new Object[] { getAttributeDescription(paramHolder), paramEntity.getName(), Double.valueOf(paramDouble) }), false);
/* 200 */     return 1;
/*     */   }
/*     */   
/*     */   private static int addModifier(CommandSourceStack paramCommandSourceStack, Entity paramEntity, Holder<Attribute> paramHolder, Identifier paramIdentifier, double paramDouble, AttributeModifier.Operation paramOperation) throws CommandSyntaxException {
/* 204 */     AttributeInstance attributeInstance = getAttributeInstance(paramEntity, paramHolder);
/* 205 */     AttributeModifier attributeModifier = new AttributeModifier(paramIdentifier, paramDouble, paramOperation);
/* 206 */     if (attributeInstance.hasModifier(paramIdentifier)) {
/* 207 */       throw ERROR_MODIFIER_ALREADY_PRESENT.create(paramEntity.getName(), getAttributeDescription(paramHolder), paramIdentifier);
/*     */     }
/* 209 */     attributeInstance.addPermanentModifier(attributeModifier);
/* 210 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.attribute.modifier.add.success", new Object[] { Component.translationArg(paramIdentifier), getAttributeDescription(paramHolder), paramEntity.getName() }), false);
/* 211 */     return 1;
/*     */   }
/*     */   
/*     */   private static int removeModifier(CommandSourceStack paramCommandSourceStack, Entity paramEntity, Holder<Attribute> paramHolder, Identifier paramIdentifier) throws CommandSyntaxException {
/* 215 */     AttributeInstance attributeInstance = getAttributeInstance(paramEntity, paramHolder);
/* 216 */     if (attributeInstance.removeModifier(paramIdentifier)) {
/* 217 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.attribute.modifier.remove.success", new Object[] { Component.translationArg(paramIdentifier), getAttributeDescription(paramHolder), paramEntity.getName() }), false);
/* 218 */       return 1;
/*     */     } 
/* 220 */     throw ERROR_NO_SUCH_MODIFIER.create(paramEntity.getName(), getAttributeDescription(paramHolder), paramIdentifier);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Component getAttributeDescription(Holder<Attribute> paramHolder) {
/* 225 */     return (Component)Component.translatable(((Attribute)paramHolder.value()).getDescriptionId());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\AttributeCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */