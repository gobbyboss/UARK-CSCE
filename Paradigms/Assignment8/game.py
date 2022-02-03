import pygame
import time

from pygame.locals import*
from time import sleep

class Sprite():
	def __init__(self, x, y, w, h, image_url, update_method, type):
		self.x = x
		self.y = y
		self.w = w
		self.h = h
		self.image_url = image_url
		self.update_method = update_method
		self.type = type

	def update(self):
		print('success!')
		
class Model():
	def __init__(self):
		self.sprites = []

	def update(self):
		print('Success!')
		for i in self.sprites:
			self.sprites[i].update
	
		



class View():
	def __init__(self, model):
		screen_size = (800,600)
		self.screen = pygame.display.set_mode(screen_size, 32)
		self.turtle_image = pygame.image.load("turtle.png")
		self.model = model
		self.model.rect = self.turtle_image.get_rect()

	def update(self):    
		self.screen.fill([0,200,100])
		self.screen.blit(self.turtle_image, self.model.rect)
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
			elif event.type == pygame.MOUSEBUTTONUP:
				self.model.set_dest(pygame.mouse.get_pos())
		keys = pygame.key.get_pressed()
		if keys[K_LEFT]:
			self.model.dest_x -= 1
		if keys[K_RIGHT]:
			self.model.dest_x += 1
		if keys[K_UP]:
			self.model.dest_y -= 1
		if keys[K_DOWN]:
			self.model.dest_y += 1

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