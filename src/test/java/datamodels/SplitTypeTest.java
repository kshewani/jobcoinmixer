package datamodels;

import junit.framework.TestCase;

public class SplitTypeTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
    }

    public void testValues() {
        Enum.valueOf(SplitType.class, "SplitType");
    }

    public void testValueOf() {
    }
}