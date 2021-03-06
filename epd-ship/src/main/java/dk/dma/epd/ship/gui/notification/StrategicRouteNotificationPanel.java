/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.dma.epd.ship.gui.notification;

import dk.dma.epd.common.prototype.EPD;
import dk.dma.epd.common.prototype.gui.notification.NotificationCenterCommon;
import dk.dma.epd.common.prototype.gui.notification.NotificationDetailPanel;
import dk.dma.epd.common.prototype.gui.notification.NotificationPanel;
import dk.dma.epd.common.prototype.gui.notification.NotificationTableModel;
import dk.dma.epd.common.prototype.gui.route.RoutePropertiesDialogCommon;
import dk.dma.epd.common.prototype.gui.route.RoutePropertiesDialogCommon.RouteChangeListener;
import dk.dma.epd.common.prototype.model.route.Route;
import dk.dma.epd.common.prototype.model.route.StrategicRouteNegotiationData;
import dk.dma.epd.common.prototype.model.route.StrategicRouteNegotiationData.StrategicRouteMessageData;
import dk.dma.epd.common.prototype.notification.NotificationType;
import dk.dma.epd.common.text.Formatter;
import dk.dma.epd.ship.EPDShip;
import dk.dma.epd.ship.layers.voyage.VoyageLayer;
import dk.dma.epd.ship.service.StrategicRouteHandler;
import dma.route.StrategicRouteStatus;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static dk.dma.epd.common.graphics.GraphicsUtil.bold;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.NORTHWEST;
import static java.awt.GridBagConstraints.SOUTH;
import static java.awt.GridBagConstraints.WEST;

/**
 * A strategic route implementation of the {@linkplain NotificationPanel} class
 */
public class StrategicRouteNotificationPanel extends NotificationPanel<StrategicRouteNotification> {

    private static final long serialVersionUID = 1L;
    
    private static final String[] NAMES = {
        "", "Name", "Date", "Status" };
    
    protected JButton routeDetailsBtn;
    protected JButton cancelBtn;
    
    StrategicNotificationReplyView replyPanel;
    
    /**
     * Constructor
     */
    public StrategicRouteNotificationPanel(NotificationCenterCommon notificationCenter) {
        super(notificationCenter);
        
        table.getColumnModel().getColumn(0).setMaxWidth(18);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(70);
        table.getColumnModel().getColumn(3).setPreferredWidth(85);
        splitPane.setDividerLocation(330);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ButtonPanel initButtonPanel() {
        ButtonPanel btnPanel = new ButtonPanel(notificationCenter);
        btnPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, UIManager.getColor("Separator.shadow")),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        cancelBtn = new JButton(
                "Cancel", 
                EPD.res().getCachedImageIcon("images/notifications/cross.png"));
        routeDetailsBtn = new JButton(
                "Route Details", 
                EPD.res().getCachedImageIcon("images/notifications/routes.png"));
        
        btnPanel.add(cancelBtn);
        btnPanel.add(routeDetailsBtn);
        btnPanel.add(gotoBtn);
        btnPanel.add(chatBtn);
        
        cancelBtn.addActionListener(new ActionListener() {            
            @Override public void actionPerformed(ActionEvent e) {
                handleCancel();
            }});
        
        routeDetailsBtn.addActionListener(new ActionListener() {            
            @Override public void actionPerformed(ActionEvent e) {
                showRouteDetails();
            }});
        
        gotoBtn.addActionListener(new ActionListener() {            
            @Override public void actionPerformed(ActionEvent e) {
                gotoSelectedRoute();
            }});
        
        chatBtn.addActionListener(new ActionListener() {            
            @Override public void actionPerformed(ActionEvent e) {
                chatWithNotificationTarget();
            }});
        
        // Configure the reply panel
        replyPanel = new StrategicNotificationReplyView();
        
        replyPanel.getAcceptBtn().addActionListener(new ActionListener() {            
            @Override public void actionPerformed(ActionEvent e) {
                handleAccept();
            }});
        
        replyPanel.getRejectBtn().addActionListener(new ActionListener() {            
            @Override public void actionPerformed(ActionEvent e) {
                handleReject();
            }});
        
        
        return btnPanel;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateButtonEnabledState() {
        StrategicRouteNotification notification = getSelectedNotification();
        if (notification != null && notification.get().hasRouteMessages()) {
            gotoBtn.setEnabled(true);
            routeDetailsBtn.setEnabled(true);
            
            StrategicRouteMessageData msg = notification.get().getLatestRouteMessage();
            cancelBtn.setEnabled(!msg.isFromStcc() && !notification.isAcknowledged());
            replyPanel.getRejectBtn().setEnabled(!notification.isAcknowledged() && msg.isFromStcc());
            replyPanel.getAcceptBtn().setEnabled(!notification.isAcknowledged() && msg.isFromStcc());
            
        } else  {
            gotoBtn.setEnabled(false);
            routeDetailsBtn.setEnabled(false);
            cancelBtn.setEnabled(false);
        }
        updateChatEnabledState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setSelectedNotification() {
        // Let super set the notification 
        super.setSelectedNotification();
        
        // If the latest message is from the STCC, install a reply panel
        StrategicRouteNotification notification = getSelectedNotification();
        if (notification != null && notification.get().hasRouteMessages()) {
            StrategicRouteMessageData msg = notification.get().getLatestRouteMessage();
            if (msg.isFromStcc()) {
                replyPanel.getMessageTxtField().setText("");
                replyPanel.getAcceptBtn().setText("Accept");
                getNotificationDetailPanel().addReplyPanelInMessagesPanel(replyPanel);
            }
        }        
    }    
    
    /**
     * Called when a voyage layer route is modified
     */
    public void changeToModifiedAcceptBtn() {
        SwingUtilities.invokeLater(new Runnable() {            
            @Override public void run() {
                replyPanel.getAcceptBtn().setText("Send modified");
            }
        });
    }
    
    /**
     * Cancels the current strategic route
     */
    private void handleCancel() {
        StrategicRouteNotification notification = getSelectedNotification();
        if (notification != null) {
            EPDShip.getInstance().getStrategicRouteHandler()
                .sendRejectMsg(
                        notification.getId(),  
                        "Request cancelled", 
                        StrategicRouteStatus.CANCELED);
            EPDShip.getInstance().getNotificationCenter().setVisible(false);
        }
    }
    
    /**
     * Rejects the current strategic route
     */
    private void handleReject() {
        StrategicRouteNotification notification = getSelectedNotification();
        if (notification != null) {
            EPDShip.getInstance().getStrategicRouteHandler()
                .sendRejectMsg(
                        notification.getId(),  
                        replyPanel.getMessageTxtField().getText(), 
                        StrategicRouteStatus.REJECTED);
            EPDShip.getInstance().getNotificationCenter().setVisible(false);
        }
    }
    
    /**
     * Accepts the current strategic route
     */
    private void handleAccept() {
        StrategicRouteNotification notification = getSelectedNotification();
        if (notification != null) {
            
            EPDShip.getInstance().getStrategicRouteHandler()
                .sendReply(notification.getId(), replyPanel.getMessageTxtField().getText());
            EPDShip.getInstance().getNotificationCenter().setVisible(false);
        }
    }
    
    /**
     * Show the route details for the selected route
     */
    private void showRouteDetails() {
        StrategicRouteNotification notification = getSelectedNotification();
        if (notification != null) {
            
            // Open the route properties dialog
            // If the latest message is from the STCC, show the modified STCC route of the voyage layer
            // Otherwise, show the latest route in read-only mode.
            final VoyageLayer voyageLayer = EPDShip.getInstance().getStrategicRouteHandler().getVoyageLayer();
            boolean readOnly = !notification.get().getLatestRouteMessage().isFromStcc();
            Route route = readOnly 
                        ? notification.get().getLatestRoute()
                        : voyageLayer.getModifiedSTCCRoute();
            RoutePropertiesDialogCommon routePropertiesDialog = new RoutePropertiesDialogCommon(
                    EPDShip.getInstance().getMainFrame(), 
                    EPDShip.getInstance().getMainFrame().getChartPanel(),
                    route, 
                    readOnly);
            
            // Apply changes to the route
            routePropertiesDialog.addRouteChangeListener(new RouteChangeListener() {                
                @Override  public void routeChanged() {
                    voyageLayer.voyageUpdated();
                }});
            
            routePropertiesDialog.setVisible(true);
        }
    }
    /**
     * Zoom to the route of the currently selected notification
     */
    public void gotoSelectedRoute() {
        StrategicRouteNotification notification = getSelectedNotification();
        if (notification != null && 
                EPD.getInstance().getMainFrame().getActiveChartPanel() != null) {
            EPD.getInstance().getMainFrame().getActiveChartPanel()
                .zoomToWaypoints(notification.get().getLatestAcceptedOrOriginalRoute().getWaypoints());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationType getNotitficationType() {
        return NotificationType.STRATEGIC_ROUTE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NotificationTableModel<StrategicRouteNotification> initTableModel() {
        return new NotificationTableModel<StrategicRouteNotification>() {
            private static final long serialVersionUID = 1L;
            
            @Override 
            public String[] getColumnNames() { 
                return NAMES; 
            }
            
            @Override 
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return ImageIcon.class;
                } else {
                    return super.getColumnClass(columnIndex);
                }
            }
            
            @Override 
            public Object getValueAt(int rowIndex, int columnIndex) {
                StrategicRouteNotification notification = getNotification(rowIndex);
                
                switch (columnIndex) {
                case 0: return !notification.isRead() 
                        ? ICON_UNREAD 
                        : (notification.isAcknowledged() ? ICON_ACKNOWLEDGED : null);
                case 1: return notification.getCallerlName();
                case 2: return Formatter.formatShortDateTimeNoTz(notification.getDate());
                case 3: return notification.get().getStatus();
                default:
                }
                return null; 
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NotificationDetailPanel<StrategicRouteNotification> initNotificationDetailPanel() {
        return new StrategicRouteNotificationDetailPanel();
    }
    
    /**
     * Returns the notification detail panel
     * @return the notification detail panel
     */
    private StrategicRouteNotificationDetailPanel getNotificationDetailPanel() {
        return (StrategicRouteNotificationDetailPanel)notificationDetailPanel;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void doRefreshNotifications() {
        StrategicRouteHandler strategicRouteHandler = EPDShip.getInstance().getStrategicRouteHandler();
        
        // The back-end does not support the "read" flag, so, we store it
        Set<Long> readNotificationIds = new HashSet<>();
        for (StrategicRouteNotification notification : tableModel.getNotifications()) {
            if (notification.isRead()) {
                readNotificationIds.add(notification.getId());
            }
        }
        
        List<StrategicRouteNotification> notifications = new ArrayList<>();
        for (StrategicRouteNegotiationData routeData : strategicRouteHandler.getSortedStrategicNegotiationData()) {
            StrategicRouteNotification notification = new StrategicRouteNotification(routeData);
            
            // Restore the "read" flag
            if (readNotificationIds.contains(notification.getId())) {
                notification.setRead(true);
            }
            notifications.add(notification);
        }
        tableModel.setNotifications(notifications);
        refreshTableData();
        notifyListeners();
    }
}


/**
 * A panel used for accepting or rejecting a route request
 */
class StrategicNotificationReplyView extends JPanel {

    private static final long serialVersionUID = 1L;

    JButton acceptBtn = new JButton("Accept", EPD.res().getCachedImageIcon("images/notifications/tick.png"));;
    JButton rejectBtn = new JButton("Reject", EPD.res().getCachedImageIcon("images/notifications/cross.png"));
    JTextArea messageTxtField = new JTextArea();
    
    /**
     * Create the panel.
     */
    public StrategicNotificationReplyView() {

        super(new GridBagLayout());
        
        Insets insets0  = new Insets(0, 0, 0, 0);
        Insets insets1  = new Insets(5, 5, 5, 5);
        Insets insets2  = new Insets(5, 5, 0, 0);

        // Title
        JPanel titlePanel = new JPanel(new GridBagLayout());
        add(titlePanel, 
                new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, WEST, HORIZONTAL, insets0, 0, 0));

        titlePanel.setBackground(titlePanel.getBackground().darker());
        titlePanel.add(bold(new JLabel("Send reply to STCC")), 
                new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, WEST, HORIZONTAL, insets1, 0, 0));
        
        // Reply
        messageTxtField.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(messageTxtField);
        scrollPane.setMinimumSize(new Dimension(180, 40));
        scrollPane.setPreferredSize(new Dimension(180, 40));
        add(new JLabel("Reply:"), 
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, NORTHWEST, NONE, insets2, 0, 0));
        add(scrollPane, 
                new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, WEST, HORIZONTAL, insets1, 0, 0));
        
        // Buttons
        JPanel btnPanel = new JPanel(new GridBagLayout());
        add(btnPanel, 
                new GridBagConstraints(1, 1, 1, 2, 0.0, 0.0, SOUTH, NONE, insets1, 0, 0));
        
        btnPanel.add(acceptBtn, 
                new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, WEST, HORIZONTAL, insets2, 0, 0));
        btnPanel.add(rejectBtn, 
                new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, WEST, HORIZONTAL, insets2, 0, 0));
    }

    public JButton getAcceptBtn() {
        return acceptBtn;
    }

    public JButton getRejectBtn() {
        return rejectBtn;
    }

    public JTextArea getMessageTxtField() {
        return messageTxtField;
    }
 }
