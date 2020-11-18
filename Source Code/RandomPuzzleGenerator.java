
import java.util.Arrays;
import java.util.Random;

public class RandomPuzzleGenerator extends Puzzle
{
	public RandomPuzzleGenerator()
	{
		createPuzzle();
	}

	private void createPuzzle()
	{
		boolean solvable = false;
		Integer[] board = new Integer[9];
		Arrays.fill(board, -1);
		int failedCounter = 0;
		int emptyPosition = 0;
		while (!solvable)
		{
			Random rand = new Random();
			board = new Integer[9];
			Arrays.fill(board, -1);
			for (int i = 0; i < board.length; ++i)
			{
				Integer tile = rand.nextInt(9);
				while (arrIndexOf(board, tile) != -1)
				{
					tile = rand.nextInt(9);
				}
				if (tile.equals(0))
					emptyPosition = i;
				board[i] = tile;
			}
			solvable = checkSolvable(board);
			if (solvable == false)
				failedCounter++;
		}
		System.out.println("Failed (" + failedCounter + ") Number of Times.");
		System.out.println("Empty Position = " + emptyPosition);
		setInitalState(board);
		setInitialStateNode(new State(board, board, 0, "noop", null, emptyPosition));
	}

	private int arrIndexOf(Integer[] arr, Integer searchFor)
	{
		for (int i = 0; i < arr.length; ++i)
		{
			if (arr[i].equals(searchFor))
				return i;
		}
		return -1;
	}
}
