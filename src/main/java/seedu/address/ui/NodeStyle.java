package seedu.address.ui;

// @@author A0135768R
/**
 * 
 * A simple Enum to encapsulate information about CSS classes
 * and the priority to which classes should be applied over another class
 * of the same type/family
 * 
 */
public enum NodeStyle {
    
    CIRCLE_HIGH ("CIRCLE", "circle_high", 0) ,
    CIRCLE_MED ("CIRCLE", "circle_med", Integer.MAX_VALUE) ,
    CIRCLE_LOW ("CIRCLE", "circle_low", 0) ,
    CIRCLE_DONE ("CIRCLE", "circle_done", -1) ,
    CIRCLE_BLOCK ("CIRCLE", "circle_block", -1) ,
    
    TITLE_NORMAL ("TITLE", "cell_big_label", Integer.MAX_VALUE) ,
    TITLE_OVERDUE ("TITLE", "cell_big_label_overdue", -2) ,
    TITLE_BLOCK ("TITLE", "cell_big_label_block", -1) ,
    TITLE_DONE ("TITLE", "cell_big_label_done", Integer.MIN_VALUE),
    
    TIME_NORMAL ("TIME", "cell_small_label", Integer.MAX_VALUE) ,
    TIME_BLOCK ("TIME", "time_block", -1) ,
    TIME_UPCOMING ("TIME", "time_upcoming", -1),
    TIME_OVERDUE ("TIME", "time_overdue", -2),
    TIME_DONE ("TIME", "time_done", Integer.MIN_VALUE)
    
    ;
    
    
    public final String family ;
    public final String className ;
    /** lower priority number = more important */
    public final int priority ; 
    
    private NodeStyle (String family, String className, int priority) {
        this.family = family ;
        this.className = className ;
        this.priority = priority ;
    }
    
    /**
     * Returns true if the given NodeStyle should overwrite this NodeStyle
     */
    public boolean shouldOverwriteStyle(NodeStyle other) {
        return other != null 
                && this.family.equals(other.family)
                && this.priority >= other.priority ;
    }

}
