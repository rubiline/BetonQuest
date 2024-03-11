package org.betonquest.betonquest.menu;

import java.util.List;

/**
 * PagedSlots are like rectangle slots, but can be manipulated via MenuPageEvent events in order to create
 * dynamically-sized paginated regions (for quest trackers, abstract inventories, and so forth)
 */
@SuppressWarnings("PMD.CommentRequired")
public class PagedSlots extends Slots {

    private final String slotID;
    private int page;

    public PagedSlots(final int start, final int end, final List<MenuItem> items, final String slotID) {
        super(Type.RECTANGLE, start, end, items);

        if (start > end) {
            throw new IllegalArgumentException("PagedSlots has end before start!");
        }

        if (slotID.isEmpty()) {
            throw new IllegalArgumentException("PagedSlots has no ID!");
        }

        this.slotID = slotID;
    }

    @Override
    public int getIndex(final int slot) {
        final int rectangleLength = end % 9 - start % 9 + 1;
        final int rectangleHeight = end / 9 - start / 9 + 1;
        final int rowPosition = slot / 9 - start / 9;
        return rectangleLength * rectangleHeight * (page) + rectangleLength * rowPosition + slot % 9 - start % 9;
    }

    public boolean isEqual(final String queryID) {
        return queryID.contentEquals(this.slotID);
    }

    public int maxPage() {
        final int rectangleLength = end % 9 - start % 9 + 1;
        final int rectangleHeight = end / 9 - start / 9 + 1;
        return Math.floorDiv(items.size(), rectangleLength * rectangleHeight);
    }

    public void iteratePage(final int delta) {
        this.page += delta;
    }

    public void firstPage() {
        this.page = 0;
    }

    public void lastPage() {
        this.page = maxPage();
    }
}
