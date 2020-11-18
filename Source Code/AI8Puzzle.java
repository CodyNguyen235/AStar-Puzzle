
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AI8Puzzle
{
	private AStar aStar = new AStar();

	public static void main(String[] args)
	{
		boolean correctInput = false;
		Integer goodInput = 1;
		String start = "8-Puzzle Problem\nPlease select an option:\n1) Random Puzzle\n2) User-Defined Puzzle\n3) Exit";

		while (!correctInput)
		{
			System.out.println(start);
			Scanner scan = new Scanner(System.in);
			String badInput = scan.nextLine();

			try
			{
				goodInput = Integer.parseInt(badInput);
				if (goodInput > -1 && goodInput <= 4)
					break;
				else
					System.out.println("Error: Input is not valid");
			} catch (NumberFormatException e)
			{
				System.out.println("Error: Input is not a number. Please try again.");
			}
		}

		AI8Puzzle ai = new AI8Puzzle();
		switch (goodInput)
		{
		case 1:
			ai.randomPuzzle();
			break;
		case 2:
			ai.userPuzzle();
			break;
		case 3:
			System.exit(0);
			break;
		default:
			break;
		}
	}

	
	

	public void userPuzzle()
	{
		Puzzle puzzle = new InputPuzzle();
		boolean madePuzzle = false;
		while (!madePuzzle)
		{
			System.out.println("Enter a new puzzle:");
			Scanner scan = new Scanner(System.in);
			String tempPuzzle = "";
			String r1 = scan.nextLine().replace(" ", "");
			if (r1.length() == 9)
			{
				tempPuzzle = r1;
			} else
			{
				String r2 = scan.nextLine().replace(" ", "");
				String r3 = scan.nextLine().replace(" ", "");
				tempPuzzle = r1.replace("\n", "") + r2.replace("\n", "") + r3.replace("\n", "");
			}
			madePuzzle = puzzle.createPuzzle(tempPuzzle);
		}
		State init = puzzle.getInitialStateNode();
		SearchData compute = solve(init);
		System.out.println("d  | Total Cases | Search Cost H1 | Total Time H1 | Search Cost H2 | Total Time H2");
		System.out.println(compute.depth + " | " + 1 + " | " + compute.searchCostH1 + " | " + compute.totalTimeH1 + " | " + compute.searchCostH2
				+ " | " + compute.totalTimeH2);
	}
	
	public void randomPuzzle()
	{
		Map<Integer, ArrayList<SearchData>> runtime = new TreeMap<>();
		int runAmount = -1;
		System.out.println("How many times do you want to run the Puzzle?");
		Scanner scan = new Scanner(System.in);
		
		while (runAmount < 1)
		{
			String temp = scan.nextLine();
			try
			{
				runAmount = Integer.parseInt(temp);
			} catch (NumberFormatException e)
			{
				System.out.println("Error: Not a Number");
				runAmount = -1;
			}
			if (runAmount < 1)
				System.out.println("How many times do you want to run this? (Error: Must be greater than 0)");
		}
		
		File random = new File(runAmount + "_Random_Test_Cases.txt");
		BufferedWriter bw = null;
		
		try
		{
			random.createNewFile();
			bw = new BufferedWriter(new FileWriter(random));
		} catch (IOException ex)
		{

		}
		for (int i = 0; i < runAmount; ++i)
		{
			Puzzle puzzle = new RandomPuzzleGenerator();
			try
			{
				bw.write(puzzle.getInitialStateNode().toString().replace(" ", "").replace("\n", ""));
				bw.newLine();
			} catch (IOException ex)
			{
			}
			SearchData compute = solve(puzzle.getInitialStateNode());
			if (!runtime.containsKey(compute.depth))
			{
				runtime.put(compute.depth, new ArrayList<>());
			}
			runtime.get(compute.depth).add(compute);
		}
		try
		{
			bw.close();
		} catch (IOException ex)
		{
			Logger.getLogger(AI8Puzzle.class.getName()).log(Level.SEVERE, null, ex);
		}
		System.out.println("d  | Total Cases | Search Cost H1 | Total Time H1 | Search Cost H2 | Total Time H2");
		runtime.entrySet().stream().forEach((entry) ->
		{
			int h1AvgCost = 0, h1AvgTime = 0, h2AvgCost = 0, h2AvgTime = 0, total = entry.getValue().size();
			for (int i = 0; i < entry.getValue().size(); ++i)
			{
				SearchData data = entry.getValue().get(i);
				h1AvgCost += data.searchCostH1;
				h1AvgTime += data.totalTimeH1;
				h2AvgCost += data.searchCostH2;
				h2AvgTime += data.totalTimeH2;
			}
			System.out.println("------------------------------------------------------------------------");
			System.out.println(entry.getKey() + " | " + total + " | " + (h1AvgCost / total) + " | " + (h1AvgTime / total) + " ms | "
					+ (h2AvgCost / total) + " | " + (h2AvgTime / total) + " ms");
		});
	}


	private SearchData solve(State init)
	{
		System.out.println(init);
		System.out.println("---------- STARTING TO SOLVE PUZZLE USING H1 --------");
		long start1 = System.currentTimeMillis();
		State goalNode1 = aStar.runAStar(init, true, true);
		long end1 = System.currentTimeMillis();
		long total1 = end1 - start1;
		System.out.println("--------------- FINISHED H1, STARTING H2 -------------------");
		long start2 = System.currentTimeMillis();
		State goalNode2 = aStar.runAStar(init, false, true);
		long end2 = System.currentTimeMillis();
		long total2 = end2 - start2;
		System.out.println("--------------- FINISHED H2 -------------------");
		System.out.println("Solved With H1\nDepth: " + goalNode1.getCost() + " - Search Cost: " + goalNode1.getSearchCost() + " - Fringe Size: "
				+ goalNode1.getFringeSize() + " - Explored Set Size: " + goalNode1.getExploredSize() + " - Total Time: " + total1 + " ms");
		System.out.println("Solved Using H2\nDepth: " + goalNode2.getCost() + " - Search Cost: " + goalNode2.getSearchCost() + " - Fringe Size: "
				+ goalNode2.getFringeSize() + " - Explored Set Size: " + goalNode2.getExploredSize() + " - Total Time: " + total2 + " ms" + "\n");
		if (goalNode1.getCost() != goalNode2.getCost())
		{
			System.out.println(goalNode1.getCost() + " != " + goalNode2.getCost());
			System.out.println("The depths calculated from the heursitics are not equal. Exitting program.");
			System.exit(0);
		}
		return new SearchData(goalNode1.getCost(), goalNode1.getSearchCost(), total1, goalNode2.getSearchCost(), total2);
	}

	private class SearchData
	{
		public int depth;
		public int searchCostH1;
		public long totalTimeH1;
		public int searchCostH2;
		public long totalTimeH2;

		public SearchData(int d, int sCostH1, long tTimeH1, int sCostH2, long tTimeH2)
		{
			depth = d;
			searchCostH1 = sCostH1;
			totalTimeH1 = tTimeH1;
			searchCostH2 = sCostH2;
			totalTimeH2 = tTimeH2;
		}
		
	}
}
