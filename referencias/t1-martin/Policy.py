import numpy as np
import Player
import random
import math
from numpy.random.mtrand import randint
class Policy:
  def __init__(self, population, data, finalX, finalY, mostFit, num):
      self.population = population
      self.data = data
      self.finalX = finalX
      self.finalY = finalY
      self.mostFit = mostFit
      self.num = num
      self.mostFit.addScore(999)

  def fitness(self):
    i=0
    while i < len(self.population):
        for index,item in enumerate(self.population[i].moves):
            if(item == 0):
                self.population[i].moveUp()
            elif(item == 1):
                self.population[i].moveDown()
            elif(item == 2):
                self.population[i].moveLeft()
            elif(item == 3):
                self.population[i].moveRight()            
            # add penalty if player hit a wall
            if(self.data[self.population[i].x][self.population[i].y] == '1' ):
                self.population[i].addPenalty()
                #Check if the moves are going back and forth
            # if (index+1 < len(self.population[i].moves) and index - 1 >= 0):    
            #     if self.population[i].moves[index + 1] != self.population[i].moves[index]: # UU or DD or LL... can work! 
            #         if (self.population[i].moves[index] in range(0,2) and self.population[i].moves[index+1] not in range(2,4)):
            #             self.population[i].addPenalty()
            #         if (self.population[i].moves[index] in range(2,4) and self.population[i].moves[index+1] not in range(0,2)):
            #             self.population[i].addPenalty()
            
        # fitness = | (x2-x1) | + | (y2-y1) | + accumulated penalties
        score = ((self.finalX - self.population[i].x)**2 + (self.finalY - self.population[i].y)**2)+ self.population[i].penalty
        # score =  ((self.finalX - self.population[i].x) + (self.finalY - self.population[i].y)) + self.population[i].penalty
        self.population[i].addScore(score)
        i+=1

#     Maze.resetPlayer()
#     score
#   }

  def fittestScore(self):
    fiteness = sorted(self.population, key=lambda population: population.score)
    self.population = fiteness
    self.mostFit = self.population[0]

  def crossOver(self):
    halfPopulationSize = int(len(self.population)/2)
    movesSize = int(len(self.population[0].moves)  )
    i=1
    newPopulation = []
    while i < halfPopulationSize:
        number = randint(movesSize)
        firstParent = self.population[randint(len(self.population)/2)].moves[0:number]
        secondParent = self.population[randint(len(self.population)/2)].moves[number:movesSize]
        child1= np.concatenate((firstParent, secondParent))
        child2= np.concatenate((secondParent, firstParent))
        newPopulation.append(Player.Player(self.data, child1))
        newPopulation.append(Player.Player(self.data, child2))
        i+=1
    self.population[0].resetPlayer()
    newPopulation.insert(0, self.population[0])
    self.population=newPopulation
  
  def mutation(self):
    movesSize = int(len(self.population[0].moves))
    newPopulation = self.population
    for index in range(len(self.population)):
        if random.randint(0,100) < 60 and index != 0:
            #print('A mutação ocorreu no cromossomo', index)
            newPopulation[index].moves[randint(movesSize)] = randint(4)
    self.population = newPopulation


  def searchResult(self, mostFitScore):
        executions = 0
        while self.mostFit.score > 0:
            self.fitness()
            self.fittestScore()
            if executions%10 == 0:
                print('Execuções: ',executions)
                print('Most Fit Score:', 'Score:',self.mostFit.score, '(',self.mostFit.x,',', self.mostFit.y, ')')
                if(self.num == '2'):
                    self.printInfo(self.mostFit)
            self.crossOver()
            self.mutation()
            # self.fitness()
            # self.fittestScore()
            executions+=1
        
        print('Resultado',self.mostFit.score, self.mostFit.x, self.mostFit.y, "Movements: ",self.mostFit.moves)
        return True

  def printInfo(self, player):
        print('Caminho melhor resultado:')
        newPlayer2 = Player.Player(self.data, player.moves)
        print(newPlayer2.moves)
        # newPlayer.resetPlayer()
        for index, item in enumerate(newPlayer2.moves):
            if(item == 0):
                newPlayer2.moveUp()
            elif(item == 1):
                newPlayer2.moveDown()
            elif(item == 2):
                newPlayer2.moveLeft()
            elif(item == 3):
                newPlayer2.moveRight()
            print('(', newPlayer2.x, ', ', newPlayer2.y, ')')


