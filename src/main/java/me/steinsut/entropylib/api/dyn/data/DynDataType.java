/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.dyn.data;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.api.registries.CommonRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class DynDataType<D> {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Identifier DEFAULT_PRESET_ID = Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "default");

    public static final Codec<DynDataType<?>> CODEC = CommonRegistries.DYN_DATA_TYPE_REGISTRY.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, DynDataType<?>> STREAM_CODEC = ByteBufCodecs.registry(CommonRegistries.DYN_DATA_TYPE_REGISTRY_KEY);

    private final Supplier<D> defaultSupplier;
    private final Map<Identifier, Supplier<D>> presets;
    private final Codec<D> dataCodec;
    private final StreamCodec<? super RegistryFriendlyByteBuf, D> dataStreamCodec;

    public DynDataType(Codec<D> dataCodec, StreamCodec<? super RegistryFriendlyByteBuf, D> dataStreamCodec, Supplier<D> defaultSupplier) {
        this(dataCodec, dataStreamCodec, defaultSupplier, new HashMap<>());
    }

    public DynDataType(Codec<D> dataCodec, StreamCodec<? super RegistryFriendlyByteBuf, D> dataStreamCodec, Supplier<D> defaultSupplier, Map<Identifier, Supplier<D>> presets) {
        this.defaultSupplier = defaultSupplier;
        this.presets = presets;
        this.dataCodec = dataCodec;
        this.dataStreamCodec = dataStreamCodec;
    }

    public Holder<D> createHolder() {
        return new Holder<>(this, defaultSupplier.get());
    }

    public Holder<D> createHolder(Identifier preset) {
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

    public static final class Builder<_D> {
        private final Supplier<_D> defaultSupplier;
        private final Map<Identifier, Supplier<_D>> presets;
        private final Codec<_D> codec;
        private final StreamCodec<? super RegistryFriendlyByteBuf, _D> streamCodec;

        private Builder(Codec<_D> codec, StreamCodec<? super RegistryFriendlyByteBuf, _D> streamCodec, Supplier<_D> defaultSupplier) {
            this.defaultSupplier = defaultSupplier;
            this.presets = new HashMap<>();
            this.codec = codec;
            this.streamCodec = streamCodec;
        }

        public static <_D> Builder<_D> of(Codec<_D> codec, StreamCodec<? super RegistryFriendlyByteBuf, _D> streamCodec, Supplier<_D> defaultSupplier) {
            return new Builder<>(codec, streamCodec, defaultSupplier);
        }

        public Builder<_D> withPreset(Identifier id, Supplier<_D> supplier) {
            if (this.presets.containsKey(id)) {
                throw new RuntimeException("Preset already exists: " + id);
            }

            this.presets.put(id, supplier);
            return this;
        }

        public DynDataType<_D> build() {
            this.presets.put(DEFAULT_PRESET_ID, defaultSupplier);
            return new DynDataType<>(this.codec, this.streamCodec, this.defaultSupplier, this.presets);
        }
    }

    public static final class Holder<_D> {
        public static StreamCodec<RegistryFriendlyByteBuf, Holder<?>> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public void encode(@NonNull RegistryFriendlyByteBuf buf, Holder<?> holder) {
                DynDataType.STREAM_CODEC.encode(buf, holder.dataType);
                holder.getWriter().encodeData(buf);
            }

            public @NonNull Holder<?> decode(@NonNull RegistryFriendlyByteBuf buf) {
                DynDataType<?> dataType = DynDataType.STREAM_CODEC.decode(buf);
                Holder<?> holder = dataType.createHolder();
                holder.getReader().decodeData(buf);

                return holder;
            }
        };
        private final DynDataType<_D> dataType;
        private final DynDataWriter<_D> writer;
        private final DynDataReader<_D> reader;
        private _D data;

        private Holder(DynDataType<_D> dataType, _D data) {
            this.dataType = dataType;
            this.data = data;

            this.writer = new DynDataWriter<>(this);
            this.reader = new DynDataReader<>(this);
        }

        public DynDataType<_D> getDataType() {
            return this.dataType;
        }

        public Codec<_D> getCodec() {
            return this.dataType.dataCodec;
        }

        public StreamCodec<? super RegistryFriendlyByteBuf, _D> getStreamCodec() {
            return this.dataType.dataStreamCodec;
        }

        public DynDataWriter<_D> getWriter() {
            return this.writer;
        }

        public DynDataReader<_D> getReader() {
            return this.reader;
        }

        public _D getData() {
            return this.data;
        }

        public void setData(_D data) {
            this.data = data;
        }

        public void setToPreset(Identifier id) {
            if (this.dataType.hasPreset(id)) {
                this.data = this.dataType.presets.get(id).get();
            } else {
                LOGGER.warn("Data dynType does not have preset {}", id);
            }
        }
    }
}