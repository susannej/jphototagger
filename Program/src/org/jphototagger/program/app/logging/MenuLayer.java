package org.jphototagger.program.app.logging;

import java.util.Arrays;
import java.util.Collection;
import org.jphototagger.api.windows.MainWindowMenuProvider;
import org.jphototagger.api.windows.MenuItemProvider;
import org.jphototagger.lib.api.MainWindowMenuProviderAdapter;
import org.jphototagger.lib.api.MenuItemProviderImpl;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Elmar Baumann
 */
@ServiceProvider(service = MainWindowMenuProvider.class)
public final class MenuLayer extends MainWindowMenuProviderAdapter {

    @Override
    public Collection<? extends MenuItemProvider> getWindowMenuItems() {
        return Arrays.asList(
                new MenuItemProviderImpl(new ShowErrorLogfileAction(), 500, true),
                new MenuItemProviderImpl(new ShowAllMessagesLogfileAction(), 600, false));
    }
}
