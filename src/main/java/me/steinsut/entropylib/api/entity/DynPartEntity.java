package me.steinsut.entropylib.api.entity;

import me.steinsut.entropylib.api.dyn.data.DynDataWriter;
import me.steinsut.entropylib.api.dyn.entity.DynEntityHelper;
import me.steinsut.entropylib.api.dyn.entity.IDynEntity;
import me.steinsut.entropylib.api.dyn.entity.sync.DynEntitySyncConfigReader;
import me.steinsut.entropylib.api.dyn.entity.sync.DynEntitySyncPolicy;
import me.steinsut.entropylib.api.dyn.renderer.entity.DynEntityRendererType;
import me.steinsut.entropylib.api.renderer.entity.DynEntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.entity.PartEntity;
import org.jspecify.annotations.NonNull;

public abstract class DynPartEntity<T extends Entity, S extends DynEntityRenderState<S>> extends PartEntity<T> implements IDynEntity<S> {
    protected final DynEntityHelper<S> helper;

    public DynPartEntity(T parent, DynEntityRendererType<?, ?, S> dynRendererType, DynEntitySyncPolicy dynSyncPolicy) {
        super(parent);

        this.helper = new DynEntityHelper<>(this, dynRendererType, dynSyncPolicy);

        this.setDynRendererType(dynRendererType);
        this.setDynSyncPolicy(dynSyncPolicy);
    }

    @Override
    public DynEntityRendererType<?, ?, S> getDynType() {
        return this.helper.getDynType();
    }

    @Override
    public void setDynRendererType(DynEntityRendererType<?, ?, ?> type) {
        this.helper.setDynRendererType(type);
    }

    @Override
    public DynEntitySyncPolicy getDynSyncPolicy() {
        return this.helper.getDynSyncPolicy();
    }

    @Override
    public void setDynSyncPolicy(DynEntitySyncPolicy policy) {
        this.helper.setDynSyncPolicy(policy);
    }

    @Override
    public DynDataWriter<?> getDynDataWriter() {
        return this.helper.getDynDataWriter();
    }

    @Override
    public DynEntitySyncConfigReader getDynSyncConfigurator() {
        return this.helper.getDynSyncConfigurator();
    }

    @Override
    public void readDataFrom(DynDataWriter<?> writer, boolean forceSync) {
        this.helper.readDataFrom(writer, forceSync);
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        this.helper.readAdditionalSaveData(input);
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        this.helper.addAdditionalSaveData(output);
    }

    @Override
    public void tick() {
        super.tick();

        this.helper.tick();
    }
}
