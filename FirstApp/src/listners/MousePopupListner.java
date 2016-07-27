package listners;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

public class MousePopupListner implements MouseListener {

	JPopupMenu popup;
	public static Component parentComponent;
	public static Component currentComponent;

	@SuppressWarnings("static-access")
	public MousePopupListner(JPopupMenu popup, Component parentCompanent) {
		super();
		this.parentComponent = parentCompanent;
		this.popup = popup;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (event.isPopupTrigger()) {
			currentComponent = null;
			currentComponent = event.getComponent();
			System.out.println(event.getX() + " - " + event.getY());
			popup.show((JComponent) event.getSource(), event.getX(), event.getY());

		}
	}

}
