package me.steinsut.entropylib.api.dyn.data;

import me.steinsut.entropylib.api.entity.BaseDynEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.storage.ValueInput;

import static me.steinsut.entropylib.EntropyLib.LOGGER;

public final class DynDataReader<D> {
    private final DynDataType.Holder<D> holder;

    public DynDataReader(DynDataType.Holder<D> holder) {
        this.holder = holder;
    }

    @SuppressWarnings("unchecked")
    public void readFromHolder(DynDataType.Holder<?> holder) {
        if (this.holder.getDataType() == holder.getDataType()) {
            this.holder.setData(((DynDataType.Holder<D>) holder).getData());
        } else {
            LOGGER.warn("Cannot copy from holder, holders belong to different data types");
        }
    }

    public void readData(ValueInput in) {
        in.read(BaseDynEntity.VALUE_IO_DYN_DATA_KEY, this.holder.getCodec()).ifPresent(this.holder::setData);
    }

    public void decodeData(RegistryFriendlyByteBuf buf) {
        this.holder.setData(this.holder.getStreamCodec().decode(buf));
    }
}
