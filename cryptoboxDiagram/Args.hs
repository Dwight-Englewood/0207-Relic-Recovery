{-# LANGUAGE BangPatterns     #-}
{-# LANGUAGE RecordWildCards  #-}
{-# LANGUAGE TemplateHaskell  #-}

module Args (
  Args(..)
, runWithArgs
) where


import           Control.Monad
import           Data.Monoid
import           Development.GitRev
import           Options.Applicative
import           System.Exit


data Custom = Custom1 | Custom2 deriving (Eq, Show, Ord)


data Args = Args { argsVerbose :: !Bool
                 , argsVersion :: !Bool
                 , argsCustom :: !Custom
                 } deriving (Show)


parseArgs :: Parser Args
parseArgs = Args
     <$> switch
         ( long "verbose"
        <> short 'v'
        <> help "Be verbose.")
     <*> switch
         ( long "version"
        <> short 'V'
        <> help "Print version and exit.")
     <*> option parseCustomArg
         ( long "custom-arg"
        <> short 'c'
        <> value Custom1
        <> showDefault
        <> help "Arg with a custom parser." )


parseCustomArg :: ReadM Custom
parseCustomArg = eitherReader $ \s ->
  case s
    of "c1" -> Right Custom1
       "c2" -> Right Custom2
       x    -> Left $ "Failed to parse custom argument " <> x <> ", expected 'c1' or 'c2'"


runWithArgs :: (Args -> IO ())
           -> IO ()
runWithArgs rwa = execParser opts >>= printVersion >>= rwa
  where
    opts = info (helper <*> parseArgs)
      ( fullDesc
     <> header "Add program header here."
     <> progDesc "Add program description here. Source: https://github.com/githubuser/cryptoboxDiagram")

    printVersion args@Args{..} = do
      when argsVersion $ die $ "Version: " <> $(gitBranch) <> "@" <> $(gitHash)
      return args