package test;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;

import javax.swing.JPanel;

public class MyDropTargetListener extends DropTargetAdapter {

	public MyDropTargetListener(JPanel panel) {
		new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
	}

	@Override
	public void drop(DropTargetDropEvent event) {

		try {

			Transferable tr = event.getTransferable();
			String transferData = (String) tr.getTransferData(DataFlavor.stringFlavor);
			System.out.println(transferData);

			// transferDate = the name of the dropped object as a string;

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
