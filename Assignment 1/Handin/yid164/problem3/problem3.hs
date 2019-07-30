-- Name: Yinsheng Dong
-- Student Number: 11148648
-- NSID: yid164
-- Lecutre: CMPT 340

-- The problem3 part a
luhnDouble :: Integer -> Integer
luhnDouble n = if ((2 * n) < 9) then (2 * n)
               else ((2 * n ) - 9)
  
-- The problem3 part b		       
luhn :: Integer -> Integer -> Integer -> Integer -> Bool
luhn a b c d = if (((luhnDouble a) +b + (luhnDouble c) + d) `mod` 10 == 0) then (True)
               else (False)