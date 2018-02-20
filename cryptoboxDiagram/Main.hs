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

renderCryptobox :: Cryptobox -> Picture
renderCryptobox a = pictures [renderColumns, renderGlyphMatrix a]


addGlyph :: Glyph -> Glyph -> Glyph
addGlyph Empty Brown = Brown
addGlyph Empty Gray = Gray
addGlyph Brown Empty = Brown
addGlyph Gray Empty = Gray
addGlyph Empty Empty = Empty
addGlyph _ _ = error "laslderljgslululululululul"

addCryptobox :: Cryptobox -> Cryptobox -> [([Glyph], [Glyph])]
addCryptobox a b = zip a b 

unzipPart :: ([Glyph], [Glyph]) -> [(Glyph, Glyph)]
unzipPart (a, b) = zip a b

merp2 :: [(Glyph, Glyph)] -> [Glyph]
merp2 = map (\(f, s) -> addGlyph f s)

endMyLife :: Cryptobox -> Cryptobox -> Cryptobox
endMyLife a b = map (\x -> merp2 (unzipPart x)) (addCryptobox a b)  


renderColumns :: Picture
renderColumns = translate (0 - (0.5 * 1.2 * glyphSize)) (1.7 * glyphSize) (pictures [translate ((1.2 * glyphSize * (fromIntegral z))) (0) (color (makeColorI 11 0 140 255) (rectangleSolid (0.2 * glyphSize) (4.4 * glyphSize ))) | z <- [0..3]])


renderGlyphMatrix :: Cryptobox -> Picture
renderGlyphMatrix merp = pictures [translate ((1.2 * glyphSize * fromIntegral z)) (0) (renderGlyphColumn (merp !! z)) | z <- [0..(length merp - 1)]]

renderGlyphColumn :: [Glyph] -> Picture
renderGlyphColumn test = pictures [(translate (0) ((1.1 * glyphSize * (fromIntegral z))) (glyphCreate (test !! z))) | z <- [0..((length test) - 1)]]

main :: IO ()
--main = display window (makeColorI 0 0 0 255) (pictures [renderCryptobox (endMyLife vufCol1 endMyLife2)])
main = animate window (makeColorI 0 0 0 255) animateTest


animateTest :: Float -> Picture
animateTest b
    | b < 5 = renderCryptobox vufCol1
    | b < 10 = renderCryptobox (endMyLife vufCol1 endMyLife2)
    | b < 15 = renderCryptobox (endMyLife (endMyLife vufCol1 endMyLife2) endMyLife3)
    | b < 20 = renderCryptobox (endMyLife (endMyLife (endMyLife vufCol1 endMyLife2) endMyLife3) endMyLife4)
    | b < 25 = renderCryptobox (endMyLife (endMyLife (endMyLife (endMyLife vufCol1 endMyLife2) endMyLife3) endMyLife4) endMyLife5)
    | b < 30 = renderCryptobox (endMyLife (endMyLife (endMyLife (endMyLife (endMyLife vufCol1 endMyLife2) endMyLife3) endMyLife4) endMyLife5) endMyLife6)
    | otherwise = renderCryptobox (endMyLife (endMyLife (endMyLife (endMyLife (endMyLife (endMyLife vufCol1 endMyLife2) endMyLife3) endMyLife4) endMyLife5) endMyLife6) endMyLife7)


vufCol1 :: Cryptobox
vufCol1 = transpose [[Gray, Empty, Empty], [Empty, Empty, Empty], [Empty, Empty, Empty], [Empty, Empty, Empty]]

endMyLife2 :: Cryptobox
endMyLife2 = transpose [[Empty, Empty, Empty], [Brown, Empty, Empty], [Brown, Empty, Empty], [Empty, Empty, Empty]]

endMyLife3 :: Cryptobox
endMyLife3 = transpose [[Empty, Brown, Gray], [Empty, Empty, Empty], [Empty, Empty, Empty], [Empty, Empty, Empty]]

endMyLife4 :: Cryptobox
endMyLife4 = transpose [[Empty, Empty, Empty], [Empty, Gray, Brown], [Empty, Empty, Empty], [Empty, Empty, Empty]]

endMyLife5 :: Cryptobox
endMyLife5 = transpose [[Empty, Empty, Empty], [Empty, Empty, Empty], [Empty, Gray, Brown], [Empty, Empty, Empty]]

endMyLife6 :: Cryptobox
endMyLife6 = transpose [[Empty, Empty, Empty], [Empty, Empty, Empty], [Empty, Empty, Empty], [Empty, Brown, Gray]]

endMyLife7 :: Cryptobox
endMyLife7 = transpose[[Empty, Empty, Empty], [Empty, Empty, Empty], [Empty, Empty, Empty], [Gray, Empty, Empty]]

bird :: [[Glyph]]
bird = transpose [[Gray, Brown, Gray], [Brown, Gray, Brown], [Brown, Gray, Brown], [Gray, Brown, Gray]]

frog :: [[Glyph]]
frog = transpose $ reverse [[Gray, Brown, Gray], [Brown, Gray, Brown], [Gray, Brown, Gray], [Brown, Gray, Brown]]

snake :: [[Glyph]]
snake = transpose [[Gray, Gray, Brown], [Gray, Brown, Brown], [Brown, Brown, Gray], [Brown, Gray, Gray]]

