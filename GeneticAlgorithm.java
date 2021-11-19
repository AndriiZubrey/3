package myproject;

import java.util.ArrayList;

public class GeneticAlgorithm {

    private final int maxWeight;
    private final int itemsAmount;
    private final int solutionListSize;
    private final ArrayList<Item> items;


    public GeneticAlgorithm(int maxWeight, int solutionListSize, ArrayList<Item> items)
    {
        this.maxWeight = maxWeight;
        this.solutionListSize = solutionListSize;
        this.itemsAmount = items.size();
        this.items = items;
    }

    public Solution start(int iterationsAmount)
    {
        ArrayList<Solution> solutionList = createStartSolutionList();

        for (int i = 0; i < iterationsAmount; i++)
        {
            solutionList = selection(solutionList);
            solutionList = cross(solutionList);
            mutation(solutionList);
            solutionList = improveOperator(solutionList);
        }

        return findBest(solutionList);
    }

    private ArrayList<Solution> createStartSolutionList()
    {
        ArrayList<Solution> solutionList = new ArrayList<>();
        ArrayList<Boolean> availability = new ArrayList<>();
        for (int i = 0; i < itemsAmount; i++)
        {
            availability.add(false);
        }

        for (int i = 0; i < solutionListSize; i++)
        {
            int pos = (int)(Math.random()*itemsAmount);
            availability.set(pos, true);
            Solution ind = new Solution(availability);
            solutionList.add(ind);
            availability.set(pos, false);
        }
        return solutionList;
    }


    private ArrayList<Solution> selection(ArrayList<Solution> solutionList)
    {
        ArrayList<Solution> newSolutionList = new ArrayList<>();

        for (int i = 0; i < solutionList.size(); i++)
        {
            int ind1 = (int)(Math.random()*solutionList.size());
            int ind2 = (int)(Math.random()*solutionList.size());
            int ind3 = (int)(Math.random()*solutionList.size());
            while(ind1 == ind2 || ind2==ind3 || ind1==ind3)
            {
                ind2 = (int)(Math.random()*solutionList.size());
                ind3 = (int)(Math.random()*solutionList.size());
            }

            Solution bestSolution = solutionList.get(ind1);

            if(bestSolution.price < solutionList.get(ind2).price)
                bestSolution = solutionList.get(ind2);

            if(bestSolution.price < solutionList.get(ind3).price)
                bestSolution = solutionList.get(ind3);

            newSolutionList.add(new Solution(bestSolution.availability));
        }
        return newSolutionList;
    }

    private ArrayList<Solution> cross(ArrayList<Solution> solutionList)
    {
        ArrayList<Solution> newSolutionList = new ArrayList<>();

        for (int i = 0; i < solutionList.size(); i+=2)
        {
            ArrayList<Boolean> availability1 = new ArrayList<>();
            ArrayList<Boolean> availability2 = new ArrayList<>();
            ArrayList<Boolean> sol1 = solutionList.get(i).availability;
            ArrayList<Boolean> sol2 = solutionList.get(i+1).availability;
            for (int j = 0; j < itemsAmount; j++)
            {
                if(j < itemsAmount*(1D/3D) || j > itemsAmount*(2D/3D))
                {
                    availability1.add(sol2.get(j));
                    availability2.add(sol1.get(j));
                }
                else
                {
                    availability1.add(sol1.get(j));
                    availability2.add(sol2.get(j));
                }
            }
            Solution child1 = new Solution(availability1);
            Solution child2 = new Solution(availability2);

            if(child1.weight > maxWeight)
                child1 = new Solution(sol1);
            if(child2.weight > maxWeight)
                child2 = new Solution(sol2);

            newSolutionList.add(child1);
            newSolutionList.add(child2);
        }
        return newSolutionList;
    }

    private void mutation(ArrayList<Solution> solutionList)
    {
        for (Solution solution : solutionList)
        {
            if(Math.random() < 0.1)
            {
                int ind1 = (int)(Math.random()*itemsAmount);

                solution.availability.set(ind1,!solution.availability.get(ind1));
                solution.calc();
                if(solution.weight > maxWeight)
                {
                    solution.availability.set(ind1,!solution.availability.get(ind1));
                    solution.calc();
                }
            }
        }
    }

    private ArrayList<Solution> improveOperator(ArrayList<Solution> solutionList)
    {
        ArrayList<Solution> newSolutionList = new ArrayList<>();
        for(Solution solution : solutionList)
        {
            Solution newSolution = new Solution(solution.availability);
            int maxWeightIndex = 0;
            int maxWeight = 0;
            int maxPriceIndex = 0;
            int maxPrice = 0;
            for (int i = 0; i < newSolution.availability.size(); i++)
            {
                boolean isInBag = newSolution.availability.get(i);
                int weight = items.get(i).getWeight();
                int price = items.get(i).getPrice();
                if(isInBag && weight > maxWeight)
                {
                    maxWeight = weight;
                    maxWeightIndex = i;
                }
                if(!isInBag && price > maxPrice)
                {
                    maxPrice = price;
                    maxPriceIndex = i;
                }
            }
            newSolution.availability.set(maxWeightIndex, false);
            newSolution.availability.set(maxPriceIndex, true);
            newSolution.calc();
            if(newSolution.weight>this.maxWeight || newSolution.price < solution.price)
            {
                newSolutionList.add(solution);
            }
            else
            {
                newSolutionList.add(newSolution);
            }
        }
        return newSolutionList;
    }

    private Solution findBest(ArrayList<Solution> solutionList)
    {
        Solution bestSolution = solutionList.get(0);
        for (int i = 1; i < solutionList.size(); i++)
        {
          if(bestSolution.price < solutionList.get(i).price)
              bestSolution = solutionList.get(i);
        }
        return bestSolution;
    }

    private class Solution
    {
        public int price;
        public int weight;
        public ArrayList<Boolean> availability;

        public Solution(ArrayList<Boolean> availability)
        {
            this.availability = new ArrayList<>(availability);
            calc();

        }
        public void calc()
        {
            int value = 0;
            int weight = 0;
            for (int i = 0; i < availability.size(); i++)
            {
               if(availability.get(i))
               {
                   value += items.get(i).getPrice();
                   weight += items.get(i).getWeight();
               }
            }
            this.price = value;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "Solution{" +
                    "price=" + price +
                    ", weight=" + weight +
                    ", availability=" + availability +
                    '}';
        }
    }
}
