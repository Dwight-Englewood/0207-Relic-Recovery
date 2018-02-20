module ProbabilityDrive where

import Data.List

data Glyph = Brown | Gray | Empty

instance Show Glyph where
    show Brown = "B"
    show Gray = "G"
    show Empty = " "

bird :: [[Glyph]]
bird = [[Gray, Brown, Gray], [Brown, Gray, Brown], [Brown, Gray, Brown], [Gray, Brown, Gray]]

frog :: [[Glyph]]
frog = [[Gray, Brown, Gray], [Brown, Gray, Brown], [Gray, Brown, Gray], [Brown, Gray, Brown]]

snake :: [[Glyph]]
snake = [[Brown, Gray, Gray], [Brown, Brown, Gray], [Gray, Brown, Brown], [Gray, Gray, Brown]]

printP :: [[Glyph]] -> String
printP merp = concat $ map (\x -> (formatRow x) ++ "\n") merp

formatRow :: [Glyph] -> String
formatRow a = concat $ map (\x -> ((show x) ++ "|")) a

main :: IO()
main = do
        putStrLn $ printP $ bird
        
        putStrLn "\n"
        putStrLn $ printP $ frog
        putStrLn "\n"
        putStrLn $ printP $ snake
        putStrLn $ printP $ transpose $ snake
