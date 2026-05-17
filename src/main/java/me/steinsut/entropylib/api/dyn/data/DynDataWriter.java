package me.steinsut.entropylib.api.dyn.data;

import me.steinsut.entropylib.api.entity.BaseDynRenderedEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.storage.ValueOutput;

import static me.steinsut.entropylib.EntropyLib.LOGGER;

public final class DynDataWriter<D> {
    private final DynDataType.Holder<D> holder;

    public DynDataWriter(DynDataType.Holder<D> holder) {
        this.holder = holder;
    }

    @SuppressWarnings("unchecked")
    public void writeToHolder(DynDataType.Holder<?> holder) {
        if (this.holder.getDataType() != holder.getDataType()) {
            ((DynDataType.Holder<D>) holder).setData(this.holder.getData());
        } else {
            LOGGER.warn("Cannot copy value to holder, holders belong to different data types");
        }
    }

    public void storeData(ValueOutput out) {
        ValueOutput dataChild = out.child(BaseDynRenderedEntity.VALUE_IO_DYN_DATA_KEY);
        dataChild.store(BaseDynRenderedEntity.VALUE_IO_DYN_DATA_KEY, this.holder.getCodec(), this.holder.getData());
    }

    public void encodeData(RegistryFriendlyByteBuf buf) {
        this.holder.getStreamCodec().encode(buf, this.holder.getData());
    }
}
