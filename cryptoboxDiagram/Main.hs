{-# LANGUAGE BangPatterns     #-}
{-# LANGUAGE RecordWildCards  #-}

module Main where

import           Args
import Graphics.Gloss as G

--main:: IO ()
--main = runWithArgs $ \args@Args{..} -> do
  --putStrLn $ show args

main :: IO()
main = do
    display window white (pictures [(translate 300 300 (createGlyph True)), (createGlyph False)])

window :: Display
window = G.InWindow "test" (1000, 1000) (100, 100)

drawing :: Picture
drawing = G.rectangleSolid 100 100


createGlyph :: Bool -> Picture
createGlyph a = if a then (color (makeColorI 135 88 56 100) (rectangleSolid 100 100)) else (color (makeColorI 119 119 119 100) (rectangleSolid 100 100))


