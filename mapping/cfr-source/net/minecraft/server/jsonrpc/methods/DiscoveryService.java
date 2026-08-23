/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.jsonrpc.methods;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.jsonrpc.IncomingRpcMethod;
import net.minecraft.server.jsonrpc.OutgoingRpcMethod;
import net.minecraft.server.jsonrpc.api.MethodInfo;
import net.minecraft.server.jsonrpc.api.Schema;
import net.minecraft.server.jsonrpc.api.SchemaComponent;

public class DiscoveryService {
    public static DiscoverResponse discover(List<SchemaComponent<?>> list) {
        ArrayList arrayList = new ArrayList(BuiltInRegistries.INCOMING_RPC_METHOD.size() + BuiltInRegistries.OUTGOING_RPC_METHOD.size());
        BuiltInRegistries.INCOMING_RPC_METHOD.listElements().forEach(reference -> {
            if (((IncomingRpcMethod)reference.value()).attributes().discoverable()) {
                arrayList.add(((IncomingRpcMethod)reference.value()).info().named(reference.key().identifier()));
            }
        });
        BuiltInRegistries.OUTGOING_RPC_METHOD.listElements().forEach(reference -> {
            if (((OutgoingRpcMethod)reference.value()).attributes().discoverable()) {
                arrayList.add(((OutgoingRpcMethod)reference.value()).info().named(reference.key().identifier()));
            }
        });
        HashMap hashMap = new HashMap();
        for (SchemaComponent<?> schemaComponent : list) {
            hashMap.put(schemaComponent.name(), schemaComponent.schema().info());
        }
        DiscoverInfo discoverInfo = new DiscoverInfo("Minecraft Server JSON-RPC", "2.0.0");
        return new DiscoverResponse("1.3.2", discoverInfo, arrayList, new DiscoverComponents(hashMap));
    }

    public record DiscoverInfo(String title, String version) {
        public static final MapCodec<DiscoverInfo> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.STRING.fieldOf("title")).forGetter(DiscoverInfo::title), ((MapCodec)Codec.STRING.fieldOf("version")).forGetter(DiscoverInfo::version)).apply((Applicative<DiscoverInfo, ?>)instance, DiscoverInfo::new));
    }

    public record DiscoverResponse(String jsonRpcProtocolVersion, DiscoverInfo discoverInfo, List<MethodInfo.Named<?, ?>> methods, DiscoverComponents components) {
        public static final MapCodec<DiscoverResponse> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.STRING.fieldOf("openrpc")).forGetter(DiscoverResponse::jsonRpcProtocolVersion), ((MapCodec)DiscoverInfo.CODEC.codec().fieldOf("info")).forGetter(DiscoverResponse::discoverInfo), ((MapCodec)Codec.list(MethodInfo.Named.CODEC).fieldOf("methods")).forGetter(DiscoverResponse::methods), ((MapCodec)DiscoverComponents.CODEC.codec().fieldOf("components")).forGetter(DiscoverResponse::components)).apply((Applicative<DiscoverResponse, ?>)instance, DiscoverResponse::new));
    }

    public record DiscoverComponents(Map<String, Schema<?>> schemas) {
        public static final MapCodec<DiscoverComponents> CODEC = DiscoverComponents.typedSchema();

        private static MapCodec<DiscoverComponents> typedSchema() {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.unboundedMap(Codec.STRING, Schema.CODEC).fieldOf("schemas")).forGetter(DiscoverComponents::schemas)).apply((Applicative<DiscoverComponents, ?>)instance, DiscoverComponents::new));
        }
    }
}

