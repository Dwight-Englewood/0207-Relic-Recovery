module Main where


import Graphics.Gloss
import Graphics.Gloss.Data.Color
import Data.List

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

data Glyph = Brown | Gray | Empty

colorGlyph ::  Glyph -> Picture -> Picture
colorGlyph Brown p = color (makeColorI 86 46 0 255) p 
colorGlyph Gray  p = color (makeColorI 119 119 119 255) p
colorGlyph Empty p = color (makeColorI 0 0 0 0) p 

glyphSize :: Float
glyphSize = 50

glyphCreate :: Glyph -> Picture
glyphCreate Brown = colorGlyph Brown (rectangleSolid glyphSize glyphSize)
glyphCreate Gray = colorGlyph Gray (rectangleSolid glyphSize glyphSize)
glyphCreate Empty = colorGlyph Empty (rectangleSolid glyphSize glyphSize)

type Cryptobox = [[Glyph]]

cryptoBoxCreate :: Cryptobox -> Picture
cryptoBoxCreate merp = pictures [translate (fromIntegral (60 * z)) (0) (renderColumn (merp !! z)) | z <- [0..(length merp - 1)]]

renderColumn :: [Glyph] -> Picture
renderColumn test = pictures [(translate (0) (fromIntegral (50*z)) (glyphCreate (test !! z))) | z <- [0..((length test) - 1)]]

main :: IO ()
main = display window white (cryptoBoxCreate bird)


bird :: [[Glyph]]
bird = transpose [[Gray, Brown, Gray], [Brown, Gray, Brown], [Brown, Gray, Brown], [Gray, Brown, Gray]]

frog :: [[Glyph]]
frog = transpose [[Gray, Brown, Gray], [Brown, Gray, Brown], [Gray, Brown, Gray], [Brown, Gray, Brown]]

snake :: [[Glyph]]
snake = transpose [[Brown, Gray, Gray], [Brown, Brown, Gray], [Gray, Brown, Brown], [Gray, Gray, Brown]]

