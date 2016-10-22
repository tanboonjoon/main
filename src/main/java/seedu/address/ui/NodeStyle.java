package seedu.address.ui;

public enum NodeStyle {
    
    CIRCLE_HIGH ("CIRCLE", "circle_high", 0) ,
    CIRCLE_MED ("CIRCLE", "circle_med", Integer.MAX_VALUE) ,
    CIRCLE_LOW ("CIRCLE", "circle_low", 0) ,
    CIRCLE_DONE ("CIRCLE", "circle_done", -1) ,
    CIRCLE_BLOCK ("CIRCLE", "circle_block", -1) ,
    
    TITLE_NORMAL ("TITLE", "cell_big_label", Integer.MAX_VALUE) ,
    TITLE_OVERDUE ("TITLE", "cell_big_label_overdue", -1) ,
    TITLE_BLOCK ("TITLE", "cell_big_label_block", -1) ,
    TITLE_DONE ("TITLE", "cell_big_label_done", Integer.MIN_VALUE)
    
    ;
    
    
    public final String family ;
    public final String className ;
    public final int priority ;
    
    private NodeStyle (String family, String className, int priority) {
        this.family = family ;
        this.className = className ;
        this.priority = priority ;
    }

}
