
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;

public class AStar
{
	private PriorityQueue<State> fringes;
	private HashSet<State> explored;
	private final Integer[] goal =
	{ 0, 1, 2, 3, 4, 5, 6, 7, 8 };

	public State runAStar(State initial, boolean isH1, boolean print)
	{
		int searchCost = 0;
		explored = new HashSet<>();
		if (isH1)
		{
			fringes = new PriorityQueue<>((final State o1, final State o2) -> (o1.getCost() + misplaced(o1)) - (o2.getCost() + misplaced(o2)));
		} else
		{
			fringes = new PriorityQueue<>((final State o1, final State o2) -> (o1.getCost() + distanceSum(o1)) - (o2.getCost() + distanceSum(o2)));
		}
		fringes.add(initial);
		while (!fringes.isEmpty())
		{
			State current = fringes.poll();
			explored.add(current);
			if (print == true)
			{
				System.out.println(current);
				System.out.println("------ Current Step Cost: " + current.getCost() + " - Search Cost: " + searchCost + "  - fringes Size: "
						+ fringes.size() + " - Explored Size:  " + explored.size() + " -------");
			}
			if (Arrays.equals(current.getCurrentState(), goal))
			{
				System.out.println("----------- GOAL FOUND AT SEARCH COST OF " + searchCost + "------------");
				return current;
			}
			ArrayList<State> children = current.expandCurrentNode();
			for (int i = 0; i < children.size(); ++i)
			{
				if (!explored.contains(children.get(i)))
				{
					searchCost++;
					children.get(i).setSearchCost(searchCost);
					children.get(i).setExploredSize(explored.size());
					children.get(i).setFringeSize(fringes.size());
					fringes.add(children.get(i));
				}
			}
		}
		return null;
	}

	public int misplaced(State node)
	{
		int misplaced = 0;
		for (int i = 0; i < node.getCurrentState().length; ++i)
		{
			if (node.getCurrentState()[i] != i)
				misplaced++;
		}
		return misplaced;
	}

	public int distanceSum(State node)
	{
		int sum = 0;
		for (int i = 0; i < node.getCurrentState().length; ++i)
		{
			if (node.getCurrentState()[i] == i)
				continue;
			if (node.getCurrentState()[i] == 0)
				continue;
			int row = node.getCurrentState()[i] / 3;
			int col = node.getCurrentState()[i] % 3;
			int goalRow = i / 3;
			int goalCol = i % 3;
			sum += Math.abs(col - goalCol) + Math.abs(row - goalRow);
		}
		return sum;
	}

}
