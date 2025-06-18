import java.util.*;

public class KnightsTourWarnsdorff {
    private static final int N = 8;
    private int[][] board;
    private int[] dx = {2, 1, -1, -2, -2, -1, 1, 2};
    private int[] dy = {1, 2, 2, 1, -1, -2, -2, -1};

    public KnightsTourWarnsdorff() {
        board = new int[N][N];
        initBoard();
    }

    private void initBoard() {
        for (int[] row : board) {
            Arrays.fill(row, -1);
        }
    }

    private boolean isSafe(int x, int y) {
        return x >= 0 && y >= 0 && x < N && y < N && board[x][y] == -1;
    }

    private int countNextMoves(int x, int y, Integer[] dirs) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[dirs[i]];
            int ny = y + dy[dirs[i]];
            if (isSafe(nx, ny)) count++;
        }
        return count;
    }

    private boolean solveWarnsdorff(int startX, int startY, Integer[] dirs) {
        initBoard();
        board[startX][startY] = 1;

        int x = startX;
        int y = startY;

        for (int movei = 2; movei <= N * N; movei++) {
            int minDeg = 9;
            int nextX = -1, nextY = -1;

            for (int i = 0; i < 8; i++) {
                int nx = x + dx[dirs[i]];
                int ny = y + dy[dirs[i]];
                if (isSafe(nx, ny)) {
                    int c = countNextMoves(nx, ny, dirs);
                    if (c < minDeg) {
                        minDeg = c;
                        nextX = nx;
                        nextY = ny;
                    }
                }
            }

            if (nextX == -1 || nextY == -1) return false;

            x = nextX;
            y = nextY;
            board[x][y] = movei;
        }

        return true;
    }

    public boolean solveWithRetries(int startX, int startY, int maxTries) {
        Integer[] dirs = {0, 1, 2, 3, 4, 5, 6, 7};

        for (int attempt = 1; attempt <= maxTries; attempt++) {
            if (solveWarnsdorff(startX, startY, dirs)) {
                System.out.println("Solusi ditemukan setelah " + attempt + " percobaan.");
                return true;
            }
            Collections.shuffle(Arrays.asList(dirs));
        }

        return false;
    }

    public void printSolution() {
        System.out.println("    A   B   C   D   E   F   G   H");
        System.out.println("  +---+---+---+---+---+---+---+---+");
        for (int i = 0; i < N; i++) {
            System.out.print((i + 1) + " |");
            for (int j = 0; j < N; j++) {
                if (board[i][j] == -1) {
                    System.out.print("   |");
                } else {
                    System.out.printf("%2d |", board[i][j]);
                }
            }
            System.out.println();
            System.out.println("  +---+---+---+---+---+---+---+---+");
        }
    }

    public static void main(String[] args) {
        KnightsTourWarnsdorff kt = new KnightsTourWarnsdorff();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Masukkan posisi awal kuda (contoh: 2B): ");
        String input = scanner.nextLine().trim().toUpperCase();

        if (input.length() < 2 || input.length() > 3) {
            System.out.println("Input tidak valid.");
            return;
        }

        try {
            int row = Integer.parseInt(input.substring(0, input.length() - 1)) - 1;
            char colChar = input.charAt(input.length() - 1);
            int col = colChar - 'A';

            if (row < 0 || row >= N || col < 0 || col >= N) {
                System.out.println("Input tidak valid.");
            } else {
                boolean solved = kt.solveWithRetries(row, col, 100);
                if (solved) {
                    kt.printSolution();
                } else {
                    System.out.println("Tidak ada solusi ditemukan setelah 100 percobaan.");
                }
            }
        } catch (Exception e) {
            System.out.println("Input tidak valid.");
        }

        scanner.close();
    }
}