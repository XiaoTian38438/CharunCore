/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.Const;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.minecraft.resources.Identifier;

public class NamespacedSchema
extends Schema {
    public static final PrimitiveCodec<String> NAMESPACED_STRING_CODEC = new PrimitiveCodec<String>(){

        @Override
        public <T> DataResult<String> read(DynamicOps<T> dynamicOps, T t) {
            return dynamicOps.getStringValue(t).map(NamespacedSchema::ensureNamespaced);
        }

        @Override
        public <T> T write(DynamicOps<T> dynamicOps, String string) {
            return dynamicOps.createString(string);
        }

        public String toString() {
            return "NamespacedString";
        }

        @Override
        public /* synthetic */ Object write(DynamicOps dynamicOps, Object object) {
            return this.write(dynamicOps, (String)object);
        }
    };
    private static final Type<String> NAMESPACED_STRING = new Const.PrimitiveType<String>(NAMESPACED_STRING_CODEC);

    public NamespacedSchema(int n, Schema schema) {
        super(n, schema);
    }

    public static String ensureNamespaced(String string) {
        Identifier identifier = Identifier.tryParse(string);
        if (identifier != null) {
            return identifier.toString();
        }
        return string;
    }

    public static Type<String> namespacedString() {
        return NAMESPACED_STRING;
    }

    @Override
    public Type<?> getChoiceType(DSL.TypeReference typeReference, String string) {
        return super.getChoiceType(typeReference, NamespacedSchema.ensureNamespaced(string));
    }
}

