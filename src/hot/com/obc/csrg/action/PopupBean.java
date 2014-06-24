package com.obc.csrg.action;

import javax.ejb.Stateless;

import com.obc.csrg.local.PopupBeanLocal;

import javax.faces.event.ActionEvent;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import java.io.Serializable;
import java.util.Map;

@Stateless
@Name("popup")
public class PopupBean implements Serializable,PopupBeanLocal {

	@In
	private Map<String, String> messages;
	
	// user entered messages for both dialogs
    private String draggableMessage = "amovivel";//messages.get("Draggable"); 
    private String modalMessage = "modal";//messages.get("Modal");
    // render flags for both dialogs
    private boolean draggableRendered = false;
    private boolean modalRendered = false;
    // if we should use the auto centre attribute on the draggable dialog
    private boolean autoCentre = true;

    public String getDraggableMessage() {
        return draggableMessage;
    }

    public void setDraggableMessage(String draggableMessage) {
        this.draggableMessage = draggableMessage;
    }

    public String getModalMessage() {
        return modalMessage;
    }

    public void setModalMessage(String modalMessage) {
        this.modalMessage = modalMessage;
    }

    public boolean isDraggableRendered() {
        return draggableRendered;
    }

    public void setDraggableRendered(boolean draggableRendered) {
        this.draggableRendered = draggableRendered;
    }

    public boolean getModalRendered() {
        return modalRendered;
    }

    public void setModalRendered(boolean modalRendered) {
        this.modalRendered = modalRendered;
    }
    
    public boolean getAutoCentre() {
        return autoCentre;
    }

    public void setAutoCentre(boolean autoCentre) {
        this.autoCentre = autoCentre;
    }

    public String getDetermineDraggableButtonText() {
        return messages.get("ModalPanelShow_"
                + draggableRendered);
    }

    public String getDetermineModalButtonText() {
        return messages.get("ModalPanelShow_"
                + !modalRendered);
    }

    public void toggleDraggable(ActionEvent event) {
        draggableRendered = !draggableRendered;
    }

    public void toggleModal(ActionEvent event) {
        modalRendered = !modalRendered;
    }
}
