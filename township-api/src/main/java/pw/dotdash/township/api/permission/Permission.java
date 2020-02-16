package pw.dotdash.township.api.permission;

import org.spongepowered.api.CatalogType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.ResettableBuilder;

import java.util.Optional;

public interface Permission extends CatalogType {

    Optional<Text> getDescription();

    interface Builder<T extends Permission, B extends Builder<T, B>> extends ResettableBuilder<T, B> {

        B id(String id);

        B name(String name);

        B description(Text description);

        T build();
    }
}