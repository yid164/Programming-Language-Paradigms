-- Name: Yinsheng Dong
-- Student Number: 11148648
-- NSID: yid164
-- Lecture Section: CMPT 340

-- The perfectNum function to created the perfect numbers
perfectNum :: [Integer]
perfectNum = [ x | x <- [1..] , ((sum x ) == x)]
             where sum :: Integer -> Integer
                   sum a = foldr (+) 0 (twoSum a)
                            where twoSum :: Integer -> [Integer]
                                  twoSum b = [ y | y <- [1..(b`div`2)], ((y < b) && ((b `mod` y) == 0))]