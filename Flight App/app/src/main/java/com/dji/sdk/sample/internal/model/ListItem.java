package com.dji.sdk.sample.internal.model;


import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.dji.sdk.sample.internal.view.PresentableView;

import java.util.ArrayList;
import java.util.List;

/**
 * A list item
 */
public abstract class ListItem {
    private @StringRes
    int titleStringId;

    /**
     * Instantiates a new List item.
     *
     * @param titleStringId the title string id
     */
    public ListItem(@StringRes int titleStringId) {
        this.titleStringId = titleStringId;
    }

    /**
     * Gets title string id.
     *
     * @return the title string id
     */
    public int getTitleStringId() {
        return titleStringId;
    }

    /**
     * Builder to easily build a hierarchical list of ListItem
     */
    public static final class ListBuilder {
        private final List<GroupHeader> demos;

        /**
         * Instantiates a new List builder.
         */
        public ListBuilder() {
            demos = new ArrayList<>();
        }

        /**
         * Ands a stand alone first level item
         */
        private ListBuilder singleItem(@StringRes int titleString,
                                       @NonNull Class<? extends PresentableView> linkedDemoView) {
            try {
                demos.add(new SingleItem(titleString, linkedDemoView));
            } catch (Exception e) {
                throw new RuntimeException("Class "
                                               + linkedDemoView.getSimpleName()
                                               + " is missing a constructor that takes Context");
            }
            return this;
        }

        /**
         * Add a group of items
         *
         * @param titleString             the title string
         * @param shouldCollapseByDefault the should collapse by default
         * @param items                   the items
         * @return the list builder
         */
        public ListBuilder addGroup(@StringRes int titleString, boolean shouldCollapseByDefault, GroupItem ... items) {
            demos.add(new GroupHeader(titleString, items, shouldCollapseByDefault));
            return this;
        }

        /**
         * Build list.
         *
         * @return the list
         */
        public List<GroupHeader> build() {
            return this.demos;
        }
    }
}
