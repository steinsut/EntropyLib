package me.steinsut.entropylib.api.renderer.dyn.entity;

import me.steinsut.entropylib.api.renderer.dyn.BaseDynRenderer;
import me.steinsut.entropylib.api.renderer.dyn.BaseDynRendererType;
import me.steinsut.entropylib.api.renderer.dyn.DynRendererDataType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;

import java.util.Optional;
import java.util.function.BiFunction;

public final class EntityDynRendererType<R extends EntityDynRenderer<D, S>, D, S extends EntityRenderState> extends BaseDynRendererType<R, D, S> {
    private R rendererInstance;
    private final HolderSet<EntityType<?>> compatibleEntities;
    private final BiFunction<EntityRendererProvider.Context, DynRendererDataType<D, ?>, R> dynRendererFactory;

    public EntityDynRendererType(DynRendererDataType<D, ?> dataType, HolderSet<EntityType<?>> compatibleEntities, BiFunction<EntityRendererProvider.Context, DynRendererDataType<D, ?>, R> dynRendererFactory) {
        super(dataType);

        this.compatibleEntities = compatibleEntities;
        this.dynRendererFactory = dynRendererFactory;
    }

    public boolean isCompatible(Holder<EntityType<?>> entityType) {
        return this.compatibleEntities.contains(entityType);
    }

    public void instantiateDynRenderer(EntityRendererProvider.Context context) {
        this.rendererInstance = this.dynRendererFactory.apply(context, this.dataType);
    }

    public Optional<R> getDynRendererInstance() {
        return Optional.ofNullable(this.rendererInstance);
    }
}
