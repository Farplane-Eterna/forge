package forge.game.keyword;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for FFTCG keyword additions.
 */
public class KeywordTest {
    @Test
    public void testExBurstKeywordPresent() {
        assertNotNull(Keyword.smartValueOf("EX Burst"));
        assertEquals(Keyword.EX_BURST, Keyword.smartValueOf("EX Burst"));
    }
}
