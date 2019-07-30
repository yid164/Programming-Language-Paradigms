-- File Name: a3-sample-solutions.hs
-- Author: David Kreiser
-- Class: CMPT 340 Assignment 3
-- NB: This is a non-exhaustive *SAMPLE* solution. There is more than one valid approach to most questions.

-- Please let me know ASAP if you spot any errors in this solution: kreiser.david@usask.ca

-- ----------------------------------------------- Problem 1 -----------------------------------------------
-- Provided
unfold p h t x   | p x            = [ ]
                 | otherwise      = h x : unfold p h t (t x)

-- Problem 1a
myMap :: (a -> b) -> [a] -> [b]
myMap f = unfold (null) (f . head) (tail)

-- Notes: 
--  null is a function which takes a list and returns True if it is empty, otherwise false
--  head is a function which returns the first element of the list
--  tail is a function which returns everything but the head of a list

-- Problem 1b
myIterate :: (a -> a) -> a -> [a]
myIterate f = unfold (const False) id f

-- Notes:
--  const False will always return False, which makes myIterate an infinite recursive loop, since the predicate will never be True
--  id is the built-in identity function - it returns what was passed in (i.e. the head is unchanged)

-- ----------------------------------------------- Problem 2 -----------------------------------------------
altMap :: (a -> b) -> (a -> b) -> [a] -> [b]
altMap _ _ []                     = []
altMap function1 function2 (x:xs) = (function1 x) : (altMap function2 function1 xs)

-- ----------------------------------------------- Problem 3 -----------------------------------------------
-- Provided from assignment 1 solution
luhnDouble :: Integer -> Integer
luhnDouble x | 2*x <= 9   = 2*x
             | otherwise  = 2*x - 9

luhn :: [Integer] -> Bool
luhn [] = False
luhn digits | (length digits) `mod` 2 == 0 = if (sum (altMap luhnDouble id digits)) `mod` 10 == 0 then True else False
            | otherwise                    = if (sum (altMap id luhnDouble digits)) `mod` 10 == 0 then True else False

-- Note there are two cases in this implementation: one where the number of digits is even
--  and one where the number of digits is odd. Only difference is the swapped order of the
--  two functions in the call to altMap
-- Also note that id is the built-in identity function (it returns what was passed in)

-- ----------------------------------------------- Problem 4 -----------------------------------------------
doubleComp :: [(Int, Int)]
doubleComp = concat [[(x, y) | y <- [4, 5, 6]] | x <- [1, 2, 3]]

-- Note the order of x and y is important otherwise it does not match the example
-- provided which uses single list comprehension

-- ----------------------------------------------- Problem 5 -----------------------------------------------
perfectNums :: [Integer]
perfectNums = [x | x <- [1 .. ], x == (sum [y | y <- [1 .. (x - 1)], x `mod` y == 0])]

-- Note in the above, x is an infinite list of integers incrementing by 1.
-- The second generator (using y) is a list of factors of x (excluding itself)
--   from which we calculate the sum and check if it equals x.
