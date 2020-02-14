package pw.dotdash.township.api.role;

import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.util.Identifiable;
import pw.dotdash.township.api.permission.Permission;

import java.util.Collection;

public interface Role extends Identifiable, DataSerializable {

    String getName();

    boolean setName(String name);

    Collection<Permission> getPermissions();

    boolean hasPermission(Permission permission);

    boolean addPermission(Permission permission);

    boolean removePermission(Permission permission);
}