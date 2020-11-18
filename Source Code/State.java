
import java.util.ArrayList;
import java.util.Arrays;

public class State
{
	private State pred;
	final private Integer[] initial;
	final private Integer[] current;
	final private int cost;
	final private String actionsTaken;
	final private int emptyPosition;
	private int fringe = 0;
	private int explored = 0;
	private int searchCost = 0;

	public State()
	{
		initial = new Integer[9];
		Arrays.fill(initial, -1);
		current = initial;
		cost = 0;
		actionsTaken = "noop";
		emptyPosition = 0;
	}

	public State(Integer[] initState, Integer[] currState, int cost, String actionsTaken, State predecssor, int emptyPos)
	{
		initial = initState;
		current = currState;
		this.cost = cost;
		this.actionsTaken = actionsTaken;
		this.pred = predecssor;
		emptyPosition = emptyPos;
	}

	public State(State node)
	{
		this(node.getInitialState(), node.getCurrentState(), node.getCost(), node.getAction(), node.getPredecessor(), node.getEmptyPosition());
	}

	public void setFringeSize(int fSize)
	{
		fringe = fSize;
	}

	public void setExploredSize(int eSize)
	{
		explored = eSize;
	}

	public void setSearchCost(int searchCost)
	{
		this.searchCost = searchCost;
	}

	public int getSearchCost()
	{
		return searchCost;
	}

	public State getPredecessor()
	{
		return pred;
	}

	public Integer[] getInitialState()
	{
		return initial;
	}

	public Integer[] getCurrentState()
	{
		return current;
	}

	public int getCost()
	{
		return cost;
	}

	public String getAction()
	{
		return actionsTaken;
	}

	public int getEmptyPosition()
	{
		return emptyPosition;
	}

	public int getFringeSize()
	{
		return fringe;
	}

	public int getExploredSize()
	{
		return explored;
	}

	public boolean inBounds(String action)
	{
		Integer[] currentBoard = current;
		int emptyPos = emptyPosition;
		switch (action)
		{
		case "up":
			if (emptyPos - 3 < 0)
				return false;
			break;
		case "down":
			if (emptyPos + 3 >= currentBoard.length)
				return false;
			break;
		case "left":
			if (emptyPos == 0)
				return false;
			if (emptyPos % 3 == 0)
				return false;
			break;
		case "right":
			if (emptyPos + 1 % 3 == 0)
				return false;
			break;
		default:
			return false;
		}
		return true;
	}


	public State generateNode(String action)
	{
		State node;
		Integer[] newState = current.clone();
		int newEmpty = emptyPosition;
		switch (action)
		{
		case "up":
			if (emptyPosition - 3 >= 0)
			{
				newState = swap(newState, emptyPosition, emptyPosition - 3);
				newEmpty = emptyPosition - 3;
			}
			break;
		case "down":
			if (emptyPosition + 3 <= current.length)
			{
				newState = swap(newState, emptyPosition, emptyPosition + 3);
				newEmpty = emptyPosition + 3;
			}
			break;
		case "left":
			if (emptyPosition == 0)
				break;
			if (emptyPosition % 3 != 0)
			{
				newState = swap(newState, emptyPosition, emptyPosition - 1);
				newEmpty = emptyPosition - 1;
			}
			break;
		case "right":
			if ((emptyPosition + 1) % 3 != 0)
			{
				newState = swap(newState, emptyPosition, emptyPosition + 1);
				newEmpty = emptyPosition + 1;
			}
			break;
		default:
		}
		node = new State(initial, newState, getCost() + 1, action, this, newEmpty);
		return node;
	}

	private Integer[] swap(Integer[] arr, int pos1, int pos2)
	{
		Integer temp = arr[pos1];
		arr[pos1] = arr[pos2];
		arr[pos2] = temp;
		return arr;
	}

	public ArrayList<State> expandCurrentNode()
	{
		ArrayList<State> successorList = new ArrayList<>();
		if (inBounds("up"))
			successorList.add(generateNode("up"));
		if (inBounds("down"))
			successorList.add(generateNode("down"));
		if (inBounds("left"))
			successorList.add(generateNode("left"));
		if (inBounds("right"))
			successorList.add(generateNode("right"));
		return successorList;
	}


	public int hashCode()
	{
		return Arrays.hashCode(current);
	}


	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final State other = (State) obj;
		return Arrays.deepEquals(this.current, other.current);
	}


	public String toString()
	{
		String board = "";
		for (int i = 0; i < current.length; ++i)
		{
			if (i != 0 && i % 3 == 0)
				board += "\n";
			board += current[i] + " ";
		}
		return board;
	}
}
