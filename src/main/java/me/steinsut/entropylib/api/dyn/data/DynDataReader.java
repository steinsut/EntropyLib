package me.steinsut.entropylib.api.dyn.data;

import com.mojang.logging.LogUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.storage.ValueInput;
import org.slf4j.Logger;


public final class DynDataReader<D> {
    private static final Logger LOGGER = LogUtils.getLogger();

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

    public void readData(ValueInput in, String childName) {
        in.read(childName, this.holder.getCodec()).ifPresent(this.holder::setData);
    }

    public void decodeData(RegistryFriendlyByteBuf buf) {
        this.holder.setData(this.holder.getStreamCodec().decode(buf));
    }
}
