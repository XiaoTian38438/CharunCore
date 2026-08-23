/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 */
package net.minecraft.util.datafix.fixes;

import com.google.gson.JsonElement;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JavaOps;
import com.mojang.serialization.JsonOps;
import java.lang.invoke.CallSite;
import java.util.Map;
import java.util.Optional;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.fixes.References;

public class LegacyHoverEventFix
extends DataFix {
    public LegacyHoverEventFix(Schema schema) {
        super(schema, false);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(References.TEXT_COMPONENT).findFieldType("hoverEvent");
        return this.createFixer(this.getInputSchema().getTypeRaw(References.TEXT_COMPONENT), type);
    }

    private <C, H extends Pair<String, ?>> TypeRewriteRule createFixer(Type<C> type, Type<H> type2) {
        Type<Pair<String, Either<Either<String, C>, Pair<Either<C, Unit>, Pair<Either<C, Unit>, Pair<Either<H, Unit>, Dynamic<?>>>>>>> type3 = DSL.named(References.TEXT_COMPONENT.typeName(), DSL.or(DSL.or(DSL.string(), DSL.list(type)), DSL.and(DSL.optional(DSL.field("extra", DSL.list(type))), DSL.optional(DSL.field("separator", type)), DSL.optional(DSL.field("hoverEvent", type2)), DSL.remainderType())));
        if (!type3.equals(this.getInputSchema().getType(References.TEXT_COMPONENT))) {
            throw new IllegalStateException("Text component type did not match, expected " + String.valueOf(type3) + " but got " + String.valueOf(this.getInputSchema().getType(References.TEXT_COMPONENT)));
        }
        return this.fixTypeEverywhere("LegacyHoverEventFix", type3, dynamicOps -> pair -> pair.mapSecond(either -> either.mapRight(pair -> pair.mapSecond(pair2 -> pair2.mapSecond(pair -> {
            Dynamic dynamic = (Dynamic)pair.getSecond();
            Optional optional = dynamic.get("hoverEvent").result();
            if (optional.isEmpty()) {
                return pair;
            }
            Optional optional2 = optional.get().get("value").result();
            if (optional2.isEmpty()) {
                return pair;
            }
            String string = ((Either)pair.getFirst()).left().map(Pair::getFirst).orElse("");
            Pair pair2 = (Pair)this.fixHoverEvent(type2, string, optional.get());
            return pair.mapFirst(either -> Either.left(pair2));
        })))));
    }

    private <H> H fixHoverEvent(Type<H> type, String string, Dynamic<?> dynamic) {
        if ("show_text".equals(string)) {
            return LegacyHoverEventFix.fixShowTextHover(type, dynamic);
        }
        return LegacyHoverEventFix.createPlaceholderHover(type, dynamic);
    }

    private static <H> H fixShowTextHover(Type<H> type, Dynamic<?> dynamic) {
        Dynamic<?> dynamic2 = dynamic.renameField("value", "contents");
        return Util.readTypedOrThrow(type, dynamic2).getValue();
    }

    private static <H> H createPlaceholderHover(Type<H> type, Dynamic<?> dynamic) {
        JsonElement jsonElement = dynamic.convert(JsonOps.INSTANCE).getValue();
        Dynamic<Map<String, Map<String, CallSite>>> dynamic2 = new Dynamic<Map<String, Map<String, CallSite>>>(JavaOps.INSTANCE, Map.of("action", "show_text", "contents", Map.of("text", "Legacy hoverEvent: " + GsonHelper.toStableString(jsonElement))));
        return Util.readTypedOrThrow(type, dynamic2).getValue();
    }
}

