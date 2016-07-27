
package views;

import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.core.runtime.jobs.ProgressProvider;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;

public class ProgresMonitor {
	private final UISynchronize sync;
	private ProgressBar progressBar;
	private GobalProgressMonitor monitor;

	@Inject
	public ProgresMonitor(UISynchronize sync) {
		this.sync = Objects.requireNonNull(sync);
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		monitor = new GobalProgressMonitor();
		Job.getJobManager().setProgressProvider(new ProgressProvider() {
			@Override
			public IProgressMonitor createMonitor(Job job) {
				return monitor.addJob(job);
			}
		});


		progressBar = new ProgressBar(parent, SWT.SMOOTH);
		GridData gd_progressBar = new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1);
		gd_progressBar.widthHint = 671;
		progressBar.setLayoutData(gd_progressBar);
		/*
		 * OutputStream out = new OutputStream() {
		 * 
		 * @Override public void write(int b) throws IOException { final String
		 * toAppend = String.valueOf((char) b);
		 * parent.getDisplay().asyncExec(new Runnable() { public void run() { if
		 * (text.isDisposed()) return; text.append(toAppend); } }); } };
		 * 
		 * System.setOut(new PrintStream(out));
		 */
	}

	private final class GobalProgressMonitor extends NullProgressMonitor {
		private long runningTasks = 0L;

		public void beginTask(String name, int totalWork) {
			sync.syncExec(new Runnable() {
				@Override
				public void run() {
					if (runningTasks <= 0) {
						progressBar.setSelection(0);
						progressBar.setMaximum(totalWork);

					} else {
						progressBar.setMaximum(progressBar.getMaximum() + totalWork);
					}

					runningTasks++;
					progressBar.setToolTipText("Currently running: " + runningTasks + "\nLast task: " + name);
				}
			});
		}

		@Override
		public void worked(int work) {
			sync.syncExec(new Runnable() {
				@Override
				public void run() {
					progressBar.setSelection(progressBar.getSelection() + work);
				}
			});
		}

		public IProgressMonitor addJob(Job job) {
			if (job != null) {
				job.addJobChangeListener(new JobChangeAdapter() {
					@Override
					public void done(IJobChangeEvent event) {
						sync.syncExec(new Runnable() {
							@Override
							public void run() {
								runningTasks--;
								if (runningTasks > 0) {
									// --- some tasks are still running ---
									progressBar.setToolTipText("Currently running: " + runningTasks);

								} else {
									// --- all tasks are done (a reset of
									// selection could also be done) ---
									progressBar.setToolTipText("No background progress running.");
								}
							}
						});

						// clean-up
						event.getJob().removeJobChangeListener(this);
					}
				});

			}
			return this;
		}

	}

}