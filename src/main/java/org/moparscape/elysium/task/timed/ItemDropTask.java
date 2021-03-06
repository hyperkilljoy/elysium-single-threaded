package org.moparscape.elysium.task.timed;

import org.moparscape.elysium.entity.InvItem;
import org.moparscape.elysium.entity.Item;
import org.moparscape.elysium.entity.Player;
import org.moparscape.elysium.entity.PlayerState;
import org.moparscape.elysium.entity.component.Inventory;
import org.moparscape.elysium.entity.component.Movement;
import org.moparscape.elysium.net.Packets;
import org.moparscape.elysium.world.Point;
import org.moparscape.elysium.world.Region;

/**
 * Created by IntelliJ IDEA.
 *
 * @author lothy
 */
public final class ItemDropTask extends AbstractTimedTask {

    private final Inventory inventory;
    private final Movement movement;
    private final int originalActionCount;
    private final Player owner;
    private final int slot;
    private boolean finished = false;

    public ItemDropTask(Player owner, int slot, int originalAcountCount) {
        super(0, 0);

        this.owner = owner;
        this.slot = slot;
        this.originalActionCount = originalAcountCount;

        this.movement = owner.getMovement();
        this.inventory = owner.getInventory();
    }

    public void run() {
        if (movement.hasMoved()) {
            return;
        }

        // If the player has started performing a new action,
        // cancel this task
        PlayerState state = owner.getState();
        if (state != PlayerState.ITEM_DROP ||
                originalActionCount != owner.getActionCount()) {
            finished = true;
            return;
        }

        InvItem item = inventory.remove(slot);
        if (item != null) {
            Point loc = owner.getLocation();
            Region region = Region.getRegion(loc);

            region.addItem(new Item(item.getItemId(), item.getAmount(), loc, owner));
            Packets.sendSound(owner, "dropobject");
        }

        finished = true;
        return;
    }

    public boolean shouldRepeat() {
        return !finished;
    }
}
