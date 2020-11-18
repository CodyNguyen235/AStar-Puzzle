
public class InputPuzzle extends Puzzle
{

	public boolean createPuzzle(String tempPuzzle)
	{
		Integer[] puzzle =
		{ -1, -1, -1, -1, -1, -1, -1, -1, -1 };
		int empty = -1;
		if (tempPuzzle.length() < 9)
		{
			System.out.println("The puzzle must contain 9 numbers");
			return false;
		}
		for (int i = 0; i < puzzle.length; ++i)
		{
			Integer val;
			try
			{
				val = Integer.parseInt(Character.toString(tempPuzzle.charAt(i)));
			} catch (NumberFormatException e)
			{
				System.out.println("The string contained a non-number, try again");
				return false;
			}
			if (val == 0)
				empty = i;
			if (val < 0)
			{
				System.out.println("The number must be within 0 and 8");
				return false;
			} else if (val > 8)
			{
				System.out.println("The number must be within 0 and 8");
				return false;
			}
			if (arrIndexOf(puzzle, val) == -1)
				puzzle[i] = val;
			else
			{
				System.out.println("The string contains duplicates");
				return false;
			}
		}
		boolean canSolve = checkSolvable(puzzle);
		if (canSolve == false)
			System.out.println("This puzzle is not solvable.");
		else
		{
			System.out.println("Empty Pos: " + empty);
			setInitalState(puzzle);
			setInitialStateNode(new State(puzzle, puzzle, 0, "noop", null, empty));
		}
		return canSolve;
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
