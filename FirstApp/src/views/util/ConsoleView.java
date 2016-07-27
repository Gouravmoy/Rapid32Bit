package views.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class ConsoleView {
	private Text text;

	@Inject
	public ConsoleView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		final PrintStream oldOut = System.out;
		parent.setLayout(new GridLayout(1, false));
		text = new Text(parent, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_text.heightHint = 196;
		gd_text.widthHint = 744;
		text.setLayoutData(gd_text);
		text.setLayoutData(gd_text);
		text.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				System.setOut(oldOut);
			}
		});

		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				final String toAppend = String.valueOf((char) b);
				parent.getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (text.isDisposed())
							return;
						text.append(toAppend);
					}
				});
			}
		};
		System.setOut(new PrintStream(out));

	}
}
