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

    public static boolean isLevelCompleted(int level) {
        return level <= highestCompletedLevel;
    }

    public static boolean isLevelUnlocked(int level) {
        return unlockedLevels.contains(level);
    }

    public static void markLevelAsCompleted(int level) {

        markLevelAsCompleted(level, true);
    }

    public static void markLevelAsCompleted(int level, boolean isCompleteGraph) {
        if (level > highestCompletedLevel) {
            highestCompletedLevel = level;
            unlockedLevels.add(level);

            if (level == 2) {
                if (isCompleteGraph) {
                    unlockedLevels.add(3);
                    System.out.println("ProgressManager: Level " + level
                        + " marked as completed with a complete graph. Level 3 unlocked.");
                } else {
                    System.out.println("ProgressManager: Level " + level
                        + " marked as completed, but the graph was not complete. Level 3 remains locked.");
                }
            } else {
                unlockedLevels.add(level + 1);
                System.out.println("ProgressManager: Level " + level
                    + " marked as completed. Level " + (level + 1) + " unlocked.");
            }
        }
    }
}