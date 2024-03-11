package org.betonquest.betonquest.menu;

import java.util.List;

/**
 * A factory class for Slots objects, in order to decouple object creation and allow for advanced Slots types.
 */
@SuppressWarnings({"PMD.CommentRequired", "PMD.CyclomaticComplexity", "PMD.CognitiveComplexity"})
public final class SlotsFactory {
    private SlotsFactory() {
    }

    public static Slots createSlots(final String slots, final List<MenuItem> items) {
        if (slots.matches("\\d+")) {
            return new Slots(Slots.Type.SINGLE, Integer.parseInt(slots), Integer.parseInt(slots), items);
        } else if (slots.matches("\\d+-\\d+")) {
            final int index = slots.indexOf('-');
            final int start = Integer.parseInt(slots.substring(0, index));
            final int end = Integer.parseInt(slots.substring(index + 1));
            if (end < start) {
                throw new IllegalArgumentException(slots + ": slot " + end + " must be after " + start);
            }

            return new Slots(Slots.Type.ROW, start, end, items);
        } else if (slots.matches("\\d+\\*\\d+")) {
            final int index = slots.indexOf('*');
            final int start = Integer.parseInt(slots.substring(0, index));
            final int end = Integer.parseInt(slots.substring(index + 1));
            if (end < start) {
                throw new IllegalArgumentException(slots + ": slot " + end + " must be after " + start);
            }
            if ((start % 9) > (end % 9)) {
                throw new IllegalArgumentException(slots + ": invalid rectangle ");
            }
            return new Slots(Slots.Type.RECTANGLE, start, end, items);
        } else if (slots.matches("pgn\\d+\\*\\d+|")) {
            final String slotString = slots.substring(4);

            final int index = slots.indexOf('*');
            final int idIdx = slots.indexOf('|');
            final int start = Integer.parseInt(slotString.substring(0, index));
            final int end = Integer.parseInt(slotString.substring(index + 1, idIdx));

            if (end < start) {
                throw new IllegalArgumentException(slots + ": slot " + end + " must be after " + start);
            }
            if ((start % 9) > (end % 9)) {
                throw new IllegalArgumentException(slots + ": invalid rectangle ");
            }

            final String slotID = slotString.substring(idIdx + 1);
            return new PagedSlots(start, end, items, slotID);
        } else {
            throw new IllegalArgumentException(slots + " is not a valid slot identifier");
        }
    }
}
