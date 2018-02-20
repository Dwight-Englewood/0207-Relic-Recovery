module Main where

import Diagrams.Prelude
import Diagrams.Backend.SVG.CmdLine


myCircle :: Diagram B
myCircle = circle 1

glyph :: Boolean -> Diagram B
glyph = square 1

main :: IO()
main = mainWith myCircle





