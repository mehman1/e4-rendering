package com.toedter.e4.ui.workbench.swt;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.contributions.IContributionFactory;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.toedter.e4.ui.workbench.generic.GenericPresentationEngine;
import com.toedter.e4.ui.workbench.generic.IRendererFactory;

@SuppressWarnings("restriction")
public class SWTPresentationEngine extends GenericPresentationEngine {

	public SWTPresentationEngine() {
		System.out.println("SWTPresentationEngine");
	}

	@Override
	public Object run(final MApplicationElement uiRoot, final IEclipseContext appContext) {

		Realm.runWithDefault(SWTObservables.getRealm(Display.getDefault()), new Runnable() {
			@Override
			public void run() {
				if (uiRoot instanceof MApplication) {
					MApplication theApp = (MApplication) uiRoot;
					for (MWindow window : theApp.getChildren()) {
						createGui(window);
					}
				}
			}
		});

		Shell appShell = null;
		if (uiRoot instanceof MApplication) {
			MApplication theApp = (MApplication) uiRoot;
			for (MWindow window : theApp.getChildren()) {
				if (window.getWidget() instanceof Shell) {
					appShell = (Shell) window.getWidget();
					break;
				}
			}

			if (appShell != null) {
				Display display = appShell.getDisplay();
				while (appShell != null && !appShell.isDisposed()) {
					if (!display.readAndDispatch()) {
						appContext.processWaiting();
						display.sleep();
					}
				}
			}
		}

		System.out.println("SWTPresentationEngine.run(): Finished");
		return IApplication.EXIT_OK;
	}

	@Override
	@PostConstruct
	public void postConstruct(IEclipseContext context) {
		System.out.println("SWTPresentationEngine.postConstruct()");
		// Add the presentation engine to the context
		context.set(IPresentationEngine.class.getName(), this);

		// TODO use parameter or registry
		IContributionFactory contribFactory = context.get(IContributionFactory.class);
		try {
			rendererFactory = (IRendererFactory) contribFactory.create(
					"platform:/plugin/com.toedter.e4.ui.workbench.renderers.swt/"
							+ "com.toedter.e4.ui.workbench.renderers.swt.SWTRendererFactory", context);
		} catch (Exception e) {
			logger.warn(e, "Could not create rendering factory");
		}
	}
}