package roadbuilder.app;

import java.util.HashSet;
import java.util.Set;

public class ProgressManager {
    private static int highestCompletedLevel = 0;
    private static final Set<Integer> unlockedLevels = new HashSet<>();

    static {
        unlockedLevels.add(1);
    }

    public static int getHighestCompletedLevel() {
        return highestCompletedLevel;
    }

    public static boolean isLevelUnlocked(int level) {
        return unlockedLevels.contains(level);
    }

    public static void markLevelAsCompleted(int level) {
        if (level > highestCompletedLevel) {
            highestCompletedLevel = level;
            unlockedLevels.add(level);
            unlockedLevels.add(level + 1);
            System.out.println("ProgressManager: Level " + level + " marked as completed. Level " + (level + 1) + " unlocked.");
        }
    }
}