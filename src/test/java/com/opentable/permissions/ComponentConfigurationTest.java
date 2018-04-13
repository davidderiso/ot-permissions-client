package com.opentable.permissions;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes= {
        PermissionsClientConfiguration.class
})
@TestPropertySource(properties= {
    "ot.component.config=foo"
})
@RunWith(SpringRunner.class)
public class ComponentConfigurationTest {
    @Inject
    PermissionsClientConfiguration component;

    @Test
    public void testComponent() {
        assert (true);
    }
}
