-- Name: Yinsheng Dong
-- Student Number: 11148648
-- NSID: yid164
-- Lecture Section: CMPT 340

-- The function that is provided
oneComprehension :: [(Int,Int)]
oneComprehension = [(x,y) | x <- [1, 2, 3], y <- [4, 5, 6]]

-- the two comprehensions with single generators, they are should be same output
twoComprehension :: [(Int,Int)]
twoComprehension = concat [ [(x, y) | y <- [4, 5, 6] ] | x <- [1, 2, 3] ] 