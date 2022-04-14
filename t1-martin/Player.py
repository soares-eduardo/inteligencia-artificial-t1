from numpy import add


class Player:
  def __init__(self, matrix, moves):
    self.matrix = matrix
    self.x = 0
    self.y = 0
    self.moves = moves
    self.penalty = 0
    self.score = 0

  def moveUp(self):
    if(self.x != 0 ):
        self.x -= 1
    else: 
      self.addPenalty()
  def moveDown(self):
    if(self.x != 11 ):
        self.x += 1
    else: 
      self.addPenalty()  
  def moveRight(self):
    if(self.y != 11 ):
        self.y += 1
    else: 
      self.addPenalty()
  def moveLeft(self):
    if(self.y != 0 ):
        self.y -= 1
    else: 
      self.addPenalty() 
  def addPenalty(self):
    self.penalty+=10
  def addScore(self, score):
    self.score = score
  def resetPlayer(self):
    self.penalty=0
    self.x=0
    self.y=0