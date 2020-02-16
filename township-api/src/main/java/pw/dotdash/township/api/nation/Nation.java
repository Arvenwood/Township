package pw.dotdash.township.api.nation;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.ResettableBuilder;
import pw.dotdash.township.api.resident.Resident;
import pw.dotdash.township.api.town.Town;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

public interface Nation extends Identifiable, MessageReceiver, DataSerializable {

    /**
     * Creates a new {@link Builder} to build a {@link Nation}.
     *
     * @return The new builder
     */
    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    /**
     * Gets the name of the nation.
     *
     * @return The name of the nation
     */
    String getName();

    /**
     * Sets the name of the nation.
     *
     * @param name The name to set
     * @return True if the action was successful
     */
    boolean setName(String name);

    /**
     * Checks if the nation is joinable without an invitation.
     *
     * @return True if the nation is joinable without an invite
     */
    boolean isOpen();

    /**
     * Sets whether the town is joinable without an invitation.
     *
     * @param open True if joinable without an invite
     * @return True if the action was successful
     */
    boolean setOpen(boolean open);

    /**
     * Gets the owner of the nation.
     *
     * @return The owner of the nation
     */
    Town getOwner();

    /**
     * Checks if the specified town is the owner of the nation.
     *
     * @param town The town to check
     * @return True if the town is the owner
     */
    boolean isOwner(Town town);

    /**
     * Sets the owner of the town.
     *
     * @param town The town to set
     * @return True if the action was successful
     */
    boolean setOwner(Town town);

    /**
     * Gets all towns in the nation.
     *
     * @return All towns in the nation
     */
    Collection<Town> getTowns();

    /**
     * Checks if the specified town is a member of the nation.
     *
     * @param town The town to check
     * @return True if the town is a member
     */
    boolean hasTown(Town town);

    /**
     * Adds the specified town as a member of the nation.
     *
     * @param town The town to add
     * @return True if the action was successful
     */
    boolean addTown(Town town);

    /**
     * Removes the specified town as a member of the nation.
     *
     * @param town The town to remove
     * @return True if the action was successful
     */
    boolean removeTown(Town town);

    /**
     * Gets all roles registered to the nation.
     *
     * @return All registered roles
     */
    default Collection<NationRole> getRoles() {
        return NationRoleService.getInstance().getRoles(this);
    }

    /**
     * Gets the role with the specified name that's registered to the nation,
     * if available.
     *
     * @param name The name to find
     * @return The role, if available
     */
    default Optional<NationRole> getRole(String name) {
        return NationRoleService.getInstance().getRole(this, name);
    }

    /**
     * Gets all residents in the nation.
     *
     * @return All nation residents
     */
    Collection<Resident> getResidents();

    /**
     * Gets the {@link Account} associated with the nation, if available.
     *
     * @return The associated account, if available
     */
    Optional<Account> getAccount();

    /**
     * Gets the nation's current balance in the specified currency.
     *
     * @param currency The currency to check
     * @return The nation's current balance
     */
    BigDecimal getBalance(Currency currency);

    /**
     * Gets the nation's current balance in the default currency.
     *
     * @return The nation's current balance
     */
    BigDecimal getBalance();

    interface Builder extends ResettableBuilder<Nation, Builder> {

        Builder name(String name);

        Builder open(boolean open);

        Builder owner(Town owner);

        Builder towns(Iterable<Town> towns);

        Builder towns(Town... towns);

        Nation build();
    }
}