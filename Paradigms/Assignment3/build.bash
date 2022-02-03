#!/bin/bash
set -u -e
javac Game.java View.java Controller.java Brick.java
java Game
