package me.steinsut.entropylib.api.dynrenderer.entity;

import me.steinsut.entropylib.api.renderer.entity.DynRenderedEntityRenderState;
import me.steinsut.entropylib.api.dynrenderer.BaseDynRendererType;
import me.steinsut.entropylib.api.dynrenderer.DynDataType;
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
import java.util.function.Function;

import static me.steinsut.entropylib.EntropyLib.LOGGER;
import static me.steinsut.entropylib.api.registries.Registries.DYN_RENDERER_TYPE_REGISTRY_KEY;

public final class EntityDynRendererType<R extends EntityDynRenderer<D, S>, D, S extends DynRenderedEntityRenderState<S>> extends BaseDynRendererType<R, D, S> {
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityDynRendererType<?, ?, ?>> STREAM_CODEC =
            ByteBufCodecs.registry(DYN_RENDERER_TYPE_REGISTRY_KEY).map(
                    (t) -> (EntityDynRendererType<?, ?, ?>) t,
                    Function.identity());


    private R rendererInstance;
    private final BiFunction<EntityRendererProvider.Context, DynDataType<D>, R> dynRendererFactory;
    private final Set<Holder<EntityType<?>>> compatibleEntities;

    public EntityDynRendererType(DynDataType<D> dataType, BiFunction<EntityRendererProvider.Context, DynDataType<D>, R> dynRendererFactory) {
        this(dataType, dynRendererFactory, new HashSet<>());
    }

    public EntityDynRendererType(DynDataType<D> dataType, BiFunction<EntityRendererProvider.Context, DynDataType<D>, R> dynRendererFactory, Set<Holder<EntityType<?>>> compatibleEntities) {
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

    public static class Builder<_R extends EntityDynRenderer<_D, _S>, _D, _S extends DynRenderedEntityRenderState<_S>> {
        private final BiFunction<EntityRendererProvider.Context, DynDataType<_D>, _R> dynRendererFactory;
        private final DynDataType<_D> dataType;
        private Set<Holder<EntityType<?>>> compatibleEntities;

        private Builder(DynDataType<_D> dataType, BiFunction<EntityRendererProvider.Context, DynDataType<_D>, _R> dynRendererFactory) {
            this.dataType = dataType;
            this.dynRendererFactory = dynRendererFactory;
        }

        public static <_R extends EntityDynRenderer<_D, _S>, _D, _S extends DynRenderedEntityRenderState<_S>> Builder<_R, _D, _S>
            of(DynDataType<_D> dataType, BiFunction<EntityRendererProvider.Context, DynDataType<_D>, _R> dynRendererFactory) {
            return new Builder<>(dataType, dynRendererFactory);
        }
        
        public Builder<_R, _D, _S> supportsEntityType(Holder<EntityType<?>> type) {
            if (this.compatibleEntities != null && this.compatibleEntities.isEmpty()) {
                LOGGER.warn("DynRenderer already supports all entities");
            }
            else {
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

        public EntityDynRendererType<_R, _D, _S> build() {
            return new EntityDynRendererType<>(this.dataType, this.dynRendererFactory, this.compatibleEntities);
        }
    }
}
