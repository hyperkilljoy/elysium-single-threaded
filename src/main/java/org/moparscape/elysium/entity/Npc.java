package org.moparscape.elysium.entity;

import org.moparscape.elysium.Server;
import org.moparscape.elysium.def.NPCDef;
import org.moparscape.elysium.def.NPCLoc;
import org.moparscape.elysium.entity.component.Movement;
import org.moparscape.elysium.util.Formulae;
import org.moparscape.elysium.world.Point;
import org.moparscape.elysium.world.Region;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author lothy
 */
public final class Npc extends MobileEntity {

    private int appearanceId = 0;
    private boolean appearanceUpdateRequired = false;
    private int combatLevel = 0;
    private NPCDef def;
    private int hitpoints;
    private int id;
    private int index;
    private int lastDamage = 0;
    private long lastMoved = 0L;
    private NPCLoc loc;
    private Movement movement = new Movement(this);

    public Npc(int id) {
        this.id = id;
    }

    public Npc(NPCLoc loc) {
        def = EntityHandler.getNpcDef(loc.getId());
        hitpoints = def.getHits();
        this.loc = loc;
        this.id = loc.getId();
        this.setLocation(new Point(loc.startX(), loc.startY()), true);
        this.setCombatLevel(Formulae.getCombatLevel(def.getAtt(), def.getDef(), def.getStr(), def.getHits(), 0, 0, 0));
    }

    public int getAttack() {
        return def.getAtt();
    }

    public int getCombatLevel() {
        return combatLevel;
    }

    public void setCombatLevel(int combatLevel) {
        this.combatLevel = combatLevel;
    }

    public int getDefense() {
        return def.getDef();
    }

    public int getHits() {
        return hitpoints;
    }

    public void setHits(int hitpoints) {
        if (hitpoints < 0) {
            hitpoints = 0;
        }

        this.hitpoints = hitpoints;
    }

    public int getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "";
    }

    public int getLastDamage() {
        return lastDamage;
    }

    public NPCLoc getLoc() {
        return loc;
    }

    @Override
    public Point getLocation() {
        return movement.getLocation();
    }

    @Override
    public void setLocation(Point location) {
        movement.setLocation(location);
    }

    @Override
    public void setLocation(Point location, boolean teleported) {
        movement.setLocation(location, teleported);
    }

    @Override
    public void updateRegion(Region oldRegion, Region newRegion) {
        if (oldRegion != newRegion) {
            if (oldRegion != null) {
                oldRegion.removeNpc(this);
            }

            newRegion.addNpc(this);
        }
    }

    public int getMaxHits() {
        return def.getHits();
    }

    public int getStrength() {
        return def.getStr();
    }

    public boolean hasMoved() {
        return movement.hasMoved();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    public boolean isAttackable() {
        return def.isAttackable();
    }

    public boolean isRemoved() {
        return false;
    }

    public void resetMoved() {
        movement.resetMoved();
    }

    public void unblock() {
        throw new NotImplementedException();
    }

    public void updatePosition() {
        long now = Server.getInstance().getHighResolutionTimestamp();
        if (now - lastMoved > 6000) {
            lastMoved = now;
            // Do movement stuff here once it can be implemented
        }
        movement.updatePosition();
    }
}
