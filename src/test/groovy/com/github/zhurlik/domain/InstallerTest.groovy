package com.github.zhurlik.domain

import org.junit.Test

/**
 * Unit tests for {@link Installer}.
 *
 * @author zhurlik@gmail.com
 */
class InstallerTest {
    @Test
    void testInstallNull() {
        final Installer installer = new Installer(null, null)
        installer.install()
    }

    @Test
    void testInstall() {
        final Installer installer = new Installer(
                { println 'a brwoser' },
                { println 'a webdriver' }
        )
        installer.install()
    }
}
