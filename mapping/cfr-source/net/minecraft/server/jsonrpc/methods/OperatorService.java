/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.jsonrpc.methods;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.server.jsonrpc.api.PlayerDto;
import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.ServerOpListEntry;
import net.minecraft.util.Util;

public class OperatorService {
    public static List<OperatorDto> get(MinecraftApi minecraftApi) {
        return minecraftApi.operatorListService().getEntries().stream().filter(serverOpListEntry -> serverOpListEntry.getUser() != null).map(OperatorDto::from).toList();
    }

    public static List<OperatorDto> clear(MinecraftApi minecraftApi, ClientInfo clientInfo) {
        minecraftApi.operatorListService().clear(clientInfo);
        return OperatorService.get(minecraftApi);
    }

    public static List<OperatorDto> remove(MinecraftApi minecraftApi, List<PlayerDto> list, ClientInfo clientInfo) {
        List<CompletableFuture> list2 = list.stream().map(playerDto -> minecraftApi.playerListService().getUser(playerDto.id(), playerDto.name())).toList();
        for (Optional optional : Util.sequence(list2).join()) {
            optional.ifPresent(nameAndId -> minecraftApi.operatorListService().deop((NameAndId)nameAndId, clientInfo));
        }
        return OperatorService.get(minecraftApi);
    }

    public static List<OperatorDto> add(MinecraftApi minecraftApi, List<OperatorDto> list, ClientInfo clientInfo) {
        List<CompletableFuture> list2 = list.stream().map(operatorDto -> minecraftApi.playerListService().getUser(operatorDto.player().id(), operatorDto.player().name()).thenApply(optional -> optional.map(nameAndId -> new Op((NameAndId)nameAndId, operatorDto.permissionLevel(), operatorDto.bypassesPlayerLimit())))).toList();
        for (Optional optional : Util.sequence(list2).join()) {
            optional.ifPresent(op -> minecraftApi.operatorListService().op(op.user(), op.permissionLevel(), op.bypassesPlayerLimit(), clientInfo));
        }
        return OperatorService.get(minecraftApi);
    }

    public static List<OperatorDto> set(MinecraftApi minecraftApi, List<OperatorDto> list, ClientInfo clientInfo) {
        List<CompletableFuture> list2 = list.stream().map(operatorDto -> minecraftApi.playerListService().getUser(operatorDto.player().id(), operatorDto.player().name()).thenApply(optional -> optional.map(nameAndId -> new Op((NameAndId)nameAndId, operatorDto.permissionLevel(), operatorDto.bypassesPlayerLimit())))).toList();
        Set set = Util.sequence(list2).join().stream().flatMap(Optional::stream).collect(Collectors.toSet());
        Set set2 = minecraftApi.operatorListService().getEntries().stream().filter(serverOpListEntry -> serverOpListEntry.getUser() != null).map(serverOpListEntry -> new Op((NameAndId)serverOpListEntry.getUser(), Optional.of(serverOpListEntry.permissions().level()), Optional.of(serverOpListEntry.getBypassesPlayerLimit()))).collect(Collectors.toSet());
        set2.stream().filter(op -> !set.contains(op)).forEach(op -> minecraftApi.operatorListService().deop(op.user(), clientInfo));
        set.stream().filter(op -> !set2.contains(op)).forEach(op -> minecraftApi.operatorListService().op(op.user(), op.permissionLevel(), op.bypassesPlayerLimit(), clientInfo));
        return OperatorService.get(minecraftApi);
    }

    record Op(NameAndId user, Optional<PermissionLevel> permissionLevel, Optional<Boolean> bypassesPlayerLimit) {
    }

    public record OperatorDto(PlayerDto player, Optional<PermissionLevel> permissionLevel, Optional<Boolean> bypassesPlayerLimit) {
        public static final MapCodec<OperatorDto> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)PlayerDto.CODEC.codec().fieldOf("player")).forGetter(OperatorDto::player), PermissionLevel.INT_CODEC.optionalFieldOf("permissionLevel").forGetter(OperatorDto::permissionLevel), Codec.BOOL.optionalFieldOf("bypassesPlayerLimit").forGetter(OperatorDto::bypassesPlayerLimit)).apply((Applicative<OperatorDto, ?>)instance, OperatorDto::new));

        public static OperatorDto from(ServerOpListEntry serverOpListEntry) {
            return new OperatorDto(PlayerDto.from(Objects.requireNonNull((NameAndId)serverOpListEntry.getUser())), Optional.of(serverOpListEntry.permissions().level()), Optional.of(serverOpListEntry.getBypassesPlayerLimit()));
        }
    }
}

