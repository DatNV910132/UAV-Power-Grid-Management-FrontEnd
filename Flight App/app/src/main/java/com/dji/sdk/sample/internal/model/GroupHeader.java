package com.dji.sdk.sample.internal.model;


import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

/**
 * List item that has at least one child.
 * This should be used to hold data for the header of a group in the ListView
 */
public class GroupHeader extends ListItem {
    private final GroupItem[] groupItemList;
    private boolean shouldCollapseByDefault;

    /**
     * Instantiates a new Group header.
     *
     * @param titleStringId the title string id
     * @param groupItemList the group item list
     */
    public GroupHeader(@StringRes int titleStringId, @NonNull GroupItem[] groupItemList) {
        this(titleStringId, groupItemList, false);
    }

    /**
     * Instantiates a new Group header.
     *
     * @param titleStringId           the title string id
     * @param groupItemList           the group item list
     * @param shouldCollapseByDefault the should collapse by default
     */
    public GroupHeader(@StringRes int titleStringId, @NonNull GroupItem[] groupItemList, boolean shouldCollapseByDefault) {
        super(titleStringId);
        this.groupItemList = groupItemList;
        this.shouldCollapseByDefault = shouldCollapseByDefault;
    }

    /**
     * Should collapse by default boolean.
     *
     * @return the boolean
     */
    public boolean shouldCollapseByDefault() {
        return shouldCollapseByDefault;
    }

    /**
     * Get group items group item [ ].
     *
     * @return the group item [ ]
     */
    @NonNull
    public GroupItem[] getGroupItems() {
        return groupItemList;
    }
}
