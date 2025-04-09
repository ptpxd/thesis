import org.junit.jupiter.api.Test;
import roadbuilder.app.ProgressManager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProgressManagerTest {

    @Test
    public void test_level_one_unlocked_initially() {
        // Arrange

        // Act
        boolean isUnlocked = ProgressManager.isLevelUnlocked(1);

        // Assert
        assertTrue(isUnlocked, "Level 1 should be unlocked initially");
    }

    @Test
    public void test_zero_or_negative_levels_not_unlocked() {
        // Arrange

        // Act
        boolean isLevelZeroUnlocked = ProgressManager.isLevelUnlocked(0);
        boolean isNegativeLevelUnlocked = ProgressManager.isLevelUnlocked(-1);

        // Assert
        assertFalse(isLevelZeroUnlocked, "Level 0 should not be unlocked");
        assertFalse(isNegativeLevelUnlocked, "Negative levels should not be unlocked");
    }

    @Test
    public void test_level_six_not_unlocked() {
        // Arrange

        // Act
        boolean isLevelSixUnlocked = ProgressManager.isLevelUnlocked(6);

        // Assert
        assertFalse(isLevelSixUnlocked, "Level 6 should not be unlocked");
    }
}