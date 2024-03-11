package org.betonquest.betonquest.menu.betonquest;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.QuestEvent;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.ObjectNotFoundException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.menu.Menu;
import org.betonquest.betonquest.menu.MenuID;
import org.betonquest.betonquest.menu.PagedSlots;

/**
 * This event allows for the control of PagedSlot slots, to create control buttons for paginated slot boxes.
 */
@SuppressWarnings("PMD.CommentRequired")
public class MenuPageEvent extends QuestEvent {
    private Operation operation;
    private MenuID menu;
    private String slotID;

    public MenuPageEvent(final Instruction instruction) throws InstructionParseException {
        super(instruction, true);
        try {
            this.menu = new MenuID(instruction.getPackage(), instruction.next());
            this.slotID = instruction.next();
            this.operation = instruction.getEnum(MenuPageEvent.Operation.class);
        } catch (final ObjectNotFoundException e) {
            throw new InstructionParseException("Error while parsing 2 argument: Error while loading menu: " + e.getMessage(), e);
        }
    }

    @Override
    protected Void execute(final Profile profile) throws QuestRuntimeException {
        final Menu currentMenu = BetonQuest.getInstance().getRpgMenu().getMenu(menu);
        final PagedSlots pagedSlots = currentMenu.getPagedSlotByID(slotID);

        switch (operation) {
            case FIRST -> pagedSlots.firstPage();
            case PREV -> pagedSlots.iteratePage(1);
            case NEXT -> pagedSlots.iteratePage(-1);
            case LAST -> pagedSlots.lastPage();
        }

        BetonQuest.getInstance().getRpgMenu().reloadMenu(menu);
        return null;
    }

    public enum Operation {
        FIRST,
        PREV,
        NEXT,
        LAST
    }
}
