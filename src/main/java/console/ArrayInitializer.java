package console;

import java.util.Scanner;

public class ArrayInitializer {

    public static void running(String[] args) {
        int rows = 10;
        int columns = 10;
        BoardModel boardModel = new BoardModel(rows, columns);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            Printer.printArray(boardModel.getBoard());
            System.out.println("Enter row, column, and value separated by spaces (e.g., 6 7 1): ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            if (parts.length == 3) {
                try {
                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);
                    int value = Integer.parseInt(parts[2]);

                    if (!boardModel.setElement(row, col, value)) {
                        System.out.println("Invalid input. Please enter valid row, column, and value (0 or 1).");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter integers.");
                }
            } else {
                System.out.println("Invalid input format. Please enter exactly three values.");
            }
        }
    }
}