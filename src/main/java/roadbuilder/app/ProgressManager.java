package roadbuilder.app;

import java.util.HashSet;
import java.util.Set;

public class ProgressManager {
    private static int highestCompletedLevel = 0;
    private static final Set<Integer> unlockedLevels = new HashSet<>();

    static {
        // Only level 1 is unlocked initially.
        unlockedLevels.add(1);
    }

    public static int getHighestCompletedLevel() {
        return highestCompletedLevel;
    }

    // New helper to check if a level has been completed.
    public static boolean isLevelCompleted(int level) {
        return level <= highestCompletedLevel;
    }

    public static boolean isLevelUnlocked(int level) {
        return unlockedLevels.contains(level);
    }

    /**
     * Marks the given level as completed.
     * For level 2, it unlocks level 3 only if the graph is complete.
     * For all other levels, it follows the default behavior of unlocking the next level.
     *
     * This method calls the overloaded markLevelAsCompleted(level, isCompleteGraph)
     * with isCompleteGraph = true by default.
     *
     * @param level the level that was completed
     */
    public static void markLevelAsCompleted(int level) {
        // Default behavior: assume complete graph for levels other than 2.
        markLevelAsCompleted(level, true);
    }

    /**
     * Marks the given level as completed.
     * If level 2 is completed, level 3 is unlocked only if isCompleteGraph is true.
     * For any other level, the next level is unlocked without checking the graph type.
     *
     * IMPORTANT: Ensure that the level number passed in matches the game design.
     * For example, a complete graph in level1 should not affect level3; the unlocking
     * of level3 is allowed only from level2 assuming complete graph conditions.
     *
     * @param level the level that was completed
     * @param isCompleteGraph indicates whether the graph in this level is complete.
     */
    public static void markLevelAsCompleted(int level, boolean isCompleteGraph) {
        // Only update if a higher level has been completed.
        if (level > highestCompletedLevel) {
            highestCompletedLevel = level;
            unlockedLevels.add(level);

            if (level == 2) {
                // For level 2, only unlock level 3 if the graph is complete.
                if (isCompleteGraph) {
                    unlockedLevels.add(3);
                    System.out.println("ProgressManager: Level " + level
                        + " marked as completed with a complete graph. Level 3 unlocked.");
                } else {
                    System.out.println("ProgressManager: Level " + level
                        + " marked as completed, but the graph was not complete. Level 3 remains locked.");
                }
            } else {
                // For levels other than 2, simply unlock the next level.
                // This ensures that a complete graph in level1 does not mistakenly unlock level3.
                unlockedLevels.add(level + 1);
                System.out.println("ProgressManager: Level " + level
                    + " marked as completed. Level " + (level + 1) + " unlocked.");
            }
        }
    }
}