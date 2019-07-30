-- Name: Yinsheng Dong
-- Student Number: 11148648
-- NSID: yid164
-- Lecutre: CMPT 340

-- Problem4 part a
averageThree :: Integer -> Integer -> Integer -> Integer
averageThree a b c = (a + b + c) `div` 3

howManyAboveAverage :: Integer -> Integer -> Integer -> Integer
howManyAboveAverage a b c = if (a>averageThree a b c && b > averageThree a b c && c > averageThree a b c) then (3)
                            else if (a<=averageThree a b c && b > averageThree a b c && c > averageThree a b c) then (2)
                            else if (a>averageThree a b c && b <= averageThree a b c && c > averageThree a b c) then (2)
                            else if (a>averageThree a b c && b > averageThree a b c && c <= averageThree a b c) then (2)
                            else if (a<=averageThree a b c && b <= averageThree a b c && c > averageThree a b c) then (1)
                            else if (a<=averageThree a b c && b > averageThree a b c && c <= averageThree a b c) then (1)
                            else if (a>averageThree a b c && b <= averageThree a b c && c <= averageThree a b c) then (1)
                            else (0)

-- problem4 part b						
averageThreeInOne :: (Integer, Integer, Integer) -> Integer
averageThreeInOne (a, b, c) = averageThree a b c

orderTriple :: (Integer, Integer, Integer) -> (Integer, Integer, Integer)
orderTriple (a, b, c) = if (howManyAboveAverage a b c == 0 || howManyAboveAverage a b c == 3) then((a,b,c))
                        else if (a > b && a > c && b >= c) then ((c,b,a))
                        else if (a > b && a > c && c >= b) then ((b,c,a))
                        else if (b > a && b > c && a >= c) then ((c,a,b))
                        else if (b > a && b > c && c <= a) then ((a,c,b))
                        else if (c > a && c > b && a >= b) then ((b,a,c))
                        else if (c > a && c > b && b <= a) then ((a,b,c))
                        else ((a,b,c))