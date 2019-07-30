-- Name: Yinsheng Dong
-- Student Number: 11148648 
-- NSID: yid164
-- Lecuture: CMPT 340

 
-- Part2 Type System

-- Problem3
data MyFloat = MyFloat (Int, Int)
         

numberLength :: Int -> Int
numberLength x = if (x < 10 && x > (-10)) then (1)
                 else(numberLength (x`div`10) + 1)
                 
floatNum :: MyFloat -> Float
floatNum (MyFloat (0, y)) = 0
floatNum (MyFloat (x, y)) = if (y >=0) then ((fromIntegral (x) / fromIntegral(10 ^ numberLength(x)) * 10 ^ y))
                            else ((fromIntegral (x) / fromIntegral(10 ^ numberLength(x)) / 10 ^ negate(y)))
                            
whole :: MyFloat -> Int
whole (MyFloat(x, y)) = floor (floatNum (MyFloat(x,y)))

fraction :: MyFloat -> Float
fraction (MyFloat(x, y)) = (floatNum(MyFloat(x,y))) - fromIntegral(floor(floatNum(MyFloat(x,y))))


-- Part3 List
-- Problem4

shuffle :: [a] -> [a] -> [a]
shuffle xs [] = xs
shuffle [] ys = ys
shuffle (x:xs) (y:ys) = x : y : shuffle xs ys

-- Problem5
split :: [a] -> Int -> ([a], [a])
split xs 0 = ([], xs)
split [] _ = ([], [])
split (x:xs) k = if (k > (length (x:xs)) || k < 0) then (error "Number out of range")
                 else (x:xs1, xs2)
                      where (xs1, xs2) = split xs (k-1)
                      
-- Problem6
black :: Int -> [String]
black 1 = [x|x <- ["black"]]
black n = black 1 ++ black (n-1)


red :: Int -> [String]
red 1 = [x|x <- ["red"]]
red n = red 1 ++ red (n-1)


nshuffle :: Int -> Int -> [String]
nshuffle 0 0 = error("can not create non list")
nshuffle 1 0 = (red 1) ++ (black 1)
nshuffle n 0 = (red n) ++ (black n)
nshuffle n 1 = shuffle (red n) (black n)
nshuffle n 2 = shuffle left right
               where ((left,right)) = split (nshuffle n 1) (length (nshuffle n 1) `div` 2) 
nshuffle n c = nshuffle n (c-2)

-- Problem7
consecutive :: [Char] -> Int
consecutive xs = if (xs == []) then (0)
                 else if (length(xs) == 1) then (1)
                 else if ((last xs)/=(last (init xs))) then ((consecutive (init xs))+0)
                 else ((consecutive (init xs))+1)

