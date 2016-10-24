package seedu.address.commons.util;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import seedu.address.ui.NodeStyle;

/**
 * @@author A0135768R
 * 
 * Contains utility methods for JavaFX views
 */
public class FxViewUtil {
    
    public static final ImmutableMap<String, NodeStyle> NODE_STYLES ;
    
    static {
        
        Builder <String, NodeStyle> builder = ImmutableMap.builder() ;
        
        for (NodeStyle nodeStyle : NodeStyle.values()) {
            builder.put(nodeStyle.className, nodeStyle) ;
        }
        
        NODE_STYLES = builder.build() ;
    }
        
    
    public static void removeAndAddCssClass (Node node, String classToRemove, String classToAdd) {
        node.getStyleClass().remove(classToRemove) ;
        node.getStyleClass().add(classToAdd) ;
    }
    
    /**
     * Sets the style of the given node with the given style CSS class if it satisfies the following rule : <p>
     * 
     * - The given style has a integer priority lower or equals to all other CSS classes of the same family
     * currently applied to this node
     * 
     * @param node      The node to apply the style to
     * @param style     A non-null NodeStyle enum representing the CSS class that is to be applied onto the given node 
     */
    public static void setNodeStyle (Node node, NodeStyle style) {
        
        assert style != null ;
        
        String family = style.family ;
        
        List<String> classToRemove = new ArrayList<>() ;
        String classToAdd = style.className ;
        
        for (String CssClass : node.getStyleClass()) {
            NodeStyle nodeStyle = NODE_STYLES.get(CssClass) ;
            
            if (nodeStyle != null && nodeStyle.family.equals(family) && nodeStyle.priority >= style.priority) {
                classToRemove.add(nodeStyle.className) ;
            }
            
            if (nodeStyle != null && nodeStyle.family.equals(family) && nodeStyle.priority < style.priority) {
                classToAdd = null ;
            }
        }

        node.getStyleClass().removeAll(classToRemove) ;
        
        if (classToAdd != null) {
            node.getStyleClass().add(classToAdd) ;
        }
        
    }
    
    // @@author reused
    public static void applyAnchorBoundaryParameters(Node node, double left, double right, double top, double bottom) {
        AnchorPane.setBottomAnchor(node, bottom);
        AnchorPane.setLeftAnchor(node, left);
        AnchorPane.setRightAnchor(node, right);
        AnchorPane.setTopAnchor(node, top);
    }
}
