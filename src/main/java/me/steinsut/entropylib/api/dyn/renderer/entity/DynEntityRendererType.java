/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.dyn.renderer.entity;

import com.mojang.serialization.Codec;
import me.steinsut.entropylib.api.dyn.data.DynDataType;
import me.steinsut.entropylib.api.dyn.renderer.BaseDynRendererType;
import me.steinsut.entropylib.api.renderer.entity.DynEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

import static me.steinsut.entropylib.EntropyLib.LOGGER;
import static me.steinsut.entropylib.api.registries.CommonRegistries.DYN_ENTITY_RENDERER_TYPE_REGISTRY;
import static me.steinsut.entropylib.api.registries.CommonRegistries.DYN_ENTITY_TYPE_REGISTRY_KEY;

public final class DynEntityRendererType<R extends EntityDynRenderer<D, S>, D, S extends DynEntityRenderState<S>> extends BaseDynRendererType<R, D, S> {
    public static final StreamCodec<RegistryFriendlyByteBuf, DynEntityRendererType<?, ?, ?>> STREAM_CODEC =
            ByteBufCodecs.registry(DYN_ENTITY_TYPE_REGISTRY_KEY);

    public static final Codec<DynEntityRendererType<?, ?, ?>> CODEC = DYN_ENTITY_RENDERER_TYPE_REGISTRY.byNameCodec();
    private final BiFunction<EntityRendererProvider.Context, DynDataType<D>, R> dynRendererFactory;
    private final Set<Holder<EntityType<?>>> compatibleEntities;
    private R rendererInstance;

    private DynEntityRendererType(DynDataType<D> dataType, BiFunction<EntityRendererProvider.Context, DynDataType<D>, R> dynRendererFactory, Set<Holder<EntityType<?>>> compatibleEntities) {
        super(dataType);

        this.compatibleEntities = compatibleEntities;
        this.dynRendererFactory = dynRendererFactory;
    }

    public boolean isCompatible(Holder<EntityType<?>> typeHolder) {
        return typeHolder.kind() == Holder.Kind.REFERENCE && (this.compatibleEntities.isEmpty() || this.compatibleEntities.contains(typeHolder));
    }

    public void instantiateDynRenderer(EntityRendererProvider.Context context) {
        this.rendererInstance = this.dynRendererFactory.apply(context, this.dataType);
    }

    public Optional<R> getDynRendererInstance() {
        return Optional.ofNullable(this.rendererInstance);
    }

    public static class Builder<_R extends EntityDynRenderer<_D, _S>, _D, _S extends DynEntityRenderState<_S>> {
        private final BiFunction<EntityRendererProvider.Context, DynDataType<_D>, _R> dynRendererFactory;
        private final DynDataType<_D> dataType;
        private Set<Holder<EntityType<?>>> compatibleEntities;

        private Builder(DynDataType<_D> dataType, BiFunction<EntityRendererProvider.Context, DynDataType<_D>, _R> dynRendererFactory) {
            this.dataType = dataType;
            this.dynRendererFactory = dynRendererFactory;
        }

        public static <_R extends EntityDynRenderer<_D, _S>, _D, _S extends DynEntityRenderState<_S>> Builder<_R, _D, _S>
        of(DynDataType<_D> dataType, BiFunction<EntityRendererProvider.Context, DynDataType<_D>, _R> dynRendererFactory) {
            return new Builder<>(dataType, dynRendererFactory);
        }

        public Builder<_R, _D, _S> supportsEntityType(Holder<EntityType<?>> type) {
            if (this.compatibleEntities != null && this.compatibleEntities.isEmpty()) {
                LOGGER.warn("DynRenderer already supports all entities");
            } else {
                if (this.compatibleEntities == null) {
                    this.compatibleEntities = new HashSet<>();
                }
                this.compatibleEntities.add(type);
            }

            return this;
        }

        public Builder<_R, _D, _S> supportsAllEntities() {
            if (this.compatibleEntities == null) {
                this.compatibleEntities = new HashSet<>();
            }

            this.compatibleEntities.clear();
            return this;
        }

        public DynEntityRendererType<_R, _D, _S> build() {
            return new DynEntityRendererType<>(this.dataType, this.dynRendererFactory, this.compatibleEntities);
        }
    }
}
