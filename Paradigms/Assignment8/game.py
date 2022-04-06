#Robert Goss
#Assignment 8
#12/09/2021
#python game.py to run
import pygame
import time
import random
from pygame.display import update

from pygame.locals import*
from time import sleep


class Sprite():
	def __init__(self, x, y, w, h, image, updateMethod, type):
		self.x = x
		self.y = y
		self.w = w
		self.h = h
		self.type = type
		self.drawX = 0
		self.groundShift = 0
		self.image = image
		self.updateMethod = updateMethod
		self.marioX = 0
		self.blockHit = False
		self.numCoins = 0
	
	def update(self):
		self.updateMethod(self)
	
	def isNotColliding(self, s):
		if self.x + self.w < s.x:
			return True
		if self.x > s.x + s.w:
			return True
		if self.y + self.h < s.y:
			return True
		if self.y > s.y + s.h:
			return True
		return False

class Mario(Sprite):
	def __init__(self):
		self.x = 200
		self.y = 50
		self.px = 50
		self.w = 60
		self.h = 95
		self.drawX = 100
		self.vert_vel = 0
		self.framesSinceGround = 0
		self.imageCount = 0
		self.type = 1
		self.canJump = False
		self.image = pygame.image.load("mario1.png")
		self.updateMethod = Mario.update
		self.images = []
		for i in range(5):
			image = pygame.image.load("mario" + str(i + 1) + ".png")
			self.images.append(image)

		
	def update(self):
		if self.framesSinceGround > 0:
			self.vert_vel += 1.5
		
		if self.y >= 330:
			self.vert_vel = 0
			self.y = 330
			self.framesSinceGround = 0

		if self.canJump == True and self.framesSinceGround < 5:
			self.vert_vel -= 5
			
		self.framesSinceGround += 1
		self.y += self.vert_vel
		return True

	def updateImageCount(self):
		if self.imageCount < 5:
			self.image = self.images[self.imageCount]
			self.imageCount += 1
		else:
			self.imageCount = 0
			self.image = self.images[self.imageCount]
	
	def collisionFix(self, s):
		if(self.x + self.w >= s.x and self.px + self.w <= s.x ):
			self.x = s.x - self.w
		elif(self.x <= s.x + s.w and self.px >= s.x + s.w):
			self.x = s.x + s.w
		elif(self.y + self.h >= s.y and self.y + self.h <= s.y + s.h):
			self.vert_vel = 0
			self.y = s.y - self.h
			self.framesSinceGround = 0
		elif(self.y <= s.y + s.h and self.y >= s.y):
			self.y = s.y + s.h + 5
			self.vert_vel = 5
			self.framesSinceGround = 5
			if s.type == 3:
				s.blockHit = True


class Brick(Sprite):
	def __init__(self, x, y, w, h, image, updateMethod, type):
		super().__init__(x, y, w, h, image, updateMethod, type)
		self.numCoins = 5

	def brick(self):
		self.drawX = self.x - self.marioX + 100
		return True

	def coinBrick(self):
		self.drawX = self.x - self.marioX + 100
		if self.blockHit == True:
			self.numCoins -= 1
			self.blockHit = False
			if self.numCoins == 0:
				self.type = 2
				self.updateMethod = Brick.brick
				emptyImage = pygame.image.load("emptyBlock.png")
				emptyImage = pygame.transform.scale(emptyImage, (40, 40))
				self.image = emptyImage
		
		return True
		

class Coin(Sprite):
	def __init__(self, x, y, x_vel):
		self.vert_vel = -10
		self.x_vel = x_vel
		self.x = x
		self.y = y
		self.w = 50
		self.h = 50
		self.type = 5
		self.drawX = self.x
		self.updateMethod = Coin.update
		coinImage = pygame.image.load("coin.png")
		coinImage = pygame.transform.scale(coinImage, (40,40))
		self.image = coinImage

	def update(self):
		self.vert_vel += 1.2
		self.y += self.vert_vel
		self.x += self.x_vel
		self.drawX = self.x - self.marioX + 100
		if self.y > 1000:
			return False
		return True


		

class Model():
	def __init__(self):
		self.sprites = []
		background_img = pygame.image.load("background.png")
		background_img = pygame.transform.scale(background_img, (2000,1000))
		ground_img = pygame.image.load("ground.png")
		ground_img = pygame.transform.scale(ground_img, (2000,250))
		brick_img = pygame.image.load("brick.png")
		brick_img = pygame.transform.scale(brick_img, (40, 40))
		coinBlock_img = pygame.image.load("coinBlock.png")
		coinBlock_img = pygame.transform.scale(coinBlock_img, (40,40))
		
		self.backgrnd = Sprite(0, 0, 2000, 1000, background_img, Model.background, 4)
		self.ground = Sprite(0, 400, 2000, 1000, ground_img, Model.background, 4)
		self.mario = Mario()

		self.sprites.append(self.backgrnd)
		self.sprites.append(self.ground)
		self.sprites.append(self.mario)
		self.sprites.append(Brick(400, 385, 40, 40, brick_img, Brick.brick, 2))
		self.sprites.append(Brick(500, 385, 40, 40, brick_img, Brick.brick, 2))
		self.sprites.append(Brick(500, 345, 40, 40, brick_img, Brick.brick, 2))
		self.sprites.append(Brick(650, 200, 40, 40, coinBlock_img, Brick.coinBrick, 3))
		self.sprites.append(Brick(700, 200, 40, 40, brick_img, Brick.brick, 2))
		self.sprites.append(Brick(750, 200, 40, 40, coinBlock_img, Brick.coinBrick, 3))
		self.sprites.append(Brick(800, 100, 40, 40, brick_img, Brick.brick, 2))
		

	def update(self):
		for obj in self.sprites:
			obj.marioX = self.mario.px
			spriteExists = obj.update()
			if spriteExists == False:
				self.sprites.remove(obj)
				break
			if((obj.type == 2 or obj.type == 3) and self.mario.isNotColliding(obj) == False):
				self.mario.collisionFix(obj)
				if obj.blockHit == True:
					self.sprites.append(Coin(obj.x, obj.y, random.randrange(-10,10)))


			
	def background(self):
		self.x += self.groundShift
		self.drawX = self.x
		self.groundShift = 0
		return True

	def doNothing(self):
		return True
		



class View():
	def __init__(self, model):
		screen_size = (800,600)
		self.screen = pygame.display.set_mode(screen_size, 32)
		self.model = model
		

	def update(self):    
		for obj in self.model.sprites:
			self.model.rect = obj.image.get_rect(x=(obj.drawX), y=(obj.y), w=(obj.w), h=(obj.h))
			self.screen.blit(obj.image, self.model.rect)
		pygame.display.flip()

class Controller():
	def __init__(self, model):
		self.model = model
		self.keep_going = True

	def update(self):
		for event in pygame.event.get():
			if event.type == QUIT:
				self.keep_going = False
			elif event.type == KEYDOWN:
				if event.key == K_ESCAPE:
					self.keep_going = False
		keys = pygame.key.get_pressed()
		self.model.mario.px = self.model.mario.x
		self.model.mario.py = self.model.mario.y
		self.model.mario.canJump = False

		if keys[K_LEFT]:
			self.model.mario.x -= 6
			self.model.backgrnd.groundShift += 1
			self.model.ground.groundShift += 4
			self.model.mario.updateImageCount()
		if keys[K_RIGHT]:
			self.model.mario.x += 6
			self.model.backgrnd.groundShift -= 1
			self.model.ground.groundShift -= 4
			self.model.mario.updateImageCount()
		if keys[K_SPACE]:
			self.model.mario.canJump = True
			
		

print("Use the arrow keys to move. Press Esc to quit.")
pygame.init()
m = Model()
v = View(m)
c = Controller(m)
while c.keep_going:
	c.update()
	m.update()
	v.update()
	sleep(0.04)
print("Goodbye")