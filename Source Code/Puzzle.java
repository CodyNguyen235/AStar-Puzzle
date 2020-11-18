
public class Puzzle
{
	private Integer[] initialState = new Integer[9];
	private State initialStateNode;

	final private Integer[] goalState =
	{ 0, 1, 2, 3, 4, 5, 6, 7, 8 };

	public void setInitalState(Integer[] state)
	{
		initialState = state;
	}

	public void setInitialStateNode(State init)
	{
		initialStateNode = init;
	}

	public State getInitialStateNode()
	{
		return initialStateNode;
	}

	public Integer[] getInitialState()
	{
		return initialState;
	}

	public Integer[] getGoalState()
	{
		return goalState;
	}

	public boolean createPuzzle(String sPuz)
	{
		return false;
	}

	public boolean checkSolvable(Integer[] board)
	{
		int inversions = 0;
		for (int i = 0; i < board.length - 1; ++i)
		{
			for (int j = i + 1; j < board.length; ++j)
			{
				if (board[i] != 0 && board[j] != 0 && board[i] > board[j])
					inversions++;
			}
		}
		return inversions % 2 == 0;
	}
}
