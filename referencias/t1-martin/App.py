import numpy as np 
import Player
import Policy
# put the text file into a matrix -> done
# create the function that move the agents in the maze -> Done
# create the function to randomize the path of the agent -> Done
# put all the walls in a structure -> Done
# create a fitness function to define if the path is good or not
# create the crossover funtion
# create the mutation function
# create the function to define wich path to take (the response)
#3,3,3
MOVE_LIMIT = 30
POPULATION_SIZE = 2000

# skipping first row
data = np.loadtxt("labirinto.txt", skiprows=1, dtype='str')
final = np.where(data == 'S')
# position of 'S'
finalX = final[0][0]
finalY = final[1][0]

print('Digite 1 para modo de exibição simplificado e 2 para o detalhado:')

num = input()

# generate player random moves
population = []
i = 0
while i < POPULATION_SIZE:
    randomlist = list(np.random.randint(low = 5,size = MOVE_LIMIT))
    population.append(Player.Player(data, randomlist))
    i +=1

maze = Policy.Policy(population, data, finalX, finalY, population[0], num)
maze.searchResult(999)