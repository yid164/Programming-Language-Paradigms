-- Name: Yinsheng Dong
-- Student Number: 11148648
-- NSID: yid164
-- Lecutre: CMPT 340


-- conditional expression 
fastExp1 :: Integer -> Integer -> Integer
fastExp1 n k = if (even k) then (n^(k`div`2))^2
               else n*n^(k-1)


-- guarded equations
fastExp2 :: Integer -> Integer -> Integer
fastExp2 n k | even k    = (n^(k`div`2))^2
             | otherwise = n*n^(k-1)


-- pattern matching
fastExp3 :: Integer -> Integer -> Integer
fastExp3 n isOdd      = n * n^(isOdd - 1)
fastExp3 n isEven     = (n^(isEven`div`2))^2

-- for judge odd integer function
isOdd :: Integer -> Integer
isOdd n | odd n     = n
        | otherwise = isEven n

-- for judge even integer function
isEven :: Integer -> Integer
isEven n | even n    = n
         | otherwise = isOdd n



