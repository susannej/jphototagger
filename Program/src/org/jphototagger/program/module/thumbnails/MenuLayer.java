package org.jphototagger.program.module.thumbnails;

import java.util.Arrays;
import java.util.Collection;

import org.openide.util.lookup.ServiceProvider;

import org.jphototagger.api.windows.MainWindowMenuProvider;
import org.jphototagger.api.windows.MenuItemProvider;
import org.jphototagger.lib.api.MainWindowMenuProviderAdapter;
import org.jphototagger.lib.api.MenuItemProviderImpl;

/**
 * @author Elmar Baumann
 */
@ServiceProvider(service = MainWindowMenuProvider.class)
public final class MenuLayer extends MainWindowMenuProviderAdapter {

    @Override
    public Collection<? extends MenuItemProvider> getViewMenuItems() {
        return Arrays.asList(new MenuItemProviderImpl(new ToggleMetaDataOverlayAction(), 100, false));
    }
}