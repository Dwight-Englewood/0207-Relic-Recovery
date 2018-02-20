module Main where


import Graphics.Gloss
import Graphics.Gloss.Data.Color

height, width, offset :: Int
width = 600
height = 600
offset = 0

window :: Display
window = InWindow "Nice Window" (width, height) (offset, offset)

background :: Color
background = white

drawing :: Picture
drawing = circle 80

data GlyphState = Brown | Gray | Empty

colorGlyph ::  GlyphState -> Picture -> Picture
colorGlyph Brown p = color (makeColorI 86 46 0 255) p 
colorGlyph Gray  p = color (makeColorI 119 119 119 255) p
colorGlyph Empty p = color (makeColorI 0 0 0 0) p 

glyph :: GlyphState -> Picture
glyph Brown = colorGlyph Brown (rectangleSolid 50 50)
glyph Gray = colorGlyph Gray (rectangleSolid 50 50)
glyph Empty = colorGlyph Empty (rectangleSolid 50 50)


main :: IO ()
main = display window white (glyph Empty)





