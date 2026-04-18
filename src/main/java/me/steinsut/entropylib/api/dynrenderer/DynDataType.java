package me.steinsut.entropylib.api.dynrenderer;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import me.steinsut.entropylib.api.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.*;
import java.util.function.Supplier;

import static me.steinsut.entropylib.EntropyLib.LOGGER;

public final class DynDataType<D, B extends ByteBuf> {
    public static final Codec<DynDataType<?, ?>> CODEC = Registries.DYN_RENDERER_DATA_TYPE_REGISTRY.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, DynDataType<?, ?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.DYN_RENDERER_DATA_TYPE_REGISTRY_KEY);

    private static final String VALUE_IO_KEY = "dyn";

    private final Supplier<D> defaultSupplier;
    private final Map<Identifier, Supplier<D>> presets;
    private final Codec<D> dataCodec;
    private final StreamCodec<B, D> dataStreamCodec;

    public DynDataType(Codec<D> dataCodec, StreamCodec<B, D> dataStreamCodec, Supplier<D> defaultSupplier) {
        this(dataCodec, dataStreamCodec, defaultSupplier, new HashMap<>());
    }

    public DynDataType(Codec<D> dataCodec, StreamCodec<B, D> dataStreamCodec, Supplier<D> defaultSupplier, Map<Identifier, Supplier<D>> presets) {
        this.defaultSupplier = defaultSupplier;
        this.presets = presets;
        this.dataCodec = dataCodec;
        this.dataStreamCodec = dataStreamCodec;
    }

    public Holder<D, B> createHolder() {
        return new Holder<>(this, defaultSupplier.get());
    }

    public Holder<D, B> createHolder(Identifier preset) {
        if (preset != null && this.presets.containsKey(preset)) {
            return new Holder<>(this, this.presets.get(preset).get());
        } else {
            LOGGER.warn("Preset \"{}\" is null or does not exist, falling back to default preset", preset);
            return this.createHolder();
        }
    }

    public boolean hasPreset(Identifier id) {
        return this.presets.containsKey(id);
    }

    public Collection<Identifier> getAllPresetsIds() {
        return Collections.unmodifiableSet(this.presets.keySet());
    }

    public static class Builder<_D, _B extends ByteBuf> {
        private final Supplier<_D> defaultSupplier;
        private final Map<Identifier, Supplier<_D>> presets;
        private final Codec<_D> codec;
        private final StreamCodec<_B, _D> streamCodec;

        public static <_D, _B extends ByteBuf> Builder<_D, _B> of(Codec<_D> codec, StreamCodec<_B, _D> streamCodec, Supplier<_D> defaultSupplier) {
            return new Builder<>(codec, streamCodec, defaultSupplier);
        }

        private Builder(Codec<_D> codec, StreamCodec<_B, _D> streamCodec, Supplier<_D> defaultSupplier) {
            this.defaultSupplier = defaultSupplier;
            this.presets = new HashMap<>();
            this.codec = codec;
            this.streamCodec = streamCodec;
        }

        public Builder<_D, _B> withPreset(Identifier id, Supplier<_D> supplier) {
            if (this.presets.containsKey(id)) {
                throw new RuntimeException("Preset already exists: " + id);
            }

            this.presets.put(id, supplier);
            return this;
        }

        public DynDataType<_D, _B> build() {
            return new DynDataType<>(this.codec, this.streamCodec, this.defaultSupplier, this.presets);
        }
    }

    public static class Holder<_D, _B extends ByteBuf> {
        private final DynDataType<_D, _B> dataType;
        private _D data;

        private Holder(DynDataType<_D, _B> dataType, _D data) {
            this.dataType = dataType;
            this.data = data;
        }

        public _D getData() {
            return this.data;
        }

        public DynDataType<_D, _B> getDataType() {
            return this.dataType;
        }

        @SuppressWarnings("unchecked")
        public void copyTo(Holder<?, ?> holder) {
            if (this.dataType != holder.dataType) {
                ((Holder<_D, _B>) holder).data = this.data;
            } else {
                LOGGER.warn("Cannot copy value to holder, holders belong to different data types");
            }
        }

        @SuppressWarnings("unchecked")
        public void copyFrom(Holder<?, ?> holder) {
            if (this.dataType == holder.dataType) {
                this.data = ((Holder<_D, _B>) holder).data;
            } else {
                LOGGER.warn("Cannot copy from holder, holders belong to different data types");
            }
        }

        public void readFromInput(ValueInput in) {
            Optional<_D> result = in.read(DynDataType.VALUE_IO_KEY, this.dataType.dataCodec);

            this.data = result.orElseGet(this.dataType.defaultSupplier);
        }

        public void writeToOutput(ValueOutput out) {
            out.store(DynDataType.VALUE_IO_KEY, this.dataType.dataCodec, this.data);
        }

        public void readFromStream(_B buf) {
            this.data = this.dataType.dataStreamCodec.decode(buf);
        }

        public void writeToStream(_B buf) {
            this.dataType.dataStreamCodec.encode(buf, this.data);
        }

        public void setData(_D data) {
            this.data = data;
        }

        public void setToPreset(Identifier id) {
            if (this.dataType.hasPreset(id)) {
                this.data = this.dataType.presets.get(id).get();
            }
            else {
                LOGGER.warn("Data dynType does not have preset {}", id);
            }
        }
    }
}
